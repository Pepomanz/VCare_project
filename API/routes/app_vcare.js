var express = require('express')
var path = require('path')
var favicon = require('serve-favicon')
var logger = require('morgan')
var cookieParser = require('cookie-parser')
var bodyParser = require('body-parser')
var mysql= require('mysql')
var http = require('http')
var PythonShell = require('python-shell')
var cors = require('cors')
var session = require('express-session')
var MySQLStore = require('express-mysql-session')(session)


var index = require('./routes/index')
var fuel = require('./routes/fuel')
var alert = require('./routes/alert')
var getdata = require('./routes/getdata')
var traindata = require('./routes/traindata')
var car_user = require('./routes/car_user')
var getpromotion = require('./routes/getpromotion')
var logIn = require('./routes/logIn')
var register = require('./routes/register')
var test = require('./routes/test')
var sessionStore ;

var app = express()
// view engine setup
app.set('views', path.join(__dirname, 'views'))
app.set('view engine', 'ejs')

// uncomment after placing your favicon in /public
//app.use(favicon(path.join(__dirname, 'public', 'favicon.ico')));
app.use(logger('dev'))
app.use(cors())
app.use(bodyParser.json())
app.use(bodyParser.urlencoded({ extended: false }))
app.use(cookieParser())
//Option
var options = {
  host     : 'localhost',
  user     : 'VCare_dev',
  password : '!12qwaszx?',
  database : 'VCareDB'
};
var optionSession = {
  host     : 'localhost',
  user     : 'VCare_dev',
  password : '!12qwaszx?',
  database : 'VCareDB',
  charset: 'utf8_unicode_ci	',
  schema: {
    tableName: 'sessions',
    columnNames: {
        session_id: 'session_id',
        expires: 'expires',
        data: 'data'
    }
 }
}
//Database connection
app.use(function(req, res, next){
	global.connection = mysql.createConnection(options);
  connection.connect();
	next();
})
sessionStore = new MySQLStore(optionSession);
app.use(session({
  secret: 'vcare',
  store: sessionStore,
  resave: false,
  saveUninitialized: true,
  cookie: { maxAge: 60000 }
}))
app.use(express.static(path.join(__dirname, 'public')))

 
app.use('/', index)
app.use('/fuel', fuel)
//app.use('/alert',alert)
app.use('/getdata',getdata)
app.use('/traindata',traindata)
app.use('/car_user',car_user)
app.use('/getpromotion',getpromotion)
app.use('/logIn',logIn)
app.use('/register',register)
app.use('/test',test)

module.exports = app;
var server = http.createServer(app);
server.listen(8081);


/*
// catch 404 and forward to error handler
app.use(function(req, res, next) {
  var err = new Error('Not Found');
  err.status = 404;
  next(err);
});

// error handler
app.use(function(err, req, res, next) {
  // set locals, only providing error in development
  res.locals.message = err.message;
  res.locals.error = req.app.get('env') === 'development' ? err : {};

  // render the error page
  res.status(err.status || 500);
  res.render('error');
});
*/
