package mz.agrogest.servlet;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;
import mz.agrogest.repo.Repositorio;

/** Endpoints de persistência: GET estado; POST /gravar, /eliminar e /restaurar. */
@WebServlet("/admin/persistencia/*")
public class AdminPersistenciaServlet extends BaseServlet {
    @Override
    protected void doGet(HttpServletRequest r, HttpServletResponse s) throws ServletException, IOException {
        r.setAttribute("existe", Repositorio.existeFicheiro());
        r.setAttribute("caminho", Repositorio.ficheiro().toAbsolutePath().toString());
        r.setAttribute("tamanho", Repositorio.tamanhoFicheiro());
        r.setAttribute("ultima", Repositorio.ultimaGravacao());
        vista(r, s, "admin/persistencia.jsp");
    }

    @Override
    protected void doPost(HttpServletRequest r, HttpServletResponse s) throws IOException {
        switch (accao(r)) {
            case "/gravar" -> tentar(r, s, "/admin/persistencia", "Estado gravado em ficheiro.", Repositorio::guardar);
            case "/eliminar" -> tentar(r, s, "/admin/persistencia", null, () -> {
                ok(r, Repositorio.eliminarFicheiro() ? "Ficheiro de dados eliminado (o estado em memória mantém-se)." : "Não existia ficheiro para eliminar.");
            });
            case "/restaurar" -> tentar(r, s, "/admin/persistencia", null, () -> {
                if (Repositorio.carregar()) { Repositorio.inicializarAdmin(); ok(r, "Estado restaurado a partir do ficheiro."); }
                else erro(r, "Não foi possível restaurar: ficheiro inexistente ou inválido.");
            });
            default -> s.sendError(404);
        }
    }
}
