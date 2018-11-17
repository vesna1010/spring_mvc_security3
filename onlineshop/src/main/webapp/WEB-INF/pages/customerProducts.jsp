<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<br>
<h5 class="text-danger text-center">${message}</h5>
<br>
<div class="table-responsive">
	<table class="table table-striped table-bordered">
		<thead>
			<tr class="table-success">
				<th>PRODUCT</th>
				<th>NUMBER PRODUCTS</th>
				<th>PRICE</th>
				<th>MANAGE</th>
			</tr>
		</thead>
		<tbody>
			<c:forEach items="${customer.products}" var="product">
				<tr>
					<td><img
						src="data:image/jpeg;base64, ${product.key.getImageString()}"
						width="50" height="50" /> ${product.key.name}</td>
					<td>${product.value}</td>
					<td>${product.key.price}</td>
					<td>
					<a class="btn btn-secondary"
						href="<c:url value='/customers/addProduct?productId=${product.key.id}&numberOfProducts=${product.value - 1}'/> ">- 1</a>
					<a class="btn btn-secondary"
						href="<c:url value='/customers/addProduct?productId=${product.key.id}&numberOfProducts=${product.value + 1}'/> ">+ 1</a>
					<a class="btn btn-danger"
						href="<c:url value='/customers/removeProduct?productId=${product.key.id}'/> ">Delete</a></td>
				</tr>
			</c:forEach>
			<tr>
				<td colspan="4" class="text-right"><b>${customer.totalAccount}</b></td>
			</tr>
			<tr>
				<td><a href="<c:url value='/' />" class="btn btn-primary">ADD MORE</a></td>
				<td></td>
				<td></td>
				<td><a href="<c:url value='/customers/form' />" class="btn btn-primary">SEND PRODUCTS</a></td>
			</tr>
		</tbody>
	</table>
</div>

<br>
<br>