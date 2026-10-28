package mz.agrogest.servlet;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;
import mz.agrogest.service.RelatorioService;

@WebServlet("/admin/relatorios")
public class AdminRelatoriosServlet extends BaseServlet {
    @Override
    protected void doGet(HttpServletRequest r, HttpServletResponse s) throws ServletException, IOException {
        r.setAttribute("porNome", RelatorioService.clientesPorNome());
        r.setAttribute("compraram", RelatorioService.clientesQueCompraram());
        r.setAttribute("assiduos", RelatorioService.clientesAssiduos());
        r.setAttribute("semCompras", RelatorioService.clientesSemCompras());
        vista(r, s, "admin/relatorios.jsp");
    }
}
