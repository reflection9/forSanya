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
    <title>Управление авторами</title>
</head>
<body>
<div class="header">
    <h1>Админ-панель: Управление авторами</h1>
</div>
<div class="sidebar">
    <a href="/admin">Админка</a>
    <a href="/admin/users">Управление пользователями</a>
    <a href="/admin/titles">Управление тайтлами</a>
    <a href="/admin/genres">Управление жанрами</a>
    <a href="/admin/comments">Управление комментариями</a>
</div>
<div class="content">
    <div class="container">
        <h2>Добавить автора</h2>
        <form method="post" action="/admin/authors">
            <label for="addAuthorName">Имя автора:</label>
            <input type="text" id="addAuthorName" name="name" class="form-control" required>
            <button type="submit" name="action" value="add" class="btn">Добавить</button>
        </form>

        <h2>Редактировать автора</h2>
        <form method="post" action="/admin/authors">
            <label for="editAuthorId">ID автора:</label>
            <input type="text" id="editAuthorId" name="authorId" class="form-control" required>
            <label for="editAuthorName">Новое имя автора:</label>
            <input type="text" id="editAuthorName" name="name" class="form-control" required>
            <button type="submit" name="action" value="update" class="btn">Обновить</button>
        </form>

        <h2>Список авторов</h2>
        <table class="table">
            <thead>
            <tr>
                <th>ID</th>
                <th>Имя</th>
                <th>Действия</th>
            </tr>
            </thead>
            <tbody>
            <c:forEach var="author" items="${authors}">
                <tr>
                    <td>${author.id}</td>
                    <td>${author.name}</td>
                    <td>
                        <form method="post" action="/admin/authors" style="display:inline;">
                            <input type="hidden" name="authorId" value="${author.id}">
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
