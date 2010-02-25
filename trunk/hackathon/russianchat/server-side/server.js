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
		redis_client1.incr('user_counter',function(err,value){
			redis_client1.close();
			if(err) show_404(req,res,err);else{ 
				show_result(req,res,value);
				sys.puts(value);
			}
			var redis_client2 = new redis.Client();
				redis_client2.sadd('user_presences',value,function(err,v){
				if(!err) sys.puts("saved presence for user "+value+":"+v);
				redis_client2.close();
			});
		});
	});
}

function logon_img_static(req, res, match) {
	var file=null;
	var temp_file='x'+(new Date())+'.jpeg';
	req.setBodyEncoding('binary');
	req.addListener('data', function(chunk) {
		sys.puts("Uploading "+chunk);
		var write=function(err,fileDescriptor){if(!err){req.pause();fs.write(fileDescriptor,chunk);req.resume();}};
		if(file == null){sys.puts("Opening file");file = fs.open(temp_file, process.O_CREAT | process.O_WRONLY, 0600,write);}
		else write(null,file);
	});
	req.addListener('end', function() {
		var redis_client1 = new redis.Client();
		if(file)fs.close(file);
		redis_client1.incr('user_counter',function(err,value){
			redis_client1.close();
			if(err)show_404(req,res,err);else {
				show_result(req,res,value);
				var redis_client2 = new redis.Client();
				redis_client2.sadd('user_presences',value,function(err,v){
					if(!err)sys.puts("saved presence for user "+value+":"+v);
					redis_client2.close();
				});
			};
			fs.rename(temp_file,''+value+'.jpeg');
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
		redis_client1.incr('user_counter',function(err,value){
			redis_client1.close();
			if(err) show_404(req,res); else {
				show_result(req,res,value);
				var redis_client2 = new redis.Client();
				redis_client2.sadd('user_presences',value,function(err,v){
					if(!err)sys.puts("saved presence for user "+value+":"+v);
					redis_client2.close();
				});
			};
			//save image
			if(""!=image){
				redis_client3 = new redis.Client();
				var image_str=base64.encode(image);
				redis_client3.set('user_image_'+value,image_str,function(err,r){
					if(!err)sys.puts("saved image for user "+value+":"+r);
					redis_client3.close();
				});
			}
		});
	});
}

function logoff(req, res, match) {
	var q=url.parse(req.url,true).query;
	if(q&&q.myID){
		var redis_client = new redis.Client();
		redis_client.srem('user_presences',q.myID,function(err,value){
			redis_client.close();
			if(err) show_404(req,res,err); else show_result(req,res,value);
		});
	}
}

function next(req, res, match) {
	var redis_client1 = new redis.Client();
	redis_client1.srandmember('user_presences',function(err,value){
		redis_client1.close();
		if(err)show_404(req,res,err); else { 
			show_result(req,res,value);
			sys.puts('next:'+value);
		}
	});
}

function next_img_static(req, res, match) {
	var redis_client1 = new redis.Client();
	redis_client1.srandmember('user_presences',function(err,value){
		redis_client1.close();
		if(err) show_404(req,res); else {
			sys.puts('next:'+value);
			server.staticHandler(req,res,''+value+'.jpeg');
		};
	});
}

function next_img(req, res, match) {
	var redis_client1 = new redis.Client();
	redis_client1.srandmember('user_presences',function(err,value){
		redis_client1.close();
		if(err)show_404(req,res,err); else { 
			sys.puts('next:'+value);
			var redis_client2 = new redis.Client();
			redis_client2.get('user_image_'+random,function(err,value){
				redis_client2.close();
				if(err)show_404(req,res,err); else{ 
					var image_bin=base64.decode(value);
					show_image(req,res,image_bin);
				}
			});
		}
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
		sys.puts('try to send '+q.fromID+'->'+q.toID+':'+txt);
		var redis_client = new redis.Client();
		redis_client.set('user_message_'+q.toID,q.fromID+':'+txt,function(err,value){
			redis_client.close();
			if(err)show_404(req,res,err); else {
				show_result(req,res,'OK');
				sys.puts('done:'+q.fromID+'->'+q.toID+':'+txt);
			}
		});
	});
}

function poll(req, res, match) {
	var q=url.parse(req.url,true).query;
	var redis_client = new redis.Client();
	redis_client.get('user_message_'+q.myID,function(err,value){
		redis_client.close();
		if(err)show_404(req,res,err); else {
			if(value)show_result(req,res,value);else show_204(req,res,'');
			sys.puts('poll:'+q.myID+':'+(value?value:'204'));
		}
	});
}
function image(req, res, match){
	var q=url.parse(req.url,true).query;
	server.staticHandler(req,res,''+q.myID+'.jpeg');
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

server.get(new RegExp("^/image"), image);
server.post(new RegExp("^/image"), image);

server.listen(8000);


function show_result(req, res, txt) {txt=""+(txt||"");
	res.sendHeader(200, {'Content-Type': 'text/plain','Content-Length': txt.length});
	res.write(""+txt);
	res.close();
	sys.puts('response,closed:'+txt);
}

function show_image(req, res, img) {img=""+(img||"");
	res.sendHeader(200, {'Content-Type': 'image/jpeg','Content-Length': img.length});
	res.write(""+img);
	res.close();
	sys.puts('response,closed:'+img);
}

function show_404(req, res, error) {error=""+(error||"ERROR");
	res.sendHeader(404, {'Content-Type': 'text/plain','Content-Length': error.length});
	res.write(error);
	res.close();
	sys.puts('response,closed:'+error);
}

function show_204(req, res, error) {error=""+(error||"");
	res.sendHeader(204, {'Content-Type': 'text/plain','Content-Length': error.length});
	res.write(error);
	res.close();
	sys.puts('response,closed:'+error);
}
