var express = require("express");
var mysql = require("mysql");
var mysql = require("mysql")
var router_register = express.Router();

var connection = mysql.createConnection({
    host     : 'localhost',
    user     : 'VCare_dev',
    password : '!12qwaszx?',
    database : 'VCareDB'
});

router_register.post('/', function(req, res, next) {
    console.log(req.body);
    var sql = "INSERT INTO user (username, password, fName, lName, email) VALUES ('" 
    +req.body.username+"\', \'"+req.body.password+"\', \'"+req.body.fname+"\', \'"+req.body.lname+
    "\', \'"+req.body.email+"\')";
    //console.log(sql);
   connection.query(sql, function (err, result, fields) {
      if (err) throw err;
        console.log(result.fName);
        res.send("Uploaded");
    });
});

module.exports = router_register;