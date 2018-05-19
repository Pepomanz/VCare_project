#include "TinyGPS++.h"
#include "SoftwareSerial.h"
#include <mcp_can.h>
#include <SPI.h>
#include <EEPROM.h>

#if standard == 1
#define LISTEN_ID 0x7EA
#define REPLY_ID 0x7E0
#define FUNCTIONAL_ID 0x7DF
#else
#define LISTEN_ID 0x98DAF101
#define REPLY_ID 0x98DA01F1
#define FUNCTIONAL_ID 0x98DB33F1
#endif

// CAN TX Variables
unsigned long prevTx = 0;
float invlTx = 1;
byte txData[] = {0x02, 0x01, 0x01, 0x55, 0x55, 0x55, 0x55, 0x55};
byte txData1[] = {0x02, 0x01, 0x00, 0x55, 0x55, 0x55, 0x55, 0x55};
byte txData2[] = {0x01, 0x03, 0x55, 0x55, 0x55, 0x55, 0x55, 0x55};
// CAN RX Variables
unsigned long rxID;
byte rxBuf[8] = {0x55, 0x55, 0x55, 0x55, 0x55, 0x55, 0x55, 0x55};;
byte dlc;
char msgString[128];                        // Array to store serial string
int dtc[4];
byte spd = 0;
byte last = 0x55;
byte sent = 0x53 ;
byte support[12] = {0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00};
int check[3] = {0, 0, 0};
int j, k;
// CAN Interrupt and Chip Select Pins
#define CAN0_INT 2                               /* Set INT to pin 2 (This rarely changes)   */
MCP_CAN CAN0(10);                                /* Set CS to pin 9 (Old shields use pin 10) */

int i = 0;

SoftwareSerial GPSserial(8, 9); //RX=pin 8, TX=pin 9
TinyGPSPlus gps;//This is the GPS object that will pretty much do all the grunt work with the NMEA data

double start_lat = 0;
double start_lng = 0;
double end_lat = 0;
double end_lng = 0;
double distanceKm = 0;
double totalDistance = 0;
double courseTest = 0;

double readDouble(unsigned int addr)
{
  union {
    byte b[8];
    double d;
  } data;
  for (int i = 0; i < 8; i++)
  {
    data.b[i] = EEPROM.read(addr + i);
  }
  return data.d;
}
void writeDouble(unsigned int addr, double x)
{
  union {
    byte b[8];
    double d;
  } data;
  data.d = x;
  for (int i = 0; i < 8; i++)
  {
    EEPROM.write(addr + i, data.b[i]);
  }
}

const byte interruptPin = 3;

void clearEEPROM() {
  writeDouble(0, 0);
  delay(500);
}
void setup() {

  Serial.begin(9600); //This open up communications to the bluetooth
  GPSserial.begin(9600);//This opens up communications to the GPS
  while (!Serial);
  pinMode(interruptPin, INPUT);
  attachInterrupt(digitalPinToInterrupt(interruptPin), clearEEPROM, RISING);
  // Initialize MCP2515 running at 16MHz with a baudrate of 500kb/s and the masks and filters disabled.
  if (CAN0.begin(MCP_ANY, CAN_500KBPS, MCP_16MHZ) == CAN_OK)
    Serial.println("MCP2515 Initialized Successfully!;");
  else {
    Serial.println("Error Initializing MCP2515... Permanent failure!  Check your code & connections;");
    while (1);
  }

  // Allow all Extended IDs
  CAN0.init_Mask(1, 0x80000000);               // Init second mask...
  CAN0.init_Filt(2, 0x80000000);               // Init third filter...
  CAN0.init_Filt(3, 0x80000000);               // Init fouth filter...
  CAN0.init_Filt(4, 0x80000000);               // Init fifth filter...
  CAN0.init_Filt(5, 0x80000000);               // Init sixth filter...

  CAN0.setMode(MCP_NORMAL);                      // Set operation mode to normal so the MCP2515 sends acks to received data.

  // Having problems?  ======================================================
  // If you are not receiving any messages, uncomment the setMode line below
  // to test the wiring between the Ardunio and the protocol controller.
  // The message that this sketch sends should be instantly received.
  // ========================================================================
  //CAN0.setCANCTRL_Mode(MCP_LOOPBACK);

  pinMode(CAN0_INT, INPUT);                          // Configuring pin for /INT input
  Serial.println("Simple CAN OBD-II PID Request");
  //delay(10000);

  do {
    if (!digitalRead(CAN0_INT)) {
      CAN0.readMsgBuf(&rxID, &dlc, rxBuf);             // Get CAN data
      if (rxBuf[1] == 0x41) {
        switch (rxBuf[2]) {
          case 0x00:
            j = 0;
            for (byte i = 3; i <= rxBuf[0]; i++) {
              support[j] = rxBuf[i];
              j++;
            }
            check[0] = 1;
            break;
          case 0x20:
            j = 4;
            for (byte i = 3; i <= rxBuf[0]; i++) {
              support[j] = rxBuf[i];
              j++;
            }
            check[1] = 1;
            break;
          case 0x40:
            j = 8;
            for (byte i = 3; i <= rxBuf[0]; i++) {
              support[j] = rxBuf[i];
              j++;
            }
            check[2] = 1;
            break;
          default:
            break;
        }
      }
      if (sent == rxBuf[2]) {
        txData1[2] += 0x20;
        if (txData1[2] > 0x40) {
          txData1[2] = 0x00;
        }
      }
    }

    if ((millis() - prevTx) >= 2 * invlTx) {
      prevTx = millis();
      if (CAN0.sendMsgBuf(FUNCTIONAL_ID, 8, txData1) == CAN_OK) {
        sent = txData1[2];
      }


    }
  } while (!check[0] || !check[1] || !check[2]);
  delay(200);
}

void loop() {
  j = 0;
  k = 7;
  txData[2] = 1;
  do {
    do {
      if (!digitalRead(CAN0_INT)) {                       // If CAN0_INT pin is low, read receive buffer

        CAN0.readMsgBuf(&rxID, &dlc, rxBuf);             // Get CAN data
        if (rxBuf[1] == 0x41 && rxBuf[2] != 0x03 && rxBuf[2] != 0x13 && rxBuf[2] != 0x20 && rxBuf[2] != 0x40 && rxBuf[2] != 0x00) {
          if (rxBuf[2] != last) {
            last = rxBuf[2];
            sprintf(msgString, "%d ", rxBuf[2]);
            Serial.print(msgString);
            switch (rxBuf[2]) {
              case 0x04: case 0x11: case 0x2C: case 0x2E: case 0x2F: case 0x45: case 0x47: case 0x49: case 0x4A: case 0x4C: case 0x52:
                Serial.println(rxBuf[3] / 2.55);
                break;
              case 0x05: case 0x0F:
                Serial.println(rxBuf[3] - 40);
                break;
              case 0x06: case 0x07:
                Serial.println((rxBuf[3] / 1.28) - 100);
                break;
              case 0x0B: case 0x30: case 0x33:
                Serial.println(rxBuf[3]);
                break;
              case 0x0D:
                Serial.println(rxBuf[3]);
                spd = rxBuf[3];
                break;
              case 0x0C:
                Serial.println((256 * rxBuf[3] + rxBuf[4]) / 4);
                break;
              case 0x0E:
                Serial.println(rxBuf[3] / 2 - 64);
                break;
              case 0x10:
                Serial.println((256 * rxBuf[3] + rxBuf[4]) / 100);
                break;
              case 0x15:
                Serial.print(rxBuf[3] / 200);
                Serial.print(" ");
                Serial.println((rxBuf[4] * 100 / 128) - 100);
                break;
              case 0x1F: case 0x21: case 0x31:
                Serial.println((256 * rxBuf[3]) + rxBuf[4]);
                break;
              case 0x2D:
                Serial.println((100 / 128 * rxBuf[3]) - 100);
                break;
              case 0x34:
                Serial.print((2 / 65536) * ((256 * rxBuf[3]) + rxBuf[4]));
                Serial.print(" ");
                Serial.println((((256 * rxBuf[5]) + rxBuf[6]) / 256) - 128);
                break;
              case 0x3C:
                Serial.println((((256 * rxBuf[3]) + rxBuf[4]) / 10) - 40);
                break;
              case 0x42:
                Serial.println((256 * rxBuf[3] + rxBuf[4]) / 1000);
                break;
              case 0x43:
                Serial.println((256 * rxBuf[3] + rxBuf[4]) * 100 / 255);
                break;
              case 0x44:
                Serial.println((2 / 65536) * (256 * rxBuf[3] + rxBuf[4]));
                break;
              //case 0x13: case 0x03:
              case 0x01: case 0x1C:  case 0x51:
                //default:
                for (byte i = 3; i <= rxBuf[0] - 1; i++) {
                  sprintf(msgString, "%.2X-", rxBuf[i]);
                  Serial.print(msgString);
                }
                sprintf(msgString, "%.2X", rxBuf[i]);
                Serial.println(msgString);
                break;
              default:
                break;
            }
          }
        }
      }
      // else
      delay(50);
      do {
        if (CAN0.sendMsgBuf(FUNCTIONAL_ID, 8, txData) == CAN_OK) {
          sent = txData[2];
        }
      } while (sent != txData[2]);
    } while (sent != rxBuf[2]);
    rxBuf[1] = 0x00;
    if (k == 0) {
      j++;
      k = 7;
    }
    else
      k--;
    while (bitRead(support[j], k) == 0) {
      if (k == 0) {
        j++;
        k = 7;
      }
      else
        k--;
    }
    txData[2] = j * 8 + (8 - k);
  } while (txData[2] <= 0x52);
  txData[2] = 0x00;
  do {
    if (CAN0.sendMsgBuf(FUNCTIONAL_ID, 8, txData2) == CAN_OK) {
      sent = txData2[1];
    }
    if (!digitalRead(CAN0_INT)) {                       // If CAN0_INT pin is low, read receive buffer
      CAN0.readMsgBuf(&rxID, &dlc, rxBuf);              // Get CAN data
    }
    if (rxBuf[1] == 0x43) {
      if (last != rxBuf[1]) {
        last = rxBuf[1];
        Serial.print("DTC: ");
        dtc[0] = bitRead(rxBuf[3], 7) * 10 + bitRead(rxBuf[3], 6);
        dtc[1] = bitRead(rxBuf[3], 5) * 2 + bitRead(rxBuf[3], 4);
        dtc[2] = bitRead(rxBuf[3], 3) * 8 + bitRead(rxBuf[3], 2) * 4 + bitRead(rxBuf[3], 1) * 2 + bitRead(rxBuf[3], 0);
        switch (dtc[0]) {
          case 0:
            sprintf(msgString, "P");
            break;
          case 1:
            sprintf(msgString, "C");
            break;
          case 10:
            sprintf(msgString, "B");
            break;
          case 11:
            sprintf(msgString, "U");
            break;
          default:
            break;
        }
        sprintf(msgString, "%s%d%d%X", msgString, dtc[1], dtc[2], rxBuf[4]);
        Serial.println(msgString);
      }
    }
  } while (sent != rxBuf[1] - 0x40);
  smartDelay(600);
  if (gps.location.isUpdated()) {
    totalDistance = readDouble(0);
    if (start_lat == 0 && start_lng == 0)
    {
      start_lat = gps.location.lat();
      start_lng = gps.location.lng();
      end_lat = start_lat;
      end_lng = start_lng;
      Serial.print("Lat: ");
      Serial.println(end_lat, 6);
      Serial.print("Long: ");
      Serial.println(end_lng, 6);
      Serial.print("Dist: ");
      Serial.println(totalDistance);
    }
    else
    {
      end_lat = gps.location.lat();
      end_lng = gps.location.lng();
      if (spd != 0)
      {
        distanceKm = gps.distanceBetween(end_lat, end_lng, start_lat, start_lng);
        totalDistance += distanceKm;
        writeDouble(0, totalDistance);
      }
      Serial.print("Lat: ");
      Serial.println(end_lat, 6);
      Serial.print("Long: ");
      Serial.println(end_lng, 6);
      Serial.print("Dist: ");
      Serial.println(totalDistance);
      start_lat = end_lat;
      start_lng = end_lng;
    }
  }
  else {
    Serial.println("Lat: 0");
    Serial.println("Long: 0");
    Serial.print("Dist: ");
    Serial.println(totalDistance);
  }

}
static void smartDelay(unsigned long ms)
{
  unsigned long start = millis();
  do
  {
    while (GPSserial.available())
      gps.encode(GPSserial.read());
  } while (millis() - start < ms);
}



