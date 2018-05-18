<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ page contentType="text/html; charset=UTF-8"%>
<%@ include file="/WEB-INF/pages/layout/head.jsp"%>

 <form class="form-signin" action="login" method="POST">
<c:if test="${param.error!=null}">
		<div class="alert alert-danger" role="alert" >
			<span class="glyphicon 	glyphicon glyphicon-flash" aria-hidden="true"></span>
			<strong>Invalid user or password</strong>
		</div>
</c:if>
        <h2 class="form-signin-heading">Please sign in</h2>
        <label  class="sr-only">Email address</label>
        <input type="text" name="username"  class="form-control" placeholder="Username" required autofocus>
        <label  class="sr-only">Password</label>
        <input type="password"  name="password" class="form-control" placeholder="Password" required>
        <div class="checkbox">
          <label>
            <input type="checkbox" value="remember-me"  name="remember-me"> Remember me
          </label>
        </div>
        <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
        <button class="btn btn-lg btn-primary btn-block" type="submit">Sign in</button>
      </form>

  
<%@ include file="/WEB-INF/pages/layout/footer.jsp"%>

