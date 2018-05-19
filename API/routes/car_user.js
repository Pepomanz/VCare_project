var express = require('express');
var router_caruser = express.Router();
var http = require('http');
/* GET users listing. */
router_caruser.get('/', function(req, res, next) {
	connection.query('SELECT u.fName,u.lName,u.tel,c.brand,c.model,c.year FROM user u JOIN car c ON c.carId = u.userId LIMIT 20', function (error, results, fields) {
		console.log(results)
	  	if(error){
	  		res.json({"status": 500, "error": error, "results": null}); 
	  		//If there is error, we send the error in the error section with 500 status
	  	} else {
			res.json({"status": 200, "error": null, "results": results})
  			//res.send(JSON.stringify({"time": "02:31:04 PM","milliseconds_since_epoch": 1516631464476,"date": "01-22-2018"}));
  			//If there is no error, all is good and response is 200OK.
	  	}
	  });

});

module.exports = router_caruser;


