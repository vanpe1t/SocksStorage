package ru.makarov.socksstorage.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ru.makarov.socksstorage.model.FileType;
import ru.makarov.socksstorage.services.FileService;

import java.io.*;
import java.nio.file.Path;

@RestController
@RequestMapping("/api/files")
@Tag(name = "Экспорт/импорт")

public class FileController {

    private final FileService fileService;

    public FileController(FileService fileService) {
        this.fileService = fileService;
    }

    @GetMapping("/socks")
    @Operation(
            summary = "Получение файла состояния товара на складе.",
            description = "Получение файла состояния товара на складе."
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Файл получен.",
                    content = {
                            @Content(
                                    mediaType = "file"
                            )
                    }
            )
    }
    )
    public ResponseEntity<InputStreamResource> downloadDataFileSocks() throws FileNotFoundException {

        File file = fileService.getFile(FileType.SOCKS);

        if (file.exists()) {
            InputStreamResource resource = new InputStreamResource(new FileInputStream(file));
            return ResponseEntity.ok()
                    .contentType(MediaType.APPLICATION_JSON)
                    .contentLength(file.length())
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename = sockslist.json")
                    .body(resource);
        } else {
            return ResponseEntity.noContent().build();
        }
    }

    @PostMapping(value = "/socks", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @Operation(
            summary = "Загрузка файла состояния товара на складе.",
            description = "Загрузка файла состояния товара на складе."
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Файл получен.",
                    content = {
                            @Content(
                                    mediaType = "file"
                            )
                    }
            )
    }
    )
    public ResponseEntity<Void> uploadDataFileSocks(@RequestParam MultipartFile file) {

        Path path = fileService.getPath(FileType.SOCKS);

        fileService.cleanDataFile(path);

        File dataFile = fileService.getFile(FileType.SOCKS);

        try (FileOutputStream fos = new FileOutputStream(dataFile)) {
            IOUtils.copy(file.getInputStream(), fos);
            return ResponseEntity.ok().build();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
    }

    @GetMapping("/trans")
    @Operation(
            summary = "Получение файла транзакций.",
            description = "Получение файла транзакций."
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Файл получен.",
                    content = {
                            @Content(
                                    mediaType = "file"
                            )
                    }
            )
    }
    )
    public ResponseEntity<InputStreamResource> downloadDataFileTransactions() throws FileNotFoundException {

        File file = fileService.getFile(FileType.TRANSACTION);

        if (file.exists()) {
            InputStreamResource resource = new InputStreamResource(new FileInputStream(file));
            return ResponseEntity.ok()
                    .contentType(MediaType.APPLICATION_JSON)
                    .contentLength(file.length())
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename = sockslist.json")
                    .body(resource);
        } else {
            return ResponseEntity.noContent().build();
        }
    }

    @PostMapping(value = "/trans", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @Operation(
            summary = "Загрузка списка транзакций.",
            description = "Загрузка списка транзакций."
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Файл получен.",
                    content = {
                            @Content(
                                    mediaType = "file"
                            )
                    }
            )
    }
    )
    public ResponseEntity<Void> uploadDataFileTransactions(@RequestParam MultipartFile file) {

        Path path = fileService.getPath(FileType.TRANSACTION);

        fileService.cleanDataFile(path);

        File dataFile = fileService.getFile(FileType.TRANSACTION);

        try (FileOutputStream fos = new FileOutputStream(dataFile)) {
            IOUtils.copy(file.getInputStream(), fos);
            return ResponseEntity.ok().build();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
    }

}
