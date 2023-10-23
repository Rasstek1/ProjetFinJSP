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
    <h2 class="mb-4">${fn:length(listeChalets)} chalet(s) trouvé(s)</h2>

    <div class="row">

        <!-- Boucle pour itérer sur chaque chalet et afficher ses détails -->
        <c:forEach var="chalet" items="${chalets}">
            <div class="col-md-4 mb-4">
                <div class="card h-100 shadow">
                    <img src="${chalet.photoPrincipale}" alt="${chalet.nom}" class="card-img-top">
                    <div class="card-body">
                        <h5 class="card-title">${chalet.nom}</h5>
                        <p class="card-text">${chalet.description}</p>
                        <p class="card-text"><small class="text-muted">à partir de <fmt:formatNumber value="${chalet.prix}" type="currency" currencySymbol="$" minFractionDigits="2" maxFractionDigits="2"/></small></p>
                        <p class="card-text">${chalet.capacite} pers. | ${chalet.nombreChambres} chbre. | ${chalet.nombreSdb} sdb.</p>
                    </div>
                </div>
            </div>
        </c:forEach>
    </div>
</div>

<!-- Inclusion du pied de page -->
<%@ include file="footer.jsp" %>
