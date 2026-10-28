package mz.agrogest.servlet;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;
import mz.agrogest.model.Utilizador;
import mz.agrogest.service.UtilizadorService;

@WebServlet("/login")
public class LoginServlet extends BaseServlet {
    @Override
    protected void doGet(HttpServletRequest r, HttpServletResponse s) throws ServletException, IOException {
        Utilizador u = utilizador(r);
        if (u != null) { redirecionar(r, s, u.isAdmin() ? "/admin/dashboard" : "/catalogo"); return; }
        vista(r, s, "login.jsp");
    }

    @Override
    protected void doPost(HttpServletRequest r, HttpServletResponse s) throws ServletException, IOException {
        Utilizador u = UtilizadorService.autenticar(r.getParameter("email"), r.getParameter("password"));
        if (u == null) {
            r.setAttribute("erro", "Email ou palavra-passe inválidos.");
            r.setAttribute("email", r.getParameter("email"));
            vista(r, s, "login.jsp");
            return;
        }
        HttpSession antiga = r.getSession(false);
        if (antiga != null) antiga.invalidate();          // evita fixação de sessão
        r.getSession(true).setAttribute("utilizador", u);
        redirecionar(r, s, u.isAdmin() ? "/admin/dashboard" : "/catalogo");
    }
}
