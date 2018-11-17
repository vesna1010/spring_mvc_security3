<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<div class="row">
	<div class="col-sm-12 text-center">
		<br> <img class="img-thumbnail"
			src="data:image/jpeg;base64, ${product.getImageString()}" width="300"
			height="300" />
		<div>${product.name}</div>
		<div>${product.price}</div>
		<a
			href="<c:url value='/customers/addProduct?productId=${product.id}' /> "
			class="btn btn-primary">Add Product</a>
	</div>
</div>
