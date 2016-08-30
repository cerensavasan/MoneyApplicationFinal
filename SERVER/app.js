var express = require('express');
var bodyParser = require('body-parser');
var cors = require('cors');
var app = express();


app.use(cors());

var port = process.env.PORT || 8080;
var listenIP = process.env.LISTENIP || "localhost";

app.use(bodyParser.json());
app.use(bodyParser.urlencoded({
    extended: true
}));

app.listen(port, function () {
    console.log('Listening on port ' + port);
});


var traderRouter = require('./routes/trader');
var pmRouter = require('./routes/pm');


app.use('/api/trader', traderRouter);
app.use('/api/pm', pmRouter);

app.use('/', function () {
    res.send('Welcome');
});
