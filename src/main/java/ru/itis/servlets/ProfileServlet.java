package ru.itis.servlets;

import ru.itis.helper.ReadingStatus;
import ru.itis.models.Title;
import ru.itis.services.TitleService;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;
import java.util.List;

@WebServlet("/profile")
public class ProfileServlet extends HttpServlet {
    private TitleService titleService;

    @Override
    public void init() throws ServletException {
        titleService = (TitleService) getServletContext().getAttribute("titleService");
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HttpSession session = req.getSession(false);

        if (session == null || session.getAttribute("userId") == null) {
            resp.sendRedirect(req.getContextPath() + "/login");
            return;
        }

        Long userId = (Long) session.getAttribute("userId");
        List<Title> readingTitles = titleService.getTitlesByUserAndStatus(userId, ReadingStatus.READING);
        List<Title> plannedTitles = titleService.getTitlesByUserAndStatus(userId, ReadingStatus.PLANNED);
        List<Title> completedTitles = titleService.getTitlesByUserAndStatus(userId, ReadingStatus.COMPLETED);
        List<Title> droppedTitles = titleService.getTitlesByUserAndStatus(userId, ReadingStatus.DROPPED);
        List<Title> favoriteTitles = titleService.getTitlesByUserAndStatus(userId, ReadingStatus.FAVORITE);

        req.setAttribute("readingTitles", readingTitles);
        req.setAttribute("plannedTitles", plannedTitles);
        req.setAttribute("completedTitles", completedTitles);
        req.setAttribute("droppedTitles", droppedTitles);
        req.setAttribute("favoriteTitles", favoriteTitles);

        req.getRequestDispatcher("/jsp/profile.jsp").forward(req, resp);
    }
}
