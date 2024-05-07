/**
 *
 *  @author Pluciński Tomasz S27477
 *
 */

package zad1;

import java.net.*;
import java.io.*;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.nio.*;
import java.nio.channels.*;
import java.nio.charset.*;

public class Server extends Thread {
    private ServerSocketChannel ssc = null;
    private Selector selector = null;
    private final Map<String, StringBuilder> clientsLogs = new HashMap<>();
    private final StringBuilder serverLogs = new StringBuilder(); //moze tez byc lista stringow
    private final Map<SocketChannel, String> clients = new HashMap<>();
    private static final Charset charset  = StandardCharsets.UTF_8;
    private static final int BSIZE = 1024;
    private final ByteBuffer bbuf = ByteBuffer.allocate(BSIZE);
    private final StringBuffer reqString = new StringBuffer();
    public Server(String host, int port ) {
        try {
            ssc = ServerSocketChannel.open();
            ssc.configureBlocking(false);
            ssc.socket().bind(new InetSocketAddress(host, port));
            selector = Selector.open();
            ssc.register(selector,SelectionKey.OP_ACCEPT);
        } catch(Exception exc) {
            exc.printStackTrace();
            System.exit(1);
        }
    }
    public void startServer(){
        start();
    }
    private void serviceRequest(SocketChannel sc) {
        if (!sc.isOpen()) return; // jeżeli kanał zamknięty - nie ma nic do roboty

        // Odczytanie zlecenia
        reqString.setLength(0);
        bbuf.clear();
        try {
            readLoop:
            // Czytanie jest nieblokujące
            while (true) {               // kontynujemy je dopóki
                int n = sc.read(bbuf);     // nie natrafimy na koniec wiersza
                if (n > 0) {
                    bbuf.flip();
                    CharBuffer cbuf = charset.decode(bbuf);
                    while (cbuf.hasRemaining()) {
                        char c = cbuf.get();
                        if (c == '\r' || c == '\n') break readLoop;
                        reqString.append(c);
                    }
                }
            }

            String userRequest = reqString.toString();
            String[] splittedUserRequest = userRequest.split(" ");

            LocalTime time = LocalTime.now();
            DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("HH:mm:ss.SSS");
            String localTimeFormatted = time.format(dateTimeFormatter);
            if (userRequest.startsWith("login")) {
                String userId = splittedUserRequest[1];
                clients.put(sc, userId);
                clientsLogs.put(userId, new StringBuilder().append("=== ").append(userId).append(" log start ===").append("\n").append("logged in\n"));
                serverLogs.append(userId).append(" logged in at ").append(localTimeFormatted).append("\n");
                writeResp(sc, "logged in");
            } else if (userRequest.equals("bye")) {
                String userId = clients.get(sc);
                clientsLogs.get(userId).append("=== ").append(userId).append(" log end ===").append("\n");
                serverLogs.append(userId).append(" logged out at ").append(localTimeFormatted).append("\n");
                writeResp(sc, "logged out");
            } else if (userRequest.equals("bye and log transfer")) {
                String userId = clients.get(sc);
                clientsLogs.get(userId).append("logged out\n").append("=== ").append(userId).append(" log end ===").append("\n");
                serverLogs.append(userId).append(" logged out at ").append(localTimeFormatted).append("\n");
                writeResp(sc, clientsLogs.get(userId).toString());
            } else {
                //data od data do
                String passed = Time.passed(splittedUserRequest[0], splittedUserRequest[1]);
                String userId = clients.get(sc);
                clientsLogs.get(userId).append("Request: ").append(userRequest).append("\nResult:\n").append(passed).append("\n");
                serverLogs.append(userId).append(" request at ").append(localTimeFormatted).append(": \"").append(userRequest).append("\"\n");
                writeResp(sc, passed);
            }

        } catch (Exception exc) {
            exc.printStackTrace();
            try {
                sc.close();
                sc.socket().close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }
    private void writeResp(SocketChannel sc, String addMsg) throws IOException {
        ByteBuffer buf = charset.encode(CharBuffer.wrap(addMsg));
        sc.write(buf);
    }

    @Override
    public void run() {
        while(!this.isInterrupted()) {
            try {
                selector.select();
                Set<SelectionKey> keys = selector.selectedKeys();

                Iterator<SelectionKey> iter = keys.iterator();
                while(iter.hasNext()) {
                    SelectionKey key = iter.next();
                    iter.remove();
                    if (key.isAcceptable()) {
                        SocketChannel cc = ssc.accept();
                        cc.configureBlocking(false);
                        cc.register(selector, SelectionKey.OP_READ);
                        continue;
                    }
                    if (key.isReadable()) {
                        SocketChannel cc = (SocketChannel) key.channel();
                        serviceRequest(cc);
                    }
                }
            } catch(Exception exc) {
                exc.printStackTrace();
            }
        }
    }

    public void stopServer() {
        this.interrupt();
    }
    public String getServerLog() {
        return serverLogs.toString();
    }
}
