package net;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;
import java.net.SocketTimeoutException;

public class LoginReceptor extends Thread {

    private static final Logger LOGGER = LogManager.getLogger();

    private final DatagramSocket socket;

    public LoginReceptor(int port, int timeout) throws SocketException {
        socket = new DatagramSocket(port);
        socket.setSoTimeout(timeout);
    } 

    @Override
    public void run() {
        while(!isInterrupted()) {
            try {
                LOGGER.log(Level.INFO, "Waiting for package on " + socket.getLocalPort());

                final DatagramPacket p = new DatagramPacket(new byte[512], 512);
                socket.receive(p);

                final String s = new String(p.getData(), p.getOffset(), p.getLength());
                LOGGER.log(Level.INFO, s);

            } catch (SocketTimeoutException socketTimeoutException) {
                LOGGER.log(Level.INFO, "Timed out!");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


}
