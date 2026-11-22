package model;

/**
 * MODEL: resultado da comparação entre dois hashes.
 * Além de dizer se são iguais, guarda quantos bits mudaram (efeito avalanche).
 */
public class ComparacaoHash {

    public static final int TOTAL_BITS_SHA256 = 256;

    private final ResultadoHash primeiro;
    private final ResultadoHash segundo;
    private final boolean iguais;
    private final int bitsDiferentes;

    public ComparacaoHash(ResultadoHash primeiro, ResultadoHash segundo, boolean iguais, int bitsDiferentes) {
        this.primeiro = primeiro;
        this.segundo = segundo;
        this.iguais = iguais;
        this.bitsDiferentes = bitsDiferentes;
    }

    public ResultadoHash getPrimeiro() { return primeiro; }
    public ResultadoHash getSegundo() { return segundo; }
    public boolean saoIguais() { return iguais; }
    public int getBitsDiferentes() { return bitsDiferentes; }

    /** Percentual de bits que mudaram (em SHA-256 o esperado é perto de 50%). */
    public double getPercentualBitsDiferentes() {
        return (bitsDiferentes * 100.0) / TOTAL_BITS_SHA256;
    }
}
