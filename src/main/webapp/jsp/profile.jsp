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
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
    <style>
        body {
            background: linear-gradient(135deg, #f0f4f8, #ffffff);
            padding-top: 80px;
            font-family: 'Montserrat', sans-serif;
            font-weight: 700;
        }

        .header {
            background-color: #ffffff;
            box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
            position: fixed;
            width: 100%;
            top: 0;
            z-index: 1000;
        }

        .header .container {
            max-width: 95%;
            margin: 0 auto;
            display: flex;
            align-items: center;
            justify-content: space-between;
            font-family: 'Montserrat', sans-serif;
            font-weight: 700;
            padding: 0 33px;
            height: 58px;
        }

        .header .logo {
            font-family: 'Montserrat', sans-serif;
            font-size: 2.0rem;
            color: #000000;
            text-shadow: 2px 2px #fcbf49;
            font-weight: 700;
            text-decoration: none;
            padding-left: 15px ;
        }

        .header nav ul {
            list-style: none;
            display: flex;
            align-items: center;
            margin-bottom: 0;
        }

        .header nav ul li {
            margin-left: 20px;
        }

        .header nav ul li a {
            font-size: 1rem;
            font-weight: 500;
            color: #333333;
            transition: color 0.3s ease;
            display: flex;
            align-items: center;
            text-decoration: none;
        }

        .header nav ul li a i {
            margin-right: 5px;
            font-size: 1rem;
        }

        .header nav ul li a:hover {
            color: #000000;
        }

        .title-card {
            transition: transform 0.1s;
            border: 4px solid #000;
            border-radius: 8px;
        }
        .title-card:hover {
            transform: scale(1.02);
        }

        .title-image {
            width: 100%;
            height: 330px;
            object-fit: cover;

        }
        .badge-custom {
            font-size: 0.8rem;
            vertical-align: middle;
        }
        .card-title a {
            text-decoration: none;
            color: inherit;
            font-weight: 700;
            font-size: 1.0rem;
        }

        .card-body{
            padding: 5px;
        }
        .card-title a:hover {
            text-decoration: none;
            color: inherit;
        }
        .nav-tabs {
            flex-wrap: nowrap;
            overflow-x: auto;
        }
        .nav-tabs .nav-item {
            flex: 1 1 auto;
            text-align: center;
        }
        .nav-tabs::-webkit-scrollbar {
            display: none;
        }

        .nav-link{
            color: #000000;
        }
        .nav-link:hover{
            color: #333333;
        }
        .col-lg-3{
            width: 250px;
        }
    </style>
    <title>Профиль</title>
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
                    <li><a href="/login">Войти</a></li>
                    <li><a href="/register"> Регистрация</a></li>
                </c:if>
            </ul>
        </nav>
    </div>
</div>
<div class="container">
    <h1 class="mb-4 text-center">Добро пожаловать, ${user.username}!</h1>
    <ul class="nav nav-tabs mb-4" id="statusTabs" role="tablist">
        <li class="nav-item" role="presentation">
            <button class="nav-link active" id="reading-tab" data-bs-toggle="tab" data-bs-target="#reading" type="button" role="tab" aria-controls="reading" aria-selected="true">
                Читаю <span class="badge bg-primary badge-custom">${readingTitles.size()}</span>
            </button>
        </li>
        <li class="nav-item" role="presentation">
            <button class="nav-link" id="planned-tab" data-bs-toggle="tab" data-bs-target="#planned" type="button" role="tab" aria-controls="planned" aria-selected="false">
                В планах <span class="badge bg-secondary badge-custom">${plannedTitles.size()}</span>
            </button>
        </li>
        <li class="nav-item" role="presentation">
            <button class="nav-link" id="completed-tab" data-bs-toggle="tab" data-bs-target="#completed" type="button" role="tab" aria-controls="completed" aria-selected="false">
                Прочитано <span class="badge bg-success badge-custom">${completedTitles.size()}</span>
            </button>
        </li>
        <li class="nav-item" role="presentation">
            <button class="nav-link" id="dropped-tab" data-bs-toggle="tab" data-bs-target="#dropped" type="button" role="tab" aria-controls="dropped" aria-selected="false">
                Брошено <span class="badge bg-warning text-dark badge-custom">${droppedTitles.size()}</span>
            </button>
        </li>
        <li class="nav-item" role="presentation">
            <button class="nav-link" id="favorite-tab" data-bs-toggle="tab" data-bs-target="#favorite" type="button" role="tab" aria-controls="favorite" aria-selected="false">
                Любимое <span class="badge bg-danger badge-custom">${favoriteTitles.size()}</span>
            </button>
        </li>
    </ul>

    <div class="tab-content" id="statusTabsContent">
        <div class="tab-pane fade show active" id="reading" role="tabpanel" aria-labelledby="reading-tab">
            <div class="row">
                <c:choose>
                    <c:when test="${not empty readingTitles}">
                        <c:forEach var="title" items="${readingTitles}">
                            <div class="col-sm-6 col-md-4 col-lg-3">
                                <div class="card title-card h-100">
                                    <a href="/title?titleId=${title.id}">
                                        <img src="${title.files[0].filePath}" class="card-img-top title-image" alt="${title.name}" loading="lazy">
                                    </a>
                                    <div class="card-body d-flex flex-column">
                                        <h5 class="card-title text-center">
                                            <a href="/title?titleId=${title.id}">${title.name}</a>
                                        </h5>
                                    </div>
                                </div>
                            </div>
                        </c:forEach>
                    </c:when>
                    <c:otherwise>
                        <div class="col-12">
                            <p class="text-center">Нет книг в этом статусе.</p>
                        </div>
                    </c:otherwise>
                </c:choose>
            </div>
        </div>

        <div class="tab-pane fade" id="planned" role="tabpanel" aria-labelledby="planned-tab">
            <div class="row">
                <c:choose>
                    <c:when test="${not empty plannedTitles}">
                        <c:forEach var="title" items="${plannedTitles}">
                            <div class="col-sm-6 col-md-4 col-lg-3">
                                <div class="card title-card h-100">
                                    <a href="/title?titleId=${title.id}">
                                        <img src="${title.files[0].filePath}" class="card-img-top title-image" alt="${title.name}" loading="lazy">
                                    </a>
                                    <div class="card-body d-flex flex-column">
                                        <h5 class="card-title text-center">
                                            <a href="/title?titleId=${title.id}">${title.name}</a>
                                        </h5>
                                    </div>
                                </div>
                            </div>
                        </c:forEach>
                    </c:when>
                    <c:otherwise>
                        <div class="col-12">
                            <p class="text-center">Нет книг в этом статусе.</p>
                        </div>
                    </c:otherwise>
                </c:choose>
            </div>
        </div>

        <div class="tab-pane fade" id="completed" role="tabpanel" aria-labelledby="completed-tab">
            <div class="row">
                <c:choose>
                    <c:when test="${not empty completedTitles}">
                        <c:forEach var="title" items="${completedTitles}">
                            <div class="col-sm-6 col-md-4 col-lg-3">
                                <div class="card title-card h-100">
                                    <a href="/title?titleId=${title.id}">
                                        <img src="${title.files[0].filePath}" class="card-img-top title-image" alt="${title.name}" loading="lazy">
                                    </a>
                                    <div class="card-body d-flex flex-column">
                                        <h5 class="card-title text-center">
                                            <a href="/title?titleId=${title.id}">${title.name}</a>
                                        </h5>
                                    </div>
                                </div>
                            </div>
                        </c:forEach>
                    </c:when>
                    <c:otherwise>
                        <div class="col-12">
                            <p class="text-center">Нет книг в этом статусе.</p>
                        </div>
                    </c:otherwise>
                </c:choose>
            </div>
        </div>

        <div class="tab-pane fade" id="dropped" role="tabpanel" aria-labelledby="dropped-tab">
            <div class="row">
                <c:choose>
                    <c:when test="${not empty droppedTitles}">
                        <c:forEach var="title" items="${droppedTitles}">
                            <div class="col-sm-6 col-md-4 col-lg-3">
                                <div class="card title-card h-100">
                                    <a href="/title?titleId=${title.id}">
                                        <img src="${title.files[0].filePath}" class="card-img-top title-image" alt="${title.name}" loading="lazy">
                                    </a>
                                    <div class="card-body d-flex flex-column">
                                        <h5 class="card-title text-center">
                                            <a href="/title?titleId=${title.id}">${title.name}</a>
                                        </h5>
                                    </div>
                                </div>
                            </div>
                        </c:forEach>
                    </c:when>
                    <c:otherwise>
                        <div class="col-12">
                            <p class="text-center">Нет книг в этом статусе.</p>
                        </div>
                    </c:otherwise>
                </c:choose>
            </div>
        </div>

        <div class="tab-pane fade" id="favorite" role="tabpanel" aria-labelledby="favorite-tab">
            <div class="row">
                <c:choose>
                    <c:when test="${not empty favoriteTitles}">
                        <c:forEach var="title" items="${favoriteTitles}">
                            <div class="col-sm-6 col-md-4 col-lg-3">
                                <div class="card title-card h-100">
                                    <a href="/title?titleId=${title.id}">
                                        <img src="${title.files[0].filePath}" class="card-img-top title-image" alt="${title.name}" loading="lazy">
                                    </a>
                                    <div class="card-body d-flex flex-column">
                                        <h5 class="card-title text-center">
                                            <a href="/title?titleId=${title.id}">${title.name}</a>
                                        </h5>
                                    </div>
                                </div>
                            </div>
                        </c:forEach>
                    </c:when>
                    <c:otherwise>
                        <div class="col-12">
                            <p class="text-center">Нет книг в этом статусе.</p>
                        </div>
                    </c:otherwise>
                </c:choose>
            </div>
        </div>
    </div>
</div>
<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>
