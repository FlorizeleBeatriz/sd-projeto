package mz.agrogest.model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/** Modelo POJO serializável: Venda. */
public class Venda implements Serializable {
    private static final long serialVersionUID = 1L;

    private int id;
    private int clienteId;
    private String numeroFactura;
    private String nomeCliente;
    private String emailCliente;
    private String endereco;
    private String telefone;
    private LocalDateTime data;
    private List<ItemVenda> itens = new ArrayList<>();

    public Venda() {}

    public Venda(int id, int clienteId, String numeroFactura, String nomeCliente, String emailCliente, String endereco, String telefone, LocalDateTime data) {
        this.id = id;
        this.clienteId = clienteId;
        this.numeroFactura = numeroFactura;
        this.nomeCliente = nomeCliente;
        this.emailCliente = emailCliente;
        this.endereco = endereco;
        this.telefone = telefone;
        this.data = data;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public int getClienteId() { return clienteId; }
    public void setClienteId(int clienteId) { this.clienteId = clienteId; }

    public String getNumeroFactura() { return numeroFactura; }
    public void setNumeroFactura(String numeroFactura) { this.numeroFactura = numeroFactura; }

    public String getNomeCliente() { return nomeCliente; }
    public void setNomeCliente(String nomeCliente) { this.nomeCliente = nomeCliente; }

    public String getEmailCliente() { return emailCliente; }
    public void setEmailCliente(String emailCliente) { this.emailCliente = emailCliente; }

    public String getEndereco() { return endereco; }
    public void setEndereco(String endereco) { this.endereco = endereco; }

    public String getTelefone() { return telefone; }
    public void setTelefone(String telefone) { this.telefone = telefone; }

    public LocalDateTime getData() { return data; }
    public void setData(LocalDateTime data) { this.data = data; }

    public List<ItemVenda> getItens() { return itens; }
    public void setItens(List<ItemVenda> itens) { this.itens = itens; }


    public static final BigDecimal TAXA_IVA = new BigDecimal("0.16");

    public BigDecimal getSubtotal() {
        return itens.stream().map(ItemVenda::getSubtotal).reduce(BigDecimal.ZERO, BigDecimal::add)
                .setScale(2, RoundingMode.HALF_UP);
    }
    public BigDecimal getValorIva() { return getSubtotal().multiply(TAXA_IVA).setScale(2, RoundingMode.HALF_UP); }
    public BigDecimal getTotal() { return getSubtotal().add(getValorIva()); }
    public String getDataFmt() { return data.format(java.time.format.DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm")); }

}
