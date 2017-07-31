var seckill = {
		url : {
			now : "/TestMavenDemo/seckill/time/now",
			exposer : function(seckillId) {
				return "/TestMavenDemo/seckill/" + seckillId + "/exposer";
			},
			execution : function(seckillId, md5) {
				return "/TestMavenDemo/seckill/" + seckillId + "/" + md5 + "/execution";
			}
		},
		validatePhone : function(phone) {
			if(phone && phone.length == 11 && !isNaN(phone)) {
				return true;
			} else {
				return false;
			}
		},
		handlerSeckill : function(seckillId, node) {
			node.hide().html("<button id='killBtn' class='btn btn-primary btn-lg'>开始秒杀</button>");
			$.ajax({
				url : seckill.url.exposer(seckillId),
				type : "POST",
				dataType : "json",
				success : function(data) {
					if(data && data.success) {
						var exposer = data.data;
						if(exposer.exposed) {
							var md5 = exposer.md5;
							var killUrl = seckill.url.execution(seckillId, md5);
							//绑定一次点击事件
							$("#killBtn").one("click", function() {
								$(this).addClass("disabled");
								$.ajax({
									url : killUrl,
									type : "POST",
									dataType : "json",
									success : function(returnData) {
										if(returnData && returnData.success) {
											var seckillResult = returnData.data;
											var stateInfo = seckillResult.stateInfo;
											node.html("<span class='label label-success'>" + stateInfo + "</span>");
										}
									}
								});
							});
							node.show();
						} else {
							var now = exposer.now;
							var start = exposer.start;
							var end = exposer.end;
							seckill.countdown(seckillId, now, start, end);
						}
					} else {
						console.log(data);
					}
				}
			});
		},
		countdown : function(seckillId, nowTime, startTime, endTime) {
			var seckillBox = $("#seckill-box");
			if(nowTime > endTime) {
				seckillBox.html("秒杀已经结束了。。。");
			} else if(nowTime < startTime) {
				var killTime = new Date(startTime);
				seckillBox.countdown(killTime, function(event){
					var format = event.strftime("秒杀倒计时:%D天  %H时  %M分  %S秒");
					seckillBox.html(format);
				}).on("finish.countdown", function(){
					seckill.handlerSeckill(seckillId, seckillBox);
				});
			} else {
				seckill.handlerSeckill(seckillId, seckillBox);
			}
		},
		detail : {
			init : function(params) {
				var killPhone = $.cookie("killPhone");
				var startTime = params.startTime;
				var endTime = params.endTime;
				var seckillId = params.seckillId;
				
				if(!seckill.validatePhone(killPhone)) {
					$("#killPhoneModal").modal({
						show : true,
						backdrop : "static",
						keyboard : false
					});
					$("#killPhoneBtn").click(function(){
						var inputPhone = $("#killPhoneKey").val();
						if(seckill.validatePhone(inputPhone)) {
							$.cookie("killPhone", inputPhone, {expires : 7, path : "/TestMavenDemo/seckill"});
							window.location.reload();
						} else {
							$("#killPhoneMessage").hide().html("<label class='label label-danger'>手机号码错误</label>").show(300);
						}
					});
				}
				
				//获取服务器当前时间
				$.ajax({
					url : seckill.url.now,
					type : "GET",
					dataType : "json",
					success : function(data) {
						if(data && data.success) {
							var nowTime = data.data;
							seckill.countdown(seckillId, nowTime, startTime, endTime);
						} else {
							console.log(data);
						}
					}
				});
			}
		}
};