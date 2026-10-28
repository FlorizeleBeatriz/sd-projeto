package mz.agrogest.repo;

import java.io.*;
import java.nio.file.*;
import java.time.Instant;
import mz.agrogest.model.Utilizador;
import mz.agrogest.util.Seguranca;

/**
 * Acesso ao estado em memória e persistência em ficheiro (ObjectOutputStream/ObjectInputStream).
 * A escrita é atómica: grava num ficheiro temporário e depois move-o para o destino final.
 */
public final class Repositorio {
    /** Monitor global usado pelos serviços para tornar as escritas e a venda atómicas. */
    public static final Object LOCK = new Object();
    public static final String ADMIN_EMAIL = "admin@agro.mz";
    public static final String ADMIN_PASSWORD = "admin234";

    private static EstadoSistema estado = new EstadoSistema();
    private static Path ficheiro = caminhoPadrao();

    private Repositorio() {}

    private static Path caminhoPadrao() {
        String explicito = System.getProperty("agrogest.data");
        if (explicito != null && !explicito.isBlank()) return Paths.get(explicito);
        return Paths.get(System.getProperty("catalina.base", "."), "agrogest-data.bin");
    }

    public static EstadoSistema estado() { synchronized (LOCK) { return estado; } }
    public static Path ficheiro() { return ficheiro; }

    /** Redefine o estado e o caminho do ficheiro (usado em testes). */
    public static void reiniciar(Path novoFicheiro) {
        synchronized (LOCK) { estado = new EstadoSistema(); ficheiro = novoFicheiro; }
    }

    public static void guardar() throws IOException {
        synchronized (LOCK) {
            Path tmp = ficheiro.resolveSibling(ficheiro.getFileName() + ".tmp");
            try (ObjectOutputStream out = new ObjectOutputStream(new BufferedOutputStream(Files.newOutputStream(tmp)))) {
                out.writeObject(estado);
            }
            try {
                Files.move(tmp, ficheiro, StandardCopyOption.REPLACE_EXISTING, StandardCopyOption.ATOMIC_MOVE);
            } catch (AtomicMoveNotSupportedException e) {
                Files.move(tmp, ficheiro, StandardCopyOption.REPLACE_EXISTING);
            }
        }
    }

    /** @return true se o estado foi restaurado do ficheiro. */
    public static boolean carregar() {
        synchronized (LOCK) {
            if (!Files.exists(ficheiro)) return false;
            try (ObjectInputStream in = new ObjectInputStream(new BufferedInputStream(Files.newInputStream(ficheiro)))) {
                Object o = in.readObject();
                if (o instanceof EstadoSistema) { estado = (EstadoSistema) o; return true; }
            } catch (Exception e) {
                System.err.println("[AgroGest] Falha ao restaurar estado (" + e + "). A iniciar com estado vazio.");
            }
            return false;
        }
    }

    public static boolean eliminarFicheiro() throws IOException {
        synchronized (LOCK) { return Files.deleteIfExists(ficheiro); }
    }

    public static boolean existeFicheiro() { return Files.exists(ficheiro); }
    public static long tamanhoFicheiro() {
        try { return Files.exists(ficheiro) ? Files.size(ficheiro) : 0; } catch (IOException e) { return 0; }
    }
    public static String ultimaGravacao() {
        try { return Files.exists(ficheiro) ? Instant.ofEpochMilli(Files.getLastModifiedTime(ficheiro).toMillis()).toString() : "—"; }
        catch (IOException e) { return "—"; }
    }

    /** Conta fixa do administrador, criada no arranque se ainda não existir (palavra-passe guardada como hash). */
    public static void inicializarAdmin() {
        synchronized (LOCK) {
            boolean existe = estado.utilizadores.stream().anyMatch(u -> ADMIN_EMAIL.equalsIgnoreCase(u.getEmail()));
            if (existe) return;
            String salt = Seguranca.novoSalt();
            estado.utilizadores.add(new Utilizador(estado.proximoId("utilizador"), "Administrador", ADMIN_EMAIL,
                    Seguranca.hash(ADMIN_PASSWORD, salt), salt, "", "", "ADMIN"));
        }
    }
}
