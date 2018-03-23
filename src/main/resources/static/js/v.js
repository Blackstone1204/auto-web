//动态全局变量定义
var version="v1.0";
var host="localhost";
var port="9999";
var isAccess=0;//是否第一次加载

var username=getParam("username");


//远端连接设备信息
var dataobj=get("/auto-web/loadDevice",{});
var connectdevice=dataobj.data;

//用户历史任务数据
  //submitTime格式转换
var historyobj=get("/auto-web/loadHistory",{'userName':username});
var taskHistory=historyobj.data;
var list=historyobj.data.tasks;

for(var i=0;i<list.length;i++){
	var old=list[i].submitTime;
	//console.log("old "+old+" type="+typeof old);
	var nw=dateFormat(old+"");
	//console.log("new "+nw);
	list[i].submitTime=nw;
	
}

//用户历史明细数据


//console.log(connectdevice.deviceInfo[0].serial);
//var connectdevice={
//	"deviceInfo":[
//	{
//		"serial":"aaa1",
//		"brand":"xiaomi1",
//		"model":"m4",
//		"version":"4.4.4"
//
//	},
//	{
//		"serial":"bbb",
//		"brand":"sanxing",
//		"model":"GTx 378",
//		"version":"5.0"
//
//	},
//	{
//		"serial":"ccc",
//		"brand":"huawei",
//		"model":"RY 9",
//	    "version":"7.0"		
//
//	}
//
//	]};
