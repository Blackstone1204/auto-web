
addEventListener();

//task页面事件注册
function addEventListener(){
	methodchoose();
	nextstep();
	back();
	devicechoose();
    brandselected();
    submittest();
    


}

//选择脚本方式
function methodchoose(){

$(".method:lt(2)").click(function(){
	if(!$(this).hasClass("selected")){

		$(".method").removeClass("selected");
		$(this).addClass("selected");

		$("#monkey-input").addClass("hidden");
		$("#account").addClass("hidden");

	}

});


$(".method:eq(2)").click(function(){
		$(".method").removeClass("selected");
		$(this).addClass("selected");

		$("#monkey-input").removeClass("hidden");
		$("#account").removeClass("hidden");
		
});
}

//上传脚本功能初始化  页面每次变化时进行
function uploadinit(method){
	console.log("method ="+method);
	  $("#uploadfile").fileinput({
          language: 'zh', //设置语言
          uploadUrl: "/auto-web/upload", //上传的地址
          //allowedFileExtensions: ['jpg', 'gif', 'png'],//接收的文件后缀
          //uploadExtraData:{"id": 1, "fileName":'123.mp3'},
          uploadAsync: true, //默认异步上传
          showUpload: true, //是否显示上传按钮
          showRemove : true, //显示移除按est钮
          showPreview : false, //是否显示预览
          showCaption: true,//是否显示标题
          browseClass: "btn btn-info", //按钮样式     
          dropZoneEnabled: true,//是否显示拖拽区域
          //minImageWidth: 50, //图片的最小宽度
          //minImageHeight: 50,//图片的最小高度
          //maxImageWidth: 1000,//图片的最大宽度
          //maxImageHeight: 1000,//图片的最大高度
          //maxFileSize: 0,//单位为kb，如果为0表示不限制文件大小
          //minFileCount: 0,
          //smaxFileCount: 10, //表示允许同时上传的最大文件个数
          enctype: 'multipart/form-data',
          validateInitialCount:true,
          uploadExtraData:{
          	"userName":username
        
          }
          //previewFileIcon: "<i class='glyphicon glyphicon-king'></i>",
          //msgFilesTooMany: "选择上传的文件数量({n}) 超过允许的最大数值{m}！",
      });
	  

	    $('#uploadfile').on('fileuploaderror', function(event, data, previewId, index) {
	        var form = data.form, files = data.files, extra = data.extra,
	                response = data.response, reader = data.reader;
	        console.log(data);
	        console.log('File upload error');
	    });

	    $('#uploadfile').on('fileerror', function(event, data) {
	        console.log(data.id);
	        console.log(data.index);
	        console.log(data.file);
	        console.log(data.reader);
	        console.log(data.files);
	    });

	    $('#uploadfile').on('fileuploaded', function(event, data, previewId, index) {
	        var form = data.form, files = data.files, extra =data.extra,
	                response = data.response, reader = data.reader;
	        console.log('File uploaded triggered');
	    });
}

//下一步
function nextstep(){

	$("#next").click(function(){
		$("#step-one").css("display","none");
		$("#step-two").css("display","block");

		// $(".checkbox").click(function(){
		// 	alert("99999");
		// });



	});


}

//上一步
function back(){
	$("#back").click(function(){
		$("#step-one").css("display","block");
		$("#step-two").css("display","none");

	});

}

//选择品牌
function brandselected(){


    //绑定全部选项事件
    $("#brand input[type='checkbox']:eq(0)").click(function(){

    	//默认只勾选全部
    	alert("点击全部");

    	var flag=$(this).attr("checked");


    	if(undefined===flag){
    		alert("勾选全部");
    		$(this).attr("checked","checked");

    	
            $("#brand input[type='checkbox']:gt(0)").removeAttr("checked");
            $(this).attr("disabled","disabled");
            alert("取消子项");

            //添加设备
            var addHtml="";
            for(var i=0;i<connectdevice.deviceInfo.length;i++){
            	addHtml+="<p class='device-item'>"+connectdevice.deviceInfo[i].model+"</p>";

            }

            alert(addHtml);
            $(".source-dropdownlist").html(addHtml);
            //重新绑定事件
            devicechoose();


    		//$(this).attr("checked","checked");

    	}else{
    		alert("取消全部");
    		var childItemSelected=false;
    		var childItem=$("#brand input[type='checkbox']:gt(0)");
    		alert(childItem.length);


    		if(childItemSelected) {
    			alert("发现子项 取消全部");
    			$(this).removeAttr("checked");
    		}
    		else {
    			alert("无子项 无法取消");
    		}

    		

   
    	}
    

    });


    //
    $("#brand input[type='checkbox']:gt(0)").click(function(){

    	var flag=$(this).attr("checked");
    	var brand=$(this).parent().children("label").text();
    	if(undefined===flag){
    		alert("勾选"+brand);
    		$(this).attr("checked","checked");

            $("#brand input[type='checkbox']:eq(0)").removeAttr("disabled");
    		$("#brand input[type='checkbox']:eq(0)").removeAttr("checked");
    		alert("取消全部");

    		if(childitemcheck("#brand")==1){
    			$(this).attr("disabled","disabled");
    			alert("禁用该项")
    		}else{
    			$("#brand input[type='checkbox']:gt(0)").removeAttr("disabled");
    		}

    		//添加设备






    	}else{
    	    alert("取消"+brand);
    		$(this).removeAttr("checked");

    		//
    		if(childitemcheck("#brand")==1){
    			$("#brand input[type='checkbox'][checked='checked']").attr("disabled","disabled");

    		}

    		//添加设备
    		var addHtml="";
    		var els=$("#brand input[type='checkbox'][checked='checked']");
    		for(var i=0;i<els.length;i++){
            	addHtml+="<p class='device-item'>"+els.eq(i).parent().children("label").text()+"</p>";
    		}

    		$(".source-dropdownlist").html(addHtml);
    		devicechoose();



    	}

    });



}

function childitemcheck(target){

    var count=0;
	var childItems=$(target+" input[type='checkbox']:gt(0)");
	for(var i=0;i<childItems.length;i++){
		if(childItems.eq(i).attr("checked")=="checked")
			count=count+1;

	}

	return count;
}


function checkboxToggle(){


}

function versionselected(){


}

function checkboxevent(){

}



//选择设备
function devicechoose(){

	$(".device-item").mouseover(function(){
		$(this).addClass("device-selected");
	});

	$(".device-item").mouseout(function(){
		$(this).removeClass("device-selected");
	});



	$(".device-item").dblclick(function(){

		if($(this).parent().hasClass("source-dropdownlist")){
			$(this).prependTo($(".dest-dropdownlist"));

		}else{

			$(this).prependTo($(".source-dropdownlist"));

		}
	
	});
}

//提交测试
function submittest(){
	
	$("#submit").click(function(){
		var str="";
		
		$(".dest-dropdownlist p").each(function(){
			str+=";"+$(this).children("a:eq(1)").text();
		});
		var taskobj={};
		taskobj.serialStr=str;
		taskobj.userName=username;
		taskobj.method=$(".run-method>.selected").attr("id");
		
		console.log("提交任务信息 serialStr="+taskobj.serialStr+" username="+taskobj.userName+" method="+taskobj.method);
		var result=get("/auto-web/runTest",taskobj);
		if(result.code==1)alert("提交成功");
		else alert("提交失败");
		
		
	});
}