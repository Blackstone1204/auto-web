//显示登录信息
function topshow(){
console.log("topshow");
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
var data={"option":["新建测试任务","测试结果查看","在线设备","系统管理"]};
console.log(data);
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
console.log(source);
return html;
}
//显示新建测试任务页面

function newtaskshow(data){
	// var data={
	// 	"deviceInfo":[
	// 	{
	// 		"serial":"aaa",
	// 		"brand":"xiaomi",
	// 		"model":"m4",
	// 		"version":"4.4.4"

	// 	},
	// 	{
	// 		"serial":"bbb",
	// 		"brand":"sanxing",
	// 		"model":"GTx 378",
	// 		"version":"5.0"

	// 	},
	// 	{
	// 		"serial":"ccc",
	// 		"brand":"huawei",
	// 		"model":"RY 9",
	// 	    "version":"7.0"		

	// 	}

	// 	]};
	var source=""
	+"<div id='new-task' class='container'>"
	+"  <div id='step-one'>"

	+"    <div class='form-group row run-method'>"
	+"      <label for='method' class='col-md-1 control-label small'>脚本类型</label>"
	+"      <div id='appium' class='col-md-2 method selected' >"
	+"        <p>appium</p>"
	+"        <small><em>兼容性强 支持ios&android</em></small>"
	+"      </div>"
	+"    <div id='robotium' class='col-md-2 method'>"
	+"      <p>robotium</p>"
	+"      <small><em>支持android 运行速度较快</em></small>"
	+"    </div>"
	+"    <div id='monkey' class='col-md-2 method' >"
	+"      <p>Monkey</p>"
	+"      <small><em>稳定性测试</em></small>"
	+"    </div>"
	+"  </div>"
	+"    <p></p>"
	+"    <div class='form-group row'>"
    +"      <label for='uploadfile' class='col-md-1 control-label small'>脚本上传</label>"	
    +"      <div class='col-md-11'>"	
	+"      <input id='uploadfile' class='file' name='file' type='file' data-show-preview='false'>"	
	+"      </div>"
	+"    </div>"
	+"    <p></p> "
    +"  <div class='form-group  form-group-sm row'>"
    +"    <label for='usefor' class='col-md-1 control-label'>脚本作用</label>"
    +"    <div class='col-md-11'>"
    +"    <input type='text' class='form-control' id='usefor' placeholder='自动化测试'></div>"
    +"  </div>"
	+"    <p></p> "
	+"    <div id='monkey-input' class='form-group form-group-sm row hidden'>"
	+"    <label for='monkey-cmd' class='col-md-1 control-label '>monkey</label>"
	+"      <div class='col-md-11'>"
	+"      <input type='email' class='form-control' id='monkey-cmd' placeholder='adb monkey -s serial -p activityName -v 200'>"
	+"      </div>"
	+"    </div>"
	+"    <p></p>"
	+"    <div id='account' class='form-group  form-group-sm row hidden'>"
	+"      <label for='email' class='col-md-1 control-label'>账号信息</label>"
	+"      <div class='col-md-11'><input type='email' class='form-control' id='email' placeholder='输入形式为用户名|密码'></div>"
	+"    </div>"
	+"    <p></p>"
	+"    <div class='form-group  form-group-sm row'>"
	+"      <label for='email' class='col-md-1 control-label'>邮箱地址</label>"
	+"      <div class='col-md-11'><input type='email' class='form-control' id='email' placeholder='hujianjian@zhexinit.com'></div>"
	+"    </div>"
	+"    <div style='text-align:left;'><button id='next' class='btn btn-info btn-sm'>下一步</button></div>"
	+"  </div>"
	+""
    +"  <div id='step-two' >"
	+"    <div id='brand' class= 'form-group form-group-sm row'>"
	+"      <label for='brand' class='col-md-1 control-label control-label-sm'>品牌</label>"
	+"      <div class='checkbox checkbox-info checkbox-inline small'>"
	+"        <input type='checkbox' class='styled' id='all—brand' value='all—brand' checked='checked' disabled='disabled'>"
	+"        <label for='all-brand'>全部</label>"
	+"      </div>"
	+"{{each deviceInfo}}"
	+"      <div class='checkbox checkbox-info checkbox-inline small'>"
	+"        <input type='checkbox' class='styled' id='{{$value.brand+$index}}' value='option1'>"
	+"        <label for='{{$value.brand+$index}}'>{{$value.brand}}</label>"
	+"      </div>"
	+"{{/each}}"	
	+"    </div>"
	+"  <p></p>"
	+"    <div id='os' class='form-group form-group-sm row'>"
	+"      <label for='brand' class='col-md-1 control-label'>系统版本</label>"
	+"      <div class='checkbox checkbox-info checkbox-inline small'>"
	+"        <input type='checkbox' class='styled' id='all—version' value='all-version' checked='checked' disabled='disabled'>"
	+"        <label for='all-version'>全部</label>"
	+"      </div>  "
	+"{{each deviceInfo}}"
	+"    <div class='checkbox checkbox-info checkbox-inline small'>"
	+"      <input type='checkbox' class='styled' id='{{$value.version+$index}}' value='option1'>"
	+"      <label for='{{$value.version+$index}}'>{{$value.version}}</label>"
	+"    </div>"
	+"{{/each}}"
	+"  </div>"
	+"  <p></p>"

	+"    <div id='os' class='form-group form-group-sm row'>"
	+"      <label for='brand' class='col-md-1 control-label'>分辨率</label>"
	+"      <div class='checkbox checkbox-info checkbox-inline small'>"
	+"        <input type='checkbox' class='styled' id='all—resolution' value='all-resolution' checked='checked' disabled='disabled'>"
	+"        <label for='all-resolution'>全部</label>"
	+"      </div>  "
	+"{{each deviceInfo}}"
	+"    <div class='checkbox checkbox-info checkbox-inline small'>"
	+"      <input type='checkbox' class='styled' id='{{$value.resolution+$index}}' value='option1'>"
	+"      <label for='{{$value.resolution+$index}}'>{{$value.resolution}}</label>"
	+"    </div>"
	+"{{/each}}"
	+"  </div>"
	
	
	
	
	
	+"  <div class='row'>"
	+"    <div  class='col-md-3 source-dropdownlist'>"
	+"{{each deviceInfo}}"
	+"      <p class='device-item'><a href='#'>{{$value.model}}</a><a href='#' style='display:none;'>{{$value.serial}}</a></p>"
	+"{{/each}}"
	+"    </div>"
	+"    <div class='col-md-1 addto'>添加到</div>"
	+"    <div  class='col-md-3 dest-dropdownlist'></div>"	
	+"  </div>"		
	+"  <div style='text-align:left;'>"
	+"    <button id='back' class='btn btn-info btn-sm'>上一步</button>"
	+"    <button id='submit' class='btn btn-info btn-sm'>提交测试</button>"
	+"  </div>"
	+"  </div>"
	+"</div>";


	var render=template.compile(source);
	console.log("设备连接信息 "+data);
	
	if(data===undefined)
		data={
			"deviceInfo":[{
				"serial":"无设备连接",
				"brand":"无设备连接",
				"model":"无设备连接",
				"version":"无设备连接"
				
			}]
	}
    var html=render(data);

    return html;


}


//显示在线设备信息

function deviceshow(){

var qr=get("/auto-web/loadDevice",{});
var deviceonline=qr.data;
//var dsource=
//+"<div class='' style='border:1px solid red;'>"
//+"{{each models}}"
//+"<label><input type='checkbox' name='model'>{{$value}}</label>"
//+"{{/each}}"
//+"</div>";


var imgsrc="http://"+host+":"+port+"/auto-web/showScreenshot?serial=";
var source="<div id='device-block' class='container'>"
	+"{{each deviceInfo}}"
	+"<div class='device-online'>"
	+"<div id='img-show-{{$value.serial}}' class=''><img src='"+imgsrc+"{{$value.serial}}"+"'></img></div>"
	+"<div id='exp-{{$value.serial}}'>"
	+"<small id='model' class='col-md-11'>{{$value.model}}</small>"
	+"<small id='os' class='col-md-11'>{{$value.version}}</small>"
	+"<small id='resolution' class='col-md-11'>{{$value.resolution}}</small>"
	+"<span class='col-md-11'><div class='btn-use available' id='{{$value.serial}}' onclick='jumpto(\"{{$value.url}}\")'><small>使用</small></div></span>"
	+"</div>"
	+"</div>"
	+"{{/each}}"
	+"</div>";
//+"<script src="+eval("js/upload.js")+"></script>";

console.log(source);
var render=template.compile(source);
var html=render(deviceonline);

return html;

}

//显示测试结果

function resultshow(data){
console.log("resultshow");
//var data={
//"tasks":[
//	{
//	"submitTime":"2017-09-23 13:45:12",
//	"serials":";8fh3333;hjfj9390303;kk939f",
//	"scriptName":"test.zip",
//	"tag":"自动化测试",
//	"method":"appium",
//	"result":"pass"
//},
//
//	{
//	"submitTime":"2017-12-23 23:12:56",
//	"serials":";8fh3333;hjfj9390303;kk939f",
//	"scriptName":"test2.zip",
//	"tag":"自动化测试",
//	"method":"appium",
//	"result":"pass"
//},
//		{
//	"submitTime":"2017-09-23 13:45:12",
//	"serials":";8fh3333;hjfj9390303;kk939f",
//	"scriptName":"test3.zip",
//	"tag":"自动化测试",
//	"method":"robotium",
//	"result":"fail"
//},
//
//	{
//	"submitTime":"2015-04-12 07:45:03",
//	"serials":";8fh3333;hjfj9390303;kk939f",
//	"scriptName":"test4.zip",
//	"tag":"自动化测试",
//	"method":"appium",
//	"result":"pass"
//}
//	
//	
//
//
//]
//	
//
//};
console.log("data");
var source="<table class='table table-striped'>"
+"<thead>"
+"<tr><th>序号</th><th>提交时间</th><th>测试手机</th><th>测试应用</th><th>简述</th><th>脚本分类</th><th>测试结果</th><th>操作</th></tr>"
+"</thead>"
+"<tbody>"
+"{{each tasks}}"
+"<tr>"
+"<td>{{$index+1}}</td><td>{{$value.submitTime}}</td><td>{{$value.model}}</td><td>{{$value.apkName2}}</td><td>{{$value.taskName}}</td><td>{{$value.method}}</td><td class='{{$value.result}}'>{{$value.result}}</td><td><a href="+"javascript:loadcontent(4,'"+new String('{{$value.taskTag}}')+"')"+">明细</a></td>"
+"</tr>"
+"{{/each}}"
+"</tbody>"
+"</table>";

var render=template.compile(source);
var html=render(data);
console.log(html);
return html;
}



//显示测试明细
function detailshow(taskTag){
console.log("detailshow");

var detailobj=get("/auto-web/loadDetail?taskTag="+taskTag,{});
var taskDetail=detailobj.data;
//var data={
//"subTasks":[
//	{
//	"startTime":"2016-08-07 12:45:34",
//	"endTime":"2016-08-07 13:15:34",
//	"serial":"7937hf",
//	"model":"小米4",
//	"scriptName":"test.java",
//	"result":"pass"
//
//},
//	{
//	"startTime":"2016-11-14 15:45:34",
//	"endTime":"2016-08-07 14:18:34",
//	"serial":"u83993fajfj",
//	"model":"vivo",
//	"scriptName":"test.java",
//	"result":"pass"
//
//}
//	
//
//]
//
//};

console.log(taskDetail);

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
var html=render(taskDetail);
console.log(html);

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

console.log(source);

return source;

}




