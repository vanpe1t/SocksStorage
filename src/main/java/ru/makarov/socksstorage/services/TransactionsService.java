package ru.makarov.socksstorage.services;

import ru.makarov.socksstorage.model.Socks;
import ru.makarov.socksstorage.model.TransactionType;

public interface TransactionsService {


    void addTransaction(Socks socks, TransactionType transactionType);
}
