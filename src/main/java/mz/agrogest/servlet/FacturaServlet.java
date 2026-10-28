package mz.agrogest.servlet;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;
import java.util.NoSuchElementException;
import mz.agrogest.service.VendaService;

@WebServlet("/factura")
public class FacturaServlet extends BaseServlet {
    @Override
    protected void doGet(HttpServletRequest r, HttpServletResponse s) throws ServletException, IOException {
        try {
            r.setAttribute("venda", VendaService.factura(inteiro(r, "id"), utilizador(r)));
            vista(r, s, "cliente/factura.jsp");
        } catch (SecurityException e) { s.sendError(403, e.getMessage()); }
        catch (NoSuchElementException e) { s.sendError(404, e.getMessage()); }
        catch (IllegalArgumentException e) { s.sendError(400, e.getMessage()); }
    }
}
