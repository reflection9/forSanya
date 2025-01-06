package ru.itis.servlets.admin;

import ru.itis.models.Chapter;
import ru.itis.models.File;
import ru.itis.models.Genre;
import ru.itis.models.Title;
import ru.itis.services.*;

import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@WebServlet("/admin/titles")
@MultipartConfig
public class TitleManagementServlet extends HttpServlet {

    private TitleService titleService;
    private GenreService genreService;
    private AuthorService authorService;
    private FileService fileService;
    private ChapterService chapterService;

    @Override
    public void init() throws ServletException {
        titleService = (TitleService) getServletContext().getAttribute("titleService");
        genreService = (GenreService) getServletContext().getAttribute("genreService");
        authorService = (AuthorService) getServletContext().getAttribute("authorService");
        fileService = (FileService) getServletContext().getAttribute("fileService");
        chapterService = (ChapterService) getServletContext().getAttribute("chapterService");
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String action = req.getParameter("action");
        if ("edit".equals(action)) {
            Long titleId = Long.parseLong(req.getParameter("titleId"));
            Optional<Title> editTitleOptional = titleService.getTitleById(titleId);
            if (editTitleOptional.isPresent()) {
                req.setAttribute("editTitle", editTitleOptional.get());

                List<Chapter> chapters = chapterService.findByTitleId(titleId);
                req.setAttribute("chapters", chapters);
            } else {
                resp.sendError(HttpServletResponse.SC_NOT_FOUND, "Title not found");
                return;
            }
        }
        List<Title> titles = titleService.getAllTitles();
        titles.forEach(title -> title.setFiles(fileService.getFilesByTitleId(title.getId())));
        req.setAttribute("titles", titles);
        req.setAttribute("authors", authorService.getAllAuthors());
        req.setAttribute("genres", genreService.getAllGenres());
        req.getRequestDispatcher("/jsp/admin/titles.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String action = req.getParameter("action");
        if ("add".equals(action)) {
            handleAdd(req, resp);
        } else if ("update".equals(action)) {
            handleUpdate(req, resp);
        } else {
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "Unknown action: " + action);
        }
    }

    private void handleAdd(HttpServletRequest req, HttpServletResponse resp) throws IOException, ServletException {
        Title title = parseTitleFromRequest(req);
        titleService.addTitle(title);

        Part filePart = req.getPart("coverImage");
        if (filePart != null && filePart.getSize() > 0) {
            File file = saveFile(filePart, title.getId());
            fileService.addFile(file);
        }

        resp.sendRedirect("/admin/titles");
    }

    private void handleUpdate(HttpServletRequest req, HttpServletResponse resp) throws IOException, ServletException {
        Title title = parseTitleFromRequest(req);
        titleService.updateTitle(title);

        Part filePart = req.getPart("coverImage");
        if (filePart != null && filePart.getSize() > 0) {
            File file = saveFile(filePart, title.getId());
            fileService.updateCoverFileForTitle(title.getId(), file);
        }

        resp.sendRedirect("/admin/titles");
    }

    private Title parseTitleFromRequest(HttpServletRequest req) {
        String name = req.getParameter("name");
        String description = req.getParameter("description");
        String type = req.getParameter("type");
        Long authorId = Long.parseLong(req.getParameter("authorId"));
        String[] genreIds = req.getParameterValues("genreIds");
        Title title = Title.builder()
                .name(name)
                .description(description)
                .type(type)
                .authorId(authorId)
                .build();
        if (genreIds != null) {
            List<Genre> genres = new ArrayList<>();
            for (String genreId : genreIds) {
                genres.add(Genre.builder().id(Long.parseLong(genreId)).build());
            }
            title.setGenres(genres);
        }

        if (req.getParameter("titleId") != null) {
            title.setId(Long.parseLong(req.getParameter("titleId")));
        }

        return title;
    }

    private File saveFile(Part filePart, Long titleId) throws IOException {
        String uploadDir = getServletContext().getRealPath("/uploads/");
        java.io.File uploadFolder = new java.io.File(uploadDir);
        if (!uploadFolder.exists()) {
            uploadFolder.mkdir();
        }

        String fileName = filePart.getSubmittedFileName();
        String filePath = "/uploads/" + fileName;
        filePart.write(uploadDir + fileName);

        return File.builder()
                .titleId(titleId)
                .filePath(filePath)
                .build();
    }
}
