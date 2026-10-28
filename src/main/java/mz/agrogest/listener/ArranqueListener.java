package mz.agrogest.listener;

import jakarta.servlet.ServletContextEvent;
import jakarta.servlet.ServletContextListener;
import jakarta.servlet.annotation.WebListener;
import mz.agrogest.repo.Repositorio;
import mz.agrogest.service.DadosDemo;

/** Restaura o estado ao arrancar o Tomcat e grava-o ao encerrar. */
@WebListener
public class ArranqueListener implements ServletContextListener {
    @Override
    public void contextInitialized(ServletContextEvent e) {
        boolean restaurado = Repositorio.carregar();
        Repositorio.inicializarAdmin();
        if (!restaurado) DadosDemo.carregarSeVazio();
        e.getServletContext().log("AgroGest iniciado. Estado restaurado do ficheiro: " + restaurado + " (" + Repositorio.ficheiro() + ")");
    }

    @Override
    public void contextDestroyed(ServletContextEvent e) {
        try { Repositorio.guardar(); e.getServletContext().log("AgroGest: estado gravado no encerramento."); }
        catch (Exception ex) { e.getServletContext().log("AgroGest: falha ao gravar no encerramento", ex); }
    }
}
