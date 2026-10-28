package mz.agrogest.servlet;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;
import mz.agrogest.service.VendaService;

@WebServlet("/minhas-compras")
public class MinhasComprasServlet extends BaseServlet {
    @Override
    protected void doGet(HttpServletRequest r, HttpServletResponse s) throws ServletException, IOException {
        r.setAttribute("vendas", VendaService.vendasDoCliente(utilizador(r).getId()));
        vista(r, s, "cliente/minhas-compras.jsp");
    }
}
