
var express = require("express");
var app = express();
app.use(express.logger());

app.get('/', function(request, response) {
  response.send('Hey Ma, look no hands!');
});

// server static files for urls with '/static' prefix
app.use('/static', express.static(__dirname + '/public'));

var port = process.env.PORT || 5000;
app.listen(port, function() {
  console.log("Listening on " + port);
});


var dburl = process.env.DATABASE_URL || "postgresql://localhost"
if (dburl) {
    var pg = require('pg');
    pg.connect(dburl, function(err, client) {
        if (!err) {
            /*
            var query = client.query('SELECT * FROM your_table');
            query.on('row', function(row) {
                console.log(JSON.stringify(row));
            });
            */
        } else {
            console.log(err)
        }
    });
}