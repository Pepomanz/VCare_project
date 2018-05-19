var express = require('express');
var router_fuel = express.Router();

/* GET users listing. */
router_fuel.get('/', function(req, res, next) {
	connection.query('SELECT s.sensorName, cs.value FROM sensor s JOIN carSensor cs ON s.sensorId = cs.sensorId WHERE s.sensorId = 94 ORDER BY cs.id DESC LIMIT 20', function (error, results, fields) {
	  	if(error){
	  		res.send(JSON.stringify({"status": 500, "error": error, "response": null})); 
	  		//If there is error, we send the error in the error section with 500 status
	  	} else {
  			res.send(JSON.stringify({"status": 200, "error": null, "response": results}));
  			//If there is no error, all is good and response is 200OK.
	  	}
  	});
});

module.exports = router_fuel;
