package tmDataNew;
import java.io.*;

public class DF_ConstantLen extends DescrField {
	public long len;
	DF_ConstantLen() {
	}
	DF_ConstantLen(/*FileInputStream f_input, */long len) {
		//super(f_input);
		this.len=len;
	}
/*	public void load() throws IOException{
		f_input.skip(len);
		ReadTMI.numByte+=len;
		System.out.println("numByteFieldConst=" + Integer.toHexString(ReadTMI.numByte));
	}*/

	public void print()
	{
		super.print();
		System.out.println("  len: " + len);
	}
}
