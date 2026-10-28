package mz.agrogest.servlet;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;
import java.util.*;
import mz.agrogest.service.CarrinhoService;
import mz.agrogest.service.LinhaCarrinho;

/** Carrinho em sessão: /carrinho (ver) e /carrinho/{adicionar,actualizar,remover,esvaziar} (POST). */
@WebServlet("/carrinho/*")
public class CarrinhoServlet extends BaseServlet {
    @SuppressWarnings("unchecked")
    static Map<Integer, Integer> carrinho(HttpServletRequest r) {
        HttpSession s = r.getSession();
        Map<Integer, Integer> m = (Map<Integer, Integer>) s.getAttribute("carrinho");
        if (m == null) { m = Collections.synchronizedMap(new LinkedHashMap<>()); s.setAttribute("carrinho", m); }
        return m;
    }

    @Override
    protected void doGet(HttpServletRequest r, HttpServletResponse s) throws ServletException, IOException {
        List<LinhaCarrinho> linhas = CarrinhoService.linhas(carrinho(r));
        r.setAttribute("linhas", linhas);
        r.setAttribute("subtotal", CarrinhoService.subtotal(linhas));
        vista(r, s, "cliente/carrinho.jsp");
    }

    @Override
    protected void doPost(HttpServletRequest r, HttpServletResponse s) throws IOException {
        Map<Integer, Integer> c = carrinho(r);
        switch (accao(r)) {
            case "/adicionar" -> {
                String volta = "/catalogo" + (r.getParameter("categoria") == null || r.getParameter("categoria").isBlank() ? "" : "?categoria=" + r.getParameter("categoria").replaceAll("[^A-Za-z]", ""));
                tentar(r, s, volta, "Produto adicionado ao carrinho.",
                        () -> CarrinhoService.adicionar(c, inteiro(r, "produtoId"), inteiro(r, "quantidade")));
            }
            case "/actualizar" -> tentar(r, s, "/carrinho", "Carrinho actualizado.", () -> {
                for (Integer id : new ArrayList<>(c.keySet())) {
                    String q = r.getParameter("q_" + id);
                    if (q != null) CarrinhoService.actualizar(c, id, Integer.parseInt(q.trim()));
                }
            });
            case "/remover" -> tentar(r, s, "/carrinho", "Item removido.", () -> CarrinhoService.remover(c, inteiro(r, "produtoId")));
            case "/esvaziar" -> tentar(r, s, "/carrinho", "Carrinho esvaziado.", () -> CarrinhoService.esvaziar(c));
            default -> s.sendError(404);
        }
    }
}
