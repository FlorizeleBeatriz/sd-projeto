package mz.agrogest.servlet;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;
import mz.agrogest.service.AdminService;

@WebServlet("/admin/cultivos/*")
public class AdminCultivosServlet extends BaseServlet {
    @Override
    protected void doGet(HttpServletRequest r, HttpServletResponse s) throws ServletException, IOException {
        r.setAttribute("cultivos", AdminService.cultivos());
        r.setAttribute("campanhas", AdminService.campanhas());
        r.setAttribute("nomesCampanhas", AdminService.nomesCampanhas());
        vista(r, s, "admin/cultivos.jsp");
    }

    @Override
    protected void doPost(HttpServletRequest r, HttpServletResponse s) throws IOException {
        if (accao(r).equals("/remover"))
            tentar(r, s, "/admin/cultivos", "Cultivo removido.", () -> AdminService.removerCultivo(inteiro(r, "id")));
        else
            tentar(r, s, "/admin/cultivos", "Cultivo registado.", () -> AdminService.criarCultivo(
                    r.getParameter("campanhaId"), r.getParameter("cultura"), r.getParameter("variedade"),
                    r.getParameter("hectares"), r.getParameter("plantio")));
    }
}
