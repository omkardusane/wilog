var app = require('express')();
var http = require('http').Server(app);
const path = require('path');
var io = require('socket.io')(http);
app.get('/show', function(req, res){
  res.sendFile(path.join(__dirname,'index.html'));
});
io.on('connection', function(socket){
    console.log("| "+new Date()+" | WiLog Server : one User Just connected");
    socket.on('wilog_export', function(msg){
        var now = new Date();
        now.setTime(msg.ts);
        now = now.toString();
        console.log("| "+now+" | "+JSON.stringify(msg));
        msg.stringTs = now ;
        io.emit('wilog_read', msg);
    });
});
http.listen(3000, function(){
  console.log('WiLog listening on port :3000');
});