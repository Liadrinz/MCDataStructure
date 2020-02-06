package app.structure;

import java.util.HashMap;
import java.util.Map;
import java.io.*;

public class HashModel extends Model {

    private Map<Integer, Block> blocks = new HashMap<>();

    private Integer getKey(int x, int y, int z) {
        return x * 73856093 ^ y * 19349663 ^ z * 83492791;
    }

    @Override
    public void read(File file) throws IOException {
        DataInputStream stream = new DataInputStream(new FileInputStream(file));
        while (true) {
            try {
                int x = stream.readInt();
                int y = stream.readInt();
                int z = stream.readInt();
                int data = stream.readInt();
                Block block = new Block(data);
                block.x = x;
                block.y = y;
                block.z = z;
                blocks.put(getKey(x, y, z), block);
            } catch (EOFException eof) {
                stream.close();
                break;
            }
        }
    }

    @Override
    public int write(File file) throws IOException {
        int fileSize = 0;
        DataOutputStream stream = new DataOutputStream(new FileOutputStream(file));
        for (Integer key : blocks.keySet()) {
            stream.writeInt(blocks.get(key).x);
            stream.writeInt(blocks.get(key).y);
            stream.writeInt(blocks.get(key).z);
            stream.writeInt(blocks.get(key).getInt());
            fileSize += 16;
        }
        stream.close();
        return fileSize;
    }

    @Override
    public Block getBlock(int x, int y, int z) {
        Integer key = getKey(x, y, z);
        if (blocks.containsKey(key)) return blocks.get(key);
        return null;
    }

    @Override
    public void setBlock(int x, int y, int z, Block block) {
        blocks.put(getKey(x, y, z), block);
    }
}