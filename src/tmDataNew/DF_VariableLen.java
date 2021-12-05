package tmDataNew;
import java.io.*;

public class DF_VariableLen  extends DescrField {
	byte view;	// byte / bit
	byte countView;	// count byte / bit
	DF_VariableLen() {
	}
//	DF_VariableLen(/*FileInputStream f_input*/) {
	//	super(f_input);
//	}
/*	public void load() throws IOException {
		byte [] buff=new byte[1]; 
		f_input.read(buff,0,1);	// пусть в 1 байте len
		ReadTMI.numByte+=1;
		f_input.skip(buff[0]);
		ReadTMI.numByte+=buff[0];
		System.out.println("numByteFieldVariable=" + Integer.toHexString(ReadTMI.numByte));
	}*/
	public void print()
	{
		super.print();
		System.out.println("  view: " + view + "  countView: " + countView);
	}
}
