package tmDataNew;

//import java.io.FileInputStream;
import java.io.IOException;

public class FVLen extends Field {
	byte countView;
	FVLen(/*FileInputStream f_input, */byte countView) {
	//	super(f_input);
		this.countView=countView;
	}
	public void load() throws IOException {
		byte buff[]=new byte[countView];
		ReadTMI.f_input.read(buff, 0, countView);
		long len=0;
		for(int i=0; i<countView; i++)
			len=len | (buff[i]<<(8*i)) & (0xff<<(8*i));
//		System.out.println("444444!!!!! len=" + len);
		ReadTMI.f_input.skip(len);
		ReadTMI.numByte+=countView;
		ReadTMI.numByte+=len;
	}
}
