package ru.itis.servlets.admin;

import ru.itis.models.User;
import ru.itis.services.UserService;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@WebServlet("/admin/users")
public class UserManagementServlet extends HttpServlet {
    private UserService userService;

    @Override
    public void init() throws ServletException {
        userService = (UserService) getServletContext().getAttribute("userService");
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        List<User> users = userService.getAllUsers();
        req.setAttribute("users", users);
        req.getRequestDispatcher("/jsp/admin/users.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Long userId = Long.parseLong(req.getParameter("userId"));
        String action = req.getParameter("action");

        if ("delete".equals(action)) {
            userService.deleteUser(userId);
        } else if ("updateRole".equals(action)) {
            String newRole = req.getParameter("role");
            userService.updateRole(userId, newRole);
        }

        resp.sendRedirect("/admin/users");
    }
}

