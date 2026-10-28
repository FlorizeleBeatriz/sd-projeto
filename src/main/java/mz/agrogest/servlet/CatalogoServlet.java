package mz.agrogest.servlet;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;
import mz.agrogest.service.CatalogoService;
import mz.agrogest.util.Validacao;

@WebServlet("/catalogo")
public class CatalogoServlet extends BaseServlet {
    @Override
    protected void doGet(HttpServletRequest r, HttpServletResponse s) throws ServletException, IOException {
        String cat = r.getParameter("categoria");
        if (cat != null && !Validacao.CATEGORIAS.contains(cat.toUpperCase())) cat = "";
        r.setAttribute("categoria", cat == null ? "" : cat.toUpperCase());
        r.setAttribute("categorias", new String[]{"SEMENTE", "FERTILIZANTE", "MAQUINARIA", "FERRAMENTA"});
        r.setAttribute("produtos", CatalogoService.produtos(cat, true, r.getParameter("ordem"), r.getParameter("dir")));
        r.setAttribute("fornecedores", CatalogoService.nomesFornecedores());
        vista(r, s, "cliente/catalogo.jsp");
    }
}
