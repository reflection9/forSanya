package ru.itis.listener;

import ru.itis.repositories.*;
import ru.itis.repositories.impl.*;
import ru.itis.services.*;
import ru.itis.services.impl.*;
import javax.servlet.ServletContextEvent;
import javax.servlet.annotation.WebListener;

@WebListener
public class ServletContextListener implements javax.servlet.ServletContextListener {

    @Override
    public void contextInitialized(ServletContextEvent sce) {

        UserRepository userRepository = new UserRepositoryJdbcImpl();
        TitleRepository titleRepository = new TitleRepositoryJdbcImpl();
        GenreRepository genreRepository = new GenreRepositoryJdbcImpl();
        ChapterRepository chapterRepository = new ChapterRepositoryJdbcImpl();
        AuthorRepository authorRepository = new AuthorRepositoryJdbcImpl();
        CommentRepository commentRepository = new CommentRepositoryJdbcImpl();
        FileRepository fileRepository = new FileRepositoryJdbcImpl();


        UserService userService = new UserServiceImpl(userRepository);
        TitleService titleService = new TitleServiceImpl(titleRepository, genreRepository, fileRepository);
        GenreService genreService = new GenreServiceImpl(genreRepository);
        ChapterService chapterService = new ChapterServiceImpl(chapterRepository, fileRepository);
        AuthorService authorService = new AuthorServiceImpl(authorRepository);
        FileService fileService = new FileServiceImpl(fileRepository);
        CommentService commentService = new CommentServiceImpl(commentRepository);


        sce.getServletContext().setAttribute("userService", userService);
        sce.getServletContext().setAttribute("titleService", titleService);
        sce.getServletContext().setAttribute("genreService", genreService);
        sce.getServletContext().setAttribute("chapterService", chapterService);
        sce.getServletContext().setAttribute("authorService", authorService);
        sce.getServletContext().setAttribute("fileService", fileService);
        sce.getServletContext().setAttribute("commentService", commentService);
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
    }
}