<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>

<br>
<h5 class="text-center">Save&nbsp;Customer</h5>
<br>

<form:form action="${pageContext.request.contextPath}/customers/save"
	method="post" modelAttribute="customer">
	
	<form:hidden path="totalAccount"/>

	<div class="form-group row">
		<c:forEach items="${customer.products}" var="product">
			<form:hidden path="products[${product.key.id}]"
				value="${product.value}" />
		</c:forEach>
		<div class="col-sm-6 offset-sm-4">
			<form:errors path="products" cssClass="text-danger" />
		</div>
	</div>

	<div class="form-group row">
		<form:label path="date"
			class="col-sm-3 offset-sm-1 col-form-label text-right">DATE</form:label>
		<div class="col-sm-6">
			<form:input path="date" class="form-control" />
			<form:errors path="date" cssClass="text-danger" />
		</div>
	</div>

	<div class="form-group row">
		<form:label path="fullName"
			class="col-sm-3 offset-sm-1 col-form-label text-right">FULL NAME</form:label>
		<div class="col-sm-6">
			<form:input path="fullName" class="form-control" />
			<form:errors path="fullName" cssClass="text-danger" />
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
		<form:label path="address.street"
			class="col-sm-3 offset-sm-1 col-form-label text-right">ADDRESS STREET</form:label>
		<div class="col-sm-6">
			<form:input path="address.street" class="form-control" />
			<form:errors path="address.street" cssClass="text-danger" />
		</div>
	</div>

	<div class="form-group row">
		<form:label path="address.zipCode"
			class="col-sm-3 offset-sm-1 col-form-label text-right">ADDRESS ZIP CODE</form:label>
		<div class="col-sm-6">
			<form:input path="address.zipCode" class="form-control" />
			<form:errors path="address.zipCode" cssClass="text-danger" />
		</div>
	</div>

	<div class="form-group row">
		<form:label path="address.city"
			class="col-sm-3 offset-sm-1 col-form-label text-right">ADDRESS CITY</form:label>
		<div class="col-sm-6">
			<form:input path="address.city" class="form-control" />
			<form:errors path="address.city" cssClass="text-danger" />
		</div>
	</div>

	<div class="form-group row">
		<form:label path="address.state"
			class="col-sm-3 offset-sm-1 col-form-label text-right">ADDRESS STATE</form:label>
		<div class="col-sm-6">
			<form:input path="address.state" class="form-control" />
			<form:errors path="address.state" cssClass="text-danger" />
		</div>
	</div>

	<div class="form-group row">
		<form:label path="telephone"
			class="col-sm-3 offset-sm-1 col-form-label text-right">TELEPHONE</form:label>
		<div class="col-sm-6">
			<form:input path="telephone" class="form-control" />
			<form:errors path="telephone" cssClass="text-danger" />
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