package net;

import java.io.IOException;
import java.net.Socket;

/**
 * 服务器到客户端的连接，出于类型安全而设置
 * 
 */
public class ConnectionToClient extends Connection {

	public ConnectionToClient(Socket socket) {
		super(socket);
	}

	@Override
	public void write(String content) {
		synchronized (ConnectionToClient.class) {
			this.pWriter.write(content + "\n");
			this.pWriter.flush();
		}
	}

	@Override
	public synchronized String readLine() {
		// readLine本身就是阻塞的，无需同步
		try {
			return this.bReader.readLine();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

}