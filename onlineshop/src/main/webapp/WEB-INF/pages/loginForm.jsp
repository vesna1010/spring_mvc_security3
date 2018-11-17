<br>
<h3 class="text-center">Log in</h3>
<br>
<h5 class="text-center text-danger">${sessionScope["SPRING_SECURITY_LAST_EXCEPTION"].message}</h5>
<br>

<form action="${pageContext.request.contextPath}/login" method="post">

	<div class="form-group row">
		<label for="username"
			class="col-sm-3 offset-sm-1 col-form-label text-right">USERNAME</label>
		<div class="col-sm-6">
			<input type="text" class="form-control" name="username">
		</div>
	</div>

	<div class="form-group row">
		<label for="password"
			class="col-sm-3 offset-sm-1 col-form-label text-right">PASSWORD</label>
		<div class="col-sm-6">
			<input type="password" class="form-control" name="password">
		</div>
	</div>

	<input type="hidden" name="${_csrf.parameterName}"
		value="${_csrf.token}" />

	<div class="form-group row">
		<div class="col-sm-6 offset-sm-4">
			<button class="btn btn-primary">Log in</button>
		</div>
	</div>
</form>

