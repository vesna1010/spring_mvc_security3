<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title><tiles:getAsString name="title"></tiles:getAsString></title>
<link rel="stylesheet"
	href="https://maxcdn.bootstrapcdn.com/bootstrap/4.0.0/css/bootstrap.min.css">
<link rel="stylesheet" href="<c:url value='/resources/css/style.css'/>">
</head>
<body>
	<div class="container-fluid">
		<div class="row">
			<div class="col-sm-12 header">
				<tiles:insertAttribute name="header"></tiles:insertAttribute>
			</div>
		</div>
		<div class="row">
			<div class="col-sm-2 menu">
				<tiles:insertAttribute name="menu"></tiles:insertAttribute>
			</div>
			<div class="col-sm-10 body">
				<tiles:insertAttribute name="body"></tiles:insertAttribute>
			</div>
		</div>
		<div class="row">
			<div class="col-sm-12 footer">
				<tiles:insertAttribute name="footer"></tiles:insertAttribute>
			</div>
		</div>
	</div>
</body>
</html>