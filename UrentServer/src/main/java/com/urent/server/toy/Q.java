package com.urent.server.toy;

/*************************************************************************
 *  Compilation:  javac BouncingBall.java
 *  Execution:    java BouncingBall
 *  Dependencies: StdDraw.java
 *
 *  Implementation of a 2-d bouncing ball in the box from (-1, -1) to (1, 1).
 *
 *  % java BouncingBall
 *
 *************************************************************************/



import java.util.Scanner;

public class Q {
	
	public static void main(String[] args) {

//		ArgsProcessor ap = new ArgsProcessor(args);
		Scanner ap = new Scanner(System.in);
		int numberBalls = ap.nextInt();
		int numberIteration = ap.nextInt();

		// set the scale of the coordinate system
		StdDraw.setXscale(-1.0, 1.0);
		StdDraw.setYscale(-1.0, 1.0);

		// initial values
		double[] rx = new double[numberBalls];
		double[] ry = new double[numberBalls];
		double[] vx = new double[numberBalls];
		double[] vy = new double[numberBalls];

		for (int i = 0; i < numberBalls; ++i) {
			rx[i] = Math.random() * 0.9;
			ry[i] = Math.random() * 0.9;
			vx[i] = Math.random() / 50;
			vy[i] = Math.random() / 50;
		}

		double radius = 0.02;              // radius

		// main animation loop
		for (int c = 0; c < numberIteration; ++c) {
			for (int i = 0; i < numberBalls; ++i) {
				if (Math.abs(rx[i] + vx[i]) > 1.0 - radius) {
					vx[i] = -vx[i];
				}
				if (Math.abs(ry[i] + vy[i]) > 1.0 - radius) {
					vy[i] = -vy[i];
				}
			}

			for (int i = 0; i < numberBalls; ++i) {
				for (int j = i + 1; j < numberBalls; ++j) {
					if ((Math.sqrt(Math.pow((rx[i] - rx[j]), 2) + Math.pow((ry[i] - ry[j]), 2)) < 2 * radius)) {
						vx[i] = -vx[i];
						vy[i] = -vy[i];
						vx[j] = -vx[j];
						vy[j] = -vy[j];
					}
				}
			}
			// if (Math.sqrt(Math.pow((rx[i][j] - rx[i][j]),2)+Math.pow((ry[i][j] - ry[i][j]),2))< 2*radius) {
			// 	vx[j] = -vx[j];
			//	vy[j] = -vy[j];

			for (int i = 0; i < numberBalls; ++i) {
				// update position
				rx[i] = rx[i] + vx[i];
				ry[i] = ry[i] + vy[i];
			}

			// clear the background
			StdDraw.setPenColor(StdDraw.GRAY);
			StdDraw.filledSquare(0, 0, 1.0);

			// draw ball on the screen

			for (int l = 0; l < numberBalls; ++l) {
				StdDraw.setPenColor(StdDraw.BLACK);
				StdDraw.filledCircle(rx[l], ry[l], radius);
			}
			StdDraw.show(2);
		}

		// display and pause for 20 ms
		//StdDraw.show(1);
	}

}
