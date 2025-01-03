<%@ page contentType="text/html; charset=UTF-8" %>
<%@ page pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <link href="<c:url value='/assets/css/base-adminStyles.css' />" rel="stylesheet">
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css">
    <link href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0/css/all.min.css" rel="stylesheet">
    <title>Управление комментариями</title>
</head>
<body>
<div class="header">
    <h1>Админ-панель: Управление комментариями</h1>
</div>
<div class="sidebar">
    <a href="/admin">Админка</a>
    <a href="/admin/users">Управление пользователями</a>
    <a href="/admin/titles">Управление тайтлами</a>
    <a href="/admin/genres">Управление жанрами</a>
    <a href="/admin/authors">Управление авторами</a>
</div>
<div class="content">
    <div class="container">
        <h2>Список комментариев</h2>
        <table class="table">
            <thead>
            <tr>
                <th>ID</th>
                <th>Пользователь</th>
                <th>Тайтл</th>
                <th>Комментарий</th>
                <th>Дата</th>
                <th>Действия</th>
            </tr>
            </thead>
            <tbody>
            <c:forEach var="comment" items="${comments}">
                <tr>
                    <td>${comment.id}</td>
                    <td>${comment.userId != null ? comment.userId : 'Гость'}</td>
                    <td>${comment.titleId}</td>
                    <td>${comment.content}</td>
                    <td>${comment.createdAt}</td>
                    <td>
                        <form method="post" action="/admin/comments" style="display:inline;">
                            <input type="hidden" name="id" value="${comment.id}">
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
