package org.sample.server;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.logging.Logger;

import javax.servlet.http.HttpServletRequest;

import org.apache.catalina.websocket.MessageInbound;
import org.apache.catalina.websocket.StreamInbound;
import org.apache.catalina.websocket.WebSocketServlet;
import org.apache.catalina.websocket.WsOutbound;

public class NoEchoServlet extends WebSocketServlet {
    private static final Logger logger = Logger.getLogger(NoEchoServlet.class
            .getName());
    private List<String> logList = Collections
            .synchronizedList(new ArrayList<String>(10000));

    private static final long serialVersionUID = 1L;

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
        }

        protected void onClose(int status) {
        }

        protected void onBinaryMessage(ByteBuffer arg0) throws IOException {
        }

        protected void onTextMessage(CharBuffer message) throws IOException {
            if (message.toString().equals("hello")) {
                return;
            } else if (message.toString().equals("get")) {
                for (String log : logList) {
                    logger.info(log);
                }
                logList.clear();
            } else {
                logList.add(message.toString() + ","
                        + System.currentTimeMillis());
            }
        }
    }
}
