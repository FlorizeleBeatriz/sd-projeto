package mz.agrogest.servlet;

import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;

@WebServlet("/logout")
public class LogoutServlet extends BaseServlet {
    @Override
    protected void doGet(HttpServletRequest r, HttpServletResponse s) throws IOException { sair(r, s); }
    @Override
    protected void doPost(HttpServletRequest r, HttpServletResponse s) throws IOException { sair(r, s); }

    private void sair(HttpServletRequest r, HttpServletResponse s) throws IOException {
        HttpSession se = r.getSession(false);
        if (se != null) se.invalidate();
        redirecionar(r, s, "/login");
    }
}
