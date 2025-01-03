package ru.itis.servlets.admin;

import ru.itis.models.Comment;
import ru.itis.services.CommentService;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@WebServlet("/admin/comments")
public class CommentManagementServlet extends HttpServlet {

    private CommentService commentService;

    @Override
    public void init() throws ServletException {
        commentService = (CommentService) getServletContext().getAttribute("commentService");
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        List<Comment> comments = commentService.getAllComments();
        req.setAttribute("comments", comments);
        req.getRequestDispatcher("/jsp/admin/comments.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String action = req.getParameter("action");
        if ("delete".equals(action)) {
            handleDelete(req, resp);
        } else {
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "Unknown action: " + action);
        }
    }

    private void handleDelete(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        try {
            Long id = Long.parseLong(req.getParameter("id"));
            commentService.deleteCommentById(id);
            resp.sendRedirect("/admin/comments");
        } catch (Exception e) {
            e.printStackTrace();
            resp.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Ошибка при удалении комментария.");
        }
    }
}
