package mz.agrogest.servlet;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;
import mz.agrogest.repo.Repositorio;
import mz.agrogest.service.AdminService;

@WebServlet("/admin/dashboard")
public class AdminDashboardServlet extends BaseServlet {
    @Override
    protected void doGet(HttpServletRequest r, HttpServletResponse s) throws ServletException, IOException {
        r.setAttribute("contadores", AdminService.contadores());
        r.setAttribute("ficheiroExiste", Repositorio.existeFicheiro());
        vista(r, s, "admin/dashboard.jsp");
    }
}
