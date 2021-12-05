package tmDataNew;

//import java.io.FileInputStream;
import java.io.IOException;

public class FCLen extends Field{
	long len;
	byte[] mass;
	FCLen(/*FileInputStream f_input, */long len) {
	//	super(f_input);
		this.len=len;
	}
	public void load() throws IOException {
		ReadTMI.f_input.skip(len);
		ReadTMI.numByte+=len;
	}
}
