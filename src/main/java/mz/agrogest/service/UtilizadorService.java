package mz.agrogest.service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import mz.agrogest.model.Utilizador;
import mz.agrogest.repo.EstadoSistema;
import mz.agrogest.repo.Repositorio;
import mz.agrogest.util.Seguranca;
import mz.agrogest.util.Validacao;

/** Autenticação e auto-registo de clientes. */
public final class UtilizadorService {
    private UtilizadorService() {}

    public static Utilizador autenticar(String email, String password) {
        if (email == null || password == null) return null;
        synchronized (Repositorio.LOCK) {
            for (Utilizador u : Repositorio.estado().utilizadores)
                if (u.getEmail().equalsIgnoreCase(email.trim()) && Seguranca.verificar(password, u.getSalt(), u.getPasswordHash()))
                    return u;
            return null;
        }
    }

    public static Utilizador registar(String nome, String email, String password, String telefone, String endereco) {
        nome = Validacao.texto(nome, "nome");
        String mail = Validacao.email(email);
        telefone = Validacao.texto(telefone, "telefone");
        endereco = Validacao.texto(endereco, "endereço");
        if (password == null || password.length() < 4) throw new IllegalArgumentException("A palavra-passe deve ter pelo menos 4 caracteres.");
        synchronized (Repositorio.LOCK) {
            EstadoSistema e = Repositorio.estado();
            if (e.utilizadores.stream().anyMatch(u -> u.getEmail().equalsIgnoreCase(mail)))
                throw new IllegalArgumentException("Já existe uma conta com este email.");
            String salt = Seguranca.novoSalt();
            Utilizador u = new Utilizador(e.proximoId("utilizador"), nome, mail, Seguranca.hash(password, salt), salt, telefone, endereco, "CLIENTE");
            e.utilizadores.add(u);
            AdminService.persistir();
            return u;
        }
    }

    public static List<Utilizador> clientes(String ordem, String dir) {
        ordem = Ordenacao.normalizar(ordem, "nome", "email");
        Comparator<Utilizador> c = ordem.equals("email")
                ? Comparator.comparing(Utilizador::getEmail, String.CASE_INSENSITIVE_ORDER)
                : Comparator.comparing(Utilizador::getNome, String.CASE_INSENSITIVE_ORDER);
        synchronized (Repositorio.LOCK) {
            List<Utilizador> r = new ArrayList<>();
            for (Utilizador u : Repositorio.estado().utilizadores) if (!u.isAdmin()) r.add(u);
            r.sort(Ordenacao.sentido(c, dir));
            return r;
        }
    }
}
