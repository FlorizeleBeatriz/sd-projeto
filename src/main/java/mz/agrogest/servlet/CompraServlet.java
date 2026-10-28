package mz.agrogest.servlet;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;
import java.util.List;
import mz.agrogest.model.Venda;
import mz.agrogest.service.*;

/** GET /compra: resumo para confirmação. POST /compra: confirma, desconta stock e emite factura. */
@WebServlet("/compra")
public class CompraServlet extends BaseServlet {
    @Override
    protected void doGet(HttpServletRequest r, HttpServletResponse s) throws ServletException, IOException {
        List<LinhaCarrinho> linhas = CarrinhoService.linhas(CarrinhoServlet.carrinho(r));
        if (linhas.isEmpty()) { erro(r, "O carrinho está vazio."); redirecionar(r, s, "/catalogo"); return; }
        r.setAttribute("linhas", linhas);
        java.math.BigDecimal sub = CarrinhoService.subtotal(linhas).setScale(2, java.math.RoundingMode.HALF_UP);
        java.math.BigDecimal iva = sub.multiply(mz.agrogest.model.Venda.TAXA_IVA).setScale(2, java.math.RoundingMode.HALF_UP);
        r.setAttribute("subtotal", sub);
        r.setAttribute("iva", iva);
        r.setAttribute("total", sub.add(iva));
        vista(r, s, "cliente/compra.jsp");
    }

    @Override
    protected void doPost(HttpServletRequest r, HttpServletResponse s) throws IOException {
        try {
            Venda v = VendaService.comprar(utilizador(r), CarrinhoServlet.carrinho(r));
            CarrinhoServlet.carrinho(r).clear();
            ok(r, "Compra concluída. Factura " + v.getNumeroFactura() + " emitida.");
            redirecionar(r, s, "/factura?id=" + v.getId());
        } catch (IllegalArgumentException | IllegalStateException ex) {
            erro(r, ex.getMessage());
            redirecionar(r, s, "/carrinho");
        }
    }
}
