package enviaArquivo;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author mgirl
 */
public class Cliente {

    public static void main(String[] args) {
        try {
            Scanner teclado = new Scanner(System.in); //ler o que o cliente digita
            System.out.println("NOME DO ARQUIVO:"); //pede para o cliente digitar o nome do arquivo
            String nomeArq = teclado.nextLine(); //armazena o nome do arquivo em uma string

            File arquivo = new File("\\Users\\mgirl\\Downloads\\" + nomeArq); //cria o arquivo que o cliente escolheu
            FileInputStream arq = new FileInputStream(arquivo); //abri o arquivo

            Socket conexao = new Socket("localhost",999); // se conecta ao servidor
            OutputStream envia = conexao.getOutputStream(); //canal de comunicao com o servidor
            OutputStreamWriter saidabytes = new OutputStreamWriter(envia); //transforma o que está no arquivo em bytes
            BufferedWriter escreve = new BufferedWriter(saidabytes); // grava o fluxo de saida em um buffer;
            escreve.write(arquivo.getName() + "\n"); //escreve o nome do arquivo q o cliente digitou
            escreve.flush(); //descarrega o buffer
            int tamanho = 16384; // buffer de 12KB  
            byte[] buffer = new byte[tamanho]; //cria um buffer
            int lidos = -1; //bytes lidos
            while ((lidos = arq.read(buffer, 0, tamanho)) != -1) {
                envia.write(buffer, 0, lidos); //vai mostrando no servidor os bytes sendo lidos
            }
            System.out.println("ENVIO CONCLUÍDO");
            conexao.close(); //fecha o fluxo
        } catch (IOException ex) {
            Logger.getLogger(Cliente.class.getName()).log(Level.SEVERE, null, ex);
        }

    }
}
