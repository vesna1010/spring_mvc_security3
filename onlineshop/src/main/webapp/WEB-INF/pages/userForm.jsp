<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="sec"
	uri="http://www.springframework.org/security/tags"%>
<%@ page import="com.vesna1010.onlineshop.enums.Authority"%>

<br>
<h5 class="text-center">Save&nbsp;User</h5>
<br>

<form:form action="${pageContext.request.contextPath}/users/save"
	method="post" modelAttribute="user">

	<sec:authorize access="!hasAuthority('ADMIN')">
		<form:hidden path="username" />
		<form:hidden path="email" />
		<form:hidden path="authority" />
	</sec:authorize>

	<sec:authorize access="hasAuthority('ADMIN')">
		<div class="form-group row">
			<form:label path="username"
				class="col-sm-3 offset-sm-1 col-form-label text-right">USERNAME</form:label>
			<div class="col-sm-6">
				<form:input path="username" class="form-control" />
				<form:errors path="username" cssClass="text-danger" />
			</div>
		</div>

		<div class="form-group row">
			<form:label path="email"
				class="col-sm-3 offset-sm-1 col-form-label text-right">EMAIL</form:label>
			<div class="col-sm-6">
				<form:input path="email" class="form-control" />
				<form:errors path="email" cssClass="text-danger" />
			</div>
		</div>

		<div class="form-group row">
			<form:label path="authority"
				class="col-sm-3 offset-sm-1 col-form-label text-right">AUTHORITY</form:label>
			<div class="col-sm-6">
				<form:select path="authority" class="form-control">
					<form:option value=""></form:option>
					<form:options items="${Authority.values()}" />
				</form:select>
				<form:errors path="authority" cssClass="text-danger" />
			</div>
		</div>
	</sec:authorize>

	<div class="form-group row">
		<form:label path="password"
			class="col-sm-3 offset-sm-1 col-form-label text-right">PASSWORD</form:label>
		<div class="col-sm-6">
			<form:password path="password" class="form-control" />
			<form:errors path="password" cssClass="text-danger" />
		</div>
	</div>

	<div class="form-group row">
		<form:label path="confirmPassword"
			class="col-sm-3 offset-sm-1 col-form-label text-right">CONFIRM PASSWORD</form:label>
		<div class="col-sm-6">
			<form:password path="confirmPassword" class="form-control" />
			<form:errors path="confirmPassword" cssClass="text-danger" />
		</div>
	</div>

	<div class="form-group row">
		<div class="col-sm-6 offset-sm-4">
			<div class="btn-group">
				<form:button type="submit" class="btn btn-primary">Save</form:button>
				<form:button type="reset" class="btn btn-secondary">Reset</form:button>
			</div>
		</div>
	</div>
</form:form>

<br>
<br>