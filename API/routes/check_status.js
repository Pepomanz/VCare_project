var express = require('express');
var router_check_status = express.Router();
var http = require('http');
var options = {
    mode: 'json',
    pythonPath: '/usr/bin/python',
    pythonOptions: ['-u'],
    scriptPath: '/home/ubuntu/www/VcareAPI/routes',
    //format ['carId','sensorId']
    // args: ['1','13','05']
    args: ['1']
};
/* GET users listing. */
var pyshell = new PythonShell('sensor_status.py',options);
var data;
router_check_status.get('/', function(req, res, next) {
    console.log("Check status")
});

module.exports = router_check_status;


