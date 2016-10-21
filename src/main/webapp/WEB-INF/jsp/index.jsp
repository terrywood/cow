<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" isErrorPage="true" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<html>
<head>
    <link rel="stylesheet" href="//cdn.bootcss.com/bootstrap/3.3.5/css/bootstrap.min.css"/>
    <link rel="stylesheet" href="//cdn.bootcss.com/bootstrap/3.3.5/css/bootstrap-theme.min.css"/>
    <script src="//cdn.bootcss.com/jquery/1.11.3/jquery.min.js"></script>
    <script src="//cdn.bootcss.com/bootstrap/3.3.5/js/bootstrap.min.js"></script>
</head>

<body>

<jsp:include page="navbar.jsp"></jsp:include>

<div class="container-fluid">
    <table class="table table-condensed table-hover" id="weibo">
        <caption>开盘1小时成交量</caption>
        <thead>
        <tr>
            <th>时间</th>
            <th>成交量</th>
            <th>收盘收</th>
            <th>涨跌</th>
        </tr>
        </thead>
        <tbody>
        <c:forEach items="${list}" var="var">
            <tr class="${var.p_change<0?'':'warning'}">
                <td> ${var.day}</td>
                <td>${var.volume60}</td>
                <td>${var.price}</td>
                <td>${var.p_change}</td>
            </tr>
        </c:forEach>
        </tbody>
    </table>
  </div>
</body>
<script type="text/javascript">
    $(document).ready(function () {  });
</script>
</html>
