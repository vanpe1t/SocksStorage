package ru.makarov.socksstorage.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.makarov.socksstorage.model.Colors;
import ru.makarov.socksstorage.model.Size;
import ru.makarov.socksstorage.model.Socks;
import ru.makarov.socksstorage.model.TransactionType;
import ru.makarov.socksstorage.services.SocksService;
import ru.makarov.socksstorage.services.TransactionsService;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/socks")
@Tag(name = "Носки")
public class SocksController {

    private SocksService socksService;

    private TransactionsService transactionsService;

    public SocksController(SocksService socksService, TransactionsService transactionsService) {
        this.socksService = socksService;
        this.transactionsService = transactionsService;
    }

    @GetMapping("/stosklist")
    @Operation(
            summary = "Получения полного списка носков на складе.",
            description = "Получения полного списка носков на складе."
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Список успешно получен.",
                    content = {
                            @Content(
                                    mediaType = "List<Socks>"
                            )
                    }
            )
    }
    )
    public ResponseEntity<List<Socks>> getSocks() {
        return ResponseEntity.ok(socksService.getSocksList());
    }

    @GetMapping()
    @Operation(
            summary = "Получение количества носков по условию.",
            description = "Получение количества носков по условию"
    )
    @Parameters({
            @Parameter(
                    name = "color",
                    required = true,
                    schema = @Schema(implementation = Colors.class),
                    description = "Цвет носков"
            ),
            @Parameter(
                    name = "size",
                    required = true,
                    schema = @Schema(implementation = Size.class),
                    description = "Размер носков"
            ),
            @Parameter(
                    name = "cottonMin",
                    description = "Минимальное содержание хлопка %"
            ),
            @Parameter(
                    name = "cottonMax",
                    description = "Максимальное содержание хлопка %"
            )
    }
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Носки успешно оприходованны.",
                    content = {
                            @Content(
                                    mediaType = ""
                            )
                    }
            )
    }
    )
    public ResponseEntity<Integer> getQuantityCottonMin(@RequestParam Colors color,
                                                        @RequestParam Size size,
                                                        @RequestParam(name = "cottonMin", required = false) Integer cottonMin,
                                                        @RequestParam(name = "cottonMax", required = false) Integer cottonMax) {

       if (cottonMin != null && cottonMax != null) {
           return ResponseEntity.internalServerError().build();
       } else if (cottonMin != null && cottonMax == null) {
           return ResponseEntity.ok(socksService.getQuantityCottonMin(cottonMin, size, color));
       } else if (cottonMin == null && cottonMax != null) {
           return ResponseEntity.ok(socksService.getQuantityCottonMax(cottonMax, size, color));
       } else {
           return ResponseEntity.internalServerError().build();
       }
    }

    @PostMapping
    @Operation(
            summary = "Поступление носков на склад.",
            description = "Поступление носков на склад"
    )
    @Parameters({
            @Parameter(
                    name = "Носки",
                    schema = @Schema(implementation = Socks.class),
                    description = "Описание носков в формате Json"
            )
    }
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Носки успешно оприходованны.",
                    content = {
                            @Content(
                                    mediaType = ""
                            )
                    }
            )
    }
    )
    public ResponseEntity<Void> postSocks(@RequestBody Socks socks) {

        socksService.addSocks(socks);

        transactionsService.addTransaction(socks, TransactionType.ISSUE);

        return ResponseEntity.ok().build();
    }

    @PutMapping
    @Operation(
            summary = "Продажа товара со склада.",
            description = "Продажа товара со склада."
    )
    @Parameters({
            @Parameter(
                    name = "Носки",
                    schema = @Schema(implementation = Socks.class),
                    description = "Описание носков в формате Json"
            )
    }
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Носки успешно проданы.",
                    content = {
                            @Content(
                                    mediaType = ""
                            )
                    }
            )
    }
    )
    public ResponseEntity<String> putSocks(@RequestBody Socks socks) {

        if (socksService.sellSocks(socks)) {
            transactionsService.addTransaction(socks, TransactionType.RECEIPTS);
            return ResponseEntity.ok("Товар успешно продан.");
        } else {
            return ResponseEntity.ok("Нет нужного количества на складе.");
        }
    }

    @DeleteMapping
    @Operation(
            summary = "Списание брака со склада.",
            description = "Списание брака со склада."
    )
    @Parameters({
            @Parameter(
                    name = "Носки",
                    schema = @Schema(implementation = Socks.class),
                    description = "Описание носков в формате Json"
            )
    }
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Носки успешно списаны.",
                    content = {
                            @Content(
                                    mediaType = ""
                            )
                    }
            )
    }
    )
    public ResponseEntity<String> deleteSoks(@RequestBody Socks socks) {

        if (socksService.sellSocks(socks)) {
            transactionsService.addTransaction(socks, TransactionType.WRITE_OFF);
            return ResponseEntity.ok("Товар успешно списан.");
        } else {
            return ResponseEntity.ok("Нет нужного количества на складе.");
        }

    }
}
