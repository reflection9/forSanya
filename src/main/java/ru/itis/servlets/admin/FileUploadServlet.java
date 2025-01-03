package ru.itis.servlets.admin;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;

@WebServlet("/uploads/*")
public class FileUploadServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String fileName = req.getPathInfo();
        if (fileName == null || fileName.equals("/")) {
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "Файл не указан");
            return;
        }

        String fileDir = getServletContext().getRealPath("/uploads");
        java.io.File file = new java.io.File(fileDir, fileName);

        if (!file.exists()) {
            resp.sendError(HttpServletResponse.SC_NOT_FOUND, "Файл не найден");
            return;
        }

        String mimeType = getServletContext().getMimeType(file.getName());
        if (mimeType == null) {
            mimeType = "application/octet-stream";
        }
        resp.setContentType(mimeType);
        resp.setContentLength((int) file.length());

        try (FileInputStream in = new FileInputStream(file);
             OutputStream out = resp.getOutputStream()) {
            byte[] buffer = new byte[1024];
            int bytesRead;
            while ((bytesRead = in.read(buffer)) != -1) {
                out.write(buffer, 0, bytesRead);
            }
        }
    }
}
