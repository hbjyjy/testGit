<%@page contentType="text/html;charset=UTF-8" language="java" %>
<%@taglib prefix="dict" uri="/WEB-INF/mytaglib.tld" %>
<%@include file="common/tag.jsp" %>
<!DOCTYPE html>
<html lang="zh-CN">
  <head>
    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <!-- 上述3个meta标签*必须*放在最前面，任何其他内容都*必须*跟随其后！ -->
    <title>秒杀列表页</title>
    <%@include file="common/head.jsp" %>
    <link rel="stylesheet" href="//cdn.datatables.net/1.10.12/css/jquery.dataTables.min.css">
    <link rel="stylesheet" href="../resource/css/uploadify.css">

  </head>
  <body>
  	<div class="container">
  		<div class="panel panel-default">
  			<div class="panel-heading text-center">
  				<h2>秒杀列表</h2>
  			</div>
  			<div class="panel-body">
  				<form>
        			名称:<input type="text" name="killName"/>
        			<input type="button" value="query" id="query"/>
        			<input type="button" value="导出数据" onclick="download()"/>
        			<dict:select defaultValue="true" name="GJ" id="GJ" dictName="GJ" cssClass="form-control"/>
    			</form>
  				<!-- <table class="table table-hover"> -->
  				<table id="killList" width="100%">
  					<thead>
  						<tr>
  							<td>seckillId</td>
  							<td>名称</td>
  							<td>库存</td>
  							<td>开始时间</td>
  							<td>结束时间</td>
  							<td>创建时间</td>
  							<td>详情页</td>
  						</tr>
  					</thead>
  					<tbody>
  						<%-- <c:forEach var="sk" items="${list}">
  							<tr>
  								<td>${sk.name}</td>
  								<td>${sk.number}</td>
  								<td><fmt:formatDate value="${sk.startTime}" pattern="yyyy-MM-dd HH:mm:ss" /></td>
  								<td><fmt:formatDate value="${sk.endTime}" pattern="yyyy-MM-dd HH:mm:ss" /></td>
  								<td><fmt:formatDate value="${sk.createTime}" pattern="yyyy-MM-dd HH:mm:ss" /></td>
  								<td><a class="btn btn-info" href="/TestMavenDemo/seckill/${sk.seckillId}/detail" target="_blank">link</a></td>
  							</tr>
  						</c:forEach> --%>
  					</tbody>
  				</table>
  			</div>
  		</div>
  		<table>
	        <tr>
	            <td><label>上传课件：</label></td>
	            <td><label><input type="file" name="uploadify" id="uploadify"/></label>&nbsp;&nbsp;</td>
	            <td><div id="fileQueue"></div></td>
	        </tr>
   		</table>
   		<img id="successUpload" />
  	</div>
  </body>
  	

	<!-- jQuery文件。务必在bootstrap.min.js 之前引入 -->
	<script src="//cdn.bootcss.com/jquery/1.11.3/jquery.min.js"></script>

	<!-- 最新的 Bootstrap 核心 JavaScript 文件 -->
	<script src="//cdn.bootcss.com/bootstrap/3.3.5/js/bootstrap.min.js"></script>
	
	<script src="//cdn.datatables.net/1.10.12/js/jquery.dataTables.min.js"></script>
	
	<script src="/TestMavenDemo/resource/uploadify/jquery.uploadify.min.js"></script>
	
	<script type="text/javascript">
	Date.prototype.format = function (fmt) { //author: meizz 
	    var o = {
	        "M+": this.getMonth() + 1, //月份 
	        "d+": this.getDate(), //日 
	        "h+": this.getHours(), //小时 
	        "m+": this.getMinutes(), //分 
	        "s+": this.getSeconds(), //秒 
	        "q+": Math.floor((this.getMonth() + 3) / 3), //季度 
	        "S": this.getMilliseconds() //毫秒 
	    };
	    if (/(y+)/.test(fmt)) fmt = fmt.replace(RegExp.$1, (this.getFullYear() + "").substr(4 - RegExp.$1.length));
	    for (var k in o)
	    if (new RegExp("(" + k + ")").test(fmt)) fmt = fmt.replace(RegExp.$1, (RegExp.$1.length == 1) ? (o[k]) : (("00" + o[k]).substr(("" + o[k]).length)));
	    return fmt;
	}
	function initTable() {
        var paras = $("form").serialize();
        $("#killList").dataTable({
            "bFilter": false,//去掉搜索框
            //"bAutoWidth": true, //自适应宽度
            "sPaginationType" : "full_numbers",
            //"sAjaxDataProp" : "aData",
            "bDestroy" : true,
            "bProcessing" : true,
            "sAjaxSource" : "/TestMavenDemo/seckill/QueryData?now=" + new Date().getTime() + "&" + paras,
            "bServerSide" : true,
            "aoColumns" : [{
            	"mDataProp" : "seckillId"
            }, {
                "mDataProp" : "name"
            }, {
                "mDataProp" : "number"
            }, {
                "mDataProp" : "startTime"
            }, {
                "mDataProp" : "endTime"
            }, {
                "mDataProp" : "createTime"
            }, {
            	"mDataProp" : "createTime"
            }],
            "columnDefs": [
                           {
                               "render": function ( data, type, row ) {
                            	   var startDate = new Date(data);
                                   return startDate.format("yyyy-MM-dd hh:mm:ss");
                               },
                               "targets": 3
                           },
                           {
                               "render": function ( data, type, row ) {
                            	   var startDate = new Date(data);
                                   return startDate.format("yyyy-MM-dd hh:mm:ss");
                               },
                               "targets": 4
                           },
                           {
                               "render": function ( data, type, row ) {
                            	   var startDate = new Date(data);
                                   return startDate.format("yyyy-MM-dd hh:mm:ss");
                               },
                               "targets": 5
                           },
                           {
                               "render": function ( data, type, row ) {
                                   return '<a class="btn btn-info" href="/TestMavenDemo/seckill/' + row.seckillId + '/detail" target="_blank">link</a>';
                               },
                               "targets": 6
                           },
                           {
                        	   "visible" : false, "targets" : [0]
                           }
                       ],
            "oLanguage" : {
                "sProcessing" : "正在加载中......",
                "sLengthMenu" : "每页显示 _MENU_ 条记录",
                "sZeroRecords" : "没有数据！",
                "sEmptyTable" : "表中无数据存在！",
                "sInfo" : "当前显示 _START_ 到 _END_ 条，共 _TOTAL_ 条记录",
                "sInfoEmpty" : "显示0到0条记录",
                "sInfoFiltered" : "数据表中共为 _MAX_ 条记录",
                //"sSearch" : "搜索",
                "oPaginate" : {
                    "sFirst" : "首页",
                    "sPrevious" : "上一页",
                    "sNext" : "下一页",
                    "sLast" : "末页"
                }
            }
        });
    }
	
	$("#query").click(function() {
        //$("#example").dataTable().fnClearTable();
        initTable();
    });
	
	function download(){
        var url="/TestMavenDemo/seckill/downloadExcel";
        window.open(url);
    }
	
	$(document).ready(function() {
		initTable();
		
		$("#uploadify").uploadify({
            'swf': '/TestMavenDemo/resource/uploadify/uploadify.swf',
            'uploader':"/TestMavenDemo/seckill/uploadFile",
            'queueID': 'fileQueue',
            'auto': true,
            'buttonText': '请选择图片',
            'fileObjName': 'uploadify',
            'uploadLimit' : 1,
            'queueSizeLimit' : 1,
            'fileSizeLimit' : '10MB',
            'fileTypeExts': '*.jpg;*.gif;*.jpeg;*.png;*.bmp;*.zip;*.rar;*.7z',
            onUploadSuccess: function(file, data, response) {
                //转换为json对象
                $("#successUpload").attr("src", "/TestMavenDemo/" + data);
            },
            onSelect:function(){
            },
            onError: function(event, queueID, fileObj) {
                alert("文件:" + fileObj.name + "上传失败");
            }
        });
	});
	
	</script>
</html>