var express = require('express');
var router_alert = express.Router();
/*
var PythonShell = require('python-shell');
var options = {
  mode: 'json',
  pythonPath: '/usr/bin/python',
  pythonOptions: ['-u'],
  scriptPath: '/home/ubuntu/www/VcareAPI/routes/analyze',
  //format ['carId','sensorId']
  // args: ['1','13','05']
  // args: ['1']
};

var pyshell = new PythonShell('sensor_status.py',options);
var data;
pyshell.on('message', function (message) {
  // received a message sent from the Python script (a simple "print" statement)
  data = message;
  router_alert.get('/', function(req, res, next) {
    console.log("alert start!!")
    var pyshell = new PythonShell('sensor_status.py',options);
    pyshell.on('message', function (message) {
      data = message;
      res.json(data)
    });
  });
});

// end the input stream and allow the process to exit
pyshell.end(function (err) {
})*//*
var FCM = require('fcm-node');
var serverKey = 'AAAA4Cqdmsc:APA91bHq8pvdZUJutH0aTH9oIhTQT12qMeNC5wIiz6o1Uo6XO8PpD7Fj4VmoT-BBt0cdn5Pb9Q84SDYeF6dNIaOHpb6dDiuxzAkylKjAeZlL2WwCxr5prfVbs2YBObIn3_zpV6ko9T2K'; //put your server key here
var fcm = new FCM(serverKey);

var message = { //this may vary according to the message type (single recipient, multicast, topic, et cetera)
    to: '/topics/Alert', 
    collapse_key: 'your_collapse_key',
    
    notification: {
        title: 'Warning Notification', 
        body: 'Test notification' 
    },
    
    data: {  //you can send only notification or only data(or include both)
        my_key: 'my value',
        my_another_key: 'my another value'
    }
};

router_alert.get('/', function(req, res, next) {
  console.log("hello")
 /* fcm.send(message, function(err, response){
    if (err) {
        console.log("Something has gone wrong!");
    } else {
        console.log("Successfully sent with response: ", response);
    }
});*/
  //res.send("hello")
//});*/
module.exports = router_alert;
