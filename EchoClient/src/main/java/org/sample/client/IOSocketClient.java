package org.sample.client;

import java.net.URI;
import java.nio.ByteBuffer;

import org.java_websocket.client.WebSocketClient;
import org.java_websocket.drafts.Draft_17;
import org.java_websocket.handshake.ServerHandshake;

public class IOSocketClient {

    private int id;
    private WebSocketClient socket;
    private WSTest tester;
    
    public IOSocketClient(WSTest tester) {
        this.tester = tester;
    }

    public int getId() {
        return this.id;
    }

    public WebSocketClient getIOSocket() {
        return this.socket;
    }

    public void connect(final int id, String host) throws Exception {
        socket = new WebSocketClient(new URI("http://" + host
                + ":8080/WebSocketServlet/echo"), new Draft_17()) {
            @Override
            public void onOpen(ServerHandshake handshakedata) {
            }

            @Override
            public void onMessage(ByteBuffer message) {
                long recv = System.currentTimeMillis();
                tester.countDown();
                tester.addLog(id + ", " + new String(message.array()) + ", " + recv + "\n");
                try {
                    tester.await();
                } catch (Exception e) {
                    // Ignore
                }
                close();
            }
            
            @Override
            public void onMessage(String message) {
                long recv = System.currentTimeMillis();
                tester.countDown();
                tester.addLog(id + ", " + message + ", " + recv + "\n");
                try {
                    tester.await();
                } catch (Exception e) {
                    // Ignore
                }
                close();
            }

            @Override
            public void onClose(int code, String reason, boolean remote) {
            }

            @Override
            public void onError(Exception ex) {
                ex.printStackTrace();
            }
        };
        boolean isConnected = socket.connectBlocking();
        while(!isConnected){
            Thread.sleep(10);
            isConnected = socket.getConnection().isOpen();
        }
        socket.send("hello");

        this.id = id;
    }

    public void close() {
        socket.close();
    }

}
