package app.structure;

import java.util.HashMap;
import java.util.Map;

public class Chunk {
    public int x, z;
    private int size;
    private Section[] sections;
    private int bitMask = 0;
    private Map<Integer, Integer> posToIndex = new HashMap<>();

    public Chunk(int x, int z) {
        assert ((x % 16 | z % 16) == 0);
        this.x = x;
        this.z = z;
    }

    public int getSize() {
        return size;
    }

    public boolean containsNull() {
        if (sections == null) return false;
        for (Section section : sections) {
            if (section == null) return true;
        }
        return false;
    }

    public void setSection(Section section, int i) {
        int k = 1 << i;
        if (posToIndex.containsKey(i)) {
            sections[posToIndex.get(i)] = section;
        } else {
            bitMask = (bitMask | k);
            int j = bitMask;
            int c = 0;
            while (k > 1) {
                c += ((k & 1) ^ (j & 1));
                k >>= 1;
                j >>= 1;
            }
            Section[] temp = sections;
            sections = new Section[++size];
            int p = 0;
            for (int ptr = 0; ptr < size; ++ptr) {
                if (ptr != c) {
                    sections[ptr] = temp[p++];
                }
            }
            sections[c] = section;
            posToIndex.put(i, c);
        }
    }

    public Section getSection(int i) {
        int k = 1 << i;
        if ((bitMask & k) == 0)
            return null;
        if (posToIndex.containsKey(i))
            return sections[posToIndex.get(i)];
        else {
            int j = bitMask;
            int c = 0;
            while (k > 1) {
                c += ((k & 1) ^ (j & 1));
                k >>= 1;
                j >>= 1;
            }
            posToIndex.put(c, i);
            return sections[c];
        }
    }

    public int[] getInts() {
        // chunkSize, bitMask, x, z, sections
        int[] res = new int[size * Section.size + 4];
        int ptr = 0;
        res[ptr++] = this.size;
        res[ptr++] = this.bitMask;
        res[ptr++] = this.x;
        res[ptr++] = this.z;
        for (Section section : this.sections) {
            int[] sectionInts = section.getInts();
            for (int i = 0; i < Section.size; ++i) {
                res[ptr++] = sectionInts[i];
            }
        }
        return res;
    }

    public void fromInts(int[] data) {
        int ptr = 0;
        this.size = data[ptr++];
        this.bitMask = data[ptr++];
        this.x = data[ptr++];
        this.z = data[ptr++];
        sections = new Section[size];
        for (int i = 0; i < size; ++i) {
            if ((bitMask & (1 << i)) == 0)
                continue;
            Section section = new Section();
            int[] k = new int[Section.size];
            int bptr = 0;
            for (int j = 0; j < Section.size; ++j) {
                k[bptr++] = data[ptr++];
            }
            section.fromInts(k);
            sections[i] = section;
        }
    }

    public Section[] getSections() {
        return sections;
    }

    public void setSections(Section[] sections) {
        this.sections = sections;
    }

    public int getBitMask() {
        return bitMask;
    }

    public void setBitMask(int bitMask) {
        this.bitMask = bitMask;
    }
}