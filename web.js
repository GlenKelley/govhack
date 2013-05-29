/*
var express = require("express");
var app = express();
app.use(express.logger());

app.get('/', function(request, response) {
  response.send('Hey Ma, look no hands!');
});

var port = process.env.PORT || 5000;
app.listen(port, function() {
  console.log("Listening on " + port);
});
*/

var dburl = process.env.DATABASE_URL
if (dburl) {
    var pg = require('pg');
    pg.connect(dburl, function(err, client) {
      var query = client.query('SELECT * FROM your_table');

      query.on('row', function(row) {
        console.log(JSON.stringify(row));
      });
    });
}

var static = require('node-static'),
  http = require('http'),
  util = require('util');
var webroot = './public',
  staticPort = 5001;
var file = new(static.Server)(webroot, {
  cache: 600,
  headers: { 'X-Powered-By': 'node-static' }
});
http.createServer(function(req, res) {
    file.serve(req, res, function(err, result) {
      if (err) {
        console.error('Error serving %s - %s', req.url, err.message);
        if (err.status === 404 || err.status === 500) {
          file.serveFile(util.format('/%d.html', err.status), err.status, {}, req, res);
        } else {
          res.writeHead(err.status, err.headers);
          res.end();
        }
      } else {
        console.log('%s - %s', req.url, res.message);
      }
  });
}).listen(staticPort);
console.log('node-static running at http://localhost:%d', staticPort);
