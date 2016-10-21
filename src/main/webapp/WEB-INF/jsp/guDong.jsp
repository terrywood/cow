<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" isErrorPage="true" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<!DOCTYPE html>
<html lang="en">
<head>
	<meta charset="UTF-8">
	<title>Title</title>
	<script src="http://cdn.hcharts.cn/jquery/jquery-1.8.3.min.js"></script>
	<script src="http://cdn.hcharts.cn/highcharts/highcharts.js"></script>

	<link rel="stylesheet" href="//cdn.bootcss.com/bootstrap/3.3.5/css/bootstrap.min.css"/>
	<link rel="stylesheet" href="//cdn.bootcss.com/bootstrap/3.3.5/css/bootstrap-theme.min.css"/>
	<script src="//cdn.bootcss.com/bootstrap/3.3.5/js/bootstrap.min.js"></script>
</head>
<body>
<jsp:include page="navbar.jsp"></jsp:include>

<div id="container" style="height: 400px"></div>

<div class="container-fluid">
    <!---
	pe,市盈率
	outstanding,流通股本
	totals,总股本(万)
	totalAssets,总资产(万)
	liquidAssets,流动资产
	fixedAssets,固定资产
	reserved,公积金
	reservedPerShare,每股公积金
	esp,每股收益
	bvps,每股净资
	pb,市净率
 -->
	<table class="table table-condensed table-hover">
		<thead>
		<tr>
			<th>每股收益</th>
			<th>每股净资</th>
			<th>市盈率</th>
			<th>流通股本</th>
			<th>总股本</th>
			<th>总资产</th>
			<th>市净率</th>
		</tr>
		</thead>
		<tbody>
			<tr id="p${stock.code}">
				<td>${stock.esp}</td>
				<td>${stock.bvps}</td>
				<td>${stock.pe}</td>
				<td>${stock.outstanding}</td>
				<td>${stock.totals}</td>
				<td>${stock.totalAssets}</td>
				<td>${stock.pb}</td>
			</tr>
		</tbody>
	</table>
</body>
<script type="text/javascript">
	$(function () {
		var seriesOptions = [],categories=[];

		function  createChart(){

			$('#container').highcharts({
				title: {
					text: '${stock.name}(${stock.code})'
				},
				xAxis: {
					tickInterval: 15,
					categories:categories
				},
				yAxis:[{
					lineWidth : 1,
					title:{
						text :'股东户数'
					}
				},{
					title:{
						text :'价格'
					},
					lineWidth : 1,
					opposite:true
				}],
				tooltip: {
					pointFormat: '<span style="color:{series.color}">{series.name}</span>: <b>{point.y}</b><br/>',
					shared: true,
					crosshairs: true
				},

				series: seriesOptions

			});
		}

		$.getJSON( 'guDongData?code=${param.code}', function(json){
			seriesOptions[0] = {
				name: '股东',
				yAxis: 0,
				data: json.counts
			};
			seriesOptions[1] = {
				name: '股价',
				yAxis: 1,
				data: json.prices
			};
			categories = json.dates;

			createChart();
		});


	});
</script>
</html>