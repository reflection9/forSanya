<%@ page contentType="text/html; charset=UTF-8" %>
<%@ page pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html lang="ru">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">

    <link href="<c:url value='/assets/css/headerStyles.css' />" rel="stylesheet">
    <link href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0/css/all.min.css" rel="stylesheet">
    <title>Сайт про мангу и комиксы</title>
</head>
<body>
<div class="header">
    <div class="container">
        <a href="/catalog" class="logo">MangaCloud</a>
        <nav>
            <ul class="nav-links">
                <c:if test="${not empty sessionScope.user and sessionScope.user.role == 'admin'}">
                    <li><a href="/admin"> Админ-панель</a></li>
                </c:if>
                <c:if test="${not empty sessionScope.user}">
                    <li><a href="/profile"> Профиль</a></li>
                    <li><a href="/logout"> Выйти</a></li>
                </c:if>
                <c:if test="${empty sessionScope.user}">
                    <li><a href="/login"> Войти</a></li>
                    <li><a href="/register"> Регистрация</a></li>
                </c:if>
            </ul>
        </nav>
    </div>
</div>
</body>
