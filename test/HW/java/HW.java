import cls.A;
import java.util.Scanner;


public class HW {
	public HW() {
		System.out.println("..");
		
		System.out.println("Hello world2!");
		new A ();
		new A ();
		new A ();
		
		Scanner sc = new Scanner (System.in);
		int n = sc.nextInt();
		while (n != -1) {
			System.out.println(n);
			n = sc.nextInt();
		}
		
		//while (true);
			
	}
	public static void main(String[] args) {
		new HW();
	}
} 