var static    = require('node-static');
var http      = require('http');
var httpProxy = require('http-proxy');

var fileServer = new static.Server('./build');
var proxy      = httpProxy.createProxyServer({
                   target: {
                     host: '192.168.1.167',
                     port: 5982
                   }
                 });
proxy.on('error', function(e) {
  console.log(e);
});

http.createServer(function (request, response) {
  fileServer.serve(request, response, function (e, res) {
    if (e && (e.status === 404)) { // If the file wasn't found
      console.log("Proxying...");
      request.headers['Host'] = '192.168.1.167:5982';
      request.headers['Accept'] = '*/*';
      request.headers['User-Agent'] = 'woop';
      request.headers['Connection'] = null
      proxy.web(request, response);
    }
  });
}).listen(8080);
