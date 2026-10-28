package mz.agrogest.model;

import java.io.Serializable;
import java.math.BigDecimal;

/** Modelo POJO serializável: ItemVenda. */
public class ItemVenda implements Serializable {
    private static final long serialVersionUID = 1L;

    private int produtoId;
    private String nomeProduto;
    private int quantidade;
    private String unidade;
    private BigDecimal precoUnitario;

    public ItemVenda() {}

    public ItemVenda(int produtoId, String nomeProduto, int quantidade, String unidade, BigDecimal precoUnitario) {
        this.produtoId = produtoId;
        this.nomeProduto = nomeProduto;
        this.quantidade = quantidade;
        this.unidade = unidade;
        this.precoUnitario = precoUnitario;
    }

    public int getProdutoId() { return produtoId; }
    public void setProdutoId(int produtoId) { this.produtoId = produtoId; }

    public String getNomeProduto() { return nomeProduto; }
    public void setNomeProduto(String nomeProduto) { this.nomeProduto = nomeProduto; }

    public int getQuantidade() { return quantidade; }
    public void setQuantidade(int quantidade) { this.quantidade = quantidade; }

    public String getUnidade() { return unidade; }
    public void setUnidade(String unidade) { this.unidade = unidade; }

    public BigDecimal getPrecoUnitario() { return precoUnitario; }
    public void setPrecoUnitario(BigDecimal precoUnitario) { this.precoUnitario = precoUnitario; }


    public BigDecimal getSubtotal() { return precoUnitario.multiply(BigDecimal.valueOf(quantidade)); }

}
