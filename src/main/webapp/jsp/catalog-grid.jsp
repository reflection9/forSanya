<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page contentType="text/html; charset=UTF-8" %>
<c:forEach var="title" items="${titles}">
    <div class="catalog-item">
        <a href="/title?id=${title.id}">
            <c:choose>
                <c:when test="${not empty title.files}">
                    <img src="${title.files[0].filePath}" alt="Обложка ${title.name}">
                </c:when>
                <c:otherwise>
                    <img src="/uploads/placeholder.png" alt="Нет обложки">
                </c:otherwise>
            </c:choose>
            <div class="catalog-details">
                <h2 class="catalog-title">${title.name}</h2>
            </div>
        </a>
    </div>
</c:forEach>
