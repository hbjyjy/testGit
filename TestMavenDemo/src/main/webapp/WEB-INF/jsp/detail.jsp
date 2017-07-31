<%@page contentType="text/html;charset=UTF-8" language="java" %>
<%@include file="common/tag.jsp" %>
<!DOCTYPE html>
<html lang="zh-CN">
  <head>
    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <!-- 上述3个meta标签*必须*放在最前面，任何其他内容都*必须*跟随其后！ -->
    <title>秒杀详情页</title>
    <%@include file="common/head.jsp" %>

  </head>
  <body>
  	<div class="container">
  		<div class="panel panel-default text-center">
  			<div class="panel-heading">
  				<h1>${seckill.name}</h1>
  			</div>
  		</div>
  		<div class="panel-body">
  			<h2 class="text-danger text-center">
  				<span class="glyphicon glyphicon-time"></span>
  				<span class="glyphicon" id="seckill-box"></span>
  			</h2>
  		</div>
  	</div>
  	
  	<div id="killPhoneModal" class="modal fade">
  			<div class="modal-dialog">
  				<div class="modal-content">
  					<div class="modal-header">
  						<h3 class="modal-title text-center">
  							<span class="glyphicon glyphicon-phone"></span>秒杀电话：
  						</h3>
  					</div>
  					<div class="modal-body">
  						<div class="row">
  							<div class="col-xs-8 col-xs-offset-2">
  								<input type="text" name="killPhone" id="killPhoneKey"
  								 placeholder="请填写手机号码" class="form-control" />
  							</div>
  						</div>
  					</div>
  					<div class="modal-footer">
  						<span id="killPhoneMessage" class="glyphicon"></span>
  						<button type="button" id="killPhoneBtn" class="btn btn-success">
  							<span class="glyphicon glyphicon-phone"></span>
  							Submit
  						</button>
  					</div>
  				</div>
  			</div>
  		</div>
  </body>
  	

	<!-- jQuery文件。务必在bootstrap.min.js 之前引入 -->
	<script src="//cdn.bootcss.com/jquery/1.11.3/jquery.min.js"></script>

	<!-- 最新的 Bootstrap 核心 JavaScript 文件 -->
	<script src="//cdn.bootcss.com/bootstrap/3.3.5/js/bootstrap.min.js"></script>
	
	<!-- jQuery cookie插件 -->
	<script src="//cdn.bootcss.com/jquery-cookie/1.4.1/jquery.cookie.min.js"></script>
	
	<!-- jQuery countdown插件 -->
	<script src="//cdn.bootcss.com/jquery.countdown/2.1.0/jquery.countdown.min.js"></script>
	
	<script src="/TestMavenDemo/resource/script/seckill.js" type="text/javascript"></script>
	
	<script type="text/javascript">
		$(document).ready(function(){
			seckill.detail.init({
				seckillId : ${seckill.seckillId},
				startTime : ${seckill.startTime.time},
				endTime : ${seckill.endTime.time}
			});
		});
	</script>
	
</html>