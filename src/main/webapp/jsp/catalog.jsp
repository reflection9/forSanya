<%@ include file="includes/header.jsp" %>
<%@ page contentType="text/html; charset=UTF-8" %>
<%@ page pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html lang="ru">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <link href="<c:url value='/assets/css/baseStyles.css' />" rel="stylesheet">
    <link href="<c:url value='/assets/css/catalogStyles.css' />" rel="stylesheet">
    <link href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0/css/all.min.css" rel="stylesheet">
    <title>Каталог</title>
    <script src="https://code.jquery.com/jquery-3.6.0.min.js"></script>
</head>
<body>
<div class="content-with-images">
    <div class="content">
        <div class="container">
            <div class="search-sort-container">
                <input type="text" id="search" placeholder="Поиск по названию" class="form-control"><br>
                <label for="type">Жанр:</label>
                <select id="genre" class="form-control">
                    <option value="">Все жанры</option>
                    <c:forEach var="genre" items="${genres}">
                        <option value="${genre.id}">${genre.name}</option>
                    </c:forEach>
                </select><br>
                <label for="type">Автор:</label>
                <select id="author" class="form-control">
                    <option value="">Все авторы</option>
                    <c:forEach var="author" items="${authors}">
                        <option value="${author.id}">${author.name}</option>
                    </c:forEach>
                </select><br>
                <label for="type">Тип:</label>
                <select id="type" class="form-control">
                    <option value="">Все типы</option>
                    <option value="manga">Манга</option>
                    <option value="comic">Комикс</option>
                </select><br>
            </div>

            <div id="catalog-grid" class="catalog-grid">
                <c:forEach var="title" items="${titles}">
                    <div class="catalog-item">
                        <a href="/title?titleId=${title.id}">
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
            </div>
        </div>
    </div>
</div>
<script>
    document.querySelectorAll('.catalog-title').forEach(title => {
        const maxLength = 25;
        if (title.textContent.length > maxLength) {
            title.textContent = title.textContent.substring(0, maxLength) + '...';
        }
    });

    $(document).ready(function () {
        $('#search, #genre, #author, #type').on('input change', function () {
            fetchCatalog();
        });

        function fetchCatalog() {
            const search = $('#search').val();
            const genre = $('#genre').val();
            const author = $('#author').val();
            const type = $('#type').val();

            $.ajax({
                url: '/catalog',
                method: 'GET',
                data: {search, genre, author, type},
                success: function (response) {
                    $('#catalog-grid').html(response);
                },
                error: function (xhr, status, error) {
                    console.error(`Ошибка: ${xhr.status} - ${xhr.responseText}`);
                    alert('Произошла ошибка при загрузке каталога.');
                }
            });
        }
    });
</script>
</body>
</html>
