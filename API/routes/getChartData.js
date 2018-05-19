var express = require("express");
var mysql = require("mysql")
var reouter_getChartData = express.Router();
var PythonShell = require('python-shell');

var connection = mysql.createConnection({
    host     : 'localhost',
    user     : 'VCare_dev',
    password : '!12qwaszx?',
    database : 'VCareDB'
})


reouter_getChartData.post('/', async function(req,res,next){
    console.log("Start getdata")
    carId = req.body.carId
    range_time = req.body.range
    sensor_type = req.body.type

    var options = {
        mode: 'json',
        pythonPath: '/usr/bin/python',
        pythonOptions: ['-u'],
        scriptPath: '/home/ubuntu/www/VcareAPI/routes/analyze',
        //format ['carId','sensorId']
        //args: ['1','13','05']
        args: [carId, range_time, sensor_type]
    };

    //Python part
    var pyshell = new PythonShell('summary_data.py',options);
    var data;
    console.log("Start python")
    const get_data_from_python = (fn) => {
        pyshell.on('message', function (message) {
            fn(message);   
        });
    }
    get_data_from_python((data) => {
        console.log("Send Data",data)
        res.json(data)
    })

});
module.exports = reouter_getChartData;

