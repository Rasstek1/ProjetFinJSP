<%@ page contentType="text/html; charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>


<div id="footer" class="footer-container" >
    <div class="footer-section">
        <p id="current-time"></p>
    </div>
    <div class="footer-section">
        <p>&copy; ${year} Les Chalets du Quebec - 102 de la mine, Gatineau, Quebec</p>
    </div>

    <!-- Section d'heure au centre -->


    <!-- Liens à droite -->
    <div class="footer-section">
        <ul class="footer-links">
            <li>
                <a href="${pageContext.request.contextPath}/accueil">Accueil</a>
            </li>
            <li>
                <a href="${pageContext.request.contextPath}/listeChalets">Réservation</a>
            </li>
            <li>
                <a href="${pageContext.request.contextPath}/admin">Administration</a>
            </li>
        </ul>
    </div>
</div>



<script>
    document.addEventListener('DOMContentLoaded', (event) => {
        function updateTime() {
            let now = new Date();
            let formattedDate = now.toLocaleDateString('fr-FR', {
                year: 'numeric', month: 'long', day: 'numeric',
                hour: '2-digit', minute: '2-digit', second: '2-digit'
            });
            document.getElementById('current-time').textContent = formattedDate;
        }

        // Appel initial pour définir l'heure au moment du chargement de la page
        updateTime();

        // Mise à jour de l'heure toutes les secondes
        setInterval(updateTime, 1000);
    });

</script>

<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.0.0-beta2/dist/js/bootstrap.bundle.min.js"></script>

<script src="https://code.jquery.com/jquery-3.3.1.slim.min.js" integrity="sha384-q8i/X+965DzO0rT7abK41JStQIAqVgRVzpbzo5smXKp4YfRvH+8abtTE1Pi6jizo" crossorigin="anonymous"></script>


<script src="https://cdnjs.cloudflare.com/ajax/libs/popper.js/1.14.7/umd/popper.min.js" integrity="sha384-UO2eT0CpHqdSJQ6hJty5KVphtPhzWj9WO1clHTMGa3JDZwrnQq4sF86dIHNDz0W1" crossorigin="anonymous"></script>

</body>
</html>
