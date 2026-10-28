package mz.agrogest.servlet;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;
import mz.agrogest.service.*;

@WebServlet("/admin/produtos/*")
public class AdminProdutosServlet extends BaseServlet {
    @Override
    protected void doGet(HttpServletRequest r, HttpServletResponse s) throws ServletException, IOException {
        r.setAttribute("produtos", CatalogoService.produtos(null, false, r.getParameter("ordem"), r.getParameter("dir")));
        r.setAttribute("fornecedoresLista", CatalogoService.fornecedores("nome", "asc"));
        r.setAttribute("fornecedores", CatalogoService.nomesFornecedores());
        r.setAttribute("categorias", new String[]{"SEMENTE", "FERTILIZANTE", "MAQUINARIA", "FERRAMENTA"});
        vista(r, s, "admin/produtos.jsp");
    }

    @Override
    protected void doPost(HttpServletRequest r, HttpServletResponse s) throws IOException {
        switch (accao(r)) {
            case "/repor" -> tentar(r, s, "/admin/produtos", "Stock reposto.",
                    () -> AdminService.reporStock(inteiro(r, "id"), r.getParameter("quantidade")));
            case "/remover" -> tentar(r, s, "/admin/produtos", "Produto removido.", () -> AdminService.removerProduto(inteiro(r, "id")));
            default -> tentar(r, s, "/admin/produtos", "Produto criado.", () -> AdminService.criarProduto(
                    r.getParameter("nome"), r.getParameter("categoria"), r.getParameter("fornecedorId"), r.getParameter("preco"),
                    r.getParameter("quantidade"), r.getParameter("unidade"), r.getParameter("descricao"),
                    r.getParameter("fotoUrl"), r.getParameter("validade")));
        }
    }
}
