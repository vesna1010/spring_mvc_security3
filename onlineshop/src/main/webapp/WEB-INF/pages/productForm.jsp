<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>

<br>
<h5 class="text-center">Save&nbsp;Product</h5>
<br>

<form:form
	action="${pageContext.request.contextPath}/products/save?${_csrf.parameterName}=${_csrf.token}"
	method="post" enctype="multipart/form-data" modelAttribute="product">

	<div class="form-group row">
		<form:label path="id"
			class="col-sm-3 offset-sm-1 col-form-label text-right">ID</form:label>
		<div class="col-sm-6">
			<form:input path="id" class="form-control" />
			<form:errors path="id" cssClass="text-danger" />
		</div>
	</div>

	<div class="form-group row">
		<form:label path="name"
			class="col-sm-3 offset-sm-1 col-form-label text-right">NAME</form:label>
		<div class="col-sm-6">
			<form:input path="name" class="form-control" />
			<form:errors path="name" cssClass="text-danger" />
		</div>
	</div>

	<div class="form-group row">
		<form:label path="description"
			class="col-sm-3 offset-sm-1 col-form-label text-right">DESCRIPTION</form:label>
		<div class="col-sm-6">
			<form:input path="description" class="form-control" />
			<form:errors path="description" cssClass="text-danger" />
		</div>
	</div>

	<div class="form-group row">
		<form:label path="price"
			class="col-sm-3 offset-sm-1 col-form-label text-right">PRICE</form:label>
		<div class="col-sm-6">
			<form:input path="price" class="form-control" />
			<form:errors path="price" cssClass="text-danger" />
		</div>
	</div>

	<div class="form-group row">
		<form:label path="category"
			class="col-sm-3 offset-sm-1 col-form-label text-right">CATEGORY</form:label>
		<div class="col-sm-6">
			<form:select path="category" class="form-control">
				<form:option value=""></form:option>
				<form:options items="${categories}" itemValue="id" itemLabel="name" />
			</form:select>
			<form:errors path="category" cssClass="text-danger" />
		</div>
	</div>

	<div class="form-group row">
		<form:label path="stocks"
			class="col-sm-3 offset-sm-1 col-form-label text-right">STOCKS</form:label>
		<div class="col-sm-6">
			<form:input path="stocks" class="form-control" />
			<form:errors path="stocks" cssClass="text-danger" />
		</div>
	</div>

	<div class="form-group row">
		<form:label path="file"
			class="col-sm-2 offset-sm-2 col-form-label text-right">IMAGE</form:label>
		<div class="col-sm-6">
			<form:input path="file" type="file" class="form-control-file" />
			<form:errors path="image" cssClass="text-danger" />
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