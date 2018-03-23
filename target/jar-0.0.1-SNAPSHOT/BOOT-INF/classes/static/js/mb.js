//显示登录信息
function topshow(){
console.log("topshow");
var username=getParam("username");
var data={"username":username};
//console.log(data);
var source="<div style='margin-left:80%;'><a href='#'><span class='glyphicon glyphicon-user'></span><span id='username'>{{username}}</span></a>  <a href='#'>退出</a>  <a href='#'>"+version+"</a> </div>";
var render=template.compile(source);
var html=render(data);
//console.log(html);
return html;
}

//显示菜单
function menushow(){
console.log("menushow");
var data={"option":["新建测试任务","测试结果查看","系统管理"]};
//console.log(data);
var source="<nav class='navbar navbar-default' role='navigation'>"
+"<div id='menu'>"
+"<ul class='nav navbar-nav'>"
+"{{each option}}"

+"<li class=''><a href='javascript:loadcontent({{$index}})'>{{$value}}</a><li>"

+"{{/each}}"
+"</ul>"
+"</div>"
+"</nav>";
var render=template.compile(source);
var html=render(data);
//console.log(source);
return html;
}


//显示在线设备信息

function deviceshow(){

//var data={
//"models":["2hfao02","93yfjap29","h783882929"]
//};

var data=jsonajax("/auto-web/loadDevice",{});
//var dsource=
//+"<div class='' style='border:1px solid red;'>"
//+"{{each models}}"
//+"<label><input type='checkbox' name='model'>{{$value}}</label>"
//+"{{/each}}"
//+"</div>";


var source=""
+"<div id='device' class='device'>"
+"{{each models}}"
+"<label class='devicelabel'>{{$value}}</label>"
+"{{/each}}"
+"</div>"
//+"<form enctype='multipart/form-data'>"
//+"<input id='file-0a' name='file' class='file' type='file' data-show-preview='false' multiple data-min-file-count='1'>"
//+"</form>"
//+"<span class='help-block'>请将需要测试的apk和脚本压缩成zip格式后上传，脚本扩展名为.java</span>"
//+"<input type='button' class='btn btn-primary' value='提交任务'>";


var render=template.compile(source);
var html=render(data);

return html;

}

//显示测试结果

function resultshow(){
console.log("resultshow");
var data={
"tasks":[
	{
	"submitTime":"2017-09-23 13:45:12",
	"serials":";8fh3333;hjfj9390303;kk939f",
	"scriptName":"test.zip",
	"tag":"自动化测试",
	"method":"appium",
	"result":"pass"
},

	{
	"submitTime":"2017-12-23 23:12:56",
	"serials":";8fh3333;hjfj9390303;kk939f",
	"scriptName":"test2.zip",
	"tag":"自动化测试",
	"method":"appium",
	"result":"pass"
},
		{
	"submitTime":"2017-09-23 13:45:12",
	"serials":";8fh3333;hjfj9390303;kk939f",
	"scriptName":"test3.zip",
	"tag":"自动化测试",
	"method":"robotium",
	"result":"fail"
},

	{
	"submitTime":"2015-04-12 07:45:03",
	"serials":";8fh3333;hjfj9390303;kk939f",
	"scriptName":"test4.zip",
	"tag":"自动化测试",
	"method":"appium",
	"result":"pass"
}
	
	


]
	

};
//console.log("data");
var source="<table class='table table-striped'>"
+"<thead>"
+"<tr><th>序号</th><th>提交时间</th><th>测试手机</th><th>脚本名称</th><th>简述</th><th>脚本分类</th><th>测试结果</th><th>操作</th></tr>"
+"</thead>"
+"<tbody>"
+"{{each tasks}}"
+"<tr>"
+"<td>{{$index+1}}</td><td>{{$value.submitTime}}</td><td>{{$value.serials}}</td><td>{{$value.scriptName}}</td><td>{{$value.tag}}</td><td>{{$value.method}}</td><td class='{{$value.result}}'>{{$value.result}}</td><td><a href='javascript:loadcontent(3)'>明细</a></td>"
+"</tr>"
+"{{/each}}"
+"</tbody>"
+"</table>";

var render=template.compile(source);
var html=render(data);
//console.log(html);
return html;
}



//显示测试明细
function detailshow(){
console.log("detailshow");
var data={
"subTasks":[
	{
	"startTime":"2016-08-07 12:45:34",
	"endTime":"2016-08-07 13:15:34",
	"serial":"7937hf",
	"model":"小米4",
	"scriptName":"test.java",
	"result":"pass"

},
	{
	"startTime":"2016-11-14 15:45:34",
	"endTime":"2016-08-07 14:18:34",
	"serial":"u83993fajfj",
	"model":"vivo",
	"scriptName":"test.java",
	"result":"pass"

}
	

]

};

//console.log(data);

var source=
//测试结果展现
"<spant style='margin-left:85%;'><a href='#' class='total'>Total 5</a> <a href='#' class='pass'>Pass 4</a> <a href='#' class='fail'>Fail 1</a></span>"
+"<table class='table table-striped'>"
+"<thead>" 
+"<tr><th>序号</th><th>开始时间</th><th>结束时间</th><th>serial</th><th>model</th><th>脚本名称</th><th>测试结果</th><th>操作</th></tr>"
+"</thead>"
+"<tbody>"
+"{{each subTasks}}"
+"<tr>"
+"<td>{{$index+1}}</td><td>{{$value.startTime}}</td><td>{{$value.endTime}}</td><td>{{$value.serial}}</td><td>{{$value.model}}</td><td>{{$value.scriptName}}</td><td class='{{$value.result}}'>{{$value.result}}</td><td><a href='#'>运行日志</a></td><td><a href='#'>截图</a></td><td><a href='javascript:loadcontent(5)'>设备曲线</a></td>"
+"</tr>"
+"{{/each}}"
+"</tbody>"
+"</table>"

;

var render=template.compile(source);
var html=render(data);
//console.log(html);

return html;

}


function chartshow(){
console.log("chartshow");
var data={};
var source=
	//添加chartjs图形

//+"<div><canvas id='chart'></canvas></div>"


+"<script src='js/chartjs/chart.js'></script>"

+"<script src='js/chartjs/chartshow.js'></script>"
;

//console.log(source);

return source;

}




