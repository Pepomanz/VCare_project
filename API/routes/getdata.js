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


router_getdata.post('/', async function(req,res,next){
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
        res.json(data)
    })

});
module.exports = router_getdata;

