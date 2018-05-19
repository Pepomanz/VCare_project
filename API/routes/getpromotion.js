var express = require("express");
var mysql = require("mysql")
var router_getpromotion = express.Router();

var connection = mysql.createConnection({
    host     : 'localhost',
    user     : 'VCare_dev',
    password : '!12qwaszx?',
    database : 'VCareDB'
})

// var promotionId = 0
router_getpromotion.post('/',async (req,res,next) => {
    //res.send(req.body.mileage);
    console.log("getpromotion")
    //console.log(req.body);

    vendorId = req.body.vendorId
    autoPart = req.body.autoparts;
    startDate = req.body.start;
    stoptDate = req.body.stop;
    price = req.body.price;
    if (price==''){
        price = 0
    }
    // promotionId += 1;
    // console.log("PromotionID",promotionId)
    console.log("VendorId",vendorId)
    console.log("DATA: ",req.body)
    
    let equipId;
    await connection.query("SELECT equipmentId FROM equipment WHERE equipName = '" + autoPart + "'", (error, results, fields) => {
        console.log("result",results)
        // equipId = JSON.stringify(results);
        equipId = results[0].equipmentId
        console.log("equipID", equipId)
        connection.query("INSERT INTO promotion (equipId) VALUES ('" 
                        + equipId +"')"
                        ,(error, results, fields)  => {
                            console.log("promotion inserted")
                            connection.query("INSERT INTO vendorPromotion (vendorId, startDate, stopDate, price) VALUES ('" 
                                            + vendorId + "','" + startDate + "','" + stoptDate + "','" + price +"')"
                                            ,(error, results, fields)  => {
                                                console.log("vendorPromotion inserted")
                            });
        });
    });


    /*

    */
    //res.send("Data inserted to database");
    
    
});

module.exports = router_getpromotion;

