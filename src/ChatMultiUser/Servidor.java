package ChatMultiUser;

/**
 *
 * @author girlene
 */
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Servidor extends Thread {

    private static Vector CLIENTES;
    private final Socket conexao; // socket do cliente que se conectou
    private String nomeCliente; // String com o nome do cliente
    private static final List LISTA_DE_NOMES = new ArrayList(); // lista que armazena nome dos clientes

    public Servidor(Socket socket) {  // construtor que recebe o socket do cliente
        this.conexao = socket;
    }

    //remove da lista o cliente que saiu do chat
    public void remove(String oldName) {
        for (int i = 0; i < LISTA_DE_NOMES.size(); i++) {
            if (LISTA_DE_NOMES.get(i).equals(oldName)) {
                LISTA_DE_NOMES.remove(oldName);
            }
        }
    }

    public static void main(String args[]) {

        CLIENTES = new Vector(); // instancia o vetor de clientes conectados
        try {
            ServerSocket servidor = new ServerSocket(12345); //cria servidor na porta 12345
            System.out.println("AGUARDANDO CONEXAO");

            while (true) {
                Socket conexao = servidor.accept(); //aceita uma conexao
                Thread thread = new Servidor(conexao); // cria uma nova thread para tratar essa conexão
                thread.start();
            }
        } catch (IOException ex) {
            Logger.getLogger(Servidor.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public void run() // execução da thread
    {
        try {
            BufferedReader entrada = new BufferedReader(new InputStreamReader(this.conexao.getInputStream()));//comunicao com o cliente, recebe o que o cliente manda
            PrintStream saida = new PrintStream(this.conexao.getOutputStream());//envia algo ao cliente

            this.nomeCliente = entrada.readLine(); // recebe o nome do cliente
            System.out.println(this.nomeCliente + " : SE CONECTOU AO SERVIDOR!");//mostra o nome do cliente conectado ao servidor

            CLIENTES.add(saida);//adiciona os dados de saida do cliente no objeto CLIENTES
            //recebe a mensagem do cliente
            String msg = entrada.readLine();
            while (msg != null && !(msg.trim().equals(""))) { // Se não for nula, mostra a troca de mensagens
                sendToAll(saida, " escreveu: ", msg);// envia a msg os clientes conectados
                msg = entrada.readLine();  // espera uma nova msg.
            }
            System.out.println(this.nomeCliente + " SAIU DO CHAT!");//se cliente enviar linha em branco, mostra que ele saiu	
            sendToAll(saida, " SAIU", " do CHAT!"); //mostra para todos do chat q tal cliente saiu
            remove(this.nomeCliente);//remove nome da lista
            CLIENTES.remove(saida); //exclui atributos setados ao cliente
            this.conexao.close();//fecha a conexao com este cliente
        } catch (IOException ex) {
            Logger.getLogger(Servidor.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    // método que envia a msg para todos os conectados no chat
    public void sendToAll(PrintStream saida, String acao, String msg) throws IOException {
        Enumeration e = CLIENTES.elements();
        while (e.hasMoreElements()) {
            PrintStream chat = (PrintStream) e.nextElement(); // obtém o fluxo de saída de um dos clientes
            if (chat != saida) {
                chat.println(this.nomeCliente + acao + msg); // envia para todos, menos para o próprio cliente
            }
        }
    }
}
