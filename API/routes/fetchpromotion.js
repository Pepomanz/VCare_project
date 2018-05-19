var express = require('express');
var router_fetchpromotion = express.Router();
var http = require('http');
var mysql = require('mysql')
var pool  = mysql.createPool({
	connectionLimit : 10,
	database		: 'VCareDB',
	host            : 'localhost',
	user            : 'VCare_dev',
	password        : '!12qwaszx?'
  });
var count = 0;
/* GET users listing. */

router_fetchpromotion.get('/', function(req, res, next) {
	pool.getConnection(function(err, connection) {	
		connection.query( 'SELECT p.promotionId, e.equipName, vp.startDate, vp.stopDate, vp.price FROM promotion p JOIN equipment e ON p.equipId = e.equipmentId JOIN vendorPromotion vp ON vp.promotionId = p.promotionId', function (error, results, fields) {

			if(pool._freeConnections.indexOf(connection)){
				console.log("Promotion",results)
				res.json({"status": 200, "error": null, "results": results})
			} // -1
			connection.release();	
		//console.log(pool._freeConnections.indexOf(connection)); // 0
		});
	});	
});

module.exports = router_fetchpromotion;


