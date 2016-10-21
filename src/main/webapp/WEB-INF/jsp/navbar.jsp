<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<nav class="navbar navbar-default">
    <div class="container-fluid">
        <!-- Brand and toggle get grouped for better mobile display -->
        <div class="navbar-header">
            <a class="navbar-brand" href="/index">Buffalo System</a>
        </div>
        <!-- Collect the nav links, forms, and other content for toggling -->
        <div class="collapse navbar-collapse" id="bs-example-navbar-collapse-1">
            <ul class="nav navbar-nav">
                <li class="${pageContext.request.requestURI.contains('fish')?"active":""}"><a href="/fish">鱼与羊</a></li>
                <li class="${pageContext.request.requestURI.contains('guDong')?"active":""}"><a href="/guDong?code=000850">股东人数</a></li>
            </ul>
        </div>
    </div>
</nav>
