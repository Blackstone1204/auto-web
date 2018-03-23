
function uploadinit(){
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
                	"userName":username,
                	'method':
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
		        var form = data.form, files = data.files, extra = data.extra,
		                response = data.response, reader = data.reader;
		        console.log('File uploaded triggered');
		    });
}