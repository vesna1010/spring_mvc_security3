<%@ tag language="java" pageEncoding="ISO-8859-1"
	import="java.lang.Math, java.lang.Integer"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<%@ attribute name="numberObjects" required="true"
	type="java.lang.Integer"%>
<%@ attribute name="currentPage" required="true"
	type="java.lang.Integer"%>
<%@ attribute name="pageSize" required="true" type="java.lang.Integer"%>
<%@ attribute name="url" required="true" type="java.lang.String"%>

<c:set var="currentPage"
	value="${currentPage == null || currentPage == '' ? 1 : currentPage}"></c:set>

<c:set var="pageSize"
	value="${pageSize == null || pageSize == '' ? 10 : pageSize}"></c:set>

<c:set var="numberPages"
	value="${Integer(Math.ceil(numberObjects/pageSize))}"></c:set>

<c:set var="startPage"
	value="${((currentPage - 2) > 1) ?  (currentPage - 2) : 1}"></c:set>

<c:set var="endPage"
	value="${((startPage + 4) < numberPages) ? (startPage + 4) : numberPages}"></c:set>

<c:set var="startPage"
	value="${((endPage - 4) > 1) ? (endPage - 4) : 1}"></c:set>

<nav>
	<ul class="pagination">
		<c:if test="${currentPage > 1}">
			<li class="page-item">
			    <c:url var="currentUrl" value="${url}">
					<c:param name="page" value="${currentPage - 1}"></c:param>
					<c:param name="size" value="${pageSize}"></c:param>
				</c:url> 
				<a class="page-link" href="${currentUrl}">Previous</a>
			</li>
		</c:if>

		<c:forEach var="num" begin="${startPage}" end="${endPage}">
			<li class="page-item ${currentPage == num ? 'active' : ''}">
			    <c:url var="currentUrl" value="${url}">
					<c:param name="page" value="${num}"></c:param>
					<c:param name="size" value="${pageSize}"></c:param>
				</c:url> 
				<a class="page-link" href="${currentUrl}">${num}</a>
			</li>
		</c:forEach>

		<c:if test="${currentPage < numberPages}">
			<li class="page-item"><c:url var="currentUrl" value="${url}">
					<c:param name="page" value="${currentPage + 1}"></c:param>
					<c:param name="size" value="${pageSize}"></c:param>
				</c:url> 
				<a class="page-link" href="${currentUrl}">Next</a>
			</li>
		</c:if>

	</ul>
</nav>

