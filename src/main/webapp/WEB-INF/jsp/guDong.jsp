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
</head>
<body>
<div id="container" style="height: 400px"></div>
</body>
<script type="text/javascript">
	$(function () {
		var seriesOptions = [],categories=[];

		function  createChart(){

			$('#container').highcharts({
				title: {
					text: '股东与股份趋势图'
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