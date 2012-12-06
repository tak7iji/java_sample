package jp.co.ntts.test.sample;

import java.net.URI;

import org.java_websocket.client.WebSocketClient;
import org.java_websocket.drafts.Draft_17;
import org.java_websocket.handshake.ServerHandshake;

public class IOSocketClient {

    private int id;
    private WebSocketClient socket;

    public int getId() {
        return this.id;
    }

    public WebSocketClient getIOSocket() {
        return this.socket;
    }

    public void connect(final int id, String host) throws Exception {
        socket = new WebSocketClient(new URI("http://" + host
                + ":8080/WebSocketTestServlet3/noecho"), new Draft_17()) {
            @Override
            public void onOpen(ServerHandshake handshakedata) {
            }

            @Override
            public void onMessage(String message) {
                System.out.println(id + " >>> " + message);
            }

            @Override
            public void onClose(int code, String reason, boolean remote) {
            }

            @Override
            public void onError(Exception ex) {
                ex.printStackTrace();
            }
        };
        socket.connectBlocking();
        socket.send("hello");

        this.id = id;
    }

    public void pushLog() {
        socket.send("get");
        socket.close();
    }

    public void close() {
        socket.close();
    }

}
