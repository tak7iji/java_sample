package jp.co.ntts.sample;

import java.io.IOException;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;

import javax.servlet.http.HttpServletRequest;
import org.eclipse.jetty.websocket.WebSocket;
import org.eclipse.jetty.websocket.WebSocket.Connection;
import org.eclipse.jetty.websocket.WebSocketServlet;

public class EchoServlet extends WebSocketServlet {

	private static final long serialVersionUID = 1L;
	private final Set<Connection> connections = new CopyOnWriteArraySet<Connection>();

	public WebSocket doWebSocketConnect(HttpServletRequest hsr, String string) {
		return new WebSocket.OnTextMessage() {

			protected Connection connection;

			public void onMessage(String data) {
			    if(data.equals("hello")) return;
				this.send(">> " + data);
			}

			public void onOpen(Connection connection) {
				connections.add(connection);
				this.connection = connection;
			}

			public void onClose(int closeCode, String message) {
				connections.remove(connection);
			}

			protected void send(String message) {
				try {
					for (Connection connection : connections) {
						connection.sendMessage(message);
					}
				} catch (IOException ex) {
					ex.printStackTrace();
				}
			}
		};
	}
}
