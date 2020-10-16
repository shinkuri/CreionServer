package data;

public interface Broadcastable {
    
    /**
     * Return data from this POJO in a byte array that can be packaged in a DatagramPacket.
     *
     * @return data
     */
    byte[] bytes();

}
