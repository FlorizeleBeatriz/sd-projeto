package mz.agrogest.service;

import java.time.LocalDate;
import java.util.LinkedHashMap;
import java.util.Map;
import mz.agrogest.model.Utilizador;
import mz.agrogest.repo.Repositorio;

/**
 * Dados de demonstração (apenas se o sistema estiver vazio). Reproduz o exemplo do enunciado:
 * 3 fornecedores, produtos, 5 clientes, 4 vendas e 1 campanha. Desactivar com -Dagrogest.demo=false.
 */
public final class DadosDemo {
    private DadosDemo() {}

    public static void carregarSeVazio() {
        if ("false".equalsIgnoreCase(System.getProperty("agrogest.demo"))) return;
        synchronized (Repositorio.LOCK) {
            if (!Repositorio.estado().fornecedores.isEmpty() || !Repositorio.estado().produtos.isEmpty()) return;
            LocalDate hoje = LocalDate.now();
            AdminService.criarFornecedor("Sementes do Sul, Lda", "SEMENTES", "+258 84 111 2222", "Maputo");
            AdminService.criarFornecedor("AgroFertil Nampula", "FERTILIZANTES", "+258 86 333 4444", "Nampula");
            AdminService.criarFornecedor("MaqAgro Moçambique", "MAQUINARIA", "+258 82 555 6666", "Nampula");
            AdminService.criarProduto("Semente de Milho", "SEMENTE", "1", "180", "500", "kg", "Milho híbrido de alto rendimento.", "", hoje.plusMonths(10).toString());
            AdminService.criarProduto("Semente Feijão nhemba", "SEMENTE", "1", "320", "400", "kg", "Feijão nhemba variedade Macia.", "", hoje.plusMonths(8).toString());
            AdminService.criarProduto("Adubo NPK 12-24-12", "FERTILIZANTE", "2", "1450", "120", "saco", "Saco de 50 kg para culturas de sequeiro.", "", hoje.plusMonths(18).toString());
            AdminService.criarProduto("Ureia 46%", "FERTILIZANTE", "2", "1280", "80", "saco", "Adubo azotado de cobertura (50 kg).", "", hoje.plusMonths(18).toString());
            AdminService.criarProduto("Enxada reforçada", "FERRAMENTA", "3", "450", "60", "un", "Enxada com cabo de madeira.", "", "");
            AdminService.criarProduto("Pulverizador costal 16L", "MAQUINARIA", "3", "2900", "15", "un", "Pulverizador manual de 16 litros.", "", "");

            String[][] clientes = {
                {"João Silva", "joao@teste.mz", "+258 84 100 0001", "Av. Eduardo Mondlane, Nampula"},
                {"Maria Chaúque", "maria@teste.mz", "+258 85 100 0002", "Bairro Muhala, Nampula"},
                {"Abdul Rashid", "abdul@teste.mz", "+258 86 100 0003", "Rua do Porto, Nacala"},
                {"Ana Muianga", "ana@teste.mz", "+258 87 100 0004", "Bairro Namicopo, Nampula"},
                {"Carlos Tembe", "carlos@teste.mz", "+258 82 100 0005", "Av. 25 de Setembro, Maputo"}};
            Utilizador[] us = new Utilizador[clientes.length];
            for (int i = 0; i < clientes.length; i++)
                us[i] = UtilizadorService.registar(clientes[i][0], clientes[i][1], "1234", clientes[i][2], clientes[i][3]);

            comprar(us[0], 1, 30, 3, 2);   // João: 2 compras
            comprar(us[0], 5, 2, -1, 0);
            comprar(us[1], 2, 20, 4, 1);   // Maria
            comprar(us[2], 3, 5, 5, 3);    // Abdul   (Ana e Carlos ficam sem compras)

            AdminService.criarCampanha("Campanha 2026/2027", "2026-10-01", "2027-06-30");
            AdminService.criarCultivo("1", "Feijão nhemba", "Macia", "8", "2026-11-15");
            AdminService.criarColheita("1", "2027-03-10", "3200", "22");
            AdminService.persistir();
        }
    }

    private static void comprar(Utilizador u, int p1, int q1, int p2, int q2) {
        Map<Integer, Integer> c = new LinkedHashMap<>();
        c.put(p1, q1);
        if (p2 > 0 && q2 > 0) c.put(p2, q2);
        VendaService.comprar(u, c);
    }
}
