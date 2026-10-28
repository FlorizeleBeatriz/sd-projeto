package mz.agrogest.service;

import java.util.*;
import mz.agrogest.model.*;
import mz.agrogest.repo.Repositorio;

/** Consultas de leitura de produtos e fornecedores (catálogo, armazém, listagens). */
public final class CatalogoService {
    private CatalogoService() {}

    public static List<Produto> produtos(String categoria, boolean apenasComStock, String ordem, String dir) {
        ordem = Ordenacao.normalizar(ordem, "nome", "preco", "stock");
        Comparator<Produto> c = switch (ordem) {
            case "preco" -> Comparator.comparing(Produto::getPreco);
            case "stock" -> Comparator.comparingInt(Produto::getQuantidade);
            default -> Comparator.comparing(Produto::getNome, String.CASE_INSENSITIVE_ORDER);
        };
        synchronized (Repositorio.LOCK) {
            List<Produto> r = new ArrayList<>();
            for (Produto p : Repositorio.estado().produtos) {
                if (categoria != null && !categoria.isBlank() && !categoria.equalsIgnoreCase(p.getCategoria())) continue;
                if (apenasComStock && p.getQuantidade() <= 0) continue;
                r.add(p);
            }
            r.sort(Ordenacao.sentido(c, dir));
            return r;
        }
    }

    public static List<Fornecedor> fornecedores(String ordem, String dir) {
        ordem = Ordenacao.normalizar(ordem, "nome", "provincia", "tipo");
        Comparator<Fornecedor> c = switch (ordem) {
            case "provincia" -> Comparator.comparing(Fornecedor::getProvincia, String.CASE_INSENSITIVE_ORDER);
            case "tipo" -> Comparator.comparing(Fornecedor::getTipo);
            default -> Comparator.comparing(Fornecedor::getNome, String.CASE_INSENSITIVE_ORDER);
        };
        synchronized (Repositorio.LOCK) {
            List<Fornecedor> r = new ArrayList<>(Repositorio.estado().fornecedores);
            r.sort(Ordenacao.sentido(c, dir));
            return r;
        }
    }

    /** id do fornecedor -> nome, para apresentar o fornecedor em cada produto. */
    public static Map<Integer, String> nomesFornecedores() {
        synchronized (Repositorio.LOCK) {
            Map<Integer, String> m = new HashMap<>();
            for (Fornecedor f : Repositorio.estado().fornecedores) m.put(f.getId(), f.getNome());
            return m;
        }
    }

    public static Produto produto(int id) {
        synchronized (Repositorio.LOCK) {
            for (Produto p : Repositorio.estado().produtos) if (p.getId() == id) return p;
            return null;
        }
    }
}
