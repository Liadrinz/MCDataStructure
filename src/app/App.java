package app;

import java.io.File;

import app.structure.*;

public class App {
    public static int xT = 32, yT = 32, zT = 32;

    public static void main(String[] args) throws Exception {
        Model chunkedModel = new ChunkedModel(), linearModel = new LinearModel(), hashModel = new HashModel();
        System.out.println("线性存储:");
        test(linearModel);
        System.out.println("纯哈希存储:");
        test(hashModel);
        System.out.println("区块存储:");
        test(chunkedModel);
    }

    public static void test(Model model) throws Exception {
        long s;
        try {
            s = System.currentTimeMillis();
            for (int x = 0; x < xT; ++x) {
                for (int y = 0; y < yT; ++y) {
                    for (int z = 0; z < zT; ++z) {
                        model.setBlock(x, y, z, new Block(233, 233, 233, 233));
                    }
                }
            }
            System.out.print("插入" + xT * yT * zT + "个方块的时间: ");
            System.out.print(System.currentTimeMillis() - s);
            System.out.println("ms");

            s = System.currentTimeMillis();
            for (int x = 0; x < xT; ++x) {
                for (int y = 0; y < yT; ++y) {
                    for (int z = 0; z < zT; ++z) {
                        model.getBlock(x, y, z);
                    }
                }
            }
            System.out.print("查找" + xT * yT * zT + "个方块的时间: ");
            System.out.print(System.currentTimeMillis() - s);
            System.out.println("ms");

            s = System.currentTimeMillis();
            int fileSize = model.write(new File("temp.mc"));
            System.out.println(xT * yT * zT + "个方块所占磁盘空间: " + fileSize + "bytes");
            System.out.print("将" + xT * yT * zT + "个方块写入磁盘的时间: ");
            System.out.print(System.currentTimeMillis() - s);
            System.out.println("ms");

            s = System.currentTimeMillis();
            model.read(new File("temp.mc"));
            System.out.print("从磁盘读出" + xT * yT * zT + "个方块的时间: ");
            System.out.print(System.currentTimeMillis() - s);
            System.out.println("ms");

            s = System.currentTimeMillis();
            for (int x = 0; x < xT; ++x) {
                for (int y = 0; y < yT; ++y) {
                    for (int z = 0; z < zT; ++z) {
                        model.getBlock(x, y, z);
                    }
                }
            }
            System.out.print("读出后查找" + xT * yT * zT + "个方块的时间: ");
            System.out.print(System.currentTimeMillis() - s);
            System.out.println("ms");

            s = System.currentTimeMillis();
            for (int x = 0; x < xT; ++x) {
                for (int y = 0; y < yT; ++y) {
                    for (int z = 0; z < zT; ++z) {
                        model.setBlock(x, y, z, new Block(66, 66, 66, 66));
                    }
                }
            }
            System.out.print("读出后修改" + xT * yT * zT + "个方块的时间: ");
            System.out.print(System.currentTimeMillis() - s);
            System.out.println("ms");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
