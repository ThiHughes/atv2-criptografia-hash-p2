package service;

import exception.CriptografiaException;
import model.ComparacaoHash;
import model.ResultadoHash;
import utils.ConversorUtil;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * SERVICE: Geração e comparação de hash SHA-256.
 *
 * SHA-256 é uma função hash: recebe uma entrada de qualquer tamanho e devolve
 * SEMPRE 256 bits (32 bytes = 64 caracteres hexadecimais). Não tem chave e
 * não tem "volta": não existe descriptografar um hash.
 */
public class HashService {

    private static final String ALGORITMO = "SHA-256";

    /** Gera o hash SHA-256 de um texto usando a classe MessageDigest do Java. */
    public ResultadoHash gerarSha256(String texto) throws CriptografiaException {
        if (texto == null) {
            throw new CriptografiaException("A mensagem não pode ser nula.");
        }
        try {
            MessageDigest digest = MessageDigest.getInstance(ALGORITMO);
            // 1) texto -> bytes (UTF-8)   2) digest() aplica o SHA-256 e devolve 32 bytes
            byte[] hash = digest.digest(texto.getBytes(StandardCharsets.UTF_8));
            return new ResultadoHash(texto, hash, ConversorUtil.bytesParaHex(hash));
        } catch (NoSuchAlgorithmException e) {
            throw new CriptografiaException("Algoritmo " + ALGORITMO + " indisponível nesta JVM.", e);
        }
    }

    /** Compara dois hashes e calcula quantos bits diferem (efeito avalanche). */
    public ComparacaoHash comparar(ResultadoHash primeiro, ResultadoHash segundo) {
        byte[] a = primeiro.getHashBytes();
        byte[] b = segundo.getHashBytes();

        boolean iguais = MessageDigest.isEqual(a, b);
        int bitsDiferentes = 0;
        for (int i = 0; i < a.length; i++) {
            // XOR resulta em 1 apenas nos bits diferentes; bitCount conta os bits 1.
            bitsDiferentes += Integer.bitCount((a[i] ^ b[i]) & 0xFF);
        }
        return new ComparacaoHash(primeiro, segundo, iguais, bitsDiferentes);
    }
}
