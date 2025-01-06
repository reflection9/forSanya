package ru.itis.servlets;

import ru.itis.helper.ReadingStatus;
import ru.itis.models.Comment;
import ru.itis.models.Title;
import ru.itis.services.CommentService;
import ru.itis.services.RatingService;
import ru.itis.services.TitleService;
import ru.itis.services.UserService;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;
import java.sql.Timestamp;
import java.text.DecimalFormat;
import java.util.List;


@WebServlet("/title")
public class TitleDetailsServlet extends HttpServlet {
    private TitleService titleService;
    private CommentService commentService;
    private UserService userService;
    private RatingService ratingService;

    @Override
    public void init() throws ServletException {
        titleService = (TitleService) getServletContext().getAttribute("titleService");
        commentService = (CommentService) getServletContext().getAttribute("commentService");
        userService = (UserService) getServletContext().getAttribute("userService");
        ratingService = (RatingService) getServletContext().getAttribute("ratingService");
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Long titleId = Long.parseLong(req.getParameter("titleId"));
        Long userId = (Long) req.getSession().getAttribute("userId");
        Title title = titleService.getTitleById(titleId).orElse(null);
        req.setAttribute("title", title);
        List<Comment> comments = commentService.getCommentsByTitleId(titleId);
        req.setAttribute("comments", comments);
        if (title != null) {
            double averageRating = ratingService.getAverageRating(titleId);
            title.setAverageRating(averageRating);
        }
        String currentStatus = "NOT_READING";
        if (userId != null) {
            ReadingStatus status = userService.getStatusByTitleIdAndUserId(titleId, userId);
            if (status != null) {
                currentStatus = status.name();
            }
        }
        req.setAttribute("currentStatus", currentStatus);
        req.getRequestDispatcher("/jsp/title-details.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String action = req.getParameter("action");

        if (action == null) {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            resp.getWriter().write("{\"success\": false, \"message\": \"Параметр action обязателен\"}");
            return;
        }

        try {
            Long userId = (Long) req.getSession().getAttribute("userId");

            if (userId == null) {
                resp.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                resp.getWriter().write("{\"success\": false, \"message\": \"Пользователь не авторизован\"}");
                return;
            }

            if ("updateStatus".equals(action)) {
                String statusParam = req.getParameter("status");
                String titleIdParam = req.getParameter("titleId");

                if (statusParam == null || titleIdParam == null) {
                    resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                    resp.getWriter().write("{\"success\": false, \"message\": \"Необходимые параметры отсутствуют\"}");
                    return;
                }

                Long titleId = Long.parseLong(titleIdParam);

                ReadingStatus readingStatus;
                try {
                    readingStatus = ReadingStatus.valueOf(statusParam);
                } catch (IllegalArgumentException e) {
                    resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                    resp.getWriter().write("{\"success\": false, \"message\": \"Неизвестный статус\"}");
                    return;
                }

                userService.linkTitle(userId, titleId, readingStatus);

                resp.setContentType("application/json");
                resp.getWriter().write("{\"success\": true}");
            } else if ("addComment".equals(action)) {
                handleAddComment(req, resp);
            } else if ("addRating".equals(action)) {
                Long titleId = Long.parseLong(req.getParameter("titleId"));
                int rating = Integer.parseInt(req.getParameter("rating"));

                ratingService.addOrUpdateRating(userId, titleId, rating);
                double averageRating = ratingService.getAverageRating(titleId);

                DecimalFormat decimalFormat = new DecimalFormat("#.##");
                String formattedAverageRating = decimalFormat.format(averageRating).replace(",", ".");

                resp.setContentType("application/json");
                resp.getWriter().write(String.format("{\"success\": true, \"averageRating\": %s}", formattedAverageRating));
            } else {
                resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                resp.getWriter().write("{\"success\": false, \"message\": \"Неизвестное действие\"}");
            }
        } catch (NumberFormatException e) {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            resp.getWriter().write("{\"success\": false, \"message\": \"Некорректный идентификатор тайтла\"}");
        } catch (Exception e) {
            e.printStackTrace();
            resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            resp.getWriter().write("{\"success\": false, \"message\": \"Внутренняя ошибка сервера\"}");
        }
    }

    private void handleAddComment(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        Long userId = (Long) req.getSession().getAttribute("userId");
        Long titleId = Long.parseLong(req.getParameter("titleId"));
        String content = req.getParameter("content");
        String username = userService.getUserById(userId).getUsername();
        Comment comment = new Comment(null, titleId, userId, content, new Timestamp(System.currentTimeMillis()));
        commentService.addCommentForTitle(comment);

        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");
        String jsonResponse = String.format(
                "{\"success\": true, \"comment\": {\"username\": \"%s\", \"createdAt\": \"%s\", \"content\": \"%s\"}}",
                escapeJson(username),
                comment.getCreatedAt().toString(),
                escapeJson(comment.getContent())
        );
        resp.getWriter().write(jsonResponse);
    }

    private String escapeJson(String str) {
        if (str == null) return "";
        return str.replace("\"", "\\\"").replace("\n", "\\n").replace("\r", "\\r");
    }
}
