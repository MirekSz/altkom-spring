<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ page contentType="text/html; charset=UTF-8"%>
<%@ include file="/WEB-INF/pages/layout/head.jsp"%>

 <sec:authorize access="hasAnyRole('ROLE_ADMIN')">
	<a href="new" class="btn btn-primary "> <i
		class=" glyphicon glyphicon-plus"></i> Add new product
	</a>
	</sec:authorize>
	<div class="col-xs-4 pull-right">
		<div class="input-group">
			<input type="text" class="form-control searcher"
				placeholder="Search..."> <span class="input-group-btn">
				<button class="btn btn-default" type="button">
					<i class="glyphicon glyphicon-send" style="height:20px"></i>
				</button>
			</span>
		</div>
	</div>

<table class="table table-hover table-striped">
		<thead>
			<tr>
				<th>Id</th>
				<th><spring:message code="product.name"/></th>
				<th><spring:message code="product.quantity"/></th>
				<th><spring:message code="product.price"/></th>
				<th><spring:message code="product.actions"/></th>
			</tr>
		</thead>
		<tbody>
			<c:forEach var="p" items="${products}">
				<tr>
					<td>${p.id}</td>
					<td>${p.name}</td>
					<td>${p.quantity}</td>
					<td>${p.price}</td>
					<td>
						<a href="${p.id}/edit"> <i class=" glyphicon glyphicon-pencil"></i></a>
						<a href="${p.id}/delete"> <i class=" glyphicon glyphicon-remove-circle"></i></a>
					</td>
				</tr>
			</c:forEach>
		</tbody>
	</table>

<%@ include file="/WEB-INF/pages/layout/footer.jsp"%>

