package tmDataNew;

//import java.io.IOException;

public class DF_Block extends DescrField{
	byte typeBlockField;
	DF_Block() {
	}
/*	public void load() throws IOException{	// read of Block
	}*/
	public void print()
	{
		super.print();
		System.out.println("  typeBlockField: " + typeBlockField);
	}

}
