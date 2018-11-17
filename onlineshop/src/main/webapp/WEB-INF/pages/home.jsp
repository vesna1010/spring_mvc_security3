<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="tag" tagdir="/WEB-INF/tags/"%>

<div class="row">
	<c:forEach items="${products}" var="product" varStatus="status">
		<div class="col-sm-4 text-center">
			<br> <a
				href="<c:url value='/products/details?productId=${product.id}'/>">
				<img src="data:image/jpeg;base64, ${product.getImageString()}"
				width="200" height="200" />
			</a> <br> <span class="text-center">${product.name}</span> <br>
			<span class="text-center">${product.price}</span>
		</div>
	</c:forEach>
</div>

<div class="row">
	<div class="col-sm-4">
		<br> <br> <br>
		<tag:pagination pageSize="${param.size}" currentPage="${param.page}"
			numberObjects="${count}"
			url="${param.categoryId == null || param.categoryId == ''   ? '/' : '/?categoryId=' += param.categoryId }"></tag:pagination>
	</div>
</div>
