<%@ page contentType="text/html; charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>

<%@ include file="header.jsp" %>

<!-- Importation du CSS de Bootstrap -->
<link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/4.5.2/css/bootstrap.min.css">

<div class="container mt-5">

    <!-- Titre principal -->
    <h1 class="mb-4">Liste des chalets</h1>

    <!-- Sous-titre pour le nombre de chalets trouvés -->
    <h2 class="mb-4">${fn:length(chalets)} chalet(s) trouvé(s)</h2>

    <div class="row">

        <!-- Boucle pour itérer sur chaque chalet et afficher ses détails -->
        <c:forEach var="chalet" items="${chalets}">
            <div class="col-md-4 mb-4">
                <div class="card h-100 shadow">
                    <c:choose>
                        <c:when test="${not empty chalet.listePhotos and chalet.listePhotos.size() > 0}">
                            <img src="uploads/${chalet.listePhotos[0]}" alt="Photo du chalet ${chalet.numChalet}">
                        </c:when>
                        <c:otherwise>
                            <img src="uploads/defaut.png" alt="Photo par défaut">
                        </c:otherwise>
                    </c:choose>

                    <div class="card-body">
                        <p class="card-text">${chalet.description}</p>
                    </div>

                    <!-- Card Footer pour le prix, le nombre de chambres et les boutons -->
                    <div class="card-footer">
                        <div class="d-flex flex-wrap justify-content-between">
                            <p class="card-text">
                                <small class="text-muted">à partir de <fmt:formatNumber value="${chalet.prix}"
                                                                                        type="currency"
                                                                                        currencySymbol="$"
                                                                                        minFractionDigits="2"
                                                                                        maxFractionDigits="2"/></small>
                            </p>
                            <p class="card-text">
                                <img src="${pageContext.request.contextPath}/img/card_bed.png"
                                     alt="Icone Chambre"> ${chalet.nombreChambres} chbre.
                            </p>
                            <!-- Ajoutez ici les icônes supplémentaires au besoin -->
                            <p class="card-text">
                                <img src="${pageContext.request.contextPath}/img/card_bath.png" alt="Icone 1">
                            </p>
                            <p class="card-text">
                                <img src="${pageContext.request.contextPath}/img/chalet_pers.png" alt="Icone 2">
                            </p>
                            <p class="card-text">
                                <img src="${pageContext.request.contextPath}/img/internet.png" alt="Icone 2">
                            </p>

                        </div>
                        <div class="mt-2">
                            <!-- Bouton Détails -->
                            <a href="${pageContext.request.contextPath}/photo?numChalet=${chalet.numChalet}"
                               class="btn btn-primary">Détails</a>
                            <!-- Bouton Réserver -->
                            <a href="${pageContext.request.contextPath}/reserverChalet/${chalet.numChalet}"
                               class="btn btn-success">Réserver</a>
                        </div>
                    </div>
                </div>
                </div>
        </c:forEach>


    </div> <!-- Fin de la balise <div class="row"> -->

</div> <!-- Fin de la balise <div class="container mt-5"> -->



<!-- Inclusion du pied de page -->
<%@ include file="footer.jsp" %>

