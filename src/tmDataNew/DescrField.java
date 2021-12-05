package tmDataNew;
import java.io.*;

public class DescrField {
	byte typeField;
	byte typeLen;
//	FileInputStream f_input;
	DescrField() {
	}
	//DescrField(/*FileInputStream f_input*/) {
	//	this.f_input = f_input;
	//}
/*	public void load()throws IOException {
		
	}*/

	public void print()
	{
		System.out.print(" typeField: " + String.format("%02x",typeField) + "  typeLen (block): " + typeLen);
	}

}
