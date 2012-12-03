package jp.co.ntts.test.sample;

import java.net.URI;

import org.java_websocket.client.WebSocketClient;
import org.java_websocket.drafts.Draft_17;
import org.java_websocket.handshake.ServerHandshake;

public class WSTest {

    public static void main(String[] args) throws NumberFormatException, Exception {
        for(int i = 0; i < args.length; i++){
            if(args[i].startsWith("-") && args.length >= (i+1)) {
                try {
                    Options.valueOf(args[i].substring(1)).setValue(args[++i]);
                } catch (IllegalArgumentException iae) {
                    //ignore
                }
            }
        }
        new WSTest().startTest(Integer.parseInt(Options.max.getValue()), Options.host.getValue());
    }

    public void startTest(int max, String host) throws Exception {
        for (int i = 0; i < max; i++) {
            open(i, host);
            Thread.sleep(10);
        }
        synchronized (this) {
            wait();
        }
    }

    public void open(final int id, String host) {
        try {
            WebSocketClient client = new WebSocketClient(new URI("http://"+host+":8080/WebSocketTestServlet3/echo"), new Draft_17()) {

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

    enum Options {
        host("localhost"),
        max("1");
        
        private String value;

        Options(String defaultValue) {
            this.value = defaultValue;
        }
        
        String getValue() {
            return this.value;
        }
        
        void setValue(String value) {
            this.value = value;
        }
    }
}
