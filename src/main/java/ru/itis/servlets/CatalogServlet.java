package ru.itis.servlets;

import ru.itis.models.Title;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;

import ru.itis.services.AuthorService;
import ru.itis.services.FileService;
import ru.itis.services.GenreService;
import ru.itis.services.TitleService;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@WebServlet("/catalog")
public class CatalogServlet extends HttpServlet {
    private TitleService titleService;
    private FileService fileService;
    private GenreService genreService;
    private AuthorService authorService;

    @Override
    public void init() throws ServletException {
        titleService = (TitleService) getServletContext().getAttribute("titleService");
        fileService = (FileService) getServletContext().getAttribute("fileService");
        genreService = (GenreService) getServletContext().getAttribute("genreService");
        authorService = (AuthorService) getServletContext().getAttribute("authorService");
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String search = req.getParameter("search");
        String genreId = req.getParameter("genre");
        String authorId = req.getParameter("author");
        String type = req.getParameter("type");

        List<Title> titles = titleService.getAllTitles();

        if (search != null && !search.isEmpty()) {
            titles = titles.stream()
                    .filter(title -> title.getName().toLowerCase().contains(search.toLowerCase()))
                    .collect(Collectors.toList());
        }

        if(type != null && !type.isEmpty()) {
            titles = titles.stream()
                    .filter(title -> title.getType().equalsIgnoreCase(type))
                    .collect(Collectors.toList());
        }

        if (genreId != null && !genreId.isEmpty()) {
            Long genreIdLong = Long.parseLong(genreId);
            titles = titles.stream()
                    .filter(title -> title.getGenres().stream()
                            .anyMatch(genre -> genre.getId().equals(genreIdLong)))
                    .collect(Collectors.toList());
        }

        if (authorId != null && !authorId.isEmpty()) {
            Long authorIdLong = Long.parseLong(authorId);
            titles = titles.stream()
                    .filter(title -> title.getAuthor() != null && title.getAuthor().getId().equals(authorIdLong))
                    .collect(Collectors.toList());
        }

        titles.forEach(title -> title.setFiles(fileService.getFilesByTitleId(title.getId())));

        if ("XMLHttpRequest".equals(req.getHeader("X-Requested-With"))) {
            req.setAttribute("titles", titles);
            req.getRequestDispatcher("/jsp/catalog-grid.jsp").forward(req, resp);
        } else {
            req.setAttribute("titles", titles);
            req.setAttribute("genres", genreService.getAllGenres());
            req.setAttribute("authors", authorService.getAllAuthors());
            req.getRequestDispatcher("/jsp/catalog.jsp").forward(req, resp);
        }
    }
}
