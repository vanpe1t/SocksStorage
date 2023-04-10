package ru.makarov.socksstorage.services;

import ru.makarov.socksstorage.model.FileType;

import java.io.File;
import java.nio.file.Path;

public interface FileService {

    Path getPath(FileType fileType);

    File getFile(FileType fileType);

    boolean saveToFile(String json, FileType fileType);

    void cleanDataFile(Path path);

    String readFromFile(Path path);
}
