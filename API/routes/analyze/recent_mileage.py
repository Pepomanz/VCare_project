import sys
import pymysql
import json

#function for checking mileage
def mileage_check(mileage):
    status = 0
    if mileage == 50000:
        status = 1
        #print("Change Spark plug")
        #print("Change fuel filter")
    elif mileage == 40000:
        status = 2
        #print("Clean Convayor belt")
        #print("Change gear oil")
    elif mileage == 30000:
        status = 3
        #print("Check Value")
        #print("Change air filter")
    elif mileage == 20000:
        status = 4
        #print("Check tyre")
        #print("Check break")
    elif mileage == 10000:
        status = 5
        #print("Change Lubricant oil")
        #print("Clean Battery")

    return status
def speed_check(speed):
    status = 0
    if speed > 140:
        status = 1
    else:
        status = 0
    return status
def temp_check(temp):
    status = 0
    if temp > 250:
        status = 1
    else:
        status = 0
    return status
def main():
    carId = sys.argv[1]
    speedId = sys.argv[2]
    tempId = sys.argv[3]
    #mileageId = sys.argv[2]
    #fuelId = sys.argv[3]

    #carId = '1'
    #mileageId = '999'
    #fuelId = '94'

    #connect to mysql
    conn = pymysql.connect(host='localhost', 
                        port=3306, 
                        user='VCare_dev', 
                        passwd='!12qwaszx?', 
                        db='VCareDB')
    cur = conn.cursor()

    #get data mileage from database
    sql = "SELECT cs.value FROM sensor s JOIN carSensor cs ON s.sensorId = cs.sensorId WHERE cs.carId = " + carId + " AND cs.sensorId = " + speedId + " ORDER BY cs.id DESC LIMIT 1"
    cur.execute(sql)

    #create data list
    speed_data = []
    temp_data = []

    buffer = 0 

    #convert object to data
    for row in cur:
        buffer = int(row[0])

    #append mileage to data
    speed_data.append(buffer)

    cur = conn.cursor()
    #get data temp from database
    sql = "SELECT cs.value FROM sensor s JOIN carSensor cs ON s.sensorId = cs.sensorId WHERE cs.carId = " + carId + " AND cs.sensorId = " + tempId + " ORDER BY cs.id DESC LIMIT 1"
    cur.execute(sql)
    #convert object to data
    for row in cur:
        buffer = int(row[0])

    #append fuel to data
    temp_data.append(buffer)

    conn.commit()
    
    speed_status = speed_check(speed_data[0])
    temp_status = temp_check(temp_data[0])

    #mileage_status = mileage_check(data[0])
    data_out = '{"status":500, "error":null, "response":[{"speed":'+ str(speed_status) + '},{"temperature":' + str(temp_status) + '}]}'
    data_out = json.dumps(json.loads(data_out))
    print(data_out)
    
    sys.stdout.flush()

    """
    print("Mileage ", data[0])
    print("Fuel ", data[1])
    """
    cur.close()
    conn.close()


# Start process
if __name__ == '__main__':
    main()
