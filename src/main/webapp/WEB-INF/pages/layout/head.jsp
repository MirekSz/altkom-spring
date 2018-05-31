<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page contentType="text/html; charset=UTF-8" %>
<%@ page isELIgnored="false" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<c:set var="contextPath" value="${pageContext.request.contextPath}" />
<html>
<head>
<link href="${contextPath}/public/assets/css/bootstrap.css" rel="stylesheet" />
<link href="${contextPath}/public/assets/css/style.css" rel="stylesheet" />
<link href="${contextPath}/public/assets/css/font-awesome.min.css" rel="stylesheet" />
<link href="${contextPath}/public/assets/css/nprogress.css" rel="stylesheet" />
<link href="https://cdn.datatables.net/1.10.16/css/jquery.dataTables.min.css" rel="stylesheet" />
<script type="text/javascript" src="${contextPath}/public/assets/js/jquery-2.2.4.js"></script>
<script type="text/javascript" src="${contextPath}/public/assets/js/nprogress.js"></script>
<script type="text/javascript" src="${contextPath}/public/assets/js/jquery.spring-friendly.js"></script>
<script type="text/javascript" src="${contextPath}/public/assets/app.js"></script>
<script type="text/javascript" src="https://cdn.datatables.net/1.10.16/js/jquery.dataTables.min.js"></script>
</head>
<sec:authentication var="user" property="principal" />
<body>
	<div class="navbar navbar-default navbar-fixed-top" role="navigation">
		<div class="container">
			<div class="navbar-header">
				<button type="button" class="navbar-toggle" data-toggle="collapse"
					data-target=".navbar-collapse">
					<span class="sr-only">Toggle navigation</span> <span
						class="icon-bar"></span> <span class="icon-bar"></span> <span
						class="icon-bar"></span>
				</button>
				<a class="navbar-brand" href="${contextPath}"><i><span style="font-size:24px"><span style="color:red">ALT</span>KOM</span> training app</i></a>
			</div>
			<div class="navbar-collapse collapse navbar-right">
				<ul class="nav navbar-nav">
					<li class="active"><a href="${contextPath}/product/list">Products list</a></li>
					<li><a href="${contextPath}/cart">Cart</a></li>
					<sec:authorize
						access="isAuthenticated()">
					<li><a href="#" onClick="$('#logoutForm').submit();return false;">Logout ${user.username}</a></li>
					</sec:authorize>
				</ul>
			</div>
			<!--/.nav-collapse -->
		</div>
		<c:url var="logoutUrl" value="/logout"/>
<form action="${logoutUrl}" method="post" style="display:none" id="logoutForm">
  <input type="submit" value="Log out" id="logout"/>
  <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
</form>
		</div>
			<br /> <br /> <br /> <br />
			<div class="container" >