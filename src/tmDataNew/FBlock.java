package tmDataNew;

import java.io.IOException;

public class FBlock extends Field {
	byte typeBlockField;
	FBlock(byte typeBlockField) {
		//super(f_input);
		this.typeBlockField=typeBlockField;
	}
	public void load() throws IOException {		
		byte buff[]=new byte[90000];
		ReadTMI.f_input.read(buff,0,1);
		ReadTMI.numByte+=1;
		int type=buff[0]&0xff;
	//	System.out.println("typeBlock=" + Integer.toHexString((int)(type&0xff)) +
		//		" numByte=" + Integer.toHexString(ReadTMI.numByte));
		if(type==1) {
			ReadTMI.readDescrBlock();		
			ReadTMI.f_input.read(buff,0,1);	// опять формируем тип блока (любой, кроме 01)
			ReadTMI.numByte+=1;
			type=buff[0]&0xff;
		//	System.out.println("typeBlock=" + Integer.toHexString((int)(type&0xff)) +
			//		" numByte=" + Integer.toHexString(ReadTMI.numByte));
		}
		Block block=new Block(ReadTMI.arrDB.get(type));
		block.load();
	}
}
