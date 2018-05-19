var express = require("express");
var mysql = require("mysql");
var mysql = require("mysql")
var router_logIn = express.Router();
var queryUserData = "SELECT * FROM user where username =";
var queryCarSensor = "SELECT sensorId,value FROM carSensor data INNER JOIN "
                    +"(SELECT sensorId AS id,MAX(time) AS maxTime FROM carSensor "
                    +"WHERE carId = (SELECT carId FROM userCar INNER JOIN user ON userCar.userId = user.userId) "
                    +"AND (sensorId = 4 OR sensorId = 5 OR sensorId = 13 OR sensorId = 66 OR sensorId = 49 ) GROUP BY sensorId) AS temp ON data.sensorId = temp.id "
                    +"AND data.time = temp.maxTime;";
var queryCardetail = "SELECT brand,model,year FROM car WHERE carId ="
                    + "(SELECT carId FROM userCar INNER JOIN user ON userCar.userId = user.userId);";   

var connection = mysql.createConnection({
    host     : 'localhost',
    user     : 'VCare_dev',
    password : '!12qwaszx?',
    database : 'VCareDB',
    multipleStatements: true
});

router_logIn.post('/', function(req, res, next) {
    console.log(req.body);
    var temp = queryUserData+'\''+req.body.username+'\' AND password =\''+req.body.password+'\';';
    connection.query(temp, function (err, result, fields) {    
        if (err) throw err;
        if(result != "")
        {
            var insert = "INSERT INTO sessions VALUES (\""+req.sessionID+"\",\""+req.session.cookie.expires+"\","+result[0].userId+")"
            connection.query(insert, function(err,result,field){
             console.log("Login"+req.sessionID)
             })
            res.send("complete");          
        }
        else
        {
           console.log("id or password not found");
           res.send("not found");
        }
      });
    //console.log(temp);
    /*connection.query(temp+queryCarSensor+queryCardetail, function (err, result, fields) {
    temp="";
    //console.log(result);
    console.log(result);      
    if (err) throw err;
    if(result[0] != "")
    {
        var data = {
            userId : result[0][0].userId,
            fName : result[0][0].fName,
            lName : result[0][0].lName,
            tel   : result[0][0].tel,
            email : result[0][0].email,
            sensorId4 : result[1][0].value,
            sensorId5 : result[1][1].value,
            sensorId13 : result[1][2].value,
            sensorId49 : result[1][3].value,
            sensorId66 : result[1][4].value,
            brand : result[2][0].brand,
            model : result[2][0].model,
            year : result[2][0].year,
            picture : "http://52.221.200.1/img/images.png",
            result : "complete"
        };
        let responseData = JSON.stringify(data)
        console.log(result);   
        res.send(data);          
    }
    else
    {
       console.log("id or password not found");
       res.send("not found");
    }
  });*/
});

module.exports = router_logIn;