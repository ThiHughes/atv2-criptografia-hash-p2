package exception;

/**
 * Exceção própria do projeto.
 * Concentra qualquer erro de criptografia/hash (chave errada, Base64 inválido,
 * algoritmo indisponível...) em uma única classe, com mensagem amigável ao usuário.
 */
public class CriptografiaException extends Exception {

    private static final long serialVersionUID = 1L;

    public CriptografiaException(String mensagem) {
        super(mensagem);
    }

    public CriptografiaException(String mensagem, Throwable causa) {
        super(mensagem, causa);
    }
}
