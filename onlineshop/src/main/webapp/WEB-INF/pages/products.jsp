<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="tag" tagdir="/WEB-INF/tags/"%>

<br>
<br>

<div class="table-responsive">
	<table class="table table-striped table-bordered">
		<thead>
			<tr class="table-success">
				<th>ID</th>
				<th>NAME</th>
				<th>DESCRIPTION</th>
				<th>PRICE</th>
				<th>CATEGORY</th>
				<th>STOCKS</th>
				<th>IMAGE</th>
				<th>MANAGE</th>
			</tr>
		</thead>
		<tbody>
			<c:if test="${empty products}">
				<td colspan="8" class="text-center">No Data</td>
			</c:if>
			<c:forEach var="product" items="${products}">
				<tr>
					<td>${product.id}</td>
					<td>${product.name}</td>
					<td>${product.description}</td>
					<td>${product.price}</td>
					<td>${product.category.name}</td>
					<td>${product.stocks}</td>
					<td><img
						src="data:image/jpeg;base64,${product.getImageString()}"
						width="100" height="100" /></td>
					<td><a class="btn btn-primary"
						href="<c:url value='/products/edit?productId=${product.id}'/>">&nbsp;Edit</a>
						<a class="btn btn-danger"
						href="<c:url value='/products/delete?productId=${product.id}'/>">&nbsp;Delete
					</a> <a class="btn btn-secondary"
						href="<c:url value='/customers?productId=${product.id}'/>">&nbsp;Customers</a>
					</td>
				</tr>
			</c:forEach>
		</tbody>
	</table>
</div>

<br>
<br>

<tag:pagination pageSize="${param.size}" currentPage="${param.page}" numberObjects="${count}"
	url="${param.categoryId == null || param.categoryId == ''   ? '/products' : '/products?categoryId=' += param.categoryId }"></tag:pagination>

<br>
<br>