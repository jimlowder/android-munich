var http = require('http');
var multipart = require('multipart');
var redis = require("./redisclient");
var sys = require('sys');
var url = require('url');
var server = require('./node-router');
var base64 = require('./base64');
var fs = require("fs");



function logon(req, res, match) {
	var image="";
	req.setBodyEncoding('binary');
	
	req.addListener('data', function(chunk) {
		sys.print("Uploading "+chunk);
		image+=chunk;
	});
	req.addListener('end', function() {
		var redis_client1 = new redis.Client();
		redis_client1.incr('user_counter').addCallback(function (value) {
			show_result(req,res,value);
			sys.puts(value);
			redis_client1.close();
			
			var redis_client2 = new redis.Client();
			redis_client2.sadd('user_presences',value).addCallback(function (v) {
				sys.puts("saved presence for user "+value+":"+v);
				redis_client2.close();
			}).addErrback(function (error) {
				redis_client2.close();
			});
		}).addErrback(function (error) {
			show_404(req,res);
			redis_client1.close();
		});
	});
}

function logon_img_static(req, res, match) {
	var file=null;
	var temp_file='x'+(new Date())+'.jpeg';
	req.setBodyEncoding('binary');
	req.addListener('data', function(chunk) {
		sys.puts("Uploading "+chunk);
		if(file == null){sys.puts("Opening file");file = fs.open(temp_file, process.O_CREAT | process.O_WRONLY, 0600);}
		file.addCallback(function(fileDescriptor) {
			req.pause();
			fs.write(fileDescriptor,chunk);
			req.resume();
		});
	});
	req.addListener('end', function() {
		var redis_client1 = new redis.Client();
		if(file)fs.close(file);
		redis_client1.incr('user_counter').addCallback(function (value) {
			redis_client1.close();
			show_result(req,res,value);
			var redis_client2 = new redis.Client();
			redis_client2.sadd('user_presences',value).addCallback(function (v) {
				sys.puts("saved presence for user "+value+":"+v);
				redis_client2.close();
			}).addErrback(function (error) {
				redis_client2.close();
			});
			fs.rename(temp_file,''+value+'.jpeg');
			
		}).addErrback(function (error) {
			show_404(req,res);
			redis_client1.close();
		});
	});
}

function logon_img(req, res, match) {
	var image="";
	req.setBodyEncoding('binary');
	req.addListener('data', function(chunk) {
		sys.print("Uploading "+chunk);
		image+=chunk;
	});
	req.addListener('end', function() {
		var redis_client1 = new redis.Client();
		redis_client1.incr('user_counter').addCallback(function (value) {
			redis_client1.close();
			show_result(req,res,value);
			var redis_client2 = new redis.Client();
			redis_client2.sadd('user_presences',value).addCallback(function (v) {
				sys.puts("saved presence for user "+value+":"+v);
				redis_client2.close();
			}).addErrback(function (error) {
				redis_client2.close();
			});
			//save image
			if(""!=image){
				redis_client3 = new redis.Client();
				var image_str=base64.encode(image);
				redis_client3.set('user_image_'+value,image_str).addCallback(function (r) {
					sys.puts("saved image for user "+value+":"+r);
					redis_client3.close();
				}).addErrback(function (error) {
					redis_client3.close();
				});
			}
		}).addErrback(function (error) {
			show_404(req,res);
			redis_client1.close();
		});
	});
}

function logoff(req, res, match) {
	var q=url.parse(req.url,true).query;
	if(q&&q.myID){
		var redis_client = new redis.Client();
		redis_client.srem('user_presences',q.myID).addCallback(function (value) {
			show_result(req,res,value);
			redis_client.close();
		}).addErrback(function (error) {
			show_404(req,res);
			redis_client.close();
		});
	}
}

function next(req, res, match) {
	
	var redis_client1 = new redis.Client();
	redis_client1.srandmember('user_presences').addCallback(function (value) {
		redis_client1.close();
		show_result(req,res,value);
		sys.puts('next:'+value);
	}).addErrback(function (error){
		show_404(req,res);
		redis_client1.close();
	});
}

function next_img_static(req, res, match) {
	var redis_client1 = new redis.Client();
	redis_client1.srandmember('user_presences').addCallback(function (value) {
		redis_client1.close();
		sys.puts('next:'+value);
		server.staticHandler(req,res,''+random+'.jpeg');
	}).addErrback(function (error) {
		show_404(req,res);
		redis_client1.close();
	});
}

function next_img(req, res, match) {
	var redis_client1 = new redis.Client();
	redis_client1.srandmember('user_presences').addCallback(function (value) {
		redis_client1.close();
		sys.puts('next:'+value);
		var redis_client2 = new redis.Client();
		redis_client2.get('user_image_'+random).addCallback(function (value) {
			var image_bin=base64.decode(value);
			show_image(req,res,image_bin);
			redis_client2.close();
		}).addErrback(function (error) {
			show_404(req,res);
			redis_client2.close();
		});
	}).addErrback(function (error) {
		show_404(req,res);
		redis_client1.close();
	});
}

function message(req, res, match) {
	var txt='';
	req.addListener('data', function(chunk) {
		sys.puts('message-chunk:'+chunk);
		txt+=chunk;
	});
	req.addListener('end', function() {
		var q=url.parse(req.url,true).query;
		if(txt==''&&q.txt)txt=q.txt;
		var redis_client = new redis.Client();
		redis_client.set('user_message_'+q.toID,q.fromID+':'+txt).addCallback(function (value) {
			sys.puts(''+q.fromID+'->'+q.toID+':'+txt);
			show_result(req,res,value);
			redis_client.close();
		}).addErrback(function (error) {
			show_404(req,res);
			redis_client.close();
		});
	});
}

function poll(req, res, match) {
	var q=url.parse(req.url,true).query;
		var redis_client = new redis.Client();
		redis_client.get('user_message_'+q.myID).addCallback(function (value) {
			redis_client.close();
			if(value)show_result(req,res,value);else show_204(req,res,'');
			sys.puts('poll:'+q.myID+':'+(value?value:'204'));
		}).addErrback(function (error) {
			redis_client.close();
			show_404(req,res);
		});
}


server.get(new RegExp("^/logon$"), logon);
server.post(new RegExp("^/logon$"), logon);

server.get(new RegExp("^/logoff$"), logoff);
server.post(new RegExp("^/logoff$"), logoff);

server.get(new RegExp("^/logon-img$"), logon_img);
server.post(new RegExp("^/logon-img$"), logon_img);

server.get(new RegExp("^/logon-img-static$"),logon_img_static);
server.post(new RegExp("^/logon-img-static$"),logon_img_static);


server.get(new RegExp("^/next$"), next);
server.post(new RegExp("^/next$"), next);

server.get(new RegExp("^/next-img$"), next_img);
server.post(new RegExp("^/next-img$"), next_img);

server.get(new RegExp("^/next-img-static$"),next_img_static);
server.post(new RegExp("^/next-img-static$"),next_img_static);

server.get(new RegExp("^/message$"), message);
server.post(new RegExp("^/message$"), message);

server.get(new RegExp("^/poll$"), poll);
server.post(new RegExp("^/poll$"), poll);

server.listen(8000);


function show_result(req, res, txt) {txt=""+txt||"";
	res.sendHeader(200, {'Content-Type': 'text/plain','Content-Length': txt.length});
	res.write(""+txt);
	res.close();
	sys.puts('response,closed:'+txt);
}

function show_image(req, res, img) {img=""+img||"";
	res.sendHeader(200, {'Content-Type': 'image/jpeg','Content-Length': img.length});
	res.write(""+img);
	res.close();
	sys.puts('response,closed:'+img);
}

function show_404(req, res, error) {error=""+error||"ERROR";
	res.sendHeader(404, {'Content-Type': 'text/plain','Content-Length': error.length});
	res.write(error);
	res.close();
	sys.puts('response,closed:'+error);
}

function show_204(req, res, error) {error=""+error||"";
	res.sendHeader(204, {'Content-Type': 'text/plain','Content-Length': error.length});
	res.write(error);
	res.close();
	sys.puts('response,closed:'+error);
}
