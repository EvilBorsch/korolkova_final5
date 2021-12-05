package tmDataNew;

import java.io.FileWriter;
import java.sql.Time;

public class TmDat implements Comparable<TmDat> {
	int num;
	String name;
	long time;
	String razm;
	int attr;
	int type;
	TmDat() {
	}
	TmDat(String name, int num, long time, String dim, int type) {
		this.name = name;
		this.num = num;
		this.time = time;
		this.razm = dim;
		this.type = type;
	}
	public void add(String name, int num, long time, String dim, int type) {
		this.name = name;
		this.num = num;
		this.time = time;
		this.razm = dim;
		this.type = type;
	}
	public int compareTo(TmDat st) {
		if(name.compareTo(st.name)==0)
			return 1;
		return name.compareTo(st.name);
	}

	public void print(FileWriter f_output, int pr) {
		//System.out.println(name + " " + num); // + " " + time + " " + razm + " " + attr + " " + type + " ");
		try
		{
			if(pr == 0)
				f_output.write(" " + name + " " + num + " ");
			else
				f_output.write("             ");
			Time time2 = new Time(time);
			f_output.write(time2.toString() + "." + (time%1000) + " type=" + type);
		}
		 catch(Exception e) {
				System.out.println(e.toString()); 
		 } 
	}
}
