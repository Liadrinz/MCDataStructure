package app.structure;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.EOFException;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class LinearModel extends Model {

    private List<Block> blocks = new ArrayList<>();

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
                blocks.add(block);
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
        for (Block block : blocks) {
            stream.writeInt(block.x);
            stream.writeInt(block.y);
            stream.writeInt(block.z);
            stream.writeInt(block.getInt());
            fileSize += 16;
        }
        stream.close();
        return fileSize;
    }

    @Override
    public Block getBlock(int x, int y, int z) {
        for (Block block : blocks) {
            if (block.x == x && block.y == y && block.z == z) {
                return block;
            }
        }
        return null;
    }

    @Override
    public void setBlock(int x, int y, int z, Block block) {
        Block target = getBlock(x, y, z);
        if (target != null) blocks.remove(target);
        block.x = x;
        block.y = y;
        block.z = z;
        blocks.add(block);
    }
}