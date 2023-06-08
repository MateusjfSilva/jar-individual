package service;

public class Limite {
    private Integer fkComponente;
    private Integer fkTipoAlerta;
    private Long maximo;
    private Long minimo;

    public Limite(Integer fkComponente, Integer fkTipoAlerta, Long maximo, Long minimo) {
        this.fkComponente = fkComponente;
        this.fkTipoAlerta = fkTipoAlerta;
        this.maximo = maximo;
        this.minimo = minimo;
    }
}
