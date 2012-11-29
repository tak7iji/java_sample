package jp.co.ntts.test.sample;

import java.net.URI;

import org.java_websocket.client.WebSocketClient;
import org.java_websocket.drafts.Draft_17;
import org.java_websocket.handshake.ServerHandshake;

public class WSTest {

    public static void main(String[] args) throws NumberFormatException, Exception {
        new WSTest().startTest(args.length == 0 ? 1 : Integer.parseInt(args[0]));
    }

    public void startTest(int max) throws Exception {
        for (int i = 0; i < max; i++) {
            open(i);
            Thread.sleep(10);
        }
        synchronized (this) {
            wait();
        }
    }

    public void open(final int id) {
        try {
            WebSocketClient client = new WebSocketClient(new URI("http://localhost:8080/WebSocketTestServlet3/echo"), new Draft_17()) {

                @Override
                public void onOpen(ServerHandshake handshakedata) {
                    System.out.println(id + " >>> Connection opened.");
                }

                @Override
                public void onMessage(String message) {
                    System.out.println(id + " >>> " + message);
                }

                @Override
                public void onClose(int code, String reason, boolean remote) {
                    System.out.println(id + " >>> Connection closed.");
                }

                @Override
                public void onError(Exception ex) {
                    ex.printStackTrace();
                }
            };
            client.connectBlocking();
            client.send("hello");
            
        } catch (Exception ex) {
            // ignore
        }
    }
}
