<%@ page import="org.springframework.security.core.context.SecurityContextHolder" %>
<%@ page contentType="text/html; charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>

<%@ include file="header.jsp" %>

<body>
<div class="content">
    <div class="row">
        <!-- Formulaire à gauche -->
        <div class="col-md-6 mx-auto"> <!-- Ajoutez la classe mx-auto ici -->
            <div class="card shadow mb-4 form-container">
                <div class="card-header py-3">
                    <h6 class="m-0 font-weight-bold text-primary">Ajout de Chalet</h6>
                </div>
                <div class="card-body">
                    <h2>Bienvenue sur le site des chalets Québec</h2>

                </div>
            </div>
        </div>
    </div>
</div>

<%@ include file="footer.jsp" %>
</body>
