var express = require("express");
var mysql = require("mysql")
var router_getdata = express.Router();
var PythonShell = require('python-shell');
var options = {
  mode: 'json',
  pythonPath: '/usr/bin/python',
  pythonOptions: ['-u'],
  scriptPath: '/home/ubuntu/www/VcareAPI/routes/analyze',
  //format ['carId','sensorId']
  //args: ['1','13','05']
  args: ['1']
};
var connection = mysql.createConnection({
    host     : 'localhost',
    user     : 'VCare_dev',
    password : '!12qwaszx?',
    database : 'VCareDB'
})
// var mileageId = String(999)
// var fuelId = String(94)

const insert_carGPS = (req) => {
    //var req2 = JSON.parse(req)
    
    carId = '1';
    dtc = req.body.DTC;
    distance = req.body.distance;
    latitude = req.body.latitude;
    longitude = req.body.longitude;
    
    connection.query("INSERT INTO carGPS (carId, DTC, latitude, longtitude, distance) VALUES ('" 
    + carId + "','" + dtc + "','" + latitude + "','" + longitude + "','" + distance + "')"
    );
    // console.log("carGPS INSERTED")
    
}
const insert_carSensor = (req) => {
    // console.log(req.body);
    for(var k in req.body) {
        if(k[0] == "p" && k[1] == "i" && k[2] == "d"){
            carId = '1';
            pid = parseInt(k[4]+k[5]);
            value = String(req.body[k])
            connection.query("INSERT INTO carSensor (carId, sensorId, value) VALUES ('" 
            + carId + "','" + pid + "','" + value + "')"
            );
            // console.log("PID_" + pid + " INSERTED")
        }
     }
}

var FCM = require('fcm-node');
var serverKey = ''; //put your server key here
var fcm = new FCM(serverKey);

router_getdata.post('/', async function(req,res,next){
    var fcm_message = { //this may vary according to the message type (single recipient, multicast, topic, et cetera)
        to: '/topics/Alert', 
        collapse_key: 'your_collapse_key',
        
        notification: {
            title: 'Warning Notification', 
            body: '' 
        },
        
        data: {  //you can send only notification or only data(or include both)
            my_key: 'my value',
            my_another_key: 'my another value'
        }
    };
    console.log("Start getdata")
    console.log("receive data",data)
    // var pyshell = new PythonShell('sensor_status.py',options);
    console.log(req.body);
    await insert_carGPS(req);
    await insert_carSensor(req);

    //Python part
    var pyshell = new PythonShell('sensor_status.py',options);
    var data;
    console.log("Start python")
    const my_python = (fn) => {
        pyshell.on('message', function (message) {
            fn(message);   
        });
    }
    my_python((data) => {
        console.log("Send Data",data)
        if (data['response'][0]['load'] == '1'){
            fcm_message['notification']['body'] += 'Load abnormal !! '
        }
        if (data['response'][1]['temp'] == '1'){
            fcm_message['notification']['body'] += 'temp abnormal !! '
        }
        if (data['response'][2]['rpm'] == '1'){
            fcm_message['notification']['body'] += 'rpm abnormal !! '
        }
        if (data['response'][3]['voltage'] == '1'){
            fcm_message['notification']['body'] += 'voltage abnormal !! '
        }
        if (data['response'][4]['speed'] == '1'){
            fcm_message['notification']['body'] += 'speed abnormal !! '
        }
        if (data['response'][5]['fuel'] == '1'){
            fcm_message['notification']['body'] += 'fuel abnormal !! '
        }
        if (data['response'][6]['mileage'] == '1'){
            fcm_message['notification']['body'] += 'mileage abnormal !! '
        }
        // res.json(data)
        console.log(fcm_message)
        fcm.send(fcm_message, function(err, response){
            if (err) {
                console.log("Something has gone wrong!");
            } else {
                console.log("Successfully sent with response: ", response);
            }
        })
    })


});
module.exports = router_getdata;

