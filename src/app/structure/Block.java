package app.structure;

public class Block {
    public int x, y, z;  // optional for normal model
    public int r, g, b, a;

    public Block(int r, int g, int b, int a) {
        this.r = r;
        this.g = g;
        this.b = b;
        this.a = a;
    }

    public Block(int data) {
        fromInt(data);
    }

    public int getInt() {
        return (r << 24) + (g << 16) + (b << 8) + a;
    }

    public void fromInt(int data) {
        r = (data >> 24) & 0xff;
        g = (data >> 16) & 0xff;
        b = (data >> 8) & 0xff;
        a = data & 0xff;
    }

    @Override
    public String toString() {
        return "(" + r + ", " + g + ", " + b + ", " + a + ")";
    }
}
