package ru.makarov.socksstorage.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class Transaction {

    private TransactionType transactionType;

    private LocalDateTime data;

    private Size size;

    private Colors color;

    private int quantity;

    private int cottonPart;


}
