package service;

import exception.CriptografiaException;
import model.ResultadoCriptografia;
import utils.ConversorUtil;

import javax.crypto.AEADBadTagException;
import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.GCMParameterSpec;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.GeneralSecurityException;
import java.security.SecureRandom;
import java.util.Arrays;

/**
 * SERVICE: Criptografia SIMÉTRICA com AES-256 no modo GCM.
 *
 * Fluxo de criptografar:
 *   1. Gera um SALT aleatório (16 bytes) e um IV aleatório (12 bytes).
 *   2. Deriva a chave AES de 256 bits a partir da senha digitada usando
 *      PBKDF2-HMAC-SHA256 (65.536 iterações) + salt.
 *   3. Cifra a mensagem com AES-GCM (que também gera uma "tag" de autenticação).
 *   4. Empacota  salt | iv | (texto cifrado + tag)  e converte para Base64.
 *
 * Fluxo de descriptografar: o inverso. Lê salt e iv do pacote, deriva a MESMA chave
 * a partir da senha e decifra. Se a senha estiver errada (ou o texto foi alterado),
 * a verificação da tag falha e nenhuma mensagem é devolvida.
 */
public class CriptografiaService {

    private static final String TRANSFORMACAO = "AES/GCM/NoPadding";
    private static final String ALGORITMO_CHAVE = "PBKDF2WithHmacSHA256";
    private static final int TAMANHO_SALT = 16;          // bytes
    private static final int TAMANHO_IV = 12;            // bytes (padrão recomendado para GCM)
    private static final int TAMANHO_TAG_BITS = 128;     // tag de autenticação do GCM
    private static final int ITERACOES = 65536;          // custo do PBKDF2
    private static final int TAMANHO_CHAVE_BITS = 256;   // AES-256
    private static final int TAMANHO_MINIMO_PACOTE = TAMANHO_SALT + TAMANHO_IV + (TAMANHO_TAG_BITS / 8);

    private final SecureRandom random = new SecureRandom();

    /** Criptografa a mensagem com a chave (senha) informada. */
    public ResultadoCriptografia criptografar(String mensagem, String chave) throws CriptografiaException {
        validar(mensagem, "A mensagem");
        validar(chave, "A chave");
        try {
            byte[] salt = gerarBytesAleatorios(TAMANHO_SALT);
            byte[] iv = gerarBytesAleatorios(TAMANHO_IV);
            SecretKey chaveAes = derivarChave(chave, salt);

            Cipher cifra = Cipher.getInstance(TRANSFORMACAO);
            cifra.init(Cipher.ENCRYPT_MODE, chaveAes, new GCMParameterSpec(TAMANHO_TAG_BITS, iv));
            byte[] cifrado = cifra.doFinal(mensagem.getBytes(StandardCharsets.UTF_8));

            byte[] pacote = ConversorUtil.concatenar(salt, iv, cifrado);

            return new ResultadoCriptografia(
                    mensagem,
                    chave,
                    ConversorUtil.bytesParaHex(salt),
                    ConversorUtil.bytesParaHex(iv),
                    ConversorUtil.bytesParaHex(chaveAes.getEncoded()),
                    ConversorUtil.bytesParaBase64(pacote));
        } catch (GeneralSecurityException e) {
            throw new CriptografiaException("Falha ao criptografar: " + e.getMessage(), e);
        }
    }

    /** Descriptografa um texto em Base64 (gerado por criptografar) usando a chave. */
    public String descriptografar(String textoCifradoBase64, String chave) throws CriptografiaException {
        validar(textoCifradoBase64, "O texto criptografado");
        validar(chave, "A chave");

        byte[] pacote;
        try {
            pacote = ConversorUtil.base64ParaBytes(textoCifradoBase64);
        } catch (IllegalArgumentException e) {
            throw new CriptografiaException("O texto criptografado não é um Base64 válido.", e);
        }
        if (pacote.length < TAMANHO_MINIMO_PACOTE) {
            throw new CriptografiaException("O texto criptografado está incompleto ou corrompido.");
        }

        try {
            // Separa o pacote nas três partes: salt | iv | cifrado+tag
            byte[] salt = Arrays.copyOfRange(pacote, 0, TAMANHO_SALT);
            byte[] iv = Arrays.copyOfRange(pacote, TAMANHO_SALT, TAMANHO_SALT + TAMANHO_IV);
            byte[] cifrado = Arrays.copyOfRange(pacote, TAMANHO_SALT + TAMANHO_IV, pacote.length);

            SecretKey chaveAes = derivarChave(chave, salt);
            Cipher cifra = Cipher.getInstance(TRANSFORMACAO);
            cifra.init(Cipher.DECRYPT_MODE, chaveAes, new GCMParameterSpec(TAMANHO_TAG_BITS, iv));
            byte[] original = cifra.doFinal(cifrado);

            return new String(original, StandardCharsets.UTF_8);
        } catch (AEADBadTagException e) {
            throw new CriptografiaException("Chave incorreta ou texto criptografado alterado.", e);
        } catch (GeneralSecurityException e) {
            throw new CriptografiaException("Falha ao descriptografar: " + e.getMessage(), e);
        }
    }

    /** Transforma a senha digitada em uma chave AES de 256 bits (PBKDF2 + salt). */
    private SecretKey derivarChave(String senha, byte[] salt) throws GeneralSecurityException {
        PBEKeySpec spec = new PBEKeySpec(senha.toCharArray(), salt, ITERACOES, TAMANHO_CHAVE_BITS);
        try {
            SecretKeyFactory fabrica = SecretKeyFactory.getInstance(ALGORITMO_CHAVE);
            byte[] bytesChave = fabrica.generateSecret(spec).getEncoded();
            return new SecretKeySpec(bytesChave, "AES");
        } finally {
            spec.clearPassword(); // apaga a senha da memória
        }
    }

    private byte[] gerarBytesAleatorios(int tamanho) {
        byte[] bytes = new byte[tamanho];
        random.nextBytes(bytes);
        return bytes;
    }

    private void validar(String valor, String nomeCampo) throws CriptografiaException {
        if (valor == null || valor.trim().isEmpty()) {
            throw new CriptografiaException(nomeCampo + " não pode ser vazio(a).");
        }
    }
}
