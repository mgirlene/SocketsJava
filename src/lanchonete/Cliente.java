package lanchonete;

import java.io.PrintStream;
import java.net.Socket;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Girlene
 */
public class Cliente {


    public static void main(String[] args) {
        try {
            Socket conexao = new Socket("localhost", 1234); // se conecta ao servidor
            System.out.println("BEM VINDO!");

            PrintStream saida = new PrintStream(conexao.getOutputStream()); //envia ao servidor
            Scanner entrada = new Scanner(conexao.getInputStream()); //mostra o que vem do servidor
            Scanner teclado = new Scanner(System.in); //ler do teclado

            String mensagem; //variavel que armazena o que o cliente digita
            System.out.println(entrada.nextLine().replace("@", "\n")); //imprime o menu que o servidor envia

            while (teclado.hasNextLine()) {
                mensagem = teclado.nextLine();

                if (mensagem.equals("0")) {
                    saida.println(mensagem);
                    System.out.println(entrada.nextLine());
                    break;
                } else {
                    saida.println(mensagem); //envia o que deseja ao servidor
                    System.out.println(entrada.nextLine().replace("@", "\n")); //imprime o menu
                }

            }
            entrada.close();
            saida.close();
            conexao.close();
        } catch (Exception ex) {
            Logger.getLogger(Cliente.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
