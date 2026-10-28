package mz.agrogest.servlet;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;
import mz.agrogest.service.*;

@WebServlet("/admin/fornecedores/*")
public class AdminFornecedoresServlet extends BaseServlet {
    @Override
    protected void doGet(HttpServletRequest r, HttpServletResponse s) throws ServletException, IOException {
        r.setAttribute("fornecedores", CatalogoService.fornecedores(r.getParameter("ordem"), r.getParameter("dir")));
        r.setAttribute("tipos", new String[]{"SEMENTES", "MAQUINARIA", "FERTILIZANTES"});
        vista(r, s, "admin/fornecedores.jsp");
    }

    @Override
    protected void doPost(HttpServletRequest r, HttpServletResponse s) throws IOException {
        if (accao(r).equals("/remover"))
            tentar(r, s, "/admin/fornecedores", "Fornecedor removido.", () -> AdminService.removerFornecedor(inteiro(r, "id")));
        else
            tentar(r, s, "/admin/fornecedores", "Fornecedor criado.", () -> AdminService.criarFornecedor(
                    r.getParameter("nome"), r.getParameter("tipo"), r.getParameter("contacto"), r.getParameter("provincia")));
    }
}
