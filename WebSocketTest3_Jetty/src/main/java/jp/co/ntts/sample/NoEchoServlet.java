package jp.co.ntts.sample;

import java.util.logging.Logger;

import javax.servlet.http.HttpServletRequest;
import org.eclipse.jetty.websocket.WebSocket;
import org.eclipse.jetty.websocket.WebSocketServlet;

public class NoEchoServlet extends WebSocketServlet {
    private static final Logger logger = Logger.getLogger(NoEchoServlet.class.getName());

    private static final long serialVersionUID = 1L;

    public WebSocket doWebSocketConnect(HttpServletRequest hsr, String string) {
        return new WebSocket.OnTextMessage() {

            public void onMessage(String data) {
                if(data.equals("hello")) return;
                logger.info(data+","+System.currentTimeMillis());
            }

            public void onOpen(Connection connection) {
                connection.setMaxIdleTime(0);
            }

            public void onClose(int closeCode, String message) {
                logger.info("Connection clonsed: "+closeCode);
            }
        };
    }
}
