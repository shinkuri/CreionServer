package network;

import java.io.Closeable;
import java.io.IOException;
import java.net.Socket;

public class NetworkClient implements Closeable {
	
	private final Socket socket;
	
	public NetworkClient(Socket socket) {
		this.socket = socket;
	}

	@Override
	public void close() throws IOException {
		socket.close();
	}
	
}
