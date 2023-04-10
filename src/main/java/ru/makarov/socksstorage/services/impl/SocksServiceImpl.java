package ru.makarov.socksstorage.services.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Service;
import ru.makarov.socksstorage.model.Colors;
import ru.makarov.socksstorage.model.FileType;
import ru.makarov.socksstorage.model.Size;
import ru.makarov.socksstorage.model.Socks;
import ru.makarov.socksstorage.services.FileService;
import ru.makarov.socksstorage.services.SocksService;
import ru.makarov.socksstorage.services.TransactionsService;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

@Service
public class SocksServiceImpl implements SocksService {

    private ArrayList<Socks> socksList = new ArrayList<>();
    private final FileService fileService;

    private final TransactionsService transactionsService;

    public SocksServiceImpl(FileService fileService, TransactionsService transactionsService) {
        this.fileService = fileService;
        this.transactionsService = transactionsService;
    }

    @PostConstruct
    private void init() {
        readFromFile();
    }

    @Override
    public void readFromFile() {
        try {
            String json = fileService.readFromFile(fileService.getPath(FileType.SOCKS));
            if (json != null) {
                socksList =  new ObjectMapper().readValue(json, new TypeReference<ArrayList<Socks>>(){});
            }
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
    }
    @Override
    public void addSocks(Socks socks) {

        Socks finded = socksList
                .stream()
                .filter(thisSocks -> (thisSocks.equals(socks)))
                .findFirst()
                .orElse(null);

        if (finded == null) {
            socksList.add(socks);
        } else {
            finded.setStock(socks.getStock());
        }

        String json = null;
        try {
            json = new ObjectMapper().writeValueAsString(socksList);
            fileService.saveToFile(json, FileType.SOCKS);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
    }

    @Override
    public ArrayList<Socks> getSocksList() {
        return socksList;
    }

    @Override
    public Integer getQuantityCottonMin(Integer сottonMin, Size size, Colors color) {
        Integer quantity = 0;
        for (Socks socks: socksList) {
            if (socks.getCottonPart() >= сottonMin && size.equals(socks.getSize()) && color.equals(socks.getColors())) {
                quantity += socks.getStock();
            }
        }
        return quantity;
    }

    @Override
    public Integer getQuantityCottonMax(Integer сottonMax, Size size, Colors color) {
        Integer quantity = 0;
        for (Socks socks: socksList) {
            if (socks.getCottonPart() <= сottonMax && size.equals(socks.getSize()) && color.equals(socks.getColors())) {
                quantity += socks.getStock();
            }
        }
        return quantity;
    }

    @Override
    public Boolean sellSocks(Socks socks) {
        return minusSocks(socks);
    }

    @Override
    public Boolean deleteSocks(Socks socks) {
        return minusSocks(socks);
    }

    public boolean minusSocks(Socks socks) {

        boolean done = false;

        Socks finded = socksList
                .stream()
                .filter(thisSocks -> (thisSocks.equals(socks)))
                .findFirst()
                .orElse(null);

        if (finded != null) {
            finded.setStock(finded.getStock() - socks.getStock());
            done = true;
        }

        String json = null;

        try {
            json = new ObjectMapper().writeValueAsString(socksList);
            fileService.saveToFile(json, FileType.SOCKS);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        return done;
    }

}
