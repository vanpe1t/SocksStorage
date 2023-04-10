package ru.makarov.socksstorage.services.impl;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import ru.makarov.socksstorage.model.FileType;
import ru.makarov.socksstorage.services.FileService;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

@Service
public class FileServiceImpl implements FileService {

    @Value("${path.to.data.file}")
    private String dataFilePath;

    @Value("name.of.data.file.socks")
    private String socksListFileName;

    @Value("name.of.data.file.transactions")
    private String transactionsFileName;

    @Override
    public Path getPath(FileType fileType) {
        if (fileType == FileType.SOCKS) {
            return Path.of(dataFilePath, socksListFileName);
        } else if (fileType == FileType.TRANSACTION) {
            return Path.of(dataFilePath, transactionsFileName);
        } else {
            return null;
        }
    }

    @Override
    public File getFile(FileType fileType) {
        return new File(getPath(fileType).toString());
    }

    @Override
    public boolean saveToFile(String json, FileType fileType) {

        Path path;

        if (fileType == FileType.SOCKS) {
            path = Path.of(dataFilePath, socksListFileName);
        } else if (fileType == FileType.TRANSACTION) {
            path = Path.of(dataFilePath, transactionsFileName);
        } else {
            path = null;
        }

        cleanDataFile(path);

        try {
            Files.writeString(path, json);
            return true;
        } catch (IOException e) {
            return false;
        }
    }

    @Override
    public void cleanDataFile(Path path) {
        try {
            Files.deleteIfExists(path);
            Files.createFile(path);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public String readFromFile(Path path) {

        if (path == null) {
            return null;
        }

        try {
            return Files.readString(path);
        } catch (IOException e) {
            return null;
        }
    }

}
