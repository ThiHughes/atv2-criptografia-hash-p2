package view;

import exception.CriptografiaException;
import model.ComparacaoHash;
import model.ResultadoCriptografia;
import model.ResultadoHash;
import service.CriptografiaService;
import service.HashService;
import utils.EntradaUtil;

import java.util.Locale;

/**
 * VIEW: interface em modo texto (terminal).
 * Só cuida de mostrar menus e ler dados; toda a lógica de criptografia e hash
 * fica nos services.
 */
public class MenuPrincipal {

    private static final String LINHA = "============================================================";
    private static final String SUBLINHA = "------------------------------------------------------------";

    private final EntradaUtil entrada;
    private final CriptografiaService criptografiaService;
    private final HashService hashService;

    public MenuPrincipal(EntradaUtil entrada, CriptografiaService criptografiaService, HashService hashService) {
        this.entrada = entrada;
        this.criptografiaService = criptografiaService;
        this.hashService = hashService;
    }

    /** Laço principal: exibe o menu até o usuário escolher sair. */
    public void iniciar() {
        boolean executando = true;
        while (executando) {
            exibirMenu();
            int opcao = entrada.lerOpcao("Escolha uma opção: ", 0, 4);
            System.out.println();
            switch (opcao) {
                case 1:
                    atividadeCriptografia();
                    break;
                case 2:
                    atividadeHash();
                    break;
                case 3:
                    descriptografarTextoInformado();
                    break;
                case 4:
                    exibirSobreAlgoritmos();
                    break;
                default:
                    executando = false;
                    break;
            }
            if (executando) {
                entrada.aguardarEnter();
            }
        }
        System.out.println("Encerrando o programa. Até logo!");
    }

    private void exibirMenu() {
        System.out.println();
        System.out.println(LINHA);
        System.out.println("   CRIPTOGRAFIA E HASH - Estrutura de Dados II");
        System.out.println(LINHA);
        System.out.println(" 1 - Criptografar e descriptografar mensagem (AES-256-GCM)");
        System.out.println(" 2 - Gerar e comparar hash (SHA-256)");
        System.out.println(" 3 - Descriptografar um texto cifrado já existente");
        System.out.println(" 4 - Sobre os algoritmos utilizados");
        System.out.println(" 0 - Sair");
        System.out.println(LINHA);
    }

    // ------------------------------------------------------------------
    // ATIVIDADE 1 - Criptografia e descriptografia
    // ------------------------------------------------------------------
    private void atividadeCriptografia() {
        System.out.println(">>> ATIVIDADE 1 - CRIPTOGRAFIA E DESCRIPTOGRAFIA (AES-256-GCM)");
        System.out.println(SUBLINHA);

        String mensagem = entrada.lerTextoNaoVazio("1) Digite a mensagem: ");
        String chave = entrada.lerTextoNaoVazio("2) Informe a chave (senha) de criptografia: ");

        try {
            // 3) Criptografar
            ResultadoCriptografia resultado = criptografiaService.criptografar(mensagem, chave);

            // 4) Exibir original e criptografado
            System.out.println();
            System.out.println("--- CRIPTOGRAFIA ---");
            System.out.println("Texto original ........: " + resultado.getMensagemOriginal());
            System.out.println("Chave informada .......: " + resultado.getChaveInformada());
            System.out.println("Salt (aleatório) ......: " + resultado.getSaltHex());
            System.out.println("IV (aleatório) ........: " + resultado.getIvHex());
            System.out.println("Chave AES derivada ....: " + resultado.getChaveDerivadaHex());
            System.out.println("Texto criptografado ...: " + resultado.getTextoCifradoBase64());

            // 5) Descriptografar
            String recuperado = criptografiaService.descriptografar(
                    resultado.getTextoCifradoBase64(), resultado.getChaveInformada());

            System.out.println();
            System.out.println("--- DESCRIPTOGRAFIA ---");
            System.out.println("Texto recuperado ......: " + recuperado);

            // 6) Demonstrar que o original foi recuperado
            System.out.println();
            boolean igual = mensagem.equals(recuperado);
            System.out.println("Original == Recuperado? " + (igual ? "SIM - texto original recuperado com sucesso!"
                    : "NÃO - algo deu errado."));
        } catch (CriptografiaException e) {
            System.out.println("[ERRO] " + e.getMessage());
        }
    }

    // ------------------------------------------------------------------
    // Opção extra: descriptografar texto colado + testar chave errada
    // ------------------------------------------------------------------
    private void descriptografarTextoInformado() {
        System.out.println(">>> DESCRIPTOGRAFAR TEXTO CIFRADO (Base64)");
        System.out.println(SUBLINHA);

        String cifrado = entrada.lerTextoNaoVazio("Cole o texto criptografado (Base64): ");
        String chave = entrada.lerTextoNaoVazio("Informe a chave: ");

        try {
            String original = criptografiaService.descriptografar(cifrado, chave);
            System.out.println();
            System.out.println("Texto descriptografado : " + original);
        } catch (CriptografiaException e) {
            System.out.println();
            System.out.println("[ERRO] " + e.getMessage());
        }
    }

    // ------------------------------------------------------------------
    // ATIVIDADE 2 - Hash SHA-256
    // ------------------------------------------------------------------
    private void atividadeHash() {
        System.out.println(">>> ATIVIDADE 2 - GERAÇÃO E COMPARAÇÃO DE HASH (SHA-256)");
        System.out.println(SUBLINHA);

        try {
            String mensagem1 = entrada.lerTextoNaoVazio("1) Digite a primeira mensagem: ");
            ResultadoHash hash1 = hashService.gerarSha256(mensagem1);
            System.out.println("   Hash 1: " + hash1.getHashHex());

            System.out.println();
            String mensagem2 = entrada.lerTextoNaoVazio("4) Digite a segunda mensagem: ");
            ResultadoHash hash2 = hashService.gerarSha256(mensagem2);
            System.out.println("   Hash 2: " + hash2.getHashHex());

            ComparacaoHash comparacao = hashService.comparar(hash1, hash2);

            System.out.println();
            System.out.println("--- COMPARAÇÃO ---");
            System.out.println("Mensagem 1 ..: " + mensagem1);
            System.out.println("Hash 1 ......: " + hash1.getHashHex());
            System.out.println("Mensagem 2 ..: " + mensagem2);
            System.out.println("Hash 2 ......: " + hash2.getHashHex());
            System.out.println("Diferenças ..: " + marcarDiferencas(hash1.getHashHex(), hash2.getHashHex()));
            System.out.println();

            if (comparacao.saoIguais()) {
                System.out.println("RESULTADO: os hashes são IGUAIS (mensagens idênticas).");
            } else {
                System.out.println("RESULTADO: os hashes são DIFERENTES.");
                System.out.println(String.format(Locale.US,
                        "Efeito avalanche: %d de %d bits mudaram (%.2f%%).",
                        comparacao.getBitsDiferentes(), ComparacaoHash.TOTAL_BITS_SHA256,
                        comparacao.getPercentualBitsDiferentes()));
            }
        } catch (CriptografiaException e) {
            System.out.println("[ERRO] " + e.getMessage());
        }
    }

    /** Monta uma linha com '^' nas posições em que os dois hashes têm caracteres diferentes. */
    private String marcarDiferencas(String a, String b) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < a.length(); i++) {
            sb.append(a.charAt(i) == b.charAt(i) ? ' ' : '^');
        }
        return sb.toString();
    }

    // ------------------------------------------------------------------
    // Informações sobre os algoritmos (apoio para a apresentação)
    // ------------------------------------------------------------------
    private void exibirSobreAlgoritmos() {
        System.out.println(">>> SOBRE OS ALGORITMOS");
        System.out.println(SUBLINHA);
        System.out.println("AES-256-GCM (criptografia simétrica)");
        System.out.println("  - Mesma chave para criptografar e descriptografar.");
        System.out.println("  - Bloco de 128 bits, chave de 256 bits, 14 rodadas.");
        System.out.println("  - GCM transforma o AES em cifra de fluxo (modo contador) e ainda");
        System.out.println("    gera uma tag que detecta chave errada ou texto alterado.");
        System.out.println("  - A chave é derivada da senha por PBKDF2-HMAC-SHA256 + salt.");
        System.out.println("  - Saída: Base64( salt[16] | iv[12] | texto cifrado | tag[16] ).");
        System.out.println();
        System.out.println("SHA-256 (função hash)");
        System.out.println("  - Entrada de qualquer tamanho -> saída fixa de 256 bits (64 hex).");
        System.out.println("  - Determinístico: mesma entrada gera sempre o mesmo hash.");
        System.out.println("  - Unidirecional: não existe 'descriptografar' um hash.");
        System.out.println("  - Efeito avalanche: 1 caractere diferente muda ~50% dos bits.");
    }
}
