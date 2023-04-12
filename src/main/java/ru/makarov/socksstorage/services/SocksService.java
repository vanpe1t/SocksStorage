package ru.makarov.socksstorage.services;

import org.springframework.stereotype.Service;
import ru.makarov.socksstorage.model.Colors;
import ru.makarov.socksstorage.model.Size;
import ru.makarov.socksstorage.model.Socks;

import java.util.ArrayList;


@Service
public interface SocksService {

    void readFromFile();

    void addSocks(Socks socks);

    ArrayList<Socks> getSocksList();

    Integer getQuantityCottonMin(Integer сottonMin, Size size, Colors color);

    Integer getQuantityCottonMax(Integer сottonMax, Size size, Colors color);

    Integer getQuantityCotton(Integer cottonMax, Integer cottonMin, Size size, Colors color);

    Boolean sellSocks(Socks socks);

    Boolean deleteSocks(Socks socks);
}
