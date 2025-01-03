package ru.itis.servlets;

import ru.itis.models.Comment;
import ru.itis.models.Title;
import ru.itis.services.CommentService;
import ru.itis.services.TitleService;
import ru.itis.services.UserService;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.Timestamp;
import java.util.List;
import java.util.Optional;

@WebServlet("/title")
public class TitleDetailsServlet extends HttpServlet {
    private TitleService titleService;
    private CommentService commentService;
    private UserService userService;

    @Override
    public void init() throws ServletException {
        titleService = (TitleService) getServletContext().getAttribute("titleService");
        commentService = (CommentService) getServletContext().getAttribute("commentService");
        userService = (UserService) getServletContext().getAttribute("userService");
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Long titleId = Long.parseLong(req.getParameter("titleId"));
        Long userId = (Long) req.getSession().getAttribute("userId");
        Title title = titleService.getTitleById(titleId).orElse(null);
        req.setAttribute("title", title);
        List<Comment> comments = commentService.getCommentsByTitleId(titleId);
        req.setAttribute("comments", comments);
        String currentStatus = userId != null ? userService.getStatusByTitleIdAndUserId(titleId, userId) : "notReading";
        req.setAttribute("currentStatus", currentStatus);
        req.getRequestDispatcher("/jsp/title-details.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String action = req.getParameter("action");

        if ("addComment".equals(action)) {
            handleAddComment(req, resp);
        } else if ("updateStatus".equals(action)) {
            handleUpdateStatus(req, resp);
        } else {
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "Неизвестное действие: " + action);
        }
    }

    private void handleUpdateStatus(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String status = req.getParameter("status");

        Long userId = (Long) req.getSession().getAttribute("userId");

        Long titleId = Long.parseLong(req.getParameter("titleId"));

        try {
            userService.linkTitle(userId, titleId, status);
            resp.setContentType("application/json");
            resp.getWriter().write("{\"success\": true}");
        } catch (Exception e) {
            e.printStackTrace();
            resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            resp.getWriter().write("{\"success\": false, \"message\": \"Ошибка при обновлении статуса\"}");
        }
    }

    private void handleAddComment(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        Long userId = (Long) req.getSession().getAttribute("userId");
        Long titleId = Long.parseLong(req.getParameter("titleId"));
        String content = req.getParameter("content");

        System.out.println("Adding comment for userId: " + userId + ", titleId: " + titleId + ", content: " + content);

        Comment comment = new Comment(null, titleId, userId, content, new Timestamp(System.currentTimeMillis()));
        commentService.addCommentForTitle(comment);

        resp.setContentType("application/json");
        resp.getWriter().write("{\"success\": true}");
    }
}
