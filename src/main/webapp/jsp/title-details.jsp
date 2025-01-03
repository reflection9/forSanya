<%@ page contentType="text/html; charset=UTF-8" %>
<%@ page pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ include file="includes/header.jsp" %>
<!DOCTYPE html>
<html lang="ru">
<head>
    <meta charset="UTF-8">
    <title>${title.name} - Детали</title>
    <link href="<c:url value='/assets/css/baseStyles.css' />" rel="stylesheet">
    <link href="<c:url value='/assets/css/headerStyles.css' />" rel="stylesheet">
    <link href="<c:url value='/assets/css/title-detailsStyles.css' />" rel="stylesheet">
    <link href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0/css/all.min.css" rel="stylesheet">
    <script src="https://code.jquery.com/jquery-3.6.0.min.js"></script>
</head>
<body>
<div class="title-hero" style="background-image: url('/uploads/backgroundimg.jpg')">
    <div class="title-hero-overlay"></div>

    <div class="title-hero-content">
        <div class="title-cover">
            <c:choose>
                <c:when test="${not empty title.files}">
                    <img src="${title.files[0].filePath}" alt="Обложка ${title.name}">
                </c:when>
                <c:otherwise>
                    <img src="/uploads/placeholder.png" alt="Нет обложки">
                </c:otherwise>
            </c:choose>
        </div>

        <div class="title-info">
            <h1 class="title-name">${title.name}</h1>
            <div class="reading-status">
                <label>Статус чтения:</label>
                <div class="status-options">
                    <label>
                        <input type="radio" name="status" value="notReading" ${currentStatus == 'notReading' ? 'checked' : ''}>
                        Не читаю
                    </label>
                    <label>
                        <input type="radio" name="status" value="reading" ${currentStatus == 'reading' ? 'checked' : ''}>
                        Читаю
                    </label>
                    <label>
                        <input type="radio" name="status" value="planned" ${currentStatus == 'planned' ? 'checked' : ''}>
                        В планах
                    </label>
                    <label>
                        <input type="radio" name="status" value="completed" ${currentStatus == 'completed' ? 'checked' : ''}>
                        Прочитано
                    </label>
                    <label>
                        <input type="radio" name="status" value="dropped" ${currentStatus == 'dropped' ? 'checked' : ''}>
                        Брошено
                    </label>
                    <label>
                        <input type="radio" name="status" value="favorite" ${currentStatus == 'favorite' ? 'checked' : ''}>
                        Любимое
                    </label>
                </div>
                <button type="button" class="status-button" id="setStatusButton">Установить статус</button>
            </div>
        </div>
    </div>
</div>

<div class="title-tabs">
    <ul>
        <li class="tab-link active" data-tab="tab-overview">О тайтле</li>
        <li class="tab-link" data-tab="tab-chapters">Главы</li>
        <li class="tab-link" data-tab="tab-comments">Комментарии</li>
    </ul>
</div>

<div class="tab-content" id="tab-overview">
    <div class="title-description">
        <p>${title.description}</p>
    </div>
    <div class="title-genres">
        <c:forEach var="genre" items="${title.genres}">
            <span class="genre-tag">${genre.name}</span>
        </c:forEach>
    </div>
</div>

<div class="tab-content" id="tab-chapters" style="display:none;">
    <p>Список глав временно пуст.</p>
</div>

<div class="tab-content" id="tab-comments" style="display:none;">
    <h3>Комментарии</h3>
    <form id="commentForm">
        <textarea name="content" placeholder="Оставьте комментарий" required></textarea>
        <input type="hidden" name="titleId" value="${title.id}">
        <input type="hidden" name="action" value="addComment"> <!-- Добавлено -->
        <button type="submit">Отправить</button>
    </form>
    <div id="comments-section">
        <c:forEach var="comment" items="${comments}">
            <div class="comment">
                <p>${comment.content}</p>
                <small>${comment.createdAt}</small>
            </div>
        </c:forEach>
    </div>
</div>

<!-- Определяем titleId для использования в JavaScript -->
<script>
    const titleId = '${title.id}';
</script>

<script>
    document.querySelectorAll('.tab-link').forEach(link => {
        link.addEventListener('click', function () {
            document.querySelectorAll('.tab-link').forEach(tab => tab.classList.remove('active'));
            document.querySelectorAll('.tab-content').forEach(tab => tab.style.display = 'none');
            this.classList.add('active');
            document.getElementById(this.dataset.tab).style.display = 'block';
        });
    });

    document.getElementById('setStatusButton').addEventListener('click', function () {
        const selectedStatus = document.querySelector('input[name="status"]:checked');
        console.log('Selected status:', selectedStatus);
        if (!selectedStatus) {
            alert('Выберите статус.');
            return;
        }

        var status = selectedStatus.value;

        console.log(`Перед отправкой: titleId=${title.id}, status=${status}`); // Логируем для проверки

        fetch('/title', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/x-www-form-urlencoded',
            },
            credentials: 'same-origin',
            body: `action=updateStatus&titleId=${title.id}&status=${status}`, // Здесь добавляем статус
        })
            .then((response) => response.json())
            .then(data => {
                console.log('Ответ сервера:', data);
                if (data.success) {
                    alert('Статус обновлён!');
                    document.getElementById('currentStatusDisplay').textContent = status;
                } else {
                    alert(`Ошибка обновления статуса: ${data.message}`);
                }
            })
            .catch(err => console.error('Ошибка запроса:', err));

    });

    document.getElementById('commentForm').addEventListener('submit', function (e) {
        e.preventDefault();

        const formData = new FormData(this);
        formData.append('action', 'addComment');

        fetch('/title', {
            method: 'POST',
            body: new URLSearchParams(formData),
            credentials: 'same-origin'
        })
            .then(res => res.json())
            .then(data => {
                console.log('Add comment response:', data); // Отладка
                if (data.success) {
                    const commentsSection = document.getElementById('comments-section');
                    const content = formData.get('content');

                    // Создаём новый элемент комментария
                    const commentDiv = document.createElement('div');
                    commentDiv.classList.add('comment');

                    const p = document.createElement('p');
                    p.textContent = content;

                    const small = document.createElement('small');
                    small.textContent = 'Только что';

                    commentDiv.appendChild(p);
                    commentDiv.appendChild(small);
                    commentsSection.appendChild(commentDiv);

                    console.log('Комментарий добавлен в DOM'); // Отладка
                    this.reset();
                } else {
                    alert('Ошибка добавления комментария.');
                }
            })
            .catch(err => console.error('Ошибка при добавлении комментария:', err));
    });
</script>
</body>
</html>