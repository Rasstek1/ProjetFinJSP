<%@ include file="header.jsp" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>

<div class="content">
    <div class="container" style="margin-bottom: 200px; margin-top:50px;">
        <div class="row justify-content-center">
            <div class="col-md-8">
                <div class="card">
                    <div class="card-header text-center">
                        <h2>Oups! Quelque chose s'est mal passé.</h2>
                    </div>
                    <div class="card-body text-center">
                        <img src="${pageContext.request.contextPath}/img/skull.gif" alt="Logo" class="gif" style="width: 150px"/>
                        <h3>Détails</h3>
                        <p><strong>Code d'erreur :</strong> <c:out value="${errorCode}"/></p>
                        <p><strong>Message :</strong> <c:out value="${errorMessage}"/></p>
                        <p><strong>Description :</strong> <c:out value="${errorDetails}"/></p>
                        <p><strong>URL :</strong> <c:out value="${requestURL}"/></p>
                        <p><strong>Méthode :</strong> <c:out value="${method}"/></p>
                        <p><strong>Exception :</strong> <c:out value="${exception}"/></p>
                        <p>Nous nous excusons pour ce désagrément. S'il vous plaît <a href="contact">contactez-nous</a> si cette erreur persiste.</p>
                    </div>
                </div>

            </div>
            </div>
        </div>
    </div>
</div>

<%@ include file="footer.jsp" %>

