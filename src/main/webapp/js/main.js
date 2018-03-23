
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



function loadcontent(index){

//
   isAccess=isAccess+1;
   if(index==0&&isAccess>1){
	   var jump="http://"+host+":"+port+"/auto-web/main?username="+$("#username").text();
	   window.location.href=jump;
	   
   }
   $("#menu").find("li").siblings().removeClass("active");

   $("#menu").find("li").eq(2*index).addClass("active");

   console.log("点击菜单 index = "+index);


//
	console.log("load page. index="+index);
	var devicehtml=deviceshow();
	var resulthtml=resultshow();
	var detailhtml=detailshow();
	var systemhtml="";
	var charthtml=chartshow();
	var map={
	"0":devicehtml,
	"1":resulthtml,
	"2":systemhtml,
	"3":detailhtml,
    //"4":"",
	"5":charthtml
	
	};

	//console.log(map[index]);

	$("#content").html(map[index]);



}

function jsonajax(url,param){
	
	var back;
	   $.ajax(
				  { 
				  url:url,
				  type:'get',
				  data:param,
				  async:false,
				  success:function(data,status){
					  back=data;
					  

				  },
				  error:function (data, status, e){
			              alert(e);
			          }
					  
				  });
	   
	   return back;
	
	
}
function getParam(name){
    var reg = new RegExp("(^|&)"+ name +"=([^&]*)(&|$)");
    var r = window.location.search.substr(1).match(reg);
    if(r!=null)return  unescape(r[2]); return null;
}