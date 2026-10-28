package mz.agrogest.filter;

import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.*;
import java.io.IOException;
import mz.agrogest.model.Utilizador;

/**
 * Controlo de acesso por sessão e perfil:
 *  - rotas públicas: /, /login, /registo, /css/*
 *  - /admin/*  : só ADMIN (HTTP 403 para outros perfis; anónimos são enviados ao login)
 *  - /catalogo, /carrinho/*, /compra, /factura, /minhas-compras, /logout : exigem sessão
 *  - /carrinho/* e /compra : só CLIENTE (o administrador não compra)
 */
@WebFilter("/*")
public class AutorizacaoFilter implements Filter {
    @Override
    public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest r = (HttpServletRequest) req;
        HttpServletResponse s = (HttpServletResponse) res;
        String p = r.getRequestURI().substring(r.getContextPath().length());

        s.setHeader("X-Content-Type-Options", "nosniff");
        s.setHeader("X-Frame-Options", "SAMEORIGIN");
        s.setHeader("Referrer-Policy", "same-origin");

        if (publica(p)) { chain.doFilter(req, res); return; }

        HttpSession sessao = r.getSession(false);
        Utilizador u = sessao == null ? null : (Utilizador) sessao.getAttribute("utilizador");
        if (u == null) { s.sendRedirect(r.getContextPath() + "/login"); return; }

        s.setHeader("Cache-Control", "no-store");
        if (p.startsWith("/admin") && !u.isAdmin()) { s.sendError(403, "Acesso reservado ao administrador."); return; }
        if ((p.equals("/compra") || p.startsWith("/carrinho")) && u.isAdmin()) {
            s.sendError(403, "O administrador não efectua compras."); return;
        }
        chain.doFilter(req, res);
    }

    private boolean publica(String p) {
        return p.isEmpty() || p.equals("/") || p.equals("/index.jsp") || p.equals("/login") || p.equals("/registo")
                || p.startsWith("/css/") || p.equals("/favicon.ico");
    }
}
