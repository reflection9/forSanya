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
    <title>Управление жанрами</title>
</head>
<body>
<div class="header">
    <h1>Админ-панель: Управление жанрами</h1>
</div>
<div class="sidebar">
    <a href="/admin">Админка</a>
    <a href="/admin/users">Управление пользователями</a>
    <a href="/admin/titles">Управление тайтлами</a>
    <a href="/admin/authors">Управление авторами</a>
    <a href="/admin/comments">Управление комментариями</a>
</div>
<div class="content">
    <div class="container">
        <h2>Добавить жанр</h2>
        <form method="post" action="/admin/genres">
            <label for="addGenreName">Название жанра:</label>
            <input type="text" id="addGenreName" name="name" class="form-control" required>
            <button type="submit" name="action" value="add" class="btn">Добавить</button>
        </form>

        <h2>Обновить жанр</h2>
        <form method="post" action="/admin/genres">
            <label for="updateGenreId">ID жанра:</label>
            <input type="text" id="updateGenreId" name="genreId" class="form-control" required>
            <label for="updateGenreName">Новое название жанра:</label>
            <input type="text" id="updateGenreName" name="name" class="form-control" required>
            <button type="submit" name="action" value="update" class="btn">Обновить</button>
        </form>

        <h2>Список жанров</h2>
        <table class="table">
            <thead>
            <tr>
                <th>ID</th>
                <th>Название</th>
                <th>Действия</th>
            </tr>
            </thead>
            <tbody>
            <c:forEach var="genre" items="${genres}">
                <tr>
                    <td>${genre.id}</td>
                    <td>${genre.name}</td>
                    <td>
                        <form method="post" action="/admin/genres" style="display:inline;">
                            <input type="hidden" name="genreId" value="${genre.id}">
                            <button type="submit" name="action" value="delete" class="btn btn-danger">Удалить</button>
                        </form>
                    </td>
                </tr>
            </c:forEach>
            </tbody>
        </table>
    </div>
</div>
</body>
</html>
