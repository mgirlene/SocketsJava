package ChatMultiUser;

/**
 *
 * @author girlene
 */
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Cliente extends Thread {

    private Socket conexao;

    public Cliente(Socket socket) { //esse construtor recebe o socket do cliente
        this.conexao = socket;
    }

    public static void main(String args[]) {
        try {
            Socket socket = new Socket("localhost", 12345); //se conecta ao servidor

            PrintStream saida = new PrintStream(socket.getOutputStream()); //envia algo
            BufferedReader teclado = new BufferedReader(new InputStreamReader(System.in)); //ler do teclado
            System.out.print("DIGITE SEU NOME: ");
            String nomec = teclado.readLine();
            saida.println(nomec.toUpperCase()); //envia o nome que o cliente digitou para o servidor
            Thread thread = new Cliente(socket); //instancia a Thread
            thread.start(); //inicia a thread

            String msg; //aqui será armazenado a mensagem que o cliente vai enviar
            while (true) {
                System.out.print("ENVIE UMA MENSAGEM: "); //pede pro cliente digitar algo
                msg = teclado.readLine(); //armazena o que foi digitado na string "msg"
                saida.println(msg);//envia a mensagem para o servidor
            }
        } catch (IOException ex) {
            Logger.getLogger(Cliente.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    // execução da thread
    @Override
    public void run() {
        try {
            BufferedReader entrada = new BufferedReader(new InputStreamReader(this.conexao.getInputStream())); //recebe mensagens de outro cliente através do servidor
            String msg; //crio uma variavel para receber a msg
            while (true) {
                msg = entrada.readLine(); // pega o que o servidor enviou e armazena nessa variavel
                //se a mensagem contiver dados, passa pelo if, 
                // caso contrario cai no break e encerra a conexao
                if (msg == null) {
                    System.out.println("CONEXAO ENCERRADA!");
                    System.exit(0);
                }
                System.out.println();
                System.out.println(msg); //imprime a mensagem recebida
                System.out.print("RESPONDER: ");//aqui o cliente pode enviar uma resposta
            }
        } catch (IOException ex) {
            Logger.getLogger(Cliente.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
