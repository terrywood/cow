<%@ page import="app.entity.AccountDaily" %>
<%@ page import="app.entity.AccountData" %>
<%@ page import="java.text.SimpleDateFormat" %>
<%@ page import="java.util.List" %>
<%@ page import="java.util.Map" %>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" isErrorPage="true" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
<html>
<head>
    <title>Cow</title>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
<%--    <link rel="stylesheet" href="//cdn.bootcss.com/bootstrap/3.3.5/css/bootstrap.min.css"/>
    <link rel="stylesheet" href="//cdn.bootcss.com/bootstrap/3.3.5/css/bootstrap-theme.min.css"/>--%>
    <script src="//cdn.bootcss.com/jquery/1.11.3/jquery.min.js"></script>
    <%--<script src="//cdn.bootcss.com/bootstrap/3.3.5/js/bootstrap.min.js"></script>--%>

    <link rel="stylesheet" href="/css/theme.default.min.css">
    <script type="text/javascript" src="/js/jquery.tablesorter.js"></script>
<%--    <script type="text/javascript" src="/js/jquery.tablesorter.widgets.js"></script>--%>

</head>
<body>
<%
    Map<Long,List<AccountDaily>> map = (Map<Long,List<AccountDaily>> )request.getAttribute("daily");
    java.text.NumberFormat percentFormat =java.text.NumberFormat.getPercentInstance();
    percentFormat.setMinimumFractionDigits(2);
    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
%>

<div class="container-fluid">
    <%--  <ul class="nav nav-pills">
          <li role="presentation" class="active"><a href="#">Home</a></li>
          <li role="presentation"><a href="#">Profile</a></li>
          <li role="presentation"><a href="#">Messages</a></li>
      </ul>
      <form class="navbar-form navbar-left" role="search" action="press">
          <div class="form-group">
              <input type="text" name="date" class="form-control" value="2016-06-02" placeholder="Date">
          </div>
          <div class="form-group">
              <input type="text" class="form-control" placeholder="Search">
          </div>
          <button type="submit" class="btn btn-default">Submit</button>
      </form>
  --%>

    <div class="row-fluid">
        <div class="span12">
            <form id="pagerForm" action="">

            </form>
            <table id="myTable" class="tablesorter table table-condensed">
                <thead>
                <tr>
                    <th>ID</th>
                    <th>Name</th>
                    <th>净值</th>
                    <th>最后交易</th>
                    <th>平均持股天数</th>
                    <th>月均交次数</th>
                    <th>月均赢利</th>
                    <th>10D赢利</th>
                    <th>胜率</th>
                    <th>时间</th>
                    <th>曲线</th>
                </tr>
                </thead>
                <tbody>
                <c:forEach items="${pageList}" var="item">
                    <tr>
                        <td>
                            <a target="_blank" href="accounts/${item.accountID}">${item.accountID}</a>
                        </td>
                        <td><p>${item.userName}</p></td>
                        <td>${item.equity}</td>
                        <td><fmt:formatDate value="${item.lastTradingTime}" pattern="yyyy-MM-dd"></fmt:formatDate></td>
                        <td> ${item.avgHoldingDay}</td>
                        <td> ${item.monthTradeNumber}</td>
                        <td> ${item.monthYield}%</td>
                        <td>
                          <%
                             Long accountId = ((AccountData)pageContext.getAttribute("item")).getAccountID();
                             List<AccountDaily> dailyList = map.get(accountId);
                             AccountDaily last = dailyList.get(0);
                             AccountDaily first = dailyList.get(dailyList.size()-1);
                             Float percent = (last.getEquity()/first.getEquity())-1f;
                             out.println(""+ percentFormat.format(percent)+"");
                          %>
                        </td>
                        <td> ${item.winRatio}%</td>
                        <td> <%
                            out.println( sdf.format(first.getDay()) + "-" +sdf.format(last.getDay())+"");
                        %></td>
                        <td><img width="100" src="${item.yieldUrl}"/></td>
                    </tr>
                </c:forEach>
                </tbody>
            </table>

            <%-- <c:import url="pageBar.jsp"></c:import>--%>
        </div>
    </div>
</div>

</body>
<script>
    $(function(){
        $("#myTable").tablesorter({
            sortList: [[7,1]] ,
            headers: {
                1: {
                    sorter: false
                },
                9: {
                    sorter: false
                },
                10: {
                    sorter: false
                }
            }

        });
    });
</script>
</html>