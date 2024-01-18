package client_server_archives;

import java.io.*;
import java.net.*;

import static java.lang.System.*;

public class Servidor {
    private static final int PORTA_SERVIDOR = 12345;

    public static void main(String[] args) {
        try {
            // Cria o socket do servidor
            DatagramSocket servidorSocket = new DatagramSocket(PORTA_SERVIDOR);

            out.println("Servidor pronto para receber mensagens...");

            // Loop principal do servidor
            while (true) {
                // Cria um buffer para receber os dados do cliente
                byte[] buffer = new byte[1024];
                DatagramPacket pacoteRecebido = new DatagramPacket(buffer, buffer.length);

                // Espera a chegada de um pacote do cliente
                servidorSocket.receive(pacoteRecebido);

                // Converte os dados recebidos em string
                String mensagemRecebida = new String(pacoteRecebido.getData(), 0, pacoteRecebido.getLength());

                // Imprime a mensagem recebida no servidor
                System.out.println("Mensagem do Cliente.Cliente: " + mensagemRecebida);

                // Envia uma resposta de confirmação para o cliente
                String resposta = "Mensagem recebida!";
                byte[] dadosEnviados = resposta.getBytes();
                DatagramPacket pacoteEnviado = new DatagramPacket(dadosEnviados, dadosEnviados.length,
                        pacoteRecebido.getAddress(), pacoteRecebido.getPort());

                // Envia o pacote de resposta para o cliente
                servidorSocket.send(pacoteEnviado);

            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
