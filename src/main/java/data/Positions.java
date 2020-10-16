package data;

import java.nio.ByteBuffer;

public class Positions implements Broadcastable {

    public static final int BYTE_COUNT = 12;

    public int id;

    public int x;

    public int y;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    @Override
    public byte[] bytes() {
        return ByteBuffer.allocate(BYTE_COUNT).putInt(id).putInt(x).putInt(y).array();
    }

}
