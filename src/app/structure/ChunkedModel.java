package app.structure;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.EOFException;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class ChunkedModel extends Model {
    private int size;
    private Map<Integer, Map<Integer, Chunk>> chunks;

    public ChunkedModel() {
        chunks = new HashMap<Integer, Map<Integer, Chunk>>();
    }

    public ChunkedModel(Chunk[] chunks) {
        this();
        fromArray(chunks);
    }

    protected void addChunk(Chunk chunk) {
        if (this.chunks.containsKey(chunk.x)) {
            this.chunks.get(chunk.x).put(chunk.z, chunk);
        } else {
            Map<Integer, Chunk> temp = new HashMap<>();
            this.chunks.put(chunk.x, temp);
            temp.put(chunk.z, chunk);
        }
        ++size;
    }

    protected void fromArray(Chunk[] chunks) {
        for (int i = 0; i < chunks.length; ++i) {
            Chunk chunk = chunks[i];
            addChunk(chunk);
        }
    }

    @Override
    public void read(File file) throws IOException {
        DataInputStream stream = new DataInputStream(new FileInputStream(file));
        stream.readInt();
        this.chunks.clear();
        while (true) {
            try {
                int chunkSize = stream.readInt();
                int bitMask = stream.readInt();
                int x = stream.readInt();
                int z = stream.readInt();
                int[] buffer = new int[chunkSize * Section.size + 4];
                int ptr = 0;
                buffer[ptr++] = chunkSize;
                buffer[ptr++] = bitMask;
                buffer[ptr++] = x;
                buffer[ptr++] = z;
                Chunk chunk = new Chunk(x, z);
                for (int i = 0; i < chunkSize; ++i) {
                    for (int j = 0; j < Section.size; ++j) {
                        buffer[ptr++] = stream.readInt();
                    }
                }
                chunk.fromInts(buffer);
                this.addChunk(chunk);
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
        stream.writeInt(size);
        fileSize += 4;
        for (Integer x : chunks.keySet()) {
            for (Integer z : chunks.get(x).keySet()) {
                int[] data = chunks.get(x).get(z).getInts();
                for (int i = 0; i < data.length; ++i) {
                    stream.writeInt(data[i]);
                    fileSize += 4;
                }
            }
        }
        stream.close();
        return fileSize;
    }

    public Chunk getChunk(int x, int z) {
        assert ((x % 16 | z % 16) == 0);
        if (chunks.containsKey(x) && chunks.get(x).containsKey(z))
            return chunks.get(x).get(z);
        return null;
    }

    @Override
    public Block getBlock(int x, int y, int z) {
        assert (y < 256);
        Chunk chunk = getChunk(x - x % 16, z - z % 16);
        return chunk.getSection(y / 16).getBlock(x % 16, y % 16, z % 16);
    }

    @Override
    public void setBlock(int x, int y, int z, Block block) {
        assert (y < 256);
        int xs = x - x % 16, zs = z - z % 16;
        Chunk chunk = getChunk(xs, zs);
        if (chunk == null) {
            chunk = new Chunk(xs, zs);
            addChunk(chunk);
            chunk.setSection(new Section(), y / 16);
        }
        if (chunk.getSection(y / 16) == null) {
            chunk.setSection(new Section(), y / 16);
        }
        chunk.getSection(y / 16).setBlock(block, x % 16, y % 16, z % 16);
    }
}