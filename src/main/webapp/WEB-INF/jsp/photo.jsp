<%@ page contentType="text/html; charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>

<%@ include file="header.jsp" %>

<!-- Importation du CSS de Bootstrap -->
<link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/4.5.2/css/bootstrap.min.css">

<div class="container mt-5">

    <!-- Titre principal -->
    <c:choose>
    <c:when test="${fn:length(photos) > 0}">
    <c:forEach var="photo" items="${photos}">
    <div class="col-md-4 mb-4">
        <img src="<c:url value='/uploads/${photo}'/>" alt="Photo du chalet ${chalet.numChalet}" class="img-fluid">
    </div>
    </c:forEach>
    </c:when>
    <c:otherwise>
    <p>Aucune photo disponible pour ce chalet.</p>
    </c:otherwise>
    </c:choose>

</div>

<!-- Inclusion du pied de page -->
<%@ include file="footer.jsp" %>

