package org.sample.server;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;

import javax.servlet.http.HttpServletRequest;

import org.apache.catalina.websocket.MessageInbound;
import org.apache.catalina.websocket.StreamInbound;
import org.apache.catalina.websocket.WebSocketServlet;
import org.apache.catalina.websocket.WsOutbound;

public class ImageSender extends WebSocketServlet {

    private static final long serialVersionUID = 1L;

    @Override
    protected StreamInbound createWebSocketInbound(String arg0,
            HttpServletRequest arg1) {
        MessageEventHandler handler = new MessageEventHandler();
        handler.setOutboundByteBufferSize(1);
        handler.setOutboundCharBufferSize(1);
        return handler;
    }

    private final class MessageEventHandler extends MessageInbound {

        protected void onOpen(WsOutbound outbound) {
        }

        protected void onClose(int status) {
        }

        protected void onBinaryMessage(ByteBuffer arg0) throws IOException {
        }

        protected void onTextMessage(CharBuffer message) throws IOException {
            byte[] buffer = new byte[Integer.parseInt(message.toString())];
            java.util.Arrays.fill(buffer, Byte.MAX_VALUE);

            try {
                this.getWsOutbound().writeBinaryMessage(ByteBuffer.wrap(buffer));
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
     }
}
