package client_server_archives;

import java.io.*;
import java.net.*;

public class Cliente {
        private static final String IP_SERVIDOR = "127.0.0.1";
        private static final int PORTA_SERVIDOR = 12345;

        public static void main(String[] args) {
            try {
                // Cria o socket do cliente
                DatagramSocket clienteSocket = new DatagramSocket();

                // Endereço do servidor
                InetAddress enderecoServidor = InetAddress.getByName(IP_SERVIDOR);

                // Loop principal do cliente
                while (true) {
                    // Lê a mensagem do usuário
                    BufferedReader leitor = new BufferedReader(new InputStreamReader(System.in));
                    System.out.print("Digite uma mensagem: ");
                    String mensagem = leitor.readLine();

                    // Envia a mensagem para o servidor
                    enviarMensagem(clienteSocket, enderecoServidor, mensagem);
                }

            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        private static void enviarMensagem(DatagramSocket clienteSocket, InetAddress enderecoServidor, String mensagem)
                throws IOException {
            // Converte a mensagem em bytes
            byte[] dadosEnviados = mensagem.getBytes();

            // Divide a mensagem em segmentos de no máximo 1400 bytes
            int tamanhoSegmento = 1400;
            int numeroSegmentos = (int) Math.ceil((double) dadosEnviados.length / tamanhoSegmento);

            // Envia cada segmento individualmente
            for (int i = 0; i < numeroSegmentos; i++) {
                int inicio = i * tamanhoSegmento;
                int fim = Math.min(inicio + tamanhoSegmento, dadosEnviados.length);
                byte[] segmento = new byte[fim - inicio];
                System.arraycopy(dadosEnviados, inicio, segmento, 0, segmento.length);

                // Cria um pacote com o segmento
                DatagramPacket pacoteEnviado = new DatagramPacket(segmento, segmento.length, enderecoServidor, PORTA_SERVIDOR);

                // Verifica se o pacote será perdido (20% de chance)
                if (Math.random() > 0.2) {
                    // Envia o pacote para o servidor
                    clienteSocket.send(pacoteEnviado);

                    // Cria um buffer para receber a resposta do servidor
                    byte[] buffer = new byte[1024];
                    DatagramPacket pacoteRecebido = new DatagramPacket(buffer, buffer.length);

                    // Espera a resposta do servidor
                    clienteSocket.receive(pacoteRecebido);

                    // Converte a resposta em string
                    String resposta = new String(pacoteRecebido.getData(), 0, pacoteRecebido.getLength());

                    // Imprime a resposta do servidor
                    System.out.println("Servidor: " + resposta);
                } else {
                    System.out.println("Pacote perdido. Reenviando...");
                    i--;
                }
            }
        }
}

