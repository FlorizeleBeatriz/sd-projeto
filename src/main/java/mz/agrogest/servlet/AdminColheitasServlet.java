package mz.agrogest.servlet;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;
import mz.agrogest.service.AdminService;

@WebServlet("/admin/colheitas/*")
public class AdminColheitasServlet extends BaseServlet {
    @Override
    protected void doGet(HttpServletRequest r, HttpServletResponse s) throws ServletException, IOException {
        r.setAttribute("colheitas", AdminService.colheitas());
        r.setAttribute("cultivos", AdminService.cultivos());
        r.setAttribute("nomesCultivos", AdminService.nomesCultivos());
        vista(r, s, "admin/colheitas.jsp");
    }

    @Override
    protected void doPost(HttpServletRequest r, HttpServletResponse s) throws IOException {
        if (accao(r).equals("/remover"))
            tentar(r, s, "/admin/colheitas", "Colheita removida.", () -> AdminService.removerColheita(inteiro(r, "id")));
        else
            tentar(r, s, "/admin/colheitas", "Colheita registada.", () -> AdminService.criarColheita(
                    r.getParameter("cultivoId"), r.getParameter("data"), r.getParameter("kg"), r.getParameter("precoKg")));
    }
}
