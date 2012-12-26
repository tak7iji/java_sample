package org.sample.server;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.catalina.websocket.MessageInbound;
import org.apache.catalina.websocket.StreamInbound;
import org.apache.catalina.websocket.WebSocketServlet;
import org.apache.catalina.websocket.WsOutbound;

public class EchoServlet extends WebSocketServlet {

    private static final long serialVersionUID = 1L;
    private List<WsOutbound> connections = Collections
            .synchronizedList(new ArrayList<WsOutbound>());

    @Override
    protected StreamInbound createWebSocketInbound(String arg0,
            HttpServletRequest arg1) {
        MessageEventHandler handler = new MessageEventHandler();
        handler.setOutboundByteBufferSize(1024);
        handler.setOutboundCharBufferSize(1024);
        return handler;
    }

    private final class MessageEventHandler extends MessageInbound {

        protected void onOpen(WsOutbound outbound) {
            connections.add(outbound);
        }

        protected void onClose(int status) {
            connections.remove(this.getWsOutbound());
        }

        protected void onBinaryMessage(ByteBuffer arg0) throws IOException {
            throw new UnsupportedOperationException(
                    "Binary message not supported.");
        }

        protected void onTextMessage(CharBuffer message) throws IOException {
            if (message.toString().equals("hello"))
                return;
            long start = System.currentTimeMillis();
            broadcast3(message.toString() + ", " + start);
            System.out.println("Errapsed: "
                    + (System.currentTimeMillis() - start));
        }

        private void broadcast(String message) {
            int size = connections.size();
            for (int i = 0; i < size; i++) {
                try {
                    connections.get(i).writeTextMessage(
                            CharBuffer.wrap(message));
                } catch (IOException ignore) {
                    // Ignore
                }
            }
        }
        private void broadcast2(String message) {
            int size = connections.size();
            char[] ca = message.toCharArray();

            for (int i = 0; i < size; i++) {
                try {
                    WsOutbound conn = connections.get(i);
                    for(int j = 0; j < ca.length; j++) {
                        conn.writeTextData(ca[j]);
                    }
                    conn.flush();
                } catch (IOException ignore) {
                    // Ignore
                }
            }
        }
        
        private void broadcast3(String message) {
            int size = connections.size();
            byte[] ba = message.getBytes();

            for (int i = 0; i < size; i++) {
                try {
                    WsOutbound conn = connections.get(i);
                    for(int j = 0; j < ba.length; j++) {
                        conn.writeBinaryData(ba[j]);
                    }
                    conn.flush();
                } catch (IOException ignore) {
                    // Ignore
                }
            }
        }

    }
}
