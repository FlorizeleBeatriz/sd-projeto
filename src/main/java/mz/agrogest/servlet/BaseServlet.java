package mz.agrogest.servlet;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.*;
import java.io.IOException;
import mz.agrogest.model.Utilizador;

/** Funcionalidade comum aos controladores: vistas, mensagens flash (padrão Post-Redirect-Get) e tratamento de erros. */
public abstract class BaseServlet extends HttpServlet {
    @FunctionalInterface
    protected interface Accao { void executar() throws Exception; }

    protected Utilizador utilizador(HttpServletRequest r) {
        HttpSession s = r.getSession(false);
        return s == null ? null : (Utilizador) s.getAttribute("utilizador");
    }

    protected void vista(HttpServletRequest r, HttpServletResponse s, String jsp) throws ServletException, IOException {
        r.getRequestDispatcher("/WEB-INF/views/" + jsp).forward(r, s);
    }

    protected void ok(HttpServletRequest r, String msg) { r.getSession().setAttribute("flashOk", msg); }
    protected void erro(HttpServletRequest r, String msg) { r.getSession().setAttribute("flashErro", msg); }

    protected void redirecionar(HttpServletRequest r, HttpServletResponse s, String caminho) throws IOException {
        s.sendRedirect(r.getContextPath() + caminho);
    }

    /** Executa a acção; em caso de erro de validação/regra guarda a mensagem e volta ao destino. */
    protected void tentar(HttpServletRequest r, HttpServletResponse s, String destino, String sucesso, Accao a) throws IOException {
        try { a.executar(); if (sucesso != null) ok(r, sucesso); }
        catch (IllegalArgumentException | IllegalStateException ex) { erro(r, ex.getMessage()); }
        catch (Exception ex) { log("Erro inesperado", ex); erro(r, "Ocorreu um erro inesperado."); }
        redirecionar(r, s, destino);
    }

    protected int inteiro(HttpServletRequest r, String nome) {
        try { return Integer.parseInt(r.getParameter(nome)); }
        catch (Exception e) { throw new IllegalArgumentException("Parâmetro '" + nome + "' inválido."); }
    }

    /** Sub-caminho após o padrão do servlet (ex.: "/repor"), ou "" se não existir. */
    protected String accao(HttpServletRequest r) { return r.getPathInfo() == null ? "" : r.getPathInfo(); }
}
