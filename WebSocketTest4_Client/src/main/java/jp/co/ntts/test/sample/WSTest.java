package jp.co.ntts.test.sample;

import java.net.URI;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.atomic.AtomicInteger;

import org.java_websocket.client.WebSocketClient;
import org.java_websocket.drafts.Draft_17;
import org.java_websocket.handshake.ServerHandshake;

public class WSTest {
    private AtomicInteger counter = new AtomicInteger(0);
    private Object lock;
    
    public static void main(String[] args) throws Exception {
        for (int i = 0; i < args.length; i++) {
            if (args[i].startsWith("-") && args.length >= (i + 1)) {
                try {
                    Options.valueOf(args[i].substring(1)).setValue(args[++i]);
                } catch (IllegalArgumentException iae) {
                    // ignore
                }
            }
        }
        new WSTest().startTest(Integer.parseInt(Options.max.getValue()),
                Options.host.getValue());
    }

    public void startTest(int max, String host) throws Exception {
        lock = new Object();
        long start = System.currentTimeMillis();
        long delay = start + 5000 + (max * 50);

        for (int i = 0; i < max; i++) {
            new Timer().schedule(new SendTask(open(i, host)), new Date(delay));
            Thread.sleep(10);
        }
        System.out.println("Elapsed time: "
                + (System.currentTimeMillis() - start));

        synchronized (lock) {
            lock.wait();
        }

        Thread.sleep(1000);
        open(max, host).getIOSocket().send("get");
    }

    public IOSocketClient open(final int id, String host) {
        IOSocketClient client = null;
        try {
            client = new IOSocketClient();
            client.connect(id, host);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return client;
    }

    class SendTask extends TimerTask {
        private IOSocketClient client;

        public SendTask(IOSocketClient client) {
            this.client = client;
        }

        @Override
        public void run() {
            try {
                client.getIOSocket().send(
                        client.getId() + "," + System.currentTimeMillis());
                if( counter.incrementAndGet() == Integer.parseInt(Options.max.getValue()) ) {
                    synchronized(lock) {
                        lock.notifyAll();
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    class IOSocketClient {

        private int id;
        private WebSocketClient socket;

        int getId() {
            return this.id;
        }

        WebSocketClient getIOSocket() {
            return this.socket;
        }

        void connect(final int id, String host) throws Exception {
            socket = new WebSocketClient(new URI("http://" + host
                    + ":8080/WebSocketTestServlet3/noecho"), new Draft_17()) {
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
            socket.connectBlocking();
            socket.send("hello");

            this.id = id;
        }

    }

    enum Options {
        host("localhost"), max("1");

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
