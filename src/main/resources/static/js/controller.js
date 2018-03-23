$(function(){

   // alert("page load");


	loadtop();
	loadmenu();
	loadcontent(0);
	
	
})
function loadtop(){

	$("#top").html(topshow());



}

function loadmenu(){

    $("#menu").html(menushow());


}



function loadcontent(index,param){

//
   isAccess=isAccess+1;
   if(index==0&&isAccess>1){
	   var jump="http://"+host+":"+port+"/auto-web/main?username="+$("#username").text();
	   window.location.href=jump;
	   
   }
//菜单选中样式修改 排除 非菜单序号
   if(index!=4){
	   $("#menu").find("li").siblings().removeClass("active");

	   $("#menu").find("li").eq(2*index).addClass("active");
   }

   console.log("点击菜单 index = "+index);


//
	console.log("load page. index="+index);
	
	
	var taskhtml=newtaskshow(connectdevice);
	var resulthtml=resultshow(taskHistory);
	var detailhtml=detailshow(param);
	var devicehtml=deviceshow();
	var charthtml=chartshow();
	var map={
	"0":taskhtml,
	"1":resulthtml,
	"2":devicehtml,
	"3":"",
    "4":detailhtml,
	"5":charthtml
	
	};

	//console.log(map[index]);

	$("#content").html(map[index]);
	
	uploadinit();
	
	if(index===2){
		cssdevice();
	}



}

//同步方式get请求接口 解析返回json对象指定属性
function get(url,param){
	
	var obj;
	   $.ajax(
				  { 
				  url:url,
				  type:'get',
				  timeout:3000,
				  data:param,
				  async:false,
				  success:function(data,status){
					  
					  console.log("type="+typeof data);
					  obj= data;


				  },
				  error:function (data, status, e){
			             
			              console.log(e);
			          }
					  
				  });
	   
	   return obj;
	
	
}


//异步方式get请求接口 
function get2(url,param){
	
	var obj;
	   $.ajax(
				  { 
				  url:url,
				  type:'get',
				 // timeout:3000,
				  data:param,
				  async:true,
				  success:function(data,status){
					  console.log("code="+data.code+" status="+status);
					  

				  },
				  error:function (data, status, e){
			             
			              console.log(e);
			          }
					  
				  });
	
	
}


function get3(url,param,f){
	
	var obj;
	   $.ajax(
				  { 
				  url:url,
				  type:'get',
				  data:param,
				  async:true,
				  success:function(data,status){
					  console.log("code="+data.code+" status="+status);
					  
					  f(data);
					  

				  },
				  error:function (data, status, e){
			             
			              console.log(e);
			          }
					  
				  });
	
	
}

//设备跳转到控制页面
function jumpto(url){

	//初始化
	var serial=getParam2("serial",url);
	get3("/auto-web/projectionInit",{"serial":serial},openurl);



}

function openurl(data){
	var delay=5000;
	var url=data.url;
	var userName=getParam("username");
	url+="&userName="+userName;
	console.log("延时5000ms 打开url "+url);
	
	///sleep(5000);
	window.open(url);
	
	
}
//

function cssdevice(){
	console.log("设备显示设置");
	var width=document.body.offsetWidth;
	console.log("width="+width);
	var scale=1.5;
	
	//设置设备图片宽高
	var aw=width/20;
	var ah=aw*scale;
	console.log("单个设备图片显示域宽 ="+aw +" 高="+ah);
	
	//设置单个设备显示域外框宽度高
	var bw=width/10;
	
	//计算设备图片外框距离最外层边框距离
	var cw=(bw-aw)/2;
	
	//$(".device-online").css("width",aw);
	$(".device-online").css("padding","0");
	$(".device-online").css("text-align","center");
	$(".device-online").css("float","left");
	$(".device-online").css("margin-left","12px");
	$(".device-online").css("border","2px solid #c0c0c0");
	$(".device-online").css("border-radius","5px");	
	$(".device-online").css("width",bw+4);
	$(".device-online").css("padding-top","10px");
	$(".device-online").css("padding-bottom","10px");
	//$(".device-online").css("height",ah+2);
	
	$("div[id^=img-show]").css("padding","0");
	$("div[id^=img-show]").css("width",aw);
	$("div[id^=img-show]").css("height",ah);
	$("div[id^=img-show]").css("margin-left",cw);
	//$("div[id^=img-show]").css("margin-top","10px");
	
	$("img").css("width","100%");
	$("img").css("height","100%");
	
	$("div[id^=exp]").css("padding","0");
	$("div[id^=exp]").css("width",bw);
	$("div[id^=exp]").css("margin-top","10px");
	//$("div[id^=exp]").css("margin-bottom","10px");
	//$("div[id^=exp]").css("height",ah);
	
	
	
}
//
//获取该js所在页面通过url传入的指定参数
function getParam(name){
    var reg = new RegExp("(^|&)"+ name +"=([^&]*)(&|$)");
    var r = window.location.search.substr(1).match(reg);
    if(r!=null)return  unescape(r[2]); return null;
}


function getParam2(name,url){
    var reg = new RegExp("(^|&)"+ name +"=([^&]*)(&|$)");
    var r = url.substr(1).match(reg);
    if(r!=null)return  unescape(r[2]); return null;
}


//时间格式转化
function dateFormat(val) {
    if (val != null) {
    	val=val+"";
        var date = new Date(parseInt(val.replace("/Date(", "").replace(")/", ""), 10));
        //月份为0-11，所以+1，月份小于10时补个0
        var month = date.getMonth() + 1 < 10 ? "0" + (date.getMonth() + 1) : date.getMonth() + 1;
        var currentDate = date.getDate() < 10 ? "0" + date.getDate() : date.getDate();
        var hour=date.getHours()<10?"0"+date.getHours():date.getHours();
        var minute=date.getMinutes()<10?"0"+date.getMinutes():date.getMinutes();
        var second=date.getSeconds()<10?"0"+date.getSeconds():date.getSeconds();

        return date.getFullYear() + "-" + month + "-" + currentDate+" "+hour+":"+minute+":"+second;
    }
    return "";
}

  

function sleep(d){  
	
	var t = Date.now();
	console.log("sleep start="+t);
    while(Date.now - t <= d){
    	
    };  
}  