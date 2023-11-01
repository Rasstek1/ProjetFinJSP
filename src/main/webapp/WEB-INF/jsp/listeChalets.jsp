<%@ page contentType="text/html; charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>

<%@ include file="header.jsp" %>

<!-- Importation du CSS de Bootstrap -->
<link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/4.5.2/css/bootstrap.min.css">

<div class="content" id="content">
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
                                        <img src="uploads/${chalet.listePhotos[0]}"
                                             alt="Photo du chalet ${chalet.numChalet}">
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
                                            <small class="text-muted">à partir de <fmt:formatNumber
                                                    value="${chalet.prix}"
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
                                            <img src="${pageContext.request.contextPath}/img/card_bath.png"
                                                 alt="Icone 1">
                                        </p>
                                        <p class="card-text">
                                            <img src="${pageContext.request.contextPath}/img/chalet_pers.png"
                                                 alt="Icone 2">
                                        </p>
                                        <p class="card-text">
                                            <img src="${pageContext.request.contextPath}/img/internet.png"
                                                 alt="Icone 2">
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
                <nav aria-label="Page navigation">
                    <ul class="pagination justify-content-center">
                        <!-- Les éléments de pagination seront ajoutés ici par le script JS -->
                    </ul>
                </nav>
    </div> <!-- Fin de la balise <div class="container mt-5"> -->

</div>


<script>
    // Fonction pour cacher tous les chalets
    function hideChalets() {
        var chalets = document.querySelectorAll('.row .col-md-4');
        chalets.forEach(function(chalet) {
            chalet.style.display = 'none';
        });
    }
    // Fonction pour faire défiler jusqu'à l'élément de contenu
    function scrollToContent() {
        var contentElement = document.getElementById('content');
        if (contentElement) {
            contentElement.scrollIntoView({ behavior: 'smooth', block: 'start' });
        }
    }
    // Fonction pour montrer les chalets pour une page donnée
    function showChalets(page) {
        var perPage = 6; // Nombre de chalets par page
        var start = (page - 1) * perPage;
        var end = start + perPage;
        var chalets = document.querySelectorAll('.row .col-md-4');
        chalets.forEach(function(chalet, index) {
            if (index >= start && index < end) {
                chalet.style.display = 'block';
            }
        });
    }

    document.addEventListener('DOMContentLoaded', function() {
        var chalets = document.querySelectorAll('.row .col-md-4');
        var numPages = Math.ceil(chalets.length / 6);
        var pagination = document.querySelector('.pagination');

        hideChalets(); // Cachez tous les chalets au début
        showChalets(1); // Affichez les premiers 6 chalets
        scrollToContent(); // Faites défiler jusqu'au contenu au chargement de la page

        // Créez les boutons de pagination
        for (let i = 1; i <= numPages; i++) {
            var li = document.createElement('li');
            li.className = 'page-item';
            var a = document.createElement('a');
            a.className = 'page-link';
            a.innerText = i;
            a.href = '#';
            li.appendChild(a);
            pagination.appendChild(li);

            // Ajoutez un événement click pour chaque bouton de pagination
            a.addEventListener('click', function(e) {
                e.preventDefault();
                hideChalets();
                showChalets(i);
                scrollToContent(); // Faites défiler jusqu'au contenu lorsqu'un numéro de page est cliqué
            });
        }
    });
</script>


<!-- Inclusion du pied de page -->
<%@ include file="footer.jsp" %>

