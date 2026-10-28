package mz.agrogest.service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.*;
import mz.agrogest.model.*;
import mz.agrogest.repo.EstadoSistema;
import mz.agrogest.repo.Repositorio;
import mz.agrogest.util.Validacao;

/** Operações de gestão do administrador. Todas as escritas são sincronizadas e gravam o estado em ficheiro. */
public final class AdminService {
    private AdminService() {}

    /** Gravação automática após cada escrita; falhas são registadas sem perder o estado em memória. */
    public static void persistir() {
        try { Repositorio.guardar(); }
        catch (Exception e) { System.err.println("[AgroGest] Aviso: não foi possível gravar o estado: " + e); }
    }

    private static EstadoSistema e() { return Repositorio.estado(); }

    // ---------------- Fornecedores ----------------
    public static Fornecedor criarFornecedor(String nome, String tipo, String contacto, String provincia) {
        nome = Validacao.texto(nome, "nome");
        tipo = Validacao.enumerado(tipo, Validacao.TIPOS_FORNECEDOR, "tipo");
        contacto = Validacao.texto(contacto, "contacto");
        provincia = Validacao.texto(provincia, "província");
        synchronized (Repositorio.LOCK) {
            Fornecedor f = new Fornecedor(e().proximoId("fornecedor"), nome, tipo, contacto, provincia);
            e().fornecedores.add(f);
            persistir();
            return f;
        }
    }

    public static void removerFornecedor(int id) {
        synchronized (Repositorio.LOCK) {
            if (e().produtos.stream().anyMatch(p -> p.getFornecedorId() == id))
                throw new IllegalStateException("Não é possível remover um fornecedor com produtos associados.");
            if (!e().fornecedores.removeIf(f -> f.getId() == id)) throw new IllegalArgumentException("Fornecedor inexistente.");
            persistir();
        }
    }

    // ---------------- Produtos ----------------
    public static Produto criarProduto(String nome, String categoria, String fornecedorId, String preco, String quantidade,
                                       String unidade, String descricao, String fotoUrl, String validade) {
        nome = Validacao.texto(nome, "nome");
        categoria = Validacao.enumerado(categoria, Validacao.CATEGORIAS, "categoria");
        int fid = Validacao.inteiro(fornecedorId, "fornecedor");
        BigDecimal p = Validacao.decimal(preco, "preço");
        int q = Validacao.inteiro(quantidade, "quantidade");
        if (p.signum() <= 0) throw new IllegalArgumentException("O preço deve ser positivo.");
        if (q < 0) throw new IllegalArgumentException("A quantidade não pode ser negativa.");
        unidade = Validacao.texto(unidade, "unidade");
        LocalDate val = Validacao.dataOpcional(validade, "validade");
        String foto = Validacao.urlOpcional(fotoUrl);
        synchronized (Repositorio.LOCK) {
            if (e().fornecedores.stream().noneMatch(f -> f.getId() == fid))
                throw new IllegalArgumentException("O fornecedor " + fid + " não existe. Registe-o primeiro.");
            Produto pr = new Produto(e().proximoId("produto"), nome, categoria, fid, p, q, unidade,
                    Validacao.textoOpcional(descricao), foto, val);
            e().produtos.add(pr);
            persistir();
            return pr;
        }
    }

    /** Repõe stock somando a quantidade indicada (deve ser positiva). */
    public static void reporStock(int produtoId, String quantidade) {
        int q = Validacao.inteiro(quantidade, "quantidade");
        if (q < 1) throw new IllegalArgumentException("A quantidade a repor deve ser pelo menos 1.");
        synchronized (Repositorio.LOCK) {
            Produto p = CatalogoService.produto(produtoId);
            if (p == null) throw new IllegalArgumentException("Produto inexistente.");
            p.setQuantidade(p.getQuantidade() + q);
            persistir();
        }
    }

    public static void removerProduto(int id) {
        synchronized (Repositorio.LOCK) {
            if (!e().produtos.removeIf(p -> p.getId() == id)) throw new IllegalArgumentException("Produto inexistente.");
            persistir();
        }
    }

    // ---------------- Campanhas ----------------
    public static Campanha criarCampanha(String nome, String inicio, String fim) {
        nome = Validacao.texto(nome, "nome");
        LocalDate i = Validacao.data(inicio, "início"), f = Validacao.data(fim, "fim");
        if (f.isBefore(i)) throw new IllegalArgumentException("A data de fim não pode ser anterior ao início.");
        synchronized (Repositorio.LOCK) {
            Campanha c = new Campanha(e().proximoId("campanha"), nome, i, f);
            e().campanhas.add(c);
            persistir();
            return c;
        }
    }

    public static void definirEstadoCampanha(int id, boolean aberta) {
        synchronized (Repositorio.LOCK) {
            Campanha c = campanha(id);
            c.setAberta(aberta);
            persistir();
        }
    }

    public static void removerCampanha(int id) {
        synchronized (Repositorio.LOCK) {
            campanha(id);
            if (e().cultivos.stream().anyMatch(c -> c.getCampanhaId() == id))
                throw new IllegalStateException("Não é possível remover uma campanha com cultivos associados.");
            e().campanhas.removeIf(c -> c.getId() == id);
            persistir();
        }
    }

    private static Campanha campanha(int id) {
        for (Campanha c : e().campanhas) if (c.getId() == id) return c;
        throw new IllegalArgumentException("Campanha inexistente.");
    }

    // ---------------- Cultivos ----------------
    public static Cultivo criarCultivo(String campanhaId, String cultura, String variedade, String hectares, String plantio) {
        int cid = Validacao.inteiro(campanhaId, "campanha");
        cultura = Validacao.texto(cultura, "cultura");
        variedade = Validacao.texto(variedade, "variedade");
        BigDecimal ha = Validacao.decimal(hectares, "hectares");
        if (ha.signum() <= 0) throw new IllegalArgumentException("Os hectares devem ser positivos.");
        LocalDate dp = Validacao.data(plantio, "data de plantio");
        synchronized (Repositorio.LOCK) {
            Campanha c = campanha(cid);
            if (!c.isAberta()) throw new IllegalStateException("A campanha está fechada. Reabra-a para registar cultivos.");
            Cultivo cu = new Cultivo(e().proximoId("cultivo"), cid, cultura, variedade, ha, dp);
            e().cultivos.add(cu);
            persistir();
            return cu;
        }
    }

    public static void removerCultivo(int id) {
        synchronized (Repositorio.LOCK) {
            if (e().colheitas.stream().anyMatch(h -> h.getCultivoId() == id))
                throw new IllegalStateException("Não é possível remover um cultivo com colheitas associadas.");
            if (!e().cultivos.removeIf(c -> c.getId() == id)) throw new IllegalArgumentException("Cultivo inexistente.");
            persistir();
        }
    }

    // ---------------- Colheitas ----------------
    public static Colheita criarColheita(String cultivoId, String data, String kg, String precoKg) {
        int cid = Validacao.inteiro(cultivoId, "cultivo");
        LocalDate d = Validacao.data(data, "data");
        BigDecimal q = Validacao.decimal(kg, "quantidade (kg)"), p = Validacao.decimal(precoKg, "preço por kg");
        if (q.signum() <= 0 || p.signum() <= 0) throw new IllegalArgumentException("Quantidade e preço por kg devem ser positivos.");
        synchronized (Repositorio.LOCK) {
            Cultivo cu = e().cultivos.stream().filter(x -> x.getId() == cid).findFirst()
                    .orElseThrow(() -> new IllegalArgumentException("Cultivo inexistente."));
            if (d.isBefore(cu.getDataPlantio())) throw new IllegalArgumentException("A colheita não pode ser anterior ao plantio.");
            Colheita h = new Colheita(e().proximoId("colheita"), cid, d, q, p);
            e().colheitas.add(h);
            persistir();
            return h;
        }
    }

    public static void removerColheita(int id) {
        synchronized (Repositorio.LOCK) {
            if (!e().colheitas.removeIf(h -> h.getId() == id)) throw new IllegalArgumentException("Colheita inexistente.");
            persistir();
        }
    }

    // ---------------- Consultas ----------------
    public static List<Campanha> campanhas() { synchronized (Repositorio.LOCK) { return new ArrayList<>(e().campanhas); } }

    public static List<Cultivo> cultivos() {
        synchronized (Repositorio.LOCK) {
            List<Cultivo> r = new ArrayList<>(e().cultivos);
            r.sort(Comparator.comparingInt(Cultivo::getCampanhaId).thenComparingInt(Cultivo::getId));
            return r;
        }
    }

    public static List<Colheita> colheitas() {
        synchronized (Repositorio.LOCK) {
            List<Colheita> r = new ArrayList<>(e().colheitas);
            r.sort(Comparator.comparing(Colheita::getData).reversed());
            return r;
        }
    }

    public static Map<Integer, String> nomesCampanhas() {
        Map<Integer, String> m = new HashMap<>();
        synchronized (Repositorio.LOCK) { for (Campanha c : e().campanhas) m.put(c.getId(), c.getNome()); }
        return m;
    }

    public static Map<Integer, String> nomesCultivos() {
        Map<Integer, String> m = new HashMap<>();
        synchronized (Repositorio.LOCK) {
            for (Cultivo c : e().cultivos) m.put(c.getId(), c.getCultura() + " (" + c.getVariedade() + ")");
        }
        return m;
    }

    /** Contadores do painel de administração. */
    public static Map<String, Integer> contadores() {
        synchronized (Repositorio.LOCK) {
            Map<String, Integer> m = new LinkedHashMap<>();
            m.put("fornecedores", e().fornecedores.size());
            m.put("produtos", e().produtos.size());
            m.put("clientes", (int) e().utilizadores.stream().filter(u -> !u.isAdmin()).count());
            m.put("vendas", e().vendas.size());
            m.put("campanhas", e().campanhas.size());
            m.put("cultivos", e().cultivos.size());
            m.put("colheitas", e().colheitas.size());
            return m;
        }
    }
}
