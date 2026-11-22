package model;

/**
 * MODEL: representa o hash SHA-256 de uma mensagem.
 */
public class ResultadoHash {

    private final String mensagem;
    private final byte[] hashBytes;
    private final String hashHex;

    public ResultadoHash(String mensagem, byte[] hashBytes, String hashHex) {
        this.mensagem = mensagem;
        this.hashBytes = hashBytes.clone(); // cópia defensiva
        this.hashHex = hashHex;
    }

    public String getMensagem() { return mensagem; }
    public byte[] getHashBytes() { return hashBytes.clone(); }
    public String getHashHex() { return hashHex; }
}
