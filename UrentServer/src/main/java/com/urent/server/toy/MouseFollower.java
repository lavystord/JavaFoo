package com.urent.server.toy;

import java.awt.Color;
import java.io.File;
import java.io.FileOutputStream;
import java.io.PrintStream;
import java.util.Scanner;


public class MouseFollower {

	public static void main(String[] args) throws Exception {

		PrintStream out = new PrintStream(new FileOutputStream(new File("e:\\points.txt")));

		Scanner input = new Scanner(System.in);
//		int n = input.nextInt();
		int n = 100;
		double[] pastX = new double[n];
		double[] pastY = new double[n];
		StdDraw.setPenColor(Color.blue);
		int count = 0;
		while (true) {
			StdDraw.clear();
			//
			// Render one frame of your animation below here
			//

			double x = StdDraw.mouseX();
			double y = StdDraw.mouseY();
			pastX[count] = x;
			pastY[count] = y;

//			System.out.format("%3.6f\t%3.6f\n", x, y);

//			for(int i = 1; i <= n; i ++) {
//				StdDraw.filledCircle(pastX[(count+i)%n], pastY[(count+i)%n], 0.01);
//			}
/*			for(int i = 0; i < n; i ++) {
				StdDraw.filledCircle(pastX[(count+i)%n], pastY[(count+i)%n], 0.01);
			}*/

			count = count + 1;
			if (count >= pastX.length) {
				count = 0;
			}


//			for (int i = 0; i <n ; i++) {
//				//out.println(i+":("+pastX[count]+","+pastY[count]+") |");
//				out.format("%3.10f\t%3.10f\n", pastX[i], pastY[i]);
//			}
//			out.println();
//			out.println();

			int past = count;
			StdDraw.filledCircle(pastX[past], pastY[past], 0.05);

			//  
			// End of your frame
			//
			// Stdraw.show(..) achieves double buffering and
			//   avoids the tight spinning loop
			StdDraw.show(10);

		}

	}

}
