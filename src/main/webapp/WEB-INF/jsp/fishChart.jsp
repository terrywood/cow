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
	<script src="https://code.highcharts.com/stock/highstock.js"></script>
	<script src="https://code.highcharts.com/stock/modules/exporting.js"></script>
</head>

<body>
<div class="container-fluid">
	<div id="container" style="height: 400px; min-width: 310px"></div>
</div>
</body>
<script type="text/javascript">
	$(function () {

		var seriesOptions = [],
				seriesCounter = 0,
				//names = ['MSFT', 'AAPL', 'GOOG'];
				names = ['GuDong','Price'];

		/**
		 * Create the chart when all data is loaded
		 * @returns {undefined}
		 */
		function createChart() {

			$('#container').highcharts('StockChart', {

				rangeSelector: {
					selected: 4
				},
				xAxis: {
					labels: {
						formatter: function() {

							return  Highcharts.dateFormat('%Y-%m-%d', this.value);
						}
					}
				},
				yAxis: {
					labels: {
						formatter: function () {
							return (this.value > 0 ? ' + ' : '') + this.value + '%';
						}
					},
					plotLines: [{
						value: 0,
						width: 2,
						color: 'silver'
					}]
				},

				plotOptions: {
					series: {
						compare: 'percent'
					}
				},

				tooltip: {
					xDateFormat: '%Y-%m-%d',
					pointFormat: '<span style="color:{series.color}">{series.name}</span>: <b>{point.y}</b> ({point.change}%)<br/>',
					valueDecimals: 2
				},

				series: seriesOptions
			});
		}

		$.each(names, function (i, name) {
			$.ajaxSettings.async = false;
			$.getJSON( name.toLowerCase()+'?code=${param.code}', function(json){
				seriesOptions[i] = {
					name: name,
					data: json
				};
				seriesCounter += 1;
				if (seriesCounter === names.length) {
					// As we're loading the data asynchronously, we don't know what order it will arrive. So
					// we keep a counter and create the chart when all the data is loaded.
					createChart();
				}
			});
			$.ajaxSettings.async = true;
		});
	});


</script>
</html>
