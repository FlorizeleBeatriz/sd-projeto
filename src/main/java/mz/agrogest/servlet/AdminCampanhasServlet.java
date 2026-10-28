package mz.agrogest.servlet;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;
import mz.agrogest.service.AdminService;

@WebServlet("/admin/campanhas/*")
public class AdminCampanhasServlet extends BaseServlet {
    @Override
    protected void doGet(HttpServletRequest r, HttpServletResponse s) throws ServletException, IOException {
        r.setAttribute("campanhas", AdminService.campanhas());
        vista(r, s, "admin/campanhas.jsp");
    }

    @Override
    protected void doPost(HttpServletRequest r, HttpServletResponse s) throws IOException {
        switch (accao(r)) {
            case "/fechar" -> tentar(r, s, "/admin/campanhas", "Campanha fechada.", () -> AdminService.definirEstadoCampanha(inteiro(r, "id"), false));
            case "/reabrir" -> tentar(r, s, "/admin/campanhas", "Campanha reaberta.", () -> AdminService.definirEstadoCampanha(inteiro(r, "id"), true));
            case "/remover" -> tentar(r, s, "/admin/campanhas", "Campanha removida.", () -> AdminService.removerCampanha(inteiro(r, "id")));
            default -> tentar(r, s, "/admin/campanhas", "Campanha criada.",
                    () -> AdminService.criarCampanha(r.getParameter("nome"), r.getParameter("inicio"), r.getParameter("fim")));
        }
    }
}
