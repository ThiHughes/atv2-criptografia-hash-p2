package utils;

import java.util.Base64;

/**
 * UTILS: conversões entre bytes e representações em texto.
 * Algoritmos de criptografia/hash trabalham com bytes; para mostrar na tela
 * precisamos convertê-los para Hexadecimal ou Base64.
 */
public final class ConversorUtil {

    private ConversorUtil() { } // classe utilitária: não se instancia

    /** Converte bytes em hexadecimal (cada byte vira 2 caracteres, ex.: 255 -> "ff"). */
    public static String bytesParaHex(byte[] bytes) {
        StringBuilder sb = new StringBuilder(bytes.length * 2);
        for (byte b : bytes) {
            sb.append(String.format("%02x", b)); // %02x = hexadecimal com 2 dígitos
        }
        return sb.toString();
    }

    /** Converte bytes em Base64 (texto seguro para copiar/colar). */
    public static String bytesParaBase64(byte[] bytes) {
        return Base64.getEncoder().encodeToString(bytes);
    }

    /** Converte Base64 de volta para bytes. Lança IllegalArgumentException se for inválido. */
    public static byte[] base64ParaBytes(String base64) {
        return Base64.getDecoder().decode(base64.trim());
    }

    /** Junta vários arrays de bytes em sequência (usado para montar salt + IV + cifrado). */
    public static byte[] concatenar(byte[]... partes) {
        int total = 0;
        for (byte[] p : partes) {
            total += p.length;
        }
        byte[] resultado = new byte[total];
        int posicao = 0;
        for (byte[] p : partes) {
            System.arraycopy(p, 0, resultado, posicao, p.length);
            posicao += p.length;
        }
        return resultado;
    }
}
