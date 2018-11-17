<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<br>
<br>

<div class="table-responsive">
	<table class="table table-striped table-bordered">
		<thead>
			<tr class="table-success">
				<th>ID</th>
				<th>NAME</th>
				<th>MANAGE</th>
			</tr>
		</thead>
		<tbody>
			<c:if test="${empty categories}">
				<td colspan="3" class="text-center">No Data</td>
			</c:if>
			<c:forEach var="category" items="${categories}">
				<tr>
					<td>${category.id}</td>
					<td>${category.name}</td>
					<td><a class="btn btn-primary"
						href="<c:url value='/categories/edit?categoryId=${category.id}'/>">&nbsp;Edit</a>
						<a class="btn btn-danger"
						href="<c:url value='/categories/delete?categoryId=${category.id}'/>">&nbsp;Delete</a>
						<a class="btn btn-secondary"
						href="<c:url value='/products?categoryId=${category.id}'/>">&nbsp;Products
					</a></td>
				</tr>
			</c:forEach>
		</tbody>
	</table>
</div>

<br>
<br>