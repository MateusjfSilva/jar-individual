package service;

import java.util.List;

public class Empresa {

    private Integer id;
    private Double bandaLarga;

    private List<Totem> totens;

    public Empresa(Integer id, Double bandaLarga) {
        this.id = id;
        this.bandaLarga = bandaLarga;
    }

    public Integer getId() {
        return id;
    }

    public Double getBandaLarga() {
        return bandaLarga;
    }
}
