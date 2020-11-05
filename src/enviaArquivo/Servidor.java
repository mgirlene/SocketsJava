package enviaArquivo;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author girlene
 */
public class Servidor {

    public static void main(String[] args) {
        try {
            try (ServerSocket servidor = new ServerSocket(999) //cria o servidor
            ) {
                System.out.println("AGUARDANDO ARQUIVO....");
                Socket conexao = servidor.accept(); //aceita a conexao
                InputStream entrada = conexao.getInputStream(); //canal de comunicao com o cliente(entrada)
                InputStreamReader leitura = new InputStreamReader(entrada); //ler o que veio do cliente
                BufferedReader dado = new BufferedReader(leitura); //faço isso para poder usar o readLine();
                String nomeArquivo = dado.readLine(); //armazeno em uma string o que veio do cliente
                System.out.println(nomeArquivo); //imprimo essa string(que é o nome do arquivo q o cliente enviou)
                File novoarq = new File(nomeArquivo); //aqui se cria um arquivo com o nome q o cliente enviou
                FileOutputStream saida = new FileOutputStream(novoarq); //utilizo para escrever no arquivo
                
                int tamanho =16384; // buffer de 12KB
                byte[] buffer = new byte[tamanho];
                int lidos = -1;
                while ((lidos = entrada.read(buffer, 0, tamanho)) != -1) {
                    System.out.println(lidos);
                    saida.write(buffer, 0, lidos);
                }
                System.out.println("O ARQUIVO FOI RECEBIDO COM SUCESSO");
                saida.flush(); 
            }
        } catch (IOException ex) {
            Logger.getLogger(Servidor.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
