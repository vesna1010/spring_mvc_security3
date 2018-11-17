<form action="${pageContext.request.contextPath}/logout" method="post">

	<input type="hidden" name="${_csrf.parameterName}"
		value="${_csrf.token}" />

	<button type="submit" class="btn btn-secondary">Logout</button>
</form>
