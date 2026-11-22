package model;

/**
 * MODEL: guarda todos os dados de uma operação de criptografia.
 */
public class ResultadoCriptografia {

    private final String mensagemOriginal;
    private final String chaveInformada;
    private final String saltHex;
    private final String ivHex;
    private final String chaveDerivadaHex;
    private final String textoCifradoBase64;

    public ResultadoCriptografia(String mensagemOriginal, String chaveInformada, String saltHex,
                                 String ivHex, String chaveDerivadaHex, String textoCifradoBase64) {
        this.mensagemOriginal = mensagemOriginal;
        this.chaveInformada = chaveInformada;
        this.saltHex = saltHex;
        this.ivHex = ivHex;
        this.chaveDerivadaHex = chaveDerivadaHex;
        this.textoCifradoBase64 = textoCifradoBase64;
    }

    public String getMensagemOriginal() { return mensagemOriginal; }
    public String getChaveInformada() { return chaveInformada; }
    public String getSaltHex() { return saltHex; }
    public String getIvHex() { return ivHex; }
    public String getChaveDerivadaHex() { return chaveDerivadaHex; }
    public String getTextoCifradoBase64() { return textoCifradoBase64; }
}
