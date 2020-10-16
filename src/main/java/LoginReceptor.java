import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;

public class LoginReceptor extends Thread {

    private final DatagramSocket socket;

    public LoginReceptor(int port) throws SocketException {
        socket = new DatagramSocket(port);
    }

    @Override
    public void run() {
        while(!isInterrupted()) {
            try {
                final DatagramPacket p = new DatagramPacket(new byte[512], 512);
                socket.receive(p);

                final String s = new String(p.getData(), p.getOffset(), p.getLength());


            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


}
