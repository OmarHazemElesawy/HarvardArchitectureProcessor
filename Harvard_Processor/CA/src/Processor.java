import java.util.ArrayList;
import java.util.Arrays;
import java.util.Hashtable;





public class Processor {
	public static Hashtable<String, Byte> GPRS = new Hashtable<String, Byte>();
	public static Short PC = 0;
	public static byte SREG = 0;
	public static int Cycle = 1;
	public static int y = 0;
	public static String display ="";
	public static ArrayList<Short[]> InstructionsFetch = new ArrayList<Short[]>();	
	public static ArrayList<Byte[]> InstructionsDecode = new ArrayList<Byte[]>();
	
	public Boolean isFull() {
		if(GPRS.size() == 64) {
			return true;
		}
		else {
			return false;
		}
	}
	static int ZeroC;
	public static int[] DecToBinArr(int dec) {
		String x;
		if(dec < 0) {
			int dec2 = (dec & 0b00000000000000000000000011111111);					 
			x = Integer.toBinaryString(dec2);
		}
		else {
			x = Integer.toBinaryString(dec);
		}
		
		int[] binaryArr = new int[8];
		int j = 0;
		ZeroC = 0;
		int len2 = 8 - x.length();
		for (int i = 0; i < len2; i++) {
			binaryArr[i] = 0;
			ZeroC++;
		}
		for(int i = len2 ; i < 8  ; i++) {
			binaryArr[i] = Integer.parseInt(x.charAt(j)+"");
			j++;
		}

		
		return binaryArr;
	}
	
	public static byte BinArrToDec(int[] Arr) {
		int res = 0;
		int j = 0;
		for (int i = Arr.length - 1; i >= 0; i--) {
			if(Arr[i] == 1) {
				res = res + (int) Math.pow(2, j);
			}
			j++;
		}
		return (byte) res;
	}
	
	public ArrayList<Short> getIns(ArrayList<Short[]> Instructions){
		ArrayList<Short> Ins = new ArrayList<Short>();
		for (int j = 0; j < Instructions.size(); j++) {
			Ins.add(Instructions.get(j)[0]);
		}
		return Ins;
	}
	
	
	
	public static boolean getCarry() {
		byte Carry = (byte) (SREG & 0b00010000);
		byte shift = (byte) (Carry >> 4);
		boolean flag;
		if(shift == 1) {
			flag = true;
		}
		else {
			flag = false;
		}
		return flag;
	}
	
	
	public static void setCarry(Short res) {
		int[] arr = DecToBinArr(SREG);
		if(res > Byte.MAX_VALUE || res < Byte.MIN_VALUE) {
			arr[3] = 1;
		}
		else {
			arr[3] = 0;
		}
		SREG = BinArrToDec(arr);
	}
	
	public static boolean getOverFlow() {
		byte Carry = (byte) (SREG & 0b00001000);
		byte shift = (byte) (Carry >> 3);
		boolean flag;
		if(shift == 1) {
			flag = true;
		}
		else {
			flag = false;
		}
		return flag;
	}
	public static void setOverFlow(byte x, byte y) {
		int c = x & y | (x ^ y);
		int[] arr = DecToBinArr(SREG);
		int[] carrys = DecToBinArr(c);
		if((carrys[0] ^ carrys[1]) == 1) {
			arr[4] = 1;	
		}
		else {
			arr[4] = 0;	
		}
		SREG = BinArrToDec(arr);
	}
	
	public static boolean getNegative() {
		byte Carry = (byte) (SREG & 0b00000100);
		byte shift = (byte) (Carry >> 2);
		boolean flag;
		if(shift == 1) {
			flag = true;
		}
		else {
			flag = false;
		}
		return flag;
	}
	
	public static void setNegative(byte res) {
		int[] arr = DecToBinArr(SREG);
		
		if(res < 0) {
			arr[5] = 1;
			
		}
		else {
			arr[5] = 0;
		}
		SREG = BinArrToDec(arr);
	}
	
	public static boolean getSign() {
		byte Carry = (byte) (SREG & 0b00000010);
		byte shift = (byte) (Carry >> 1);
		boolean flag;
		if(shift == 1) {
			flag = true;
		}
		else {
			flag = false;
		}
		return flag;
	}
	
	public static void setSign() {
		int[] arr = DecToBinArr(SREG);
		arr[6] = arr[5] ^ arr[4];
		SREG = BinArrToDec(arr);
	}
	
	public static boolean getZero() {
		byte Carry = (byte) (SREG & 0b00000001);
		boolean flag;
		if(Carry == 1) {
			flag = true;
		}
		else {
			flag = false;
		}
		return flag;
	}
	
	public static void setZero(byte res) {
		int[] arr = DecToBinArr(SREG);
		if(res == 0) {
			arr[7] = 1;
		}
		else {
			arr[7] = 0;
		}
		SREG = BinArrToDec(arr);
	}
	
	
	
	public static byte Add(Byte R1, Byte R2)  {
		Short x =  (short) (R1 + R2);
		R1 = (byte) (R1 + R2);
		setCarry(x);
		setOverFlow(R1,R2);
		setNegative(R1);
		setSign();
		setZero(R1);

		return R1;
	}
	
	public static byte Subtract(Byte R1, Byte R2)  {
		Short x = (short) (R1 - R2);
		R1 = (byte) (R1 - R2);
		
		setCarry(x);
		setOverFlow(R1, R2);
		setNegative(R1);
		setSign();
		setZero(R1);

		return R1;
	}
	
	public static byte Multiply(Byte R1, Byte R2)  {
		R1 = (byte) (R1 * R2);
		Short x = (short) (R1 * R2);
		setCarry(x);
		setNegative(R1);
		setZero(R1);

		return R1;
	}
	
	public static byte ExclusiveOr(Byte R1, Byte R2)  {
		R1 = (byte) (R1 ^ R2);
		setNegative(R1);
		setZero(R1);

		return R1;
	}
	
	public static void BranchRegister(Byte R1, Byte R2)  {
		int[] x1 = DecToBinArr(R1);
		int xZeros = ZeroC;
		int[] y1 = DecToBinArr(R2);
		int yZeros = ZeroC;
		
		int len = (x1.length - xZeros) + (y1.length - yZeros);
		
		int[] res = new int[len];
		int j = 0;
		for(int i = xZeros ; i < x1.length;i++) {
			res[j] = x1[i];
			j++;
		}
		for(int i = yZeros ; i < y1.length;i++) {
			res[j] = y1[i];
			j++;
		}
		
		Short resN = (short) BinArrToDec(res);
		
		PC = (short) (resN-1);
		InstructionsFetch.remove(0);
	}
	
	public static void MoveImmediate(String R1, Byte IMM) {
		GPRS.put(R1, IMM);
	}
	
	public static void BranchifEqualZero(String R1, Byte IMM) {
		byte x = GPRS.get(R1);
		if(x == 0) {
			PC = (short) (PC + IMM -2) ;
			InstructionsFetch.remove(0);
		}	
	}
	
	public static void AndImmediate(String R1, Byte IMM) {
		byte x = GPRS.get(R1);
		x = (byte) (x & IMM);
		setNegative(x);
		setZero(x);
		GPRS.put(R1,x);
	}
	
	public static void ShiftArithmeticLeft(String R1, Byte IMM) {
		byte tmp = (byte) (GPRS.get(R1) << IMM);
		GPRS.put(R1,tmp);
	}
	
	public static void ShiftArithmeticRight(String R1, Byte IMM) {
		byte tmp = (byte) (GPRS.get(R1) >> IMM);
		GPRS.put(R1,tmp);
	}
	
	public static void LoadtoRegister(String R1, Byte Address) {
		byte adr = DataMem.Data.get("Mem"+Address);
		GPRS.put(R1,adr);
	}
	
	public static void StorefromRegister(String R1, Byte Address) {
		byte x = GPRS.get(R1);
		DataMem.put("Mem"+Address, x);
	}
	
	
	public static void ALU(byte operandA, byte operandB, byte operation,boolean negative) {
		String R = "R" + operandA;
		byte r = GPRS.get(R);
		String R2 = "R" + operandB;
		byte r2 = GPRS.get(R2);
		byte res;
		
		switch (operation) {
		case 0:
			res = Add(r, r2);
			GPRS.put(R, res);
			break;
		case 1:
			res = Subtract(r, r2);
			GPRS.put(R, res);
			break;
		case 2:
			res = Multiply(r, r2);
			GPRS.put(R, res);
			break;
		case 3:
			if(negative)
				operandB=(byte)(operandB*-1);
			MoveImmediate(R, operandB);
			break;
		case 4:
			BranchifEqualZero(R, operandB);
			break;
		case 5:
			AndImmediate(R, operandB);
			break;
		case 6:
			res = ExclusiveOr(r, r2);
			GPRS.put(R, res);
			break;
		case 7:
			BranchRegister(r, r2);
			break;
		case -8:
			ShiftArithmeticLeft(R, operandB);
			break;
		case -7:
			ShiftArithmeticRight(R, operandB);
			break;
		case -6:
			LoadtoRegister(R, operandB);
			break;
		case -5:
			StorefromRegister(R, operandB);
			break;			
		default:
			break;
		}
	}
	
	
	
	public static void fetch() {
		short instruction = 0;		
        instruction = InstructionMem.Instructions.get(PC)[0];
        Short[] tmp = InstructionMem.Instructions.get(PC);
        InstructionsFetch.add(tmp);
        System.out.print(" Instruction "+InstructionMem.Instructions.get(PC)[1]);
	}
	
	public static void decode() {
		short instruction = InstructionsFetch.get(0)[0];
		System.out.print(" Instruction "+InstructionsFetch.get(0)[1]+" || ");
		byte insNum = Byte.parseByte(InstructionsFetch.get(0)[1]+"");
		
		InstructionsFetch.remove(0);
		
		short Opcode = 0;  // bits15:12		
		short R1 = 0;	  // bits11:6		
		short R2 = 0;	  // bits5:0
		short Immediate = 0;//bits5:0
		
		
		Opcode = (short) (instruction & 0b1111000000000000);
		Opcode = (byte) (Opcode >> 12);
		
		
		R1 = (short) (instruction & 0b0000111111000000);
		R1 = (byte) (R1 >> 6);
		
		R2 = (byte) (instruction & 0b0000000000111111);

		
		Immediate = (byte) (instruction & 0b0000000000111111);

        
        Byte[] tmp = {(byte)R1, (byte)R2, (byte)Opcode,insNum};
        InstructionsDecode.add(tmp);
        
	}
	
	public static void Execute() {
		display = "";
		Byte[] tmp = InstructionsDecode.get(0);
		System.out.print(" Instruction "+InstructionsDecode.get(0)[3] + " || ");
		InstructionsDecode.remove(0);
		if(tmp[2] == 0 || tmp[2] == 1 || tmp[2] == 2 || tmp[2] == 6 || tmp[2] == 7) {
			String R = "R" + tmp[0];
			byte r = GPRS.get(R);
			String R2 = "R" + tmp[1];
			byte r2 = GPRS.get(R2);
			display += "\nbefore execution";
			display += "\nfirst operand "+ R +" : " + r;
			display += "  , Second operand "+ R2 +" : " + r2;
			ALU(tmp[0],tmp[1],tmp[2],false);
			R = "R" + tmp[0];
			r = GPRS.get(R);
			R2 = "R" + tmp[1];
			r2 = GPRS.get(R2);
			display += "\n---------------";
			display += "\nAfter execution";
			display += "\nfirst operand "+ R +" : " + r;
			display += " , Second operand "+ R2 +" : " + r2 + "\n";
		}
		else {
			if(tmp[2] == -5 || tmp[2] == -6) {
				
				String R = "R" + tmp[0];
				byte r = GPRS.get(R);
				String Add = "Mem" + tmp[1];
				byte r2 =0;
				if(DataMem.Data.containsKey(Add)) {
					r2 = DataMem.Data.get(Add);
				}
				display += "\nbefore execution";
				display += "\n"+ R +" : " + r;
				display += "  , "+ Add +" : " + r2;
				ALU(tmp[0],tmp[1],tmp[2],false);
				R = "R" + tmp[0];
				r = GPRS.get(R);
				Add = "Mem" + tmp[1];

				if(tmp[2] == -5) {
					display += "\n---------------";
					display += "\nAfter execution";
					display += "\nStore From "+ R +" : " + r;
					display += "  , In "+ Add +" : " + r2 + "\n";
				}
				else {
					display += "\n---------------";
					display += "\nAfter execution";
					display += "\nLoad to "+ R +" : " + r;
					display += "  , From "+ Add +" : " + r2 + "\n";
				}
				
			}
			else {
				String R = "R" + tmp[0];
				byte r = GPRS.get(R);
				if(tmp[2] ==3) {
					if(InstructionMem.negative.contains((short)tmp[3])) {
						ALU(tmp[0],tmp[1],tmp[2],true);
					display += "\nbefore execution";
					display += "\n"+ R +" : " + r;
					display += "  , Immediate "+" : -" + tmp[1];
					}
					else {
						ALU(tmp[0],tmp[1],tmp[2],false);
						display += "\nbefore execution";
						display += "\n"+ R +" : " + r;
						display += "  , Immediate "+" : " + tmp[1];
					}
				}
				else
					ALU(tmp[0],tmp[1],tmp[2],false);
				R = "R" + tmp[0];
				r = GPRS.get(R);
				if(InstructionMem.negative.contains((short)tmp[3])) {
				display += "\n---------------";
				display += "\nAfter execution";
				display += "\n"+ R +" : " + r;
				display += "  , Immediate "+" :  -" + tmp[1] + "\n";
				}
				else {
					display += "\n---------------";
					display += "\nAfter execution";
					display += "\n"+ R +" : " + r;
					display += "  , Immediate "+" : " + tmp[1] + "\n";
				}
			}
		}
		
	}
	
	public void runProgram(String filename) {
		for (int i = 0; i < 64; i++) {
			GPRS.put("R" + i, (byte)0);
		}
		InstructionMem I = new InstructionMem();
		I.Parse(filename);
		while(PC < InstructionMem.Instructions.size() + 2) {
			System.out.println("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
			System.out.print("Cycle " + Cycle + " --->");
			if(y >= 2 && !(InstructionsDecode.isEmpty())) {
				System.out.print(" Execute " );
				Execute();
			}
			if(y >= 1 && !(PC >= InstructionMem.Instructions.size() + 1) && !(InstructionsFetch.isEmpty())) {
				System.out.print(" Decode " );
				decode();
			}
			if(!(PC >= InstructionMem.Instructions.size())) {
				System.out.print(" Fetch " );
				fetch();
			}
			System.out.println("");
			System.out.println(display);
			Cycle++;
			PC++;
			y++;
		}
		
		System.out.println("----------------end of execution-------------------");
		System.out.println("Data Memory = "+DataMem.Data);
		ArrayList<Short> Ins = getIns(InstructionMem.Instructions);
		System.out.println("Instruction Memory = "+Ins);
		System.out.println("Registers File = "+GPRS);
		System.out.println("Status Register = "+ Arrays.toString(DecToBinArr(SREG)));
	}
	
	
	public static void main(String[] args) {
		
		
		Processor P = new Processor();
		P.runProgram("Loop.txt");//Assembly code implementing a loop
		
		
	}
}

