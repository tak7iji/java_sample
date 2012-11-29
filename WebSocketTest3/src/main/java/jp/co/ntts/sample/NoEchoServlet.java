package jp.co.ntts.sample;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.util.logging.Logger;

import javax.servlet.http.HttpServletRequest;

import org.apache.catalina.websocket.MessageInbound;
import org.apache.catalina.websocket.StreamInbound;
import org.apache.catalina.websocket.WebSocketServlet;
import org.apache.catalina.websocket.WsOutbound;

public class NoEchoServlet extends WebSocketServlet {
    private static final Logger logger = Logger.getLogger(NoEchoServlet.class.getName());

	private static final long serialVersionUID = 1L;

	@Override
	protected StreamInbound createWebSocketInbound(String arg0,
			HttpServletRequest arg1) {
		return new MessageEventHandler();
	}

	private final class MessageEventHandler extends MessageInbound {

		protected void onOpen(WsOutbound outbound) {
		}

		protected void onClose(int status) {
		}

		protected void onBinaryMessage(ByteBuffer arg0) throws IOException {
		}

		protected void onTextMessage(CharBuffer message) throws IOException {
		    if(message.toString().equals("hello")) return;
		    logger.info(message.toString()+","+System.currentTimeMillis());
		}
	}
}
