package mz.agrogest.servlet;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;
import mz.agrogest.service.*;

/** Listagens com ordenação: /admin/listagens/{produtos,fornecedores,clientes,armazem,compras}. */
@WebServlet("/admin/listagens/*")
public class AdminListagensServlet extends BaseServlet {
    @Override
    protected void doGet(HttpServletRequest r, HttpServletResponse s) throws ServletException, IOException {
        String o = r.getParameter("ordem"), d = r.getParameter("dir");
        String tipo = accao(r).isEmpty() ? "/produtos" : accao(r);
        r.setAttribute("tipo", tipo.substring(1));
        r.setAttribute("ordem", o == null ? "" : o);
        r.setAttribute("dir", "desc".equalsIgnoreCase(d) ? "desc" : "asc");
        switch (tipo) {
            case "/produtos" -> { r.setAttribute("produtos", CatalogoService.produtos(null, false, o, d)); r.setAttribute("fornecedores", CatalogoService.nomesFornecedores()); }
            case "/armazem" -> { r.setAttribute("produtos", CatalogoService.produtos(null, true, o, d)); r.setAttribute("fornecedores", CatalogoService.nomesFornecedores()); }
            case "/fornecedores" -> r.setAttribute("fornecedoresLista", CatalogoService.fornecedores(o, d));
            case "/clientes" -> r.setAttribute("clientes", UtilizadorService.clientes(o, d));
            case "/compras" -> r.setAttribute("vendas", VendaService.todasVendas(o, d));
            default -> { s.sendError(404); return; }
        }
        vista(r, s, "admin/listagens.jsp");
    }
}
