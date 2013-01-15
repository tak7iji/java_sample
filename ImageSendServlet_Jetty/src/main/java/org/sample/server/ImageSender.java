package org.sample.server;

import java.io.File;
import java.io.FileInputStream;

import javax.servlet.http.HttpServletRequest;

import org.eclipse.jetty.websocket.WebSocket;
import org.eclipse.jetty.websocket.WebSocketServlet;

public class ImageSender extends WebSocketServlet {

    private static final long serialVersionUID = 1L;

    public WebSocket doWebSocketConnect(HttpServletRequest arg0, String arg1) {
        return new WebSocket.OnTextMessage() {
            protected Connection connection;

            public void onClose(int arg0, String arg1) {
            }

            public void onOpen(Connection connection) {
                this.connection = connection;
            }

            public void onMessage(String arg0) {

                int len = Integer.parseInt(arg0);
                byte[] buffer = new byte[len];
                java.util.Arrays.fill(buffer, Byte.MAX_VALUE);

                try {
                    this.connection.sendMessage(buffer, 0, len);
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        };
    }
}
