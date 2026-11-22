package utils;

import java.util.Scanner;

/**
 * UTILS: leitura segura de dados digitados no terminal.
 * Centraliza o Scanner e as validações de entrada (texto vazio, opção inválida).
 */
public class EntradaUtil {

    private final Scanner scanner;

    public EntradaUtil(Scanner scanner) {
        this.scanner = scanner;
    }

    /** Lê uma linha qualquer (pode ser vazia). */
    public String lerLinha(String mensagem) {
        System.out.print(mensagem);
        System.out.flush();
        if (!scanner.hasNextLine()) {
            throw new IllegalStateException("Entrada encerrada.");
        }
        return scanner.nextLine();
    }

    /** Lê um texto e repete a pergunta enquanto o usuário não digitar nada. */
    public String lerTextoNaoVazio(String mensagem) {
        String texto = lerLinha(mensagem);
        while (texto.trim().isEmpty()) {
            System.out.println("  [!] O valor não pode ser vazio. Tente novamente.");
            texto = lerLinha(mensagem);
        }
        return texto;
    }

    /** Lê uma opção inteira entre minimo e maximo (inclusive). */
    public int lerOpcao(String mensagem, int minimo, int maximo) {
        while (true) {
            String linha = lerLinha(mensagem).trim();

            // ENTER sozinho no menu não deve ser considerado uma opção inválida.
            if (linha.isEmpty()) {
                continue;
            }

            try {
                int valor = Integer.parseInt(linha);
                if (valor >= minimo && valor <= maximo) {
                    return valor;
                }
            } catch (NumberFormatException e) {
                // cai na mensagem abaixo
            }
            System.out.println("  [!] Opção inválida. Digite um número de " + minimo + " a " + maximo + ".");
        }
    }

    public void aguardarEnter() {
        while (true) {
            String linha = lerLinha("\nPressione ENTER para voltar ao menu...").trim();
            if (linha.isEmpty()) {
                return;
            }
        }
    }
}
