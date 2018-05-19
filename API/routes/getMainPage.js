var express = require('express')
var router_test= express.Router()
var mysql = require("mysql")
var session = require('express-session')
var queryDetail = "Select * FROM sessions Where session_id"
const queryData = "SELECT sensorId,value FROM carSensor data , "
+"(SELECT sensorId AS id,MAX(time) AS maxTime FROM carSensor "
+"WHERE carId = (SELECT carId FROM userCar WHERE userId = "
const queryData2 = " ) AND (sensorId = 47 OR sensorId = 49 OR sensorId = 66 ) GROUP BY sensorId) AS temp WHERE data.sensorId = temp.id "
+"AND data.time = temp.maxTime;"
const queryPicture = "Select "

var connection = mysql.createConnection({
    host     : 'localhost',
    user     : 'VCare_dev',
    password : '!12qwaszx?',
    database : 'VCareDB',
    multipleStatements: true
});

/* GET users listing. */
router_test.get('/', function(req, res, next) {
    console.log("hello"+req.sessionID)
    let queryUserId = queryDetail + "= \"" + req.sessionID +"\""
    connection.query(queryUserId, function(err,result,field){
        let queryMainPage = queryData + result[0].UserId + queryData2

        connection.query(queryMainPage,function(err,result,field){
            let data = {
                Picture : "http://52.221.200.1/img/images.png",
                FuelLevel : result[0].value,
                Mileage  : result[1].value,
                Voltage  : result[2].value
            };
            let responseData = JSON.stringify(data)
            res.send(data)
        })
    })

});

module.exports = router_test;
