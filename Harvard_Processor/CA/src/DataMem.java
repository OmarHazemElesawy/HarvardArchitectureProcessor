import java.util.ArrayList;
import java.util.Hashtable;

public class DataMem {
	
	//each row is 8 bit (byte)
	
	static int maxSize = 2048;
	
	public static Hashtable<String,Byte> Data = new Hashtable<String,Byte>();
	public static int PC = 0;
	
	public DataMem() {
		
	}
	
	public static void put(String x,byte key) {
		if(!(isFull())) {
			Data.put(x, key);
		}
		else {
			System.out.println("Memory is Full.");
		}
	}
	
	public static boolean isFull() {
		if(Data.size() == maxSize) {
			return true;
		}
		else{
			return false;
		}
		
	}
	
	public static void main(String[] args) {
		
	}
}
