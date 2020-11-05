package lanchonete;

import java.io.IOException;
import java.io.PrintStream;
import java.net.Socket;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author girlene
 */
public class Tratamento implements Runnable {

    int soma = 0; //variavel que armazena a soma da conta do cliente
    private final Socket conexao; //criacao do socket
    String menu;

    public Tratamento(Socket conexao) { //construtor;
        this.conexao = conexao;
    }

    public String menu() { //Nessa funcao eu mostro as opcoes disponiveis para o cliente escolher;

        menu = " @###FAÇA SEU PEDIDO### @1- REFRIGERANTE(R$3,00) @2- SANDUÍCHE(R$6,00) @3- BATATA FRITA(R$4,00) @0- SAIR ";

        return menu;
    }

    public void pedidoCliente(String pedido) throws IOException {
        switch (pedido) {
            case "1":
                soma += 3;
                break;

            case "2":
                soma += 6;
                break;

            case "3":
                soma += 4;
                break;
        }

    }

    @Override
    public void run() {
        try {

            System.out.println("###ATENDENDO CLIENTE!###");
            PrintStream envio = new PrintStream(conexao.getOutputStream()); //envia alguma coisa para o cliente
            Scanner leitura = new Scanner(conexao.getInputStream()); // captura o que o cliente digita
            envio.println(menu()); //imprime o menu para o cliente
            String pedido;

            while (leitura.hasNextLine()) { //verifica se o cliente digitou alguma coisa

                pedido = leitura.nextLine();

                if (pedido.equals("0")) { //Quando o cliente digita 0, a conta é mostrada
                    envio.print("TOTAL DO PEDIDO: R$ " + soma + ",00"); //envia a conta para o cliente
                    break;

                } else {
                    System.out.println(pedido); //imprime no servidor, a escolha do cliente
                    pedidoCliente(pedido); //funcao onde tem o case para definir a consequencia de cada escolha do cliente
                    envio.println(menu()); //imprime o menu para o cliente
                }

            }

            leitura.close(); //fechamento dOs fluxos que criei;
            conexao.close();
            envio.close();
        } catch (IOException ex) {
            Logger.getLogger(Tratamento.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
