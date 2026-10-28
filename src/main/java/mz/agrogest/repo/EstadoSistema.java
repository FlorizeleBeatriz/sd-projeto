package mz.agrogest.repo;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import mz.agrogest.model.*;

/** Estado completo do sistema (camada de persistência temporária em memória, serializável). */
public class EstadoSistema implements Serializable {
    private static final long serialVersionUID = 2L;

    public final List<Utilizador> utilizadores = Collections.synchronizedList(new ArrayList<>());
    public final List<Fornecedor> fornecedores = Collections.synchronizedList(new ArrayList<>());
    public final List<Produto> produtos = Collections.synchronizedList(new ArrayList<>());
    public final List<Campanha> campanhas = Collections.synchronizedList(new ArrayList<>());
    public final List<Cultivo> cultivos = Collections.synchronizedList(new ArrayList<>());
    public final List<Colheita> colheitas = Collections.synchronizedList(new ArrayList<>());
    public final List<Venda> vendas = Collections.synchronizedList(new ArrayList<>());

    /** Um contador sequencial atómico por entidade (RN-f). */
    private final Map<String, AtomicInteger> contadores = new HashMap<>();
    public final AtomicInteger facturas = new AtomicInteger(1);

    public int proximoId(String entidade) {
        return contadores.computeIfAbsent(entidade, k -> new AtomicInteger(1)).getAndIncrement();
    }
}
