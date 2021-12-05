package tmDataNew;
import java.io.*;
//import java.io.IOException;

public class Main {
	ReadTMI readTMI;
	FileInputStream f_input;
	Main(String nameFile) throws FileNotFoundException {
		//ReadTMI.f_input = new FileInputStream(nameFile);
		readTMI = new ReadTMI(nameFile);
	}
	public static void main(String[] args) 
			throws IOException, java.lang.NullPointerException, 
			java.lang.ArrayIndexOutOfBoundsException{
		Main obj = new Main(args[0]);
		obj.readTMI.load(); 
		obj.readTMI.print();
	}
}

