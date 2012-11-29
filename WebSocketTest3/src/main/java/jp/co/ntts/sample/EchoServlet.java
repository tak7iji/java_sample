package jp.co.ntts.sample;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.util.Calendar;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;
import java.util.concurrent.RejectedExecutionHandler;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.catalina.websocket.MessageInbound;
import org.apache.catalina.websocket.StreamInbound;
import org.apache.catalina.websocket.WebSocketServlet;
import org.apache.catalina.websocket.WsOutbound;

public class EchoServlet extends WebSocketServlet {

	private static final long serialVersionUID = 1L;
	private final Set<MessageEventHandler> connections = new CopyOnWriteArraySet<MessageEventHandler>();

	@Override
	protected StreamInbound createWebSocketInbound(String arg0,
			HttpServletRequest arg1) {
		return new MessageEventHandler();
	}

	private final class MessageEventHandler extends MessageInbound {

		protected void onOpen(WsOutbound outbound) {
			connections.add(this);
                    CharBuffer buffer = CharBuffer.wrap("Welcome to the EchoServer");
                    try {
                        getWsOutbound().writeTextMessage(buffer);
                    } catch (Exception ex) {}
		}

		protected void onClose(int status) {
			connections.remove(this);
		}

		protected void onBinaryMessage(ByteBuffer arg0) throws IOException {
			throw new UnsupportedOperationException(
					"Binary message not supported.");
		}

		protected void onTextMessage(CharBuffer message) throws IOException {
            if(message.toString().equals("hello")) return;
			broadcast(new StringBuilder().append(">> ").append(message).toString());
		}
        private void broadcast(String message) {
            for (MessageEventHandler connection : connections) {
                try {
                    connection.getWsOutbound().writeTextMessage(CharBuffer.wrap(message));
                } catch (IOException ignore) {
                    // Ignore
                }
            }
        }

	}
}
