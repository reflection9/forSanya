package ru.itis.servlets;

import ru.itis.models.User;
import ru.itis.services.UserService;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

@WebServlet("/register")
public class RegisterServlet extends HttpServlet {

    private UserService userService;

    @Override
    public void init() throws ServletException {
        userService = (UserService) getServletContext().getAttribute("userService");
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.getRequestDispatcher("/jsp/register.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Long userId = Long.parseLong(request.getParameter("userId"));
        String username = request.getParameter("username");
        String email = request.getParameter("email");
        String password = request.getParameter("password");

        try {
            if (username == null || username.trim().isEmpty()) {
                throw new IllegalArgumentException("Имя не может быть пустым.");
            }

            if (password == null || password.trim().isEmpty()) {
                throw new IllegalArgumentException("Пароль не может быть пустым.");
            }

            if (userService.isEmailOrUsernameTaken(email, username)) {
                throw new IllegalArgumentException("Email или имя пользователя уже заняты.");
            }
            if (!userService.isValidEmail(email)) {
                throw new IllegalArgumentException("Некорректный формат email.");
            }

            User user = User.builder()
                    .id(userId)
                    .username(username)
                    .email(email)
                    .password(password)
                    .build();

            userService.register(user);
            response.sendRedirect("/login");
        } catch (IllegalArgumentException e) {
            request.setAttribute("error", e.getMessage());
            request.getRequestDispatcher("/jsp/register.jsp").forward(request, response);
        } catch (Exception e) {
            throw new RuntimeException("Ошибка при регистрации пользователя", e);
        }
    }
}
