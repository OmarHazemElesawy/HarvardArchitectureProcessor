import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;
public class InstructionMem {
	
	//each row 16 bit (short)
	
	static int maxSize = 1024;
	public static ArrayList<Short[]> Instructions = new ArrayList<Short[]>();
	public static int PC = 0;
	public int start = 1;
	public static ArrayList<Short> negative=new ArrayList<Short>();
	
	public InstructionMem() {
		
	}
	
	public static boolean isFull() {
		if(Instructions.size() == maxSize) {
			return true;
		}
		else{
			return false;
		}
		
	}
	
	public void Parse(String filename) {
		try {
			String workingDirectory = System.getProperty("user.dir");
			String abFilePath = "";
			abFilePath = workingDirectory + File.separator +"src\\"+ filename;
			
			BufferedReader br = new BufferedReader(new FileReader(abFilePath));
			ArrayList<String[]> AllLines=new ArrayList<String[]>();
			String current=br.readLine();
			while(current !=null) {
				current = current + " " + start;
				AllLines.add(current.split(" "));
				start++;
				current = br.readLine();
				
			}
			br.close();
			
			Short InsNum = 0;
			Byte opp1; 
			Byte opp2;
			for (int i = 0; i < AllLines.size(); i++) {
				InsNum = 0;
				
				opp1 = Byte.parseByte((AllLines.get(i)[1]).substring(1)); 
				if((AllLines.get(i)[2]).charAt(0) == 'R') {
					opp2 = Byte.parseByte((AllLines.get(i)[2]).substring(1));
				}
				
				else {
					opp2 = Byte.parseByte(AllLines.get(i)[2]);
					
					if(opp2<0) {
						opp2=(byte)(opp2*-1);
						negative.add(Short.parseShort(AllLines.get(i)[3]));
					}
					}
				switch (AllLines.get(i)[0]) {
				case "ADD": {
					InsNum = (short) (InsNum + opp2);
					InsNum = (short) (InsNum + (opp1*64));
					InsNum = (short) (InsNum + (0*4096));
					break;
				}
				case "SUB": {
					InsNum = (short) (InsNum + opp2);
					InsNum = (short) (InsNum + (opp1*64));
					InsNum = (short) (InsNum + (1*4096));
					break;
				}
				case "MUL": {
					InsNum = (short) (InsNum + opp2);
					InsNum = (short) (InsNum + (opp1*64));
					InsNum = (short) (InsNum + (2*4096));
					break;
				}
				case "MOVI": {
					InsNum = (short) (InsNum + opp2);
					InsNum = (short) (InsNum + (opp1*64));
					InsNum = (short) (InsNum + (3*4096));
					break;
				}
				case "BEQZ": {
					InsNum = (short) (InsNum + opp2);
					InsNum = (short) (InsNum + (opp1*64));
					InsNum = (short) (InsNum + (4*4096));
					break;
				}
				case "ANDI": {
					InsNum = (short) (InsNum + opp2);
					InsNum = (short) (InsNum + (opp1*64));
					InsNum = (short) (InsNum + (5*4096));
					break;
				}
				case "EOR": {
					InsNum = (short) (InsNum + opp2);
					InsNum = (short) (InsNum + (opp1*64));
					InsNum = (short) (InsNum + (6*4096));
					break;
				}
				case "BR": {
					InsNum = (short) (InsNum + opp2);
					InsNum = (short) (InsNum + (opp1*64));
					InsNum = (short) (InsNum + (7*4096));
					break;
				}
				case "SAL": {
					InsNum = (short) (InsNum + opp2);
					InsNum = (short) (InsNum + (opp1*64));
					InsNum = (short) (InsNum + (8*4096));
					break;
				}
				case "SAR": {
					InsNum = (short) (InsNum + opp2);
					InsNum = (short) (InsNum + (opp1*64));
					InsNum = (short) (InsNum + (9*4096));
					break;
				}
				case "LDR": {
					InsNum = (short) (InsNum + opp2);
					InsNum = (short) (InsNum + (opp1*64));
					InsNum = (short) (InsNum + (10*4096));
					break;
				}
				case "STR": {
					InsNum = (short) (InsNum + opp2);
					InsNum = (short) (InsNum + (opp1*64));
					InsNum = (short) (InsNum + (11*4096));
					break;
				}
				
				default:
					throw new IllegalArgumentException("Unexpected value: " + AllLines.get(i)[0]);
				}
				Short[] tmp = {InsNum,Short.parseShort(AllLines.get(i)[3])};
				add(tmp);
			}
		}
		catch (Exception e) {
			// TODO: handle exception
		}
		
		
	}
	public static void add(Short[] ins) {
		if(!(isFull())) {
			Instructions.add(ins);
		}
		else {
			System.out.println("Instruction Memory is Full.");
		}
	}
	public static void main(String[] args)  {

	}
}
