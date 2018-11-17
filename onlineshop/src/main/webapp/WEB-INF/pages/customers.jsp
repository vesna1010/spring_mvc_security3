<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="tag" tagdir="/WEB-INF/tags/"%>

<br>
<br>

<div class="table-responsive">
	<table class="table table-striped table-bordered">
		<thead>
			<tr class="table-success">
				<th>ID</th>
				<th>FULL NAME</th>
				<th>EMAIL</th>
				<th>ADDRESS</th>
				<th>TELEPHONE</th>
				<th>DATE</th>
				<th>PRODUCTS</th>
				<th>TOTAL ACCOUNT</th>
			</tr>
		</thead>
		<tbody>
			<c:if test="${empty customers}">
				<td colspan="8" class="text-center">No Data</td>
			</c:if>
			<c:forEach var="customer" items="${customers}">
				<tr>
					<td>${customer.id}</td>
					<td>${customer.fullName}</td>
					<td>${customer.email}</td>
					<td><pre>${customer.address}</pre></td>
					<td>${customer.telephone}</td>
					<td>${customer.date}</td>
					<td><c:forEach var="product" items="${customer.products}">
			        ${product.key.name} - ${product.value}</c:forEach></td>
					<td>${customer.totalAccount}</td>
				</tr>
			</c:forEach>
		</tbody>
	</table>
</div>

<br>
<br>

<tag:pagination pageSize="${param.size}" currentPage="${param.page}" numberObjects="${count}"
	url="${param.productId == null || param.productId == ''   ? '/customers' : '/customers?productId=' += param.productId}"></tag:pagination>

<br>
<br>
