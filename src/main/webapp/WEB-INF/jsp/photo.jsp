<%@ page contentType="text/html; charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<%@ include file="header.jsp" %>

<h1>Photos du chalet</h1>

<h2>Détails du Chalet</h2>
<p><strong>Numéro du Chalet :</strong> ${chalet.numChalet}</p>
<p><strong>Nombre de Chambres :</strong> ${chalet.nombreChambres}</p>
<p><strong>Description :</strong> ${chalet.description}</p>
<p><strong>Prix :</strong> ${chalet.prix}</p>

<div id="chaletCarousel" class="carousel slide" data-ride="carousel">

    <!-- Indicateurs (miniatures) -->
    <ol class="carousel-indicators">
        <c:forEach var="photo" items="${photos}" varStatus="status">
            <li data-target="#chaletCarousel" data-slide-to="${status.index}" class="${status.index == 0 ? 'active' : ''}"></li>
        </c:forEach>
    </ol>

    <!-- Slides du carrousel -->
    <div class="carousel-inner">
        <c:forEach var="photo" items="${photos}" varStatus="status">
            <div class="carousel-item ${status.index == 0 ? 'active' : ''}">
                <img src="${photo.path}" alt="Photo du chalet ${status.index + 1}" class="d-block w-100">
            </div>
        </c:forEach>
    </div>

    <!-- Contrôles (flèches) -->
    <a class="carousel-control-prev" href="#chaletCarousel" role="button" data-slide="prev">
        <span class="carousel-control-prev-icon" aria-hidden="true"></span>
        <span class="sr-only">Précédent</span>
    </a>
    <a class="carousel-control-next" href="#chaletCarousel" role="button" data-slide="next">
        <span class="carousel-control-next-icon" aria-hidden="true"></span>
        <span class="sr-only">Suivant</span>
    </a>
</div>


<%@ include file="footer.jsp" %>
