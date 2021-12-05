package tmDataNew;
//import java.util.TreeSet;

import java.io.*;
import java.util.Map;
//import java.util.Map;
import java.util.TreeMap;

public class ReadTMI {
    public static FileInputStream f_input;
    public static int numByte;
    public static TreeMap<Integer, DescrBlock> arrDB = new TreeMap<Integer, DescrBlock>();

    ReadTMI(String nameFile/*FileInputStream f_input*/) throws FileNotFoundException { // throws ParserConfigurationException, SAXException, IOException {
        f_input = new FileInputStream(nameFile);
        numByte = 0;
    }

    public static void readDescrBlock() throws IOException {
        byte[] buff = new byte[90000];
        f_input.read(buff, 0, 4);
        int lenBlock = buff[0] & 0xff | (buff[1] << 8) & 0xff00 |
                (buff[2] << 16) & 0xff0000 | (buff[3] << 24) & 0xff000000;
        f_input.read(buff, 0, lenBlock);
        numByte += 4;
        numByte += lenBlock;
        DescrBlock dBlock = new DescrBlock(buff);
        //	System.out.println("numByte=" + Integer.toHexString(numByte));
        dBlock.load();
        arrDB.put(dBlock.type, dBlock);
        //System.out.println("numByte=" + Integer.toHexString(numByte));
    }


    public void load() throws IOException {
        byte[] buff = new byte[90000];
        int type;
        try {
            f_input.read(buff, 0, 10);
            numByte += 10;
            while (true) {
                f_input.read(buff, 0, 1);
                numByte += 1;
                type = buff[0] & 0xff;
                //	System.out.println("typeBlock=" + Integer.toHexString(type&0xff) +
                //		" numByte=" + Integer.toHexString(numByte));
                if (type == 1) {
                    readDescrBlock();
                } else {
                    int temp = numByte;
                    Block block = new Block(arrDB.get(type));
                    block.load();
                    if (type == 0x20) {
                        System.out.print("data:");
                        System.out.println(numByte - temp);
                    }

                }
                if (type == 0x83)        // ����� ������
                    break;
            }
            f_input.close();
        } catch (IOException ex) {
            System.out.println("Error!!! from ReadTMI: public void load()");
        }
    }

    public void print() {
        for (Map.Entry<Integer, DescrBlock> item : arrDB.entrySet())
            System.out.println(Integer.toHexString(item.getKey()) +
                    " " + Integer.toHexString(item.getValue().blocksCount));
    }
}
