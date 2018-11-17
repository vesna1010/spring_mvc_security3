<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="sec"
	uri="http://www.springframework.org/security/tags"%>

<div class="row">
	<div class="col-sm-4 text-left">
		<a class="nav-link" href="<c:url value='/'/>"><img alt="Home"
			src="<c:url value='/resources/images/home.jpg'/>"></a>
	</div>
	<div class="col-sm-4 text-center">
		<h3>ONLINE&nbsp;SHOP</h3>
	</div>
	<div class="col-sm-2 text-right">
		<a href="<c:url value='/customers/products'/>"><img alt="Home"
			src="<c:url value='/resources/images/shop.png'/>"></a>
	</div>
	<div class="col-sm-2 text-right">
		<sec:authorize access="isAuthenticated()">
			<c:import url="../pages/logoutForm.jsp"></c:import>
		</sec:authorize>
		<sec:authorize access="!isAuthenticated()">
			<a href="<c:url value='/login'/>" class="btn btn-secondary">Login</a>
		</sec:authorize>
	</div>
</div>