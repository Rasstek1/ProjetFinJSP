<%@ page contentType="text/html; charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<%@ include file="header.jsp" %>

<div class="container mt-5">
    <h1>Confirmation de Réservation</h1>

    <div class="alert alert-success" role="alert">
        Votre réservation a été confirmée avec succès.
    </div>

    <p>Numéro de Réservation : ${numReservation}</p>

    <!-- Code de la notification toast -->
    <div class="toast" role="alert" aria-live="assertive" aria-atomic="true" data-delay="3000">
        <div class="toast-header">
            <strong class="mr-auto">Notification</strong>
            <button type="button" class="ml-2 mb-1 close" data-dismiss="toast" aria-label="Close">
                <span aria-hidden="true">&times;</span>
            </button>
        </div>
        <div class="toast-body">
            <% if (Boolean.TRUE.equals(request.getAttribute("emailSent"))) { %>
            Un courriel de confirmation a été envoyé à votre adresse.
            <% } else { %>
            Désolé, nous n'avons pas pu envoyer le courriel de confirmation. Veuillez vérifier votre boîte de réception ultérieurement ou nous contacter.
            <% } %>
        </div>
    </div>

</div>

<%@ include file="footer.jsp" %>

<!-- Script pour afficher la notification toast au chargement de la page -->
<script>
    $(document).ready(function(){
        $('.toast').toast('show');
    });
</script>

