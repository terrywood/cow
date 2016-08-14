<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" isErrorPage="true" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<html>
<head>
	<link rel="stylesheet" href="//cdn.bootcss.com/bootstrap/3.3.5/css/bootstrap.min.css"/>
	<link rel="stylesheet" href="//cdn.bootcss.com/bootstrap/3.3.5/css/bootstrap-theme.min.css"/>
	<script src="//cdn.bootcss.com/jquery/1.11.3/jquery.min.js"></script>
	<script src="//cdn.bootcss.com/bootstrap/3.3.5/js/bootstrap.min.js"></script>

</head>

<body>


<div class="container-fluid">

	<table class="table table-condensed table-hover">
		<tr>
			<th>现价</th>
			<th>说明</th>
		</tr>
		<tr>
			<td>黄金518880:${sh518880} | 创业板159915:${sz159915}</td>
			<td >${desc28}</td>
		</tr>
	</table>

	<table class="table table-condensed table-hover">
		<tr>
			<th>Day</th>
			<th>代码</th>
			<th>名称</th>
			<th>临界</th>
			<th>上车</th>
			<th>Price</th>
			<th>Speed</th>
		</tr>
		<c:forEach items="${fishList}" var="var">
			<tr  id="p${var.symbol}">
				<td> <fmt:formatDate value="${var.date}" pattern="yyyy-MM-dd"/></td>
				<td>${var.symbol}</td>
				<td>${var.name}</td>
				<td>${var.edge}</td>
				<td>${var.work}</td>
				<td></td>
				<td></td>
			</tr>
		</c:forEach>

	</table>
	<blockquote>
		<p>ETF代码 沪深300[510300] 创业板[159915] 中小板[159902]  国证有色[150197] 中证军工[512660] 证券公司[512880].</p>
	</blockquote>
</div>
</body>
<script type="text/javascript">
//http://api.money.126.net/data/feed/0000001,1399006,1399005,1399395,1399967,1399975,money.api

$.ajax({
	type: "get",
	async: false,
	url: "http://api.money.126.net/data/feed/0000001,1399006,1399005,1399395,1399967,1399975,money.api",
	dataType: "jsonp",
	success: function(data){
		$.each(data, function(i,obj){
			 console.log(obj);
			$('#p'+obj.symbol+' td:eq(5)').html(obj.price);
			var speed = obj.price - parseFloat($('#p'+obj.symbol+' td:eq(3)').text());
			var className = speed>0?"warning":"";
			$('#p'+obj.symbol+' td:eq(6)').html(speed.toFixed(3)).addClass(className);
		});

	},
	error: function(){
		alert('fail');
	}
});

</script>
</html>
