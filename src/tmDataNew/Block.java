package tmDataNew;

import java.io.*;

//import java.lang.reflect.Array;
public class Block {
    private static final Exception MyException = null;
    DescrBlock dBlock;
    byte[] shkBit;
    byte[] shkByte;
    int lenBitValue;

    Block(DescrBlock dB) throws IOException {
        dBlock = dB;
        dBlock.blocksCount++;
        if ((dBlock.type == 0x20) && (dBlock.blocksCount % 50000 == 0))
            System.out.println("count Block20 = " + dBlock.blocksCount);
        lenBitValue = 0;
        //System.out.println("this.dBlock.blocksCount++ = " + this.dBlock.blocksCount);
        //System.in.read();
    }

    public void printBuff(byte[] buff) {
        System.out.println("!!!!!!!!!!!!! =========================== ");
        for (int i = 0; i < buff.length; i++)
            System.out.print(String.format("%02x", buff[i]));
        System.out.println();
    }

    public int getBitField(byte[] buff, int beg, int count) {
        int len = 0;
        for (int i = 0; i < count; i++) {
            len = len | ((getbit(buff, beg + i) << i) & (0x1 << i));
        }
        //	System.out.println("lenBit = " + len);
        return len;
    }

    public void swap(byte[] buff) {
        for (int i = 0; i < buff.length / 2; i++) {
            byte tmp = buff[i];
            buff[i] = buff[buff.length - 1 - i];
            buff[buff.length - 1 - i] = tmp;
        }
    }

    public byte getbit(byte[] buff, int ind) {
        byte b = buff[buff.length - (ind / 8) - 1];
        int numBit = ind % 8;
        byte rez = (byte) ((b >> numBit) & 0x1);
        return rez;
    }

    public byte[] plusShkByteVP(byte[] b, int c) throws IOException {
        swap(b);
        byte[] tmpbuff = new byte[b.length + c];
        for (int i = 0; i < b.length; i++)
            tmpbuff[i] = b[i];
        ReadTMI.f_input.read(tmpbuff, b.length, c);
        ReadTMI.numByte += c;
        return tmpbuff;
        //	b=tmpbuff;
        //	printBuff(b);
    }


    public void fakeBitValue() throws IOException {
        int kek = ReadTMI.numByte;

        int iMassDF = dBlock.countByteFieldConstantPart;
        int mem;
        byte[] buff; //=new byte[400];
        byte b;
        try {
            for (int i = 0; i < dBlock.countBitFieldConstantPart; i++, iMassDF++) {
                if (dBlock.massDF[iMassDF].typeLen == 0)
                    lenBitValue += ((DF_ConstantLen) dBlock.massDF[iMassDF]).len;
                else {
                    System.out.println("It's do'nt, just!!!! =================");
                    System.in.read();
                    throw MyException;
                }
            }
            int iShkBit = lenBitValue;
            lenBitValue += dBlock.countBitFieldVariablePart;    // shkBitFieldVariablePart
            int count_byte = (lenBitValue + 7) / 8;
            //	System.out.println("count_byte shkBitFieldVariablePart===========================" + count_byte);
            buff = new byte[count_byte];
            ReadTMI.f_input.read(buff, 0, count_byte); // ������ ������. ��� shkBitFieldVariablePart
            ReadTMI.numByte += count_byte;
            int kek2 = ReadTMI.numByte;
            swap(buff);
            int count_before = dBlock.countBitFieldConstantPart + dBlock.countByteFieldConstantPart;
            //	System.out.println("count_before===========================" + count_before);
            for (int i = 0; i < dBlock.countBitFieldVariablePart; i++) {
                b = getbit(buff, iShkBit + i);
                if (b == 1) {
                    DescrField df = dBlock.massDF[count_before + i];
                    if (df.typeLen == 0) {
                        long len = ((DF_ConstantLen) df).len;
                        lenBitValue += len;
                        //		System.out.println("numByte = " + Integer.toHexString(ReadTMI.numByte) +
                        //			"  lenBitValue=" + lenBitValue + " len= " + len);
                    } else {
                        System.out.println("Need to do!!!!!! df.typeLen==1");
                        System.in.read();
                        //throw MyException;
                    }
                }
            }
            mem = lenBitValue;
            //		System.out.println("mem========================== "+ mem);
            lenBitValue += dBlock.countByteFieldVariablePart;
            count_byte = (lenBitValue + 7) / 8 - count_byte;
            //	System.out.println("count_byte=========================== " + count_byte);
            buff = plusShkByteVP(buff, count_byte); // ������ ����� ����� ���. �� ����� ����.
            //	printBuff(buff);
            //		System.out.println("numByte=" + Integer.toHexString(ReadTMI.numByte));
            swap(buff);
            count_byte = (lenBitValue + 7) / 8;

            //	printBuff(buff);

            count_before += dBlock.countBitFieldVariablePart;        // for dBlock.massDF[count_before+i]
            //	System.out.println("!!!!!!!!!!!!! =========================== count_before=" + count_before);
            for (int i = 0; i < dBlock.countByteFieldVariablePart; i++) {
                b = getbit(buff, mem + i);
                //			System.out.println("b=" + (b&0xff));
                if (b == 1) {
                    //System.out.println("!!!!!!!!!!!!! =========================== mem=" + mem);
                    DescrField df = dBlock.massDF[count_before + i];
                    //	System.out.println("!!!!!!!!!!!!! =========================== ");
                    //	df.print();		// ������ ����������� ����
                    //	System.out.println("!!!!!!!!!!!!! =========================== ");
                    if (df.typeLen == 0) {
                        long len = ((DF_ConstantLen) df).len;
                        ReadTMI.f_input.skip(len);
                        ReadTMI.numByte += len;
                        int kek3 = ReadTMI.numByte;
                        //			System.out.println("numByte=" + Integer.toHexString(ReadTMI.numByte));
                    } else if (df.typeLen == 1) {

                        if (((DF_VariableLen) df).view == 1) {
                            //System.out.println("====== ((DF_VariableLen)df).view == 1 ===================== ");
                            //System.out.println("count_byte=========================== " + count_byte);
                            lenBitValue += ((DF_VariableLen) df).countView;
                            //	System.out.println("lenBitValue=========================== " + lenBitValue);
                            count_byte = (lenBitValue + 7) / 8 - count_byte;
                            //	System.out.println("count_byte this this !!!!!!!!!=========================== " + count_byte);
                            buff = plusShkByteVP(buff, count_byte);
                            //		printBuff(buff);
                            //		System.out.println("numByte=" + Integer.toHexString(ReadTMI.numByte));
                            swap(buff);
                            //	printBuff(buff);
                            int len = getBitField(buff, lenBitValue - ((DF_VariableLen) df).countView, ((DF_VariableLen) df).countView);
                            ReadTMI.f_input.skip(len);
                            ReadTMI.numByte += (len);
                            int kek4 = ReadTMI.numByte;
                            //		System.out.println("!!!!!  numByte=" + Integer.toHexString(ReadTMI.numByte));
                        } else {

                            byte[] buffView = new byte[((DF_VariableLen) df).countView];
                            ReadTMI.f_input.read(buffView, 0, buffView.length);
                            int len = buffView[0]; //!!!!!!!!!!!!!!
                            ReadTMI.f_input.skip(len);
                            ReadTMI.numByte += (len + 1);
                            int kek5 = ReadTMI.numByte;
                            //		System.out.println("!!!!!  numByte=" + Integer.toHexString(ReadTMI.numByte));
                        }
                    } else if (df.typeLen == 2) {
                        //	System.out.println("3333!!!! from Block.load(pr).bitValue()");
                        //	dBlock.print();
                        //		DescrField df=dBlock.massDF[count_before+i];
                        Field f = new FBlock(((DF_Block) df).typeBlockField);
                        f.load();
                    } else throw new Exception("No this ");
                }
            }
        } catch (Exception e) {
            System.out.println(e + " It's do'nt, just!!!! =================");
            System.in.read();
            return;
        }
    }

    public void bitValue() throws IOException {    // create len for Bit_Value

        if (this.dBlock.type == 0x20) {
            fakeBitValue();
        } else {
            int kek = ReadTMI.numByte;

            int iMassDF = dBlock.countByteFieldConstantPart;
            int mem;
            byte[] buff; //=new byte[400];
            byte b;
            try {
                for (int i = 0; i < dBlock.countBitFieldConstantPart; i++, iMassDF++) {
                    if (dBlock.massDF[iMassDF].typeLen == 0)
                        lenBitValue += ((DF_ConstantLen) dBlock.massDF[iMassDF]).len;
                    else {
                        System.out.println("It's do'nt, just!!!! =================");
                        System.in.read();
                        throw MyException;
                    }
                }
                int iShkBit = lenBitValue;
                lenBitValue += dBlock.countBitFieldVariablePart;    // shkBitFieldVariablePart
                int count_byte = (lenBitValue + 7) / 8;
                //	System.out.println("count_byte shkBitFieldVariablePart===========================" + count_byte);
                buff = new byte[count_byte];
                ReadTMI.f_input.read(buff, 0, count_byte); // ������ ������. ��� shkBitFieldVariablePart
                ReadTMI.numByte += count_byte;
                int kek2 = ReadTMI.numByte;
                swap(buff);
                int count_before = dBlock.countBitFieldConstantPart + dBlock.countByteFieldConstantPart;
                //	System.out.println("count_before===========================" + count_before);
                for (int i = 0; i < dBlock.countBitFieldVariablePart; i++) {
                    b = getbit(buff, iShkBit + i);
                    if (b == 1) {
                        DescrField df = dBlock.massDF[count_before + i];
                        if (df.typeLen == 0) {
                            long len = ((DF_ConstantLen) df).len;
                            lenBitValue += len;
                            //		System.out.println("numByte = " + Integer.toHexString(ReadTMI.numByte) +
                            //			"  lenBitValue=" + lenBitValue + " len= " + len);
                        } else {
                            System.out.println("Need to do!!!!!! df.typeLen==1");
                            System.in.read();
                            //throw MyException;
                        }
                    }
                }
                mem = lenBitValue;
                //		System.out.println("mem========================== "+ mem);
                lenBitValue += dBlock.countByteFieldVariablePart;
                count_byte = (lenBitValue + 7) / 8 - count_byte;
                //	System.out.println("count_byte=========================== " + count_byte);
                buff = plusShkByteVP(buff, count_byte); // ������ ����� ����� ���. �� ����� ����.
                //	printBuff(buff);
                //		System.out.println("numByte=" + Integer.toHexString(ReadTMI.numByte));
                swap(buff);
                count_byte = (lenBitValue + 7) / 8;

                //	printBuff(buff);

                count_before += dBlock.countBitFieldVariablePart;        // for dBlock.massDF[count_before+i]
                //	System.out.println("!!!!!!!!!!!!! =========================== count_before=" + count_before);
                for (int i = 0; i < dBlock.countByteFieldVariablePart; i++) {
                    b = getbit(buff, mem + i);
                    //			System.out.println("b=" + (b&0xff));
                    if (b == 1) {
                        //System.out.println("!!!!!!!!!!!!! =========================== mem=" + mem);
                        DescrField df = dBlock.massDF[count_before + i];
                        //	System.out.println("!!!!!!!!!!!!! =========================== ");
                        //	df.print();		// ������ ����������� ����
                        //	System.out.println("!!!!!!!!!!!!! =========================== ");
                        if (df.typeLen == 0) {
                            long len = ((DF_ConstantLen) df).len;
                            ReadTMI.f_input.skip(len);
                            ReadTMI.numByte += len;
                            int kek3 = ReadTMI.numByte;
                            //			System.out.println("numByte=" + Integer.toHexString(ReadTMI.numByte));
                        } else if (df.typeLen == 1) {

                            if (((DF_VariableLen) df).view == 1) {
                                //System.out.println("====== ((DF_VariableLen)df).view == 1 ===================== ");
                                //System.out.println("count_byte=========================== " + count_byte);
                                lenBitValue += ((DF_VariableLen) df).countView;
                                //	System.out.println("lenBitValue=========================== " + lenBitValue);
                                count_byte = (lenBitValue + 7) / 8 - count_byte;
                                //	System.out.println("count_byte this this !!!!!!!!!=========================== " + count_byte);
                                buff = plusShkByteVP(buff, count_byte);
                                //		printBuff(buff);
                                //		System.out.println("numByte=" + Integer.toHexString(ReadTMI.numByte));
                                swap(buff);
                                //	printBuff(buff);
                                int len = getBitField(buff, lenBitValue - ((DF_VariableLen) df).countView, ((DF_VariableLen) df).countView);
                                ReadTMI.f_input.skip(len);
                                ReadTMI.numByte += (len);
                                int kek4 = ReadTMI.numByte;
                                //		System.out.println("!!!!!  numByte=" + Integer.toHexString(ReadTMI.numByte));
                            } else {

                                byte[] buffView = new byte[((DF_VariableLen) df).countView];
                                ReadTMI.f_input.read(buffView, 0, buffView.length);
                                int len = buffView[0]; //!!!!!!!!!!!!!!
                                ReadTMI.f_input.skip(len);
                                ReadTMI.numByte += (len + 1);
                                int kek5 = ReadTMI.numByte;
                                //		System.out.println("!!!!!  numByte=" + Integer.toHexString(ReadTMI.numByte));
                            }
                        } else if (df.typeLen == 2) {
                            //	System.out.println("3333!!!! from Block.load(pr).bitValue()");
                            //	dBlock.print();
                            //		DescrField df=dBlock.massDF[count_before+i];
                            Field f = new FBlock(((DF_Block) df).typeBlockField);
                            f.load();
                        } else throw new Exception("No this ");
                    }
                }
            } catch (Exception e) {
                System.out.println(e + " It's do'nt, just!!!! =================");
                System.in.read();
                return;
            }
        }
        //	System.out.println("lenBitValue=" + lenBitValue);
    }

    public static int getByte(byte[] buff) throws IOException {
        ReadTMI.f_input.read(buff, 0, 1);
        ReadTMI.numByte++;
        return buff[0];
    }

    public void parseTMData() throws IOException {
        System.out.println(this);
        int blockLen = 23;
        byte[] kekBuff = new byte[blockLen];
        for (var i = 0; i < blockLen; i++) {
            int byt = getByte(kekBuff);
            System.out.println(byt);
        }
    }


    public void load() throws java.lang.NullPointerException, IOException {
        if (this.dBlock.type == 32) {
            System.out.println("kek");
        }
        Field f;
        int iMassDF = 0;
        int kek = ReadTMI.numByte;
        for (int i = 0; i < dBlock.countByteFieldConstantPart; i++, iMassDF++) {
            if (dBlock.massDF[iMassDF].typeLen == 0)
                f = new FCLen(((DF_ConstantLen) dBlock.massDF[iMassDF]).len);
            else if (dBlock.massDF[iMassDF].typeLen == 1)
                f = new FVLen(((DF_VariableLen) dBlock.massDF[iMassDF]).countView);
            else if (dBlock.massDF[iMassDF].typeLen == 2) {
                //System.out.println("Yes!!! dBlock.massDF[iMassDF].typeLen==2");
                f = new FBlock(((DF_Block) dBlock.massDF[iMassDF]).typeBlockField);
            } else throw new IOException("My error!!!!!!!!!!!!!!!");
            f.load();
            int kek2 = ReadTMI.numByte;
            //System.out.println("numByte3=" + Integer.toHexString(ReadTMI.numByte));
        }
        bitValue();


//		System.out.println("End of Block " + Integer.toHexString((dBlock.type)&0xff) + " ======================================");
    }
}

