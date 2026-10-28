package mz.agrogest.util;

import java.security.GeneralSecurityException;
import java.security.MessageDigest;
import java.security.SecureRandom;
import java.util.Base64;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;

/** Utilitários de segurança: hash de palavras-passe com PBKDF2 + salt (nunca texto simples). */
public final class Seguranca {
    private static final int ITERACOES = 65_536;
    private static final int BITS = 256;
    private static final SecureRandom RANDOM = new SecureRandom();

    private Seguranca() {}

    public static String novoSalt() {
        byte[] s = new byte[16];
        RANDOM.nextBytes(s);
        return Base64.getEncoder().encodeToString(s);
    }

    public static String hash(String password, String salt) {
        try {
            PBEKeySpec spec = new PBEKeySpec(password.toCharArray(), Base64.getDecoder().decode(salt), ITERACOES, BITS);
            byte[] h = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256").generateSecret(spec).getEncoded();
            return Base64.getEncoder().encodeToString(h);
        } catch (GeneralSecurityException e) {
            throw new IllegalStateException("Algoritmo de hash indisponível", e);
        }
    }

    /** Comparação em tempo constante. */
    public static boolean verificar(String password, String salt, String hashGuardado) {
        if (password == null || salt == null || hashGuardado == null) return false;
        return MessageDigest.isEqual(hash(password, salt).getBytes(), hashGuardado.getBytes());
    }
}
