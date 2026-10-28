package mz.agrogest.service;

import java.math.BigDecimal;
import mz.agrogest.model.Utilizador;

/** Linha de relatório: cliente com número de compras e volume (valor total) comprado. */
public class ResumoCliente {
    private final Utilizador cliente;
    private final int numeroCompras;
    private final BigDecimal volume;

    public ResumoCliente(Utilizador cliente, int numeroCompras, BigDecimal volume) {
        this.cliente = cliente; this.numeroCompras = numeroCompras; this.volume = volume;
    }
    public Utilizador getCliente() { return cliente; }
    public int getNumeroCompras() { return numeroCompras; }
    public BigDecimal getVolume() { return volume; }
}
