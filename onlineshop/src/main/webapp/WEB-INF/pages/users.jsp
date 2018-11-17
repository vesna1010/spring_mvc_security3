<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="tag" tagdir="/WEB-INF/tags/"%>

<br>
<br>

<div class="table-responsive">
	<table class="table table-striped table-bordered">
		<thead>
			<tr class="table-success">
				<th>USERNAME</th>
				<th>EMAIL</th>
				<th>ENABLED</th>
				<th>AUTHORITY</th>
				<th>MANAGE</th>
			</tr>
		</thead>
		<tbody>
			<c:if test="${empty users}">
				<td colspan="5" class="text-center">No Data</td>
			</c:if>
			<c:forEach var="user" items="${users}">
				<tr>
					<td>${user.username}</td>
					<td>${user.email}</td>
					<td>${user.enabled}</td>
					<td>${user.authority}</td>
					<td><a class="btn btn-primary"
						href="<c:url value='/users/disable?username=${user.username}'/>"><span
							class="glyphicon glyphicon-pencil"></span>&nbsp;Disable</a> <a
						class="btn btn-danger"
						href="<c:url value='/users/delete?username=${user.username}'/>"><span
							class="glyphicon glyphicon-remove"></span>&nbsp;Delete</a></td>
				</tr>
			</c:forEach>
		</tbody>
	</table>
</div>

<br>
<br>

<tag:pagination pageSize="${param.size}" currentPage="${param.page}"
	numberObjects="${count}" url="/users"></tag:pagination>
<br>
<br>

