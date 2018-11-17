<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>

<br>
<h5 class="text-center">Save&nbsp;Category</h5>
<br>

<form:form action="${pageContext.request.contextPath}/categories/save"
	method="post" modelAttribute="category">
	
	<form:hidden path="id" />

	<div class="form-group row">
		<form:label path="name"
			class="col-sm-3 offset-sm-1 col-form-label text-right">NAME</form:label>
		<div class="col-sm-6">
			<form:input path="name" class="form-control" />
			<form:errors path="name" cssClass="text-danger" />
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

