package mz.agrogest.service;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;
import mz.agrogest.model.*;
import mz.agrogest.repo.Repositorio;

/** Relatórios de clientes e vendas. */
public final class RelatorioService {
    private RelatorioService() {}

    private static List<ResumoCliente> resumos() {
        synchronized (Repositorio.LOCK) {
            List<ResumoCliente> r = new ArrayList<>();
            for (Utilizador u : Repositorio.estado().utilizadores) {
                if (u.isAdmin()) continue;
                int n = 0; BigDecimal vol = BigDecimal.ZERO;
                for (Venda v : Repositorio.estado().vendas) if (v.getClienteId() == u.getId()) { n++; vol = vol.add(v.getTotal()); }
                r.add(new ResumoCliente(u, n, vol));
            }
            return r;
        }
    }

    /** Clientes ordenados por nome (crescente). */
    public static List<ResumoCliente> clientesPorNome() {
        List<ResumoCliente> r = resumos();
        r.sort(Comparator.comparing(x -> x.getCliente().getNome(), String.CASE_INSENSITIVE_ORDER));
        return r;
    }

    /** Clientes que compraram, ordenados por volume (valor total) decrescente. */
    public static List<ResumoCliente> clientesQueCompraram() {
        List<ResumoCliente> r = resumos().stream().filter(x -> x.getNumeroCompras() > 0).collect(Collectors.toList());
        r.sort(Comparator.comparing(ResumoCliente::getVolume).reversed());
        return r;
    }

    /** Ranking de clientes mais assíduos (número de compras, desempate por volume). */
    public static List<ResumoCliente> clientesAssiduos() {
        List<ResumoCliente> r = resumos().stream().filter(x -> x.getNumeroCompras() > 0).collect(Collectors.toList());
        r.sort(Comparator.comparingInt(ResumoCliente::getNumeroCompras).thenComparing(ResumoCliente::getVolume).reversed());
        return r;
    }

    /** Extra: clientes sem compras. */
    public static List<ResumoCliente> clientesSemCompras() {
        List<ResumoCliente> r = resumos().stream().filter(x -> x.getNumeroCompras() == 0).collect(Collectors.toList());
        r.sort(Comparator.comparing(x -> x.getCliente().getNome(), String.CASE_INSENSITIVE_ORDER));
        return r;
    }
}
