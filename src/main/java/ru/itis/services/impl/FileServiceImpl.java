package ru.itis.services.impl;

import ru.itis.models.File;
import ru.itis.repositories.FileRepository;
import ru.itis.services.FileService;

import java.util.List;
import java.util.Optional;

public class FileServiceImpl implements FileService {

    private final FileRepository fileRepository;

    public FileServiceImpl(FileRepository fileRepository) {
        this.fileRepository = fileRepository;
    }

    @Override
    public void addFile(File file) {
        fileRepository.save(file);
    }


    @Override
    public List<File> getFilesByTitleId(Long titleId) {
        return fileRepository.findByTitleId(titleId);
    }

    @Override
    public void updateCoverFileForTitle(Long titleId, File file) {
        fileRepository.removeByTitleId(titleId);
        fileRepository.save(file);
    }

}
