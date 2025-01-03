<%@ page contentType="text/html; charset=UTF-8" %>
<%@ page pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <link href="<c:url value='/assets/css/base-adminStyles.css' />" rel="stylesheet">
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
    <title>Админ-панель</title>
</head>
<body>
<div class="header">
    <h1>Панель администратора</h1>
</div>
<div class="sidebar">
    <a href="/jsp/catalog">На главную</a>
    <a href="/admin/users">Управление пользователями</a>
    <a href="/admin/titles">Управление тайтлами</a>
    <a href="/admin/genres">Управление жанрами</a>
    <a href="/admin/authors">Управление авторами</a>
    <a href="/admin/comments">Управление комментариями</a>
</div>
<div class="content">
    <div class="container">
        <h2>Добро пожаловать в панель администратора</h2>
        <p>Выберите раздел для управления ресурсами.</p>
    </div>
</div>
</body>
</html>
