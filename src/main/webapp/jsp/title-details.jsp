<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page contentType="text/html; charset=UTF-8" %>
<%@ page pageEncoding="UTF-8" %>
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
<div class="title-hero">
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
                <label for="statusSelect">Статус чтения:</label>
                <select id="statusSelect" name="status">
                    <option value="NOT_READING" <c:if test="${currentStatus == 'NOT_READING'}">selected</c:if>>Не
                        читаю
                    </option>
                    <option value="READING" <c:if test="${currentStatus == 'READING'}">selected</c:if>>Читаю</option>
                    <option value="PLANNED" <c:if test="${currentStatus == 'PLANNED'}">selected</c:if>>В планах</option>
                    <option value="COMPLETED" <c:if test="${currentStatus == 'COMPLETED'}">selected</c:if>>Прочитано
                    </option>
                    <option value="DROPPED" <c:if test="${currentStatus == 'DROPPED'}">selected</c:if>>Брошено</option>
                    <option value="FAVORITE" <c:if test="${currentStatus == 'FAVORITE'}">selected</c:if>>Любимое
                    </option>
                </select>
                <button id="setStatusButton">Установить статус</button>
            </div>
        </div>
        <div class="rating-section">
            <h3>Оцените этот тайтл:</h3>
            <div class="stars" id="stars" data-title-id="${title.id}">
                <span class="star" data-value="1">&#9734;</span>
                <span class="star" data-value="2">&#9734;</span>
                <span class="star" data-value="3">&#9734;</span>
                <span class="star" data-value="4">&#9734;</span>
                <span class="star" data-value="5">&#9734;</span>
            </div>
            <p>Средний рейтинг: <span id="averageRating">${title.averageRating}</span> из 5</p>
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
    <div class = "title-author">
        <span class="author-tag">${title.author.name}</span>
    </div>
</div>

<div class="tab-content" id="tab-chapters" style="display:none;">
    <p>Список глав временно пуст.</p>
</div>

<div class="tab-content" id="tab-comments" style="display:none;">
    <form id="commentForm" class="comment-form">
        <textarea name="content" placeholder="Оставьте комментарий" required></textarea>
        <input type="hidden" name="titleId" value="${title.id}">
        <input type="hidden" name="action" value="addComment">
        <button type="submit">Отправить</button>
    </form>
    <div id="comments-section">
        <c:forEach var="comment" items="${comments}">
            <div class="comment">
                <div class="comment-header">
                    <span class="username">${user.username}</span>
                    <span class="comment-date">${comment.createdAt}</span>
                </div>
                <p class="comment-content">${comment.content}</p>
            </div>
        </c:forEach>
    </div>
</div>

<script>
    const titleId = '${title.id}';
    let status = null;

    document.querySelectorAll('.comment-date').forEach(title => {
        const maxLength = 16;
        if (title.textContent.length > maxLength) {
            title.textContent = title.textContent.substring(0, maxLength);
        }
    });

    document.addEventListener('DOMContentLoaded', function () {
        const defaultTab = document.querySelector('.tab-link[data-tab="tab-overview"]');
        if (defaultTab) {
            defaultTab.classList.add('active');
            document.getElementById('tab-overview').style.display = 'block';
        }

        document.querySelectorAll('.tab-link').forEach(link => {
            link.addEventListener('click', function () {
                document.querySelectorAll('.tab-link').forEach(tab => tab.classList.remove('active'));
                document.querySelectorAll('.tab-content').forEach(tab => tab.style.display = 'none');
                this.classList.add('active');
                document.getElementById(this.dataset.tab).style.display = 'block';
            });
        });
    });

    document.getElementById('setStatusButton').addEventListener('click', function () {
        const statusSelect = document.getElementById('statusSelect');
        const selectedStatus = statusSelect.value;

        if (!selectedStatus) {
            alert('Выберите статус.');
            return;
        }

        let statusEnum;

        switch (selectedStatus) {
            case 'NOT_READING':
                statusEnum = 'NOT_READING';
                break;
            case 'READING':
                statusEnum = 'READING';
                break;
            case 'PLANNED':
                statusEnum = 'PLANNED';
                break;
            case 'COMPLETED':
                statusEnum = 'COMPLETED';
                break;
            case 'DROPPED':
                statusEnum = 'DROPPED';
                break;
            case 'FAVORITE':
                statusEnum = 'FAVORITE';
                break;
            default:
                alert('Неизвестный статус.');
                console.error('Неизвестный статус:', selectedStatus);
                return;
        }

        const params = new URLSearchParams();
        params.append('action', 'updateStatus');
        params.append('titleId', titleId);
        params.append('status', statusEnum);

        fetch('/title', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/x-www-form-urlencoded',
            },
            credentials: 'same-origin',
            body: params.toString(),
        })
            .then((response) => response.json())
            .then((data) => {
                if (data.success) {

                    alert('Статус успешно обновлён!');
                } else {
                    alert(`Ошибка: ${data.message}`);
                }
            })
            .catch((error) => {
                console.error('Ошибка запроса:', error);
            });
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
                if (data.success) {
                    const commentsSection = document.getElementById('comments-section');
                    const {username, createdAt, content } = data.comment;

                    const truncatedDate = createdAt.length > 16 ? createdAt.substring(0, 16) : createdAt;

                    const commentDiv = document.createElement('div');
                    commentDiv.classList.add('comment');

                    const commentHeader = document.createElement('div');
                    commentHeader.classList.add('comment-header');

                    const usernameSpan = document.createElement('span');
                    usernameSpan.classList.add('username');
                    usernameSpan.textContent = username;

                    const dateSpan = document.createElement('span');
                    dateSpan.classList.add('comment-date');
                    dateSpan.textContent = truncatedDate;

                    const contentP = document.createElement('p');
                    contentP.classList.add('comment-content');
                    contentP.textContent = content;

                    commentHeader.appendChild(usernameSpan);
                    commentHeader.appendChild(dateSpan);
                    commentDiv.appendChild(commentHeader);
                    commentDiv.appendChild(contentP);
                    commentsSection.appendChild(commentDiv);

                    this.reset();
                } else {
                    alert('Ошибка добавления комментария.');
                }
            })
            .catch(err => console.error('Ошибка при добавлении комментария:', err));
    });

    document.addEventListener("DOMContentLoaded", function () {
        const starsContainer = document.getElementById('stars');
        const stars = starsContainer.querySelectorAll('.star');
        const averageRatingElement = document.getElementById("averageRating");
        const titleId = starsContainer.getAttribute('data-title-id');
        let userRating = ${userRating != null ? userRating : 'null'};

        function highlightStars(rating) {
            stars.forEach(star => {
                if (parseInt(star.getAttribute('data-value')) <= rating) {
                    star.classList.add('selected');
                } else {
                    star.classList.remove('selected');
                }
            });
        }

        stars.forEach(star => {
            star.addEventListener('mouseover', function () {
                const rating = parseInt(this.getAttribute('data-value'));
                highlightStars(rating);
            });

            star.addEventListener('mouseout', function () {
                if (userRating) {
                    highlightStars(userRating);
                } else {
                    stars.forEach(star => star.classList.remove('selected'));
                }
            });

            star.addEventListener('click', function () {
                const selectedRating = parseInt(this.getAttribute('data-value'));
                sendRating(selectedRating);
            });
        });

        function sendRating(rating) {
            const params = new URLSearchParams();
            params.append('action', 'addRating');
            params.append('titleId', titleId);
            params.append('rating', rating);

            fetch('/title', {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/x-www-form-urlencoded',
                },
                credentials: 'same-origin',
                body: params.toString(),
            })
                .then(response => {
                    const contentType = response.headers.get("content-type");
                    if (contentType && contentType.indexOf("application/json") !== -1) {
                        return response.json();
                    } else {
                        throw new TypeError("Oops, мы ожидали JSON!");
                    }
                })
                .then(data => {
                    if (data.success) {
                        averageRatingElement.textContent = data.averageRating.toFixed(1);
                        userRating = rating;
                        highlightStars(rating);
                        alert("Спасибо за ваш рейтинг!");
                    } else {
                        alert("Ошибка: " + data.message);
                    }
                })
        }
    });
</script>
</body>
</html>
