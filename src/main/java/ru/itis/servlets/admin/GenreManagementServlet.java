package ru.itis.servlets.admin;

import ru.itis.models.Genre;
import ru.itis.services.GenreService;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@WebServlet("/admin/genres")
public class GenreManagementServlet extends HttpServlet {
    private GenreService genreService;

    @Override
    public void init() throws ServletException {
        genreService = (GenreService) getServletContext().getAttribute("genreService");
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        List<Genre> genres = genreService.getAllGenres();
        req.setAttribute("genres", genres);
        req.getRequestDispatcher("/jsp/admin/genres.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String action = req.getParameter("action");
        if ("add".equals(action)) {
            String name = req.getParameter("name");
            genreService.addGenre(new Genre(null, name));
        } else if ("delete".equals(action)) {
            Long genreId = Long.parseLong(req.getParameter("genreId"));
            genreService.deleteGenre(genreId);
        } else if ("update".equals(action)) {
            Long genreId = Long.parseLong(req.getParameter("genreId"));
            String name = req.getParameter("name");
            genreService.updateGenre(new Genre(genreId, name));
        }
        resp.sendRedirect("/admin/genres");
    }
}
