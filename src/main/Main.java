package main;

import service.CriptografiaService;
import service.HashService;
import utils.EntradaUtil;
import view.MenuPrincipal;

import java.io.PrintStream;
import java.io.UnsupportedEncodingException;
import java.util.Scanner;

/**
 * Ponto de entrada da aplicação.
 * Cria os objetos (Scanner, services, menu) e inicia o menu interativo.
 */
public class Main {

    public static void main(String[] args) {
        configurarSaidaUtf8();

        try (Scanner scanner = new Scanner(System.in, "UTF-8")) {
            EntradaUtil entrada = new EntradaUtil(scanner);
            CriptografiaService criptografiaService = new CriptografiaService();
            HashService hashService = new HashService();

            MenuPrincipal menu = new MenuPrincipal(entrada, criptografiaService, hashService);
            menu.iniciar();
        } catch (IllegalStateException e) {
            System.out.println("\nEntrada encerrada. Programa finalizado.");
        }
    }

    /** Garante que acentos apareçam corretamente no terminal. */
    private static void configurarSaidaUtf8() {
        try {
            System.setOut(new PrintStream(System.out, true, "UTF-8"));
        } catch (UnsupportedEncodingException e) {
            // se não suportar, segue com a codificação padrão
        }
    }
}
