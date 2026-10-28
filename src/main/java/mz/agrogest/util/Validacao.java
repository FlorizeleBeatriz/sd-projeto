package mz.agrogest.util;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.Set;
import java.util.regex.Pattern;

/** Validação e conversão de parâmetros de formulário. Lança IllegalArgumentException com mensagem em português. */
public final class Validacao {
    public static final Set<String> TIPOS_FORNECEDOR = Set.of("SEMENTES", "MAQUINARIA", "FERTILIZANTES");
    public static final Set<String> CATEGORIAS = Set.of("SEMENTE", "FERTILIZANTE", "MAQUINARIA", "FERRAMENTA");
    private static final Pattern EMAIL = Pattern.compile("^[\\w.+-]+@[\\w-]+(\\.[\\w-]+)+$");

    private Validacao() {}

    public static String texto(String v, String campo) {
        if (v == null || v.isBlank()) throw new IllegalArgumentException("O campo '" + campo + "' é obrigatório.");
        return v.trim();
    }

    public static String textoOpcional(String v) { return v == null ? "" : v.trim(); }

    public static String email(String v) {
        String e = texto(v, "email").toLowerCase();
        if (!EMAIL.matcher(e).matches()) throw new IllegalArgumentException("Email inválido.");
        return e;
    }

    public static int inteiro(String v, String campo) {
        try { return Integer.parseInt(texto(v, campo)); }
        catch (NumberFormatException ex) { throw new IllegalArgumentException("O campo '" + campo + "' deve ser um número inteiro."); }
    }

    public static BigDecimal decimal(String v, String campo) {
        try { return new BigDecimal(texto(v, campo).replace(',', '.')); }
        catch (NumberFormatException ex) { throw new IllegalArgumentException("O campo '" + campo + "' deve ser numérico."); }
    }

    public static LocalDate data(String v, String campo) {
        try { return LocalDate.parse(texto(v, campo)); }
        catch (DateTimeParseException ex) { throw new IllegalArgumentException("Data inválida em '" + campo + "'."); }
    }

    public static LocalDate dataOpcional(String v, String campo) {
        return (v == null || v.isBlank()) ? null : data(v, campo);
    }

    public static String enumerado(String v, Set<String> validos, String campo) {
        String t = texto(v, campo).toUpperCase();
        if (!validos.contains(t)) throw new IllegalArgumentException("Valor inválido em '" + campo + "'. Permitidos: " + validos);
        return t;
    }

    /** Aceita apenas URLs http(s) ou vazio, evitando esquemas perigosos (javascript:). */
    public static String urlOpcional(String v) {
        String t = textoOpcional(v);
        if (!t.isEmpty() && !t.matches("(?i)^https?://\\S+$")) throw new IllegalArgumentException("URL da foto inválida (use http:// ou https://).");
        return t;
    }
}
