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
            <h6 class="m-0 font-weight-bold text-primary">Connexion</h6>
        </div>
        <div class="card-body">
            <p>Vous êtes déconnecté!</p>
        </div>
    </div>
    <div style="height: 100px"></div>
</div>
</body>
<%@ include file="footer.jsp" %>
