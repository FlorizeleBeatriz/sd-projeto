package mz.agrogest.service;

import java.math.BigDecimal;
import java.util.*;
import mz.agrogest.model.Produto;
import mz.agrogest.repo.Repositorio;

/** Operações sobre o carrinho (mapa produtoId -> quantidade) guardado na sessão HTTP. */
public final class CarrinhoService {
    private CarrinhoService() {}

    public static void adicionar(Map<Integer, Integer> carrinho, int produtoId, int quantidade) {
        if (quantidade < 1) throw new IllegalArgumentException("A quantidade deve ser pelo menos 1.");
        synchronized (Repositorio.LOCK) {
            Produto p = obter(produtoId);
            int total = carrinho.getOrDefault(produtoId, 0) + quantidade;
            if (total > p.getQuantidade())
                throw new IllegalStateException("Stock insuficiente para " + p.getNome() + " (disponível: " + p.getQuantidade() + " " + p.getUnidade() + ").");
            carrinho.put(produtoId, total);
        }
    }

    /** Define a quantidade de uma linha; 0 remove a linha. */
    public static void actualizar(Map<Integer, Integer> carrinho, int produtoId, int quantidade) {
        if (quantidade < 0) throw new IllegalArgumentException("Quantidade inválida.");
        if (quantidade == 0) { carrinho.remove(produtoId); return; }
        synchronized (Repositorio.LOCK) {
            Produto p = obter(produtoId);
            if (quantidade > p.getQuantidade())
                throw new IllegalStateException("Stock insuficiente para " + p.getNome() + " (disponível: " + p.getQuantidade() + " " + p.getUnidade() + ").");
            carrinho.put(produtoId, quantidade);
        }
    }

    public static void remover(Map<Integer, Integer> carrinho, int produtoId) { carrinho.remove(produtoId); }
    public static void esvaziar(Map<Integer, Integer> carrinho) { carrinho.clear(); }

    public static List<LinhaCarrinho> linhas(Map<Integer, Integer> carrinho) {
        List<LinhaCarrinho> r = new ArrayList<>();
        synchronized (Repositorio.LOCK) {
            for (Map.Entry<Integer, Integer> l : carrinho.entrySet()) {
                Produto p = CatalogoService.produto(l.getKey());
                if (p != null) r.add(new LinhaCarrinho(p, l.getValue()));
            }
        }
        return r;
    }

    public static BigDecimal subtotal(List<LinhaCarrinho> linhas) {
        return linhas.stream().map(LinhaCarrinho::getSubtotal).reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    private static Produto obter(int id) {
        Produto p = CatalogoService.produto(id);
        if (p == null) throw new IllegalArgumentException("Produto inexistente.");
        return p;
    }
}
