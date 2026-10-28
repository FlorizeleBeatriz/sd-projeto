package mz.agrogest.service;

import java.time.LocalDateTime;
import java.util.*;
import mz.agrogest.model.*;
import mz.agrogest.repo.EstadoSistema;
import mz.agrogest.repo.Repositorio;

/** Venda transaccional: valida todos os itens e só depois desconta o stock (tudo ou nada). */
public final class VendaService {
    private VendaService() {}

    public static Venda comprar(Utilizador clienteSessao, Map<Integer, Integer> carrinho) {
        if (clienteSessao == null) throw new IllegalArgumentException("Sessão inválida.");
        if (carrinho == null || carrinho.isEmpty()) throw new IllegalArgumentException("O carrinho está vazio.");
        synchronized (Repositorio.LOCK) {
            EstadoSistema e = Repositorio.estado();
            Utilizador cliente = e.utilizadores.stream().filter(u -> u.getId() == clienteSessao.getId()).findFirst()
                    .orElseThrow(() -> new IllegalArgumentException("Cliente inexistente."));

            // Fase 1 — validar TODOS os itens sem alterar nada.
            List<Produto> produtos = new ArrayList<>();
            List<Integer> quantidades = new ArrayList<>();
            List<String> problemas = new ArrayList<>();
            for (Map.Entry<Integer, Integer> linha : carrinho.entrySet()) {
                Produto p = CatalogoService.produto(linha.getKey());
                int q = linha.getValue();
                if (p == null) problemas.add("Produto " + linha.getKey() + " já não existe.");
                else if (q < 1) problemas.add("Quantidade inválida para " + p.getNome() + ".");
                else if (p.isExpirado()) problemas.add(p.getNome() + " está fora do prazo de validade.");
                else if (p.getQuantidade() < q)
                    problemas.add("Stock insuficiente para " + p.getNome() + " (pedido: " + q + ", disponível: " + p.getQuantidade() + ").");
                else { produtos.add(p); quantidades.add(q); }
            }
            if (!problemas.isEmpty()) throw new IllegalStateException(String.join(" ", problemas));

            // Fase 2 — aplicar: descontar stock e emitir factura.
            List<ItemVenda> itens = new ArrayList<>();
            for (int i = 0; i < produtos.size(); i++) {
                Produto p = produtos.get(i);
                int q = quantidades.get(i);
                p.setQuantidade(p.getQuantidade() - q);
                itens.add(new ItemVenda(p.getId(), p.getNome(), q, p.getUnidade(), p.getPreco()));
            }
            String numero = "FT-" + java.time.Year.now().getValue() + "-" + e.facturas.getAndIncrement();
            Venda v = new Venda(e.proximoId("venda"), cliente.getId(), numero, cliente.getNome(), cliente.getEmail(),
                    cliente.getEndereco(), cliente.getTelefone(), LocalDateTime.now());
            v.setItens(itens);
            e.vendas.add(v);
            AdminService.persistir();
            return v;
        }
    }

    /** Devolve a venda se o utilizador for o dono ou administrador (RN-c). */
    public static Venda factura(int vendaId, Utilizador u) {
        synchronized (Repositorio.LOCK) {
            for (Venda v : Repositorio.estado().vendas) {
                if (v.getId() != vendaId) continue;
                if (u.isAdmin() || v.getClienteId() == u.getId()) return v;
                throw new SecurityException("Só pode consultar as suas próprias facturas.");
            }
            throw new NoSuchElementException("Factura inexistente.");
        }
    }

    public static List<Venda> vendasDoCliente(int clienteId) {
        synchronized (Repositorio.LOCK) {
            List<Venda> r = new ArrayList<>();
            for (Venda v : Repositorio.estado().vendas) if (v.getClienteId() == clienteId) r.add(v);
            r.sort(Comparator.comparing(Venda::getData).reversed());
            return r;
        }
    }

    public static List<Venda> todasVendas(String ordem, String dir) {
        ordem = Ordenacao.normalizar(ordem, "data", "valor", "cliente");
        Comparator<Venda> c = switch (ordem) {
            case "valor" -> Comparator.comparing(Venda::getTotal);
            case "cliente" -> Comparator.comparing(Venda::getNomeCliente, String.CASE_INSENSITIVE_ORDER);
            default -> Comparator.comparing(Venda::getData);
        };
        // por omissão a data é mostrada da mais recente para a mais antiga
        boolean desc = dir == null || dir.isBlank() ? ordem.equals("data") : "desc".equalsIgnoreCase(dir);
        synchronized (Repositorio.LOCK) {
            List<Venda> r = new ArrayList<>(Repositorio.estado().vendas);
            r.sort(Ordenacao.sentido(c, desc ? "desc" : "asc"));
            return r;
        }
    }
}
