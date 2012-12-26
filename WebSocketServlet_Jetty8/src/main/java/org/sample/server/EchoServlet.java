package org.sample.server;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.eclipse.jetty.websocket.WebSocket;
import org.eclipse.jetty.websocket.WebSocket.Connection;
import org.eclipse.jetty.websocket.WebSocketServlet;

public class EchoServlet extends WebSocketServlet {

    private static final long serialVersionUID = 1L;
    private List<Connection> connections = Collections
            .synchronizedList(new ArrayList<Connection>());

    public WebSocket doWebSocketConnect(HttpServletRequest hsr, String string) {
        return new WebSocket.OnTextMessage() {

            protected Connection connection;

            public void onMessage(String data) {
                if (data.equals("hello"))
                    return;
                long start = System.currentTimeMillis();
                this.broadcast(data+", "+start);
                System.out.println("Errapsed: "
                        + (System.currentTimeMillis() - start));
            }

            public void onOpen(Connection connection) {
                connections.add(connection);
                this.connection = connection;
            }

            public void onClose(int closeCode, String message) {
                connections.remove(connection);
            }

            protected void broadcast(String message) {
                try {
                    int size = connections.size();
                    for (int i = 0; i < size; i++) {
                        connections.get(i).sendMessage(message);
                    }
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            }
        };
    }
}
