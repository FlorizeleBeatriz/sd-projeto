package mz.agrogest.servlet;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;
import mz.agrogest.service.UtilizadorService;

@WebServlet("/registo")
public class RegistoServlet extends BaseServlet {
    @Override
    protected void doGet(HttpServletRequest r, HttpServletResponse s) throws ServletException, IOException { vista(r, s, "registo.jsp"); }

    @Override
    protected void doPost(HttpServletRequest r, HttpServletResponse s) throws ServletException, IOException {
        try {
            UtilizadorService.registar(r.getParameter("nome"), r.getParameter("email"), r.getParameter("password"),
                    r.getParameter("telefone"), r.getParameter("endereco"));
            ok(r, "Conta criada com sucesso. Já pode entrar.");
            redirecionar(r, s, "/login");
        } catch (IllegalArgumentException ex) {
            r.setAttribute("erro", ex.getMessage());
            for (String k : new String[]{"nome", "email", "telefone", "endereco"}) r.setAttribute(k, r.getParameter(k));
            vista(r, s, "registo.jsp");
        }
    }
}
