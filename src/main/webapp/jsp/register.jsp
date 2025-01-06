<%@ page contentType="text/html; charset=UTF-8" %>
<%@ page pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html lang="ru">
<head>
    <meta charset="UTF-8">
    <title>Регистрация</title>
    <link href="https://fonts.googleapis.com/css2?family=Montserrat:wght@500;700&display=swap" rel="stylesheet">
    <style>

        html, body {
            height: 100%;
            font-family: 'Montserrat', sans-serif;
            background: linear-gradient(135deg, #ADA996, #F2F2F2,#DBDBDB,#EAEAEA);
        }

        body {
            display: flex;
            justify-content: center;
            align-items: center;
        }

        .register-container {
            background-color: #ffffff;
            border: 4px solid #000;
            border-radius: 8px;
            padding: 2rem;
            width: 350px;
            text-align: center;
            position: relative;
        }

        h1 {
            font-size: 2rem;
            margin-bottom: 1.5rem;
            color: #000;
            text-shadow: 2px 2px #abbaab;
        }

        form {
            display: flex;
            flex-direction: column;
        }

        label {
            text-align: left;
            margin-bottom: 0.2rem;
            color: #000;
            font-size: 1.2rem;
        }

        input[type="text"],
        input[type="email"],
        input[type="password"] {
            padding: 0.5rem;
            margin-bottom: 1rem;
            border: 2px solid #000;
            outline: none;
            font-family: inherit;
            font-size: 1rem;
        }

        input[type="text"]:focus,
        input[type="email"]:focus,
        input[type="password"]:focus {
            border-color: #ff9da7;
            background: #ffe6ec;
        }

        button {
            font-size: 1.2rem;
            padding: 0.5rem;
            background-color: #abbaab;
            border: 3px solid #000;
            cursor: pointer;
            transition: transform 0.2s ease;
        }

        button:hover {
            background-color: #abbaab;
            transform: scale(1.05);
        }

        p {
            margin-top: 1rem;
            font-size: 1rem;
        }

        a {
            text-decoration: none;
            color: #0077cc;
            font-weight: bold;
        }

        .error-message {
            color: red;
            margin-top: 1rem;
            font-size: 1rem;
        }
    </style>
</head>
<body>
<div class="register-container">
    <h1>Регистрация</h1>
    <form method="post" action="/register">
        <label for="username">Имя:</label>
        <input id="username" type="text" name="username" required>

        <label for="email">Email:</label>
        <input id="email" type="email" name="email" required>

        <label for="password">Пароль:</label>
        <input id="password" type="password" name="password" required>

        <button type="submit">Создать аккаунт!</button>
    </form>
    <p>Уже есть аккаунт? <a href="/login">Войти</a></p>

    <c:if test="${error != null}">
        <p class="error-message">${error}</p>
    </c:if>
</div>
</body>
</html>
