package jp.co.ntts.test.sample;

import java.net.URI;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

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
        System.out.println("host: "+host);
        System.out.println("max: "+max);
        long start = System.currentTimeMillis();
        
        for (int i = 0; i < max; i++) {
            new Timer().schedule(new SendTask(open(i, host)), new Date(start+(max*50)));
            Thread.sleep(10);
        }
        System.out.println("Elapsed time: " + (System.currentTimeMillis() - start));
        synchronized (this) {
            wait();
        }
    }
    
    MyWebSocketClient open(int id, String host) {
        MyWebSocketClient client = null;
        try {
            client = new MyWebSocketClient(id, host);
            client.connectBlocking();
            client.send("hello");
            
        } catch (Exception ex) {
            // ignore
        }
        return client;
    }
    
    class MyWebSocketClient extends WebSocketClient {
        private int id;
        
        public int getId() {
            return this.id;
        }
        
        public MyWebSocketClient(int id, String host) throws Exception {
            super(new URI("http://"+host+":8080/WebSocketTestServlet3/noecho"), new Draft_17());
            this.id = id;
        }

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
    }
    
    class SendTask extends TimerTask {        
        private MyWebSocketClient client;

        public SendTask(MyWebSocketClient client) {
            this.client = client;
        }
        
        @Override
        public void run() {
            client.send(client.getId() +"," + System.currentTimeMillis());
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
