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
    <title>Управление пользователями</title>
</head>
<body>
<div class="header">
    <h1>Админ-панель: Управление Пользователями</h1>
</div>
<div class="sidebar">
    <a href="/admin">Админка</a>
    <a href="/admin/titles">Управление тайтлами</a>
    <a href="/admin/genres">Управление жанрами</a>
    <a href="/admin/authors">Управление авторами</a>
    <a href="/admin/comments">Управление комментариями</a>
</div>
<div class="content">
    <div class="container">
        <h2>Список пользователей</h2>
        <table class="table">
            <thead>
            <tr>
                <th>ID</th>
                <th>Имя</th>
                <th>Email</th>
                <th>Роль</th>
                <th>Действия</th>
            </tr>
            </thead>
            <tbody>
            <c:forEach var="user" items="${users}">
                <tr>
                    <td>${user.id}</td>
                    <td>${user.username}</td>
                    <td>${user.email}</td>
                    <td>
                        <form method="post" action="/admin/users" style="display:inline;">
                            <input type="hidden" name="userId" value="${user.id}">
                            <select name="role" class="form-control" style="display:inline; width:auto;">
                                <option value="user" ${user.role == 'user' ? 'selected' : ''}>User</option>
                                <option value="admin" ${user.role == 'admin' ? 'selected' : ''}>Admin</option>
                            </select>
                            <button type="submit" name="action" value="updateRole" class="btn">Изменить</button>
                        </form>
                    </td>
                    <td>
                        <form method="post" action="/admin/users" style="display:inline;">
                            <input type="hidden" name="userId" value="${user.id}">
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
