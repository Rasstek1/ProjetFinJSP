<%@ page contentType="text/html; charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>

<%@ include file="header.jsp" %>

<!-- Importation du CSS de Bootstrap -->
<link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/4.5.2/css/bootstrap.min.css">

<div class="container mt-5">

    <!-- Carrousel Bootstrap -->
    <div id="chaletCarousel" class="carousel slide" data-ride="carousel">
        <!-- Indicateurs (miniatures) -->
        <ol class="carousel-indicators">
            <c:forEach var="photo" items="${photos}" varStatus="status">
                <li data-target="#chaletCarousel" data-slide-to="${status.index}" class="<c:if test="${status.index == 0}">active</c:if>"></li>
            </c:forEach>
        </ol>

        <div class="carousel-inner">
            <c:choose>
                <c:when test="${fn:length(photos) > 0}">
                    <c:forEach var="photo" items="${photos}" varStatus="status">
                        <div class="carousel-item <c:if test="${status.index == 0}">active</c:if>">
                            <img src="<c:url value='/uploads/${photo}'/>" alt="Photo du chalet ${chalet.numChalet}" class="d-block w-100">
                        </div>
                    </c:forEach>
                </c:when>
                <c:otherwise>
                    <p>Aucune photo disponible pour ce chalet.</p>
                </c:otherwise>
            </c:choose>
        </div>

        <!-- Contrôles du carrousel (flèches) -->
        <a class="carousel-control-prev" href="#chaletCarousel" role="button" data-slide="prev">
            <span class="carousel-control-prev-icon" aria-hidden="true"></span>
            <span class="sr-only">Précédent</span>
        </a>
        <a class="carousel-control-next" href="#chaletCarousel" role="button" data-slide="next">
            <span class="carousel-control-next-icon" aria-hidden="true"></span>
            <span class="sr-only">Suivant</span>
        </a>
    </div>

    <!-- Conteneur des miniatures -->
    <div class="miniatures-container mt-3">
        <div class="row no-gutters">
            <c:forEach var="photo" items="${photos}" varStatus="status">
                <div class="col">
                    <a href="#" data-target="#chaletCarousel" data-slide-to="${status.index}">
                        <img src="<c:url value='/uploads/${photo}'/>" alt="Miniature du chalet ${chalet.numChalet}" class="img-fluid">
                    </a>
                </div>
            </c:forEach>
        </div>
    </div>
    <!-- Description du chalet -->
    <h1 class="mt-5">Description</h1>
    <p>${chalet.description}</p>
</div>


<!-- Scripts JS de Bootstrap -->
<script src="https://code.jquery.com/jquery-3.5.1.slim.min.js"></script>
<script src="https://cdn.jsdelivr.net/npm/@popperjs/core@2.9.3/dist/umd/popper.min.js"></script>
<script src="https://maxcdn.bootstrapcdn.com/bootstrap/4.5.2/js/bootstrap.min.js"></script>

<!-- Inclusion du pied de page -->
<%@ include file="footer.jsp" %>
