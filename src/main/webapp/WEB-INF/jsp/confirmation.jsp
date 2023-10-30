<%@ page contentType="text/html; charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<%@ include file="header.jsp" %>

    <div class="content">
        <div class="card shadow mb-4 form-container">
            <div class="card-header py-3">
                <h6 class="m-0 font-weight-bold text-primary">Confirmation</h6>
            </div>
            <div class="card-body">


                <div class="alert alert-success" role="alert">
                    Votre réservation a été confirmée avec succès.
                </div>


                <div>
                    <a class="nav-link" href="${pageContext.request.contextPath}/accueil">Retour a l'accueil</a>
                </div>
            </div>
        </div>
    </div>

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
            Un courriel de confirmation a été envoyé à votre adresse.
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

