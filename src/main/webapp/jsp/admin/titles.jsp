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
    <link href="https://cdnjs.cloudflare.com/ajax/libs/select2/4.0.13/css/select2.min.css" rel="stylesheet" />
    <script src="https://code.jquery.com/jquery-3.6.0.min.js"></script>
    <script src="https://cdnjs.cloudflare.com/ajax/libs/select2/4.0.13/js/select2.min.js"></script>
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
    <title>Управление тайтлами</title>
</head>
<body>
<div class="header">
    <h1>Админ-панель: Управление тайтлами</h1>
</div>
<div class="sidebar">
    <a href="/admin">Админка</a>
    <a href="/admin/users">Управление пользователями</a>
    <a href="/admin/authors">Управление авторами</a>
    <a href="/admin/genres">Управление жанрами</a>
    <a href="/admin/comments">Управление комментариями</a>
</div>
<div class="content">
    <div class="container">
        <h2>Список тайтлов</h2>
        <table class="table">
            <thead>
            <tr>
                <th>ID</th>
                <th>Название</th>
                <th>Описание</th>
                <th>Тип</th>
                <th>Обложка</th>
                <th>Автор</th>
                <th>Жанры</th>
                <th>Действия</th>
            </tr>
            </thead>
            <tbody>
            <c:forEach var="title" items="${titles}">
                <tr>
                    <td>${title.id}</td>
                    <td>${title.name}</td>
                    <td>${title.description}</td>
                    <td>${title.type}</td>
                    <td>
                        <c:choose>
                            <c:when test="${not empty title.files}">
                                <img src="${title.files[0].filePath}" alt="Обложка" style="max-width: 80px;">
                            </c:when>
                            <c:otherwise>
                                Нет обложки
                            </c:otherwise>
                        </c:choose>
                    </td>
                    <td>${title.author != null ? title.author.name : 'Не указан'}</td>
                    <td>
                        <c:forEach var="genre" items="${title.genres}">
                            ${genre.name}<br>
                        </c:forEach>
                    </td>
                    <td>
                        <form action="/admin/titles" method="GET" style="display:inline;">
                            <input type="hidden" name="action" value="edit">
                            <input type="hidden" name="id" value="${title.id}">
                            <button type="submit" class="btn">Редактировать</button>
                        </form>
                        <form action="/admin/titles" method="POST" style="display:inline;">
                            <input type="hidden" name="action" value="delete">
                            <input type="hidden" name="id" value="${title.id}">
                            <button type="submit" class="btn btn-danger">Удалить</button>
                        </form>
                    </td>
                </tr>
            </c:forEach>
            </tbody>
        </table>

        <h2>Добавить новый тайтл</h2>
        <form action="/admin/titles" method="POST" enctype="multipart/form-data">
            <input type="hidden" name="action" value="add">
            <label for="name">Название:</label>
            <input type="text" id="name" name="name" class="form-control" required>

            <label for="description">Описание:</label>
            <textarea id="description" name="description" class="form-control" required></textarea>

            <label for="type">Тип:</label>
            <select id="type" name="type" class="form-control">
                <option value="manga">Манга</option>
                <option value="comic">Комикс</option>
            </select>

            <label for="authorId">Автор:</label>
            <select id="authorId" name="authorId" class="form-control">
                <c:forEach var="author" items="${authors}">
                    <option value="${author.id}">${author.name}</option>
                </c:forEach>
            </select>

            <label for="genreIds">Жанры:</label>
            <select id="genreIds" name="genreIds" class="form-control select2" multiple>
                <c:forEach var="genre" items="${genres}">
                    <option value="${genre.id}">${genre.name}</option>
                </c:forEach>
            </select>

            <label for="coverImage">Обложка:</label>
            <input type="file" id="coverImage" name="coverImage" class="form-control">
            <button type="submit" class="btn">Добавить</button>
        </form>

        <h2>Редактировать тайтл</h2>
        <c:if test="${not empty editTitle}">
            <form action="/admin/titles" method="POST" enctype="multipart/form-data">
                <input type="hidden" name="action" value="update">
                <input type="hidden" name="id" value="${editTitle.id}">
                <label for="edit-name">Название:</label>
                <input type="text" id="edit-name" name="name" value="${editTitle.name}" class="form-control" required>

                <label for="edit-description">Описание:</label>
                <textarea id="edit-description" name="description" class="form-control" required>${editTitle.description}</textarea>

                <label for="edit-type">Тип:</label>
                <select id="edit-type" name="type" class="form-control">
                    <option value="manga" ${editTitle.type == 'manga' ? 'selected' : ''}>Манга</option>
                    <option value="comic" ${editTitle.type == 'comic' ? 'selected' : ''}>Комикс</option>
                </select>

                <label for="edit-authorId">Автор:</label>
                <select id="edit-authorId" name="authorId" class="form-control">
                    <c:forEach var="author" items="${authors}">
                        <option value="${author.id}" ${author.id == editTitle.author.id ? 'selected' : ''}>${author.name}</option>
                    </c:forEach>
                </select>

                <label for="edit-genreIds">Жанры:</label>
                <select id="edit-genreIds" name="genreIds" class="form-control select2" multiple>
                    <c:forEach var="genre" items="${genres}">
                        <option value="${genre.id}" ${editTitle.genres.contains(genre) ? 'selected' : ''}>${genre.name}</option>
                    </c:forEach>
                </select>

                <label for="edit-coverImage">Обложка:</label>
                <input type="file" id="edit-coverImage" name="coverImage" class="form-control">
                <button type="submit" class="btn">Сохранить изменения</button>
            </form>
        </c:if>
    </div>
</div>
</body>
</html>
