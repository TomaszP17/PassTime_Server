/**
 *
 *  @author Pluciński Tomasz S27477
 *
 */

package zad1;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.channels.SocketChannel;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

/**
 * Nieblokujace wejscie - wyjscie
 */
public class Client {
    private final String host;
    private final int port;
    private final String id;
    private SocketChannel socketChannel = null;
    private static final int BSIZE = 1024;

    public Client(String host, int port, String id) {
        this.host = host;
        this.port = port;
        this.id = id;
    }

    /**
     * Laczy sie z serwerem
     */
    public void connect() {
        try {
            socketChannel = SocketChannel.open(new InetSocketAddress(host, port));
            socketChannel.configureBlocking(false);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Wysyla zadanie req i zwraca odpowiedz serwera
     * @param req - request do wyslania
     * @return odpowiedz serwera
     */
    public String send(String req) {
        try{
            if(socketChannel.isOpen()){
                String theReq = req + "\n";
                Charset charset = Charset.forName("UTF-8");
                ByteBuffer requestBuffer = charset.encode(theReq);
                socketChannel.write(requestBuffer);
                ByteBuffer byteBuffer = ByteBuffer.allocate(BSIZE);

                while(byteBuffer.position() == 0){
                    socketChannel.read(byteBuffer);
                }
                byteBuffer.flip();
                CharBuffer charBuffer = StandardCharsets.UTF_8.decode(byteBuffer);
                return charBuffer.toString();
            }
        }catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public String getId() {
        return id;
    }
}
