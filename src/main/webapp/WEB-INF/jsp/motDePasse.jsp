<%@ page contentType="text/html; charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<%@ include file="header.jsp" %>

<head>
    <style>
        .form-container {
            max-width: 600px;
            margin: 0 auto;
        }
    </style>
</head>

<body>
<div class="content" id="content">
    <div class="card shadow mb-4 form-container">
        <div class="card-header py-3">
            <h6 class="m-0 font-weight-bold text-primary">Changement de mot de passe</h6>
        </div>
        <div class="card-body">
            <!-- Afficher le message d'erreur ici -->
            <c:if test="${param.MPError}">
                <div class="alert alert-danger">
                    Erreur lors du changement de mot de passe. Veuillez réessayer.
                </div>
            </c:if>

            <form action="${pageContext.request.contextPath}/changerMotDePasse" method="post">
                <div class="form-group">
                    <label for="ancienMotDePasse">Ancien Mot de Passe :</label>
                    <input type="password" class="form-control" id="ancienMotDePasse" name="ancienMotDePasse" autocomplete="current-password" required>
                </div>

                <div class="form-group">
                    <label for="nouveauMotDePasse">Nouveau Mot de Passe :</label>
                    <input type="password" class="form-control" id="nouveauMotDePasse" name="nouveauMotDePasse" autocomplete="new-password" required>
                </div>

                <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}" />

                <button type="submit" class="btn btn-primary">Changer le mot de passe</button>
            </form>




        </div>
    </div>
</div>
</body>
<%@ include file="footer.jsp" %>
