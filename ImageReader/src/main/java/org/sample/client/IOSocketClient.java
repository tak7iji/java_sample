package org.sample.client;

import java.net.URI;
import java.nio.ByteBuffer;

import org.java_websocket.client.WebSocketClient;
import org.java_websocket.drafts.Draft_17;
import org.java_websocket.handshake.ServerHandshake;

public class IOSocketClient {

    private WebSocketClient socket;
    private long time;
    private WSTest tester;
    
    public void setTime(long time) {
        this.time = time;
    }
    
    public long getTime() {
        return time;
    }
    
    public IOSocketClient(WSTest tester) {
        this.tester = tester;
    }
    
    public WebSocketClient getIOSocket() {
        return this.socket;
    }

    public void connect(String host) throws Exception {
        socket = new WebSocketClient(new URI("http://" + host
                + ":8080/ImageSender/echo"), new Draft_17()) {
            @Override
            public void onOpen(ServerHandshake handshakedata) {
            }

            @Override
            public void onMessage(ByteBuffer message) {
                //System.err.println("Get size: "+message.array().length);
                setTime(System.currentTimeMillis());
                tester.countDown();
            }
            
            @Override
            public void onMessage(String message) {
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
    }

    public void close() {
        socket.close();
    }
}
