import data.Positions;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.nio.ByteBuffer;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

public class Broadcaster {

    /**
     * 64kilobytes should be the maximum but apparently some network nodes only work with smaller sizes.
     * Choosing a smaller size here means that a larger part of the total network load will be packet headers.
     */
    private static final int MAX_PACKET_SIZE = 512;

    private final DatagramSocket socket;

    public Broadcaster(int port) throws SocketException {
        socket = new DatagramSocket(port);
    }

    public void updatePositions(Set<InetAddress> inetAddresses, Set<Positions> positions) throws IOException {
        // Wrap data in packets
        final Set<DatagramPacket> packets = new HashSet<>();
        for(ByteBuffer b : packageToBuffers(positions)) {
            final byte[] bytes = b.array();
            packets.add(new DatagramPacket(bytes, bytes.length));
        }

        // For each packet, set address and send
        for (InetAddress address : inetAddresses) {
            for(DatagramPacket p : packets) {
                p.setAddress(address);
                socket.send(p);
            }
        }
    }

    private List<ByteBuffer> packageToBuffers(Set<Positions> positions) {
        final LinkedList<ByteBuffer> buffers = new LinkedList<>();
        // Determine how many positions fit into one packet
        final int posPerPacket = MAX_PACKET_SIZE / Positions.BYTE_COUNT;
        // Package
        int s = 0;
        for(Positions pos : positions) {
            if(s % posPerPacket == 0) {
                buffers.add(ByteBuffer.allocate(posPerPacket * Positions.BYTE_COUNT));
            }
            buffers.getLast().put(pos.bytes());
            s++;
        }
        return buffers;
    }
}
