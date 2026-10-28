package mz.agrogest.service;

import java.util.Comparator;

/** Ajuda para aplicar o sentido (asc/desc) a comparadores das listagens. */
public final class Ordenacao {
    private Ordenacao() {}
    public static <T> Comparator<T> sentido(Comparator<T> c, String dir) {
        return "desc".equalsIgnoreCase(dir) ? c.reversed() : c;
    }
    public static String normalizar(String v, String... validos) {
        if (v != null) for (String s : validos) if (s.equalsIgnoreCase(v)) return s;
        return validos[0];
    }
}
