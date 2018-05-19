var express = require("express");
var mysql = require("mysql")
var router_traindata = express.Router();

var connection = mysql.createConnection({
    host     : 'localhost',
    user     : 'VCare_dev',
    password : '!12qwaszx?',
    database : 'VCare_traindata'
})

router_traindata.post('/',function(req,res,next){
    //res.send(req.body.mileage);
    //console.log(Object.keys(req.body).length)
    count = Object.keys(req.body).length/4
    console.log(count)

    
    for (i=0;i<count;i++){
        mileage = req.body['mileage'+ i];
        fuel = req.body['fuel'+ i];
        engine = req.body['engine_load'+ i]
        result = req.body['result'+ i]
        connection.query("INSERT INTO dataset(mileage,fuel,engine_load,result) VALUES ('" + mileage + "','" + fuel + "','" + engine + "','" + result +"')");
        
    }
    //res.send("Data added to database");
    res.redirect('http://52.221.200.1/train_data.php');
    
    
    /*
    connection.query("INSERT INTO dataset(mileage,fuel,engine_load,result) VALUES ('" + mileage 
    + "','" + fuel + "','" + engine + "','" + result +"')"
                    );
    res.send(req.body.mileage);
    */
});

module.exports = router_traindata;