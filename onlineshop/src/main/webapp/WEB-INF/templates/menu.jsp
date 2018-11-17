<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="sec"
	uri="http://www.springframework.org/security/tags"%>

<c:url var="url"
	value="${requestScope['javax.servlet.forward.servlet_path']}" />

<br>
<ul class="nav flex-column nav-pills">
	<c:forEach var="category" items="${categories}">
		<li class="nav-item"><a
			class="nav-link ${(url == '/onlineshop/' && param.categoryId == category.id) ? 'active' : ''}"
			href="<c:url value='/?categoryId=${category.id}'/>">${category.name}</a></li>
	</c:forEach>

	<sec:authorize access="isAuthenticated()">
		<li class="nav-item"><a
			class="nav-link ${(url == '/onlineshop/users/edit') ? 'active' : ''}"
			href="<c:url value='/users/edit'/>">CHANGE&nbsp;PASSWORD</a></li>
		<li class="nav-item"><a
			class="nav-link ${(url == '/onlineshop/categories') ? 'active' : ''}"
			href="<c:url value='/categories'/>">CATEGORIES</a></li>
		<li class="nav-item"><a
			class="nav-link ${(url == '/onlineshop/categories/form') ? 'active' : ''}"
			href="<c:url value='/categories/form'/>">CATEGORY&nbsp;FORM</a></li>
		<li class="nav-item"><a
			class="nav-link ${(url == '/onlineshop/products') ? 'active' : ''}"
			href="<c:url value='/products'/>">PRODUCTS</a></li>
		<li class="nav-item"><a
			class="nav-link ${(url == '/onlineshop/products/form') ? 'active' : ''}"
			href="<c:url value='/products/form'/>">PRODUCT&nbsp;FORM</a></li>
		<li class="nav-item"><a
			class="nav-link ${(url == '/onlineshop/customers') ? 'active' : ''}"
			href="<c:url value='/customers'/>">CUSTOMERS</a></li>
	</sec:authorize>

	<li class="nav-item"><a
		class="nav-link ${(url == '/onlineshop/customers/form') ? 'active' : ''}"
		href="<c:url value='/customers/form'/>">CUSTOMER&nbsp;FORM</a></li>

	<sec:authorize access="hasAuthority('ADMIN')">
		<li class="nav-item"><a
			class="nav-link ${(url == '/onlineshop/users') ? 'active' : ''}"
			href="<c:url value='/users'/>">USERS</a></li>
		<li class="nav-item"><a
			class="nav-link ${(url == '/onlineshop/users/form') ? 'active' : ''}"
			href="<c:url value='/users/form'/>">USER&nbsp;FORM</a></li>
	</sec:authorize>

</ul>
