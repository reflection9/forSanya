package ru.itis.services;

import ru.itis.models.File;

import java.util.List;

public interface FileService {
    void addFile(File file);
    List<File> getFilesByTitleId(Long titleId);

    void updateCoverFileForTitle(Long titleId, File file);

}