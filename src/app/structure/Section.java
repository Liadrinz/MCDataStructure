package app.structure;

public class Section {
    public static int size = 4096;
    Block[] data;

    protected int dimConvert(int x, int y, int z) {
        return (x << 8) + (y << 4) + z;
    }

    public Section() {
        data = new Block[4096];
    }

    public Section(Block[] data) {
        assert (data.length == size);
        this.data = data;
    }

    public void setBlock(Block block, int x, int y, int z) {
        data[dimConvert(x, y, z)] = block;
    }

    public Block getBlock(int x, int y, int z) {
        return data[dimConvert(x, y, z)];
    }

    public int[] getInts() {
        // sectionSize, data
        int[] res = new int[size];
        int ptr = 0;
        for (int i = 0; i < size; ++i) {
            if (data[i] != null)
                res[ptr++] = data[i].getInt();
            else res[ptr++] = 0;
        }
        return res;
    }

    public void fromInts(int[] data) {
        int ptr = 0;
        for (int i = 0; i < size; ++i) {
            int k = data[ptr++];
            if (k != 0) {
                Block block = new Block(k);
                this.data[i] = block;
            }
        }
    }
}