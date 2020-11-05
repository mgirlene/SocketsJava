package lanchonete;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Girlene
 */
public class Lanchonete {

    public static void main(String[] args) {

        try (ServerSocket servidor = new ServerSocket(1234)) {
            System.out.println("##CENTRAL DE PEDIDOS LIVRE!...##");

            while (true) {
                Socket conexao = servidor.accept(); //cliente se conecta
                Tratamento tratamento = new Tratamento(conexao); //comunicacao entre cliente e servidor;
                Thread thread = new Thread(tratamento); //permite que mais de um cliente se conecte ao servidor;
                thread.start();//inicia a comunicacao
            }
        } catch (IOException ex) {
            Logger.getLogger(Lanchonete.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

}
