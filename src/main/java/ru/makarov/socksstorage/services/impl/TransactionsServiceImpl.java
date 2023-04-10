package ru.makarov.socksstorage.services.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.springframework.stereotype.Service;
import ru.makarov.socksstorage.model.FileType;
import ru.makarov.socksstorage.model.Socks;
import ru.makarov.socksstorage.model.Transaction;
import ru.makarov.socksstorage.model.TransactionType;
import ru.makarov.socksstorage.services.FileService;
import ru.makarov.socksstorage.services.TransactionsService;

import javax.annotation.PostConstruct;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;

@Service
public class TransactionsServiceImpl implements TransactionsService {

    private ArrayList<Transaction> transactions = new ArrayList<Transaction>();
    private final FileService fileService;

    public TransactionsServiceImpl(FileService fileService) {
        this.fileService = fileService;
    }

    @PostConstruct
    private void init() {
        readFromFile();
    }

    public void readFromFile() {
        try {
            String json = fileService.readFromFile(fileService.getPath(FileType.TRANSACTION));
            if (json != null) {
                ObjectMapper objectMapper = new ObjectMapper();
                objectMapper.registerModule(new JavaTimeModule());

                transactions =  objectMapper.readValue(json, new TypeReference<ArrayList<Transaction>>(){});
            }
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void addTransaction(Socks socks, TransactionType transactionType) {
        if (socks != null) {
            Transaction transaction = new Transaction(transactionType, LocalDateTime.now(),
                    socks.getSize(), socks.getColors(),
                    socks.getStock(), socks.getCottonPart());

            transactions.add(transaction);

            ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.registerModule(new JavaTimeModule());

            String json = null;
            try {
                json = objectMapper.writeValueAsString(transactions);
                fileService.saveToFile(json, FileType.TRANSACTION);
            } catch (JsonProcessingException e) {
                e.printStackTrace();
            }
        }
    }

}
