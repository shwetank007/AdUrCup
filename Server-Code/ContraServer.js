var app = require('express')();
var server = require('http').createServer(app);
var io = require('socket.io')(server);
var socketids = new Array();
var counter = 0;
app.get('/', function(req,res,next) {
  console.log("Time to play the GAME!!!");
});

io.on('connection', function(socket) {
	console.log("Phone Connected"+socket.id);
	socketids.push(socket.id);
	socket.on('send', function(mssg) {
		counter = 0;
		while(counter < socketids.length) {
			if(socketids[counter] != socket.id){
				socket.to(socketids[counter++]).emit("receive", mssg);	
			}else{counter++;}
			
		}
	});

	socket.on('acceptance', function(mssg) {
		counter = 0;
		while(counter < socketids.length) {
			if(socketids[counter] != socket.id){
				console.log(socketids[counter]);
				socket.to(socketids[counter]).emit("accepted", mssg);
				counter++;	
			}else{counter++;}
			
		}
	});

	socket.on('rejectance', function(mssg) {
		counter = 0;
		while(counter < socketids.length) {
			if(socketids[counter] != socket.id){
				console.log(socketids[counter]);
				socket.to(socketids[counter]).emit("rejected", mssg);
				counter++;	
			}else{counter++;}
			
		}
	});
});

server.listen(process.env.PORT || 8080,function(){});