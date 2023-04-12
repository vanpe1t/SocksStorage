package ru.makarov.socksstorage.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.Objects;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class Socks {

    private Colors colors;

    private Size size;

    private int cottonPart;

    private int stock;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Socks socks = (Socks) o;
        return cottonPart == socks.cottonPart && colors == socks.colors && size == socks.size;
    }

    @Override
    public int hashCode() {
        return Objects.hash(colors, size, cottonPart);
    }
}
