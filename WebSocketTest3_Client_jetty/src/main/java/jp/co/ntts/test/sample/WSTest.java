package jp.co.ntts.test.sample;

import java.net.URI;
import java.util.concurrent.Future;

import org.eclipse.jetty.util.thread.QueuedThreadPool;
import org.eclipse.jetty.websocket.WebSocket;
import org.eclipse.jetty.websocket.WebSocketClient;
import org.eclipse.jetty.websocket.WebSocketClientFactory;
import org.eclipse.jetty.websocket.WebSocket.Connection;

public class WSTest {
    public static void main(String[] args) throws Exception {
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

    Connection open(int id) {
        Connection connection = null;
        try {
            WebSocketClientFactory webSocketClientFactory = new WebSocketClientFactory(new QueuedThreadPool(2));
            webSocketClientFactory.start();
            WebSocketClient client = webSocketClientFactory
                    .newWebSocketClient();
            MyWebSocket myWS = new MyWebSocket(id);
            Future<Connection> futureConnection = client.open(new URI(
                    "ws://localhost:8080/WebSocketTestServlet3/echo"), myWS);
            connection = futureConnection.get();

            // for Tomcat7
            connection.sendMessage("hello");

            System.out.println(">>> Connection opend: " + connection);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return connection;
    }

    class MyWebSocket implements WebSocket.OnTextMessage {
        private int id;

        public MyWebSocket(int id) {
            this.id = id;
        }

        public void onMessage(String data) {
            System.out.println(id + " >>> " + data);
        }

        public void onOpen(Connection connection) {
            System.out.println(id + " >>> Connection opened.");
        }

        public void onClose(int closeCode, String message) {
            System.out.println(id + " >>> Connection closed.");
        }
    }
}
