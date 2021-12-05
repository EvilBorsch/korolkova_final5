package tmDataNew;

//import java.io.*;

public class DescrBlock {
	public int blocksCount;	// кол-во блоков данного типа
	int type;
	byte countByteFieldConstantPart;
	byte countBitFieldConstantPart;
	byte countBitFieldVariablePart;
	byte countByteFieldVariablePart;
	DescrField[] massDF;
	int summDF;
	byte[] buff;
	DescrBlock(byte[] b) {
		blocksCount=0;
		buff=b;
		//System.out.println("buff=" + buff[2]);
		type=buff[0]&0xff;
		countByteFieldConstantPart=buff[1];
		countBitFieldConstantPart=buff[2];
		countBitFieldVariablePart=buff[3];
		countByteFieldVariablePart=buff[4];
		summDF=buff[1]&0xff;
		summDF+= buff[2]&0xff;
		summDF+= buff[3]&0xff;
		summDF+= buff[4]&0xff;		
		massDF=new DescrField[summDF];	// !!!!!!!!!!!!!!!!!!!
		//System.out.println("summDF=" + summDF);
		//System.out.println(massDF.length);
	}
	public void load() {
		int lenDF, indBuff=5, tmpIndBuff, iMassDF=0;
	//	System.out.println("summDF=" + summDF);
		try {
			int summBuf = summDF;
		while(summBuf>0) {
	//		System.out.println("while iMassDF=" + iMassDF + " summBuf=" + summBuf);
			summBuf--;
			lenDF=(buff[indBuff]&0xff) + ((buff[indBuff+1]<<8)&0xff00) + 
					((buff[indBuff+2]<<16)&0xff0000) + ((buff[indBuff+3]<<24)&0xff000000);
	//		System.out.println("lenDF=" + lenDF);
	//		System.in.read();
			tmpIndBuff=indBuff+=4;	
			if(buff[indBuff+1]==0) {	// constant len
				DF_ConstantLen DF_CL = new DF_ConstantLen();
				massDF[iMassDF]=DF_CL;
				indBuff+=2;
				DF_CL.len=buff[indBuff]&0xff;	// ???????????????
				indBuff+=8;
				if(DF_CL.len==0) {
					int itmp=summDF-summBuf-1;
					if(itmp < countByteFieldConstantPart)
						countByteFieldConstantPart--;
					else if(itmp < countBitFieldConstantPart)
						countBitFieldConstantPart--;
					else if(itmp < countBitFieldVariablePart)
						countBitFieldVariablePart--;
					else
						countByteFieldVariablePart--;
					continue;
				}
			}
			else if(buff[indBuff+1]==1) {	//  Variable len
				DF_VariableLen DF_VL = new DF_VariableLen();
				DF_VL.view=buff[indBuff+2];
				DF_VL.countView=buff[indBuff+3];
				massDF[iMassDF]= DF_VL;
				indBuff+=4;
				
			}
			else if(buff[indBuff+1]==2) {	// block struct
				// !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
				DF_Block df_Block = new DF_Block();
				df_Block.typeBlockField=buff[indBuff+2];
				massDF[iMassDF]= df_Block;
				//System.out.println("        lenDF=" + lenDF);
				indBuff+=lenDF;
			//	System.out.println("==2 iMassDF=" + iMassDF + " summBuf=" + summBuf);
			}
	//		System.out.println(">2 iMassDF=" + iMassDF + " summBuf=" + summBuf);
			massDF[iMassDF].typeField=buff[tmpIndBuff];		
			massDF[iMassDF].typeLen=buff[tmpIndBuff+1];
			iMassDF++;
	//		System.out.println("iMassDF=" + iMassDF + " summDF=" + summDF);
	
		}
		summDF=iMassDF;
	//	System.out.println("        iMassDF=" + iMassDF + " summDF=" + summDF);
	//	print();
		}
		catch(Exception e) {
			System.out.println(e+ " from public void load()");
		}
	}

	public void print()
	{
		System.out.print(" type - " + Integer.toHexString(type&0xff));
		System.out.print(" CountByteFCP - " + countByteFieldConstantPart);
		System.out.print(" CountBitFCP - " + countBitFieldConstantPart);
		System.out.print(" CountBitFVP - " + countBitFieldVariablePart);
		System.out.println(" CountByteFVP - " + countByteFieldVariablePart);

		System.out.println("SUMMDF - " + summDF);

		for (int i = 0; i < summDF; i++)
		{
			massDF[i].print();
		}
		System.out.println();
	}

}
