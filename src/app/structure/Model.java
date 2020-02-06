package app.structure;

import java.io.File;
import java.io.IOException;

public abstract class Model {
    public abstract void read(File file) throws IOException;
    public abstract int write(File file) throws IOException;
    public abstract Block getBlock(int x, int y, int z);
    public abstract void setBlock(int x, int y, int z, Block block);
}