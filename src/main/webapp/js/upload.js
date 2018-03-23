   
$('#file-0a').fileinput({
        language: 'zh',
        uploadUrl: '/auto-web/upload',
        allowedPreviewTypes : ['zip'],
        uploadExtraData:{
        	"userName":$("userName").text,
        	"serialStr":';216a84c4'
        }

    });

    $('#file-0a').on('fileuploaderror', function(event, data, previewId, index) {
        var form = data.form, files = data.files, extra = data.extra,
                response = data.response, reader = data.reader;
        console.log(data);
        console.log('File upload error');
    });

    $('#file-0a').on('fileerror', function(event, data) {
        console.log(data.id);
        console.log(data.index);
        console.log(data.file);
        console.log(data.reader);
        console.log(data.files);
    });

    $('#file-0a').on('fileuploaded', function(event, data, previewId, index) {
        var form = data.form, files = data.files, extra = data.extra,
                response = data.response, reader = data.reader;
        console.log('File uploaded triggered');
    });