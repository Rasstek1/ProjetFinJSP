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
<div class="content">
    <div class="card shadow mb-4 form-container">
        <div class="card-header py-3">
            <h6 class="m-0 font-weight-bold text-primary">Connexion</h6>
        </div>
        <div class="card-body">
            <!-- Afficher le message d'erreur ici -->
            <c:if test="${param.loginError}">
                <div class="alert alert-danger">
                    Identifiants incorrects. Veuillez réessayer.
                </div>
            </c:if>

            <form action="${pageContext.request.contextPath}/connexion" method="post">
                <div class="form-group">
                    <label for="username">Nom d'utilisateur :</label>
                    <input type="text" class="form-control" id="username" name="username" value="${courriel}" required>
                </div>

                <div class="form-group">
                    <label for="password">Mot de Passe :</label>
                    <input type="password" class="form-control" id="password" name="password" value="${motPasse}" required>
                </div>

                <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}" />

                <button type="submit" class="btn btn-primary">Se connecter</button>
            </form>
        </div>
    </div>
</div>
</body>
<%@ include file="footer.jsp" %>