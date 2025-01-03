package ru.itis.servlets.admin;

import ru.itis.models.Author;
import ru.itis.services.AuthorService;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@WebServlet("/admin/authors")
public class AuthorManagementServlet extends HttpServlet {
    private AuthorService authorService;

    @Override
    public void init() throws ServletException {
        authorService = (AuthorService) getServletContext().getAttribute("authorService");
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        List<Author> authors = authorService.getAllAuthors();
        req.setAttribute("authors", authors);
        req.getRequestDispatcher("/jsp/admin/authors.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String action = req.getParameter("action");
        if ("add".equals(action)) {
            String name = req.getParameter("name");
            authorService.addAuthor(new Author(null, name));
        } else if ("delete".equals(action)) {
            Long authorId = Long.parseLong(req.getParameter("authorId"));
            authorService.deleteAuthor(authorId);
        }
        resp.sendRedirect("/admin/authors");
    }
}
