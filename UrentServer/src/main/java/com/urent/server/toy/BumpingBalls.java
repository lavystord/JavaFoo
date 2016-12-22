package com.urent.server.toy;


import java.util.Scanner;

public class BumpingBalls {
	
	public static void main(String[] args) {
	//	ArgsProcessor ap = new ArgsProcessor(args);
        Scanner ap=new Scanner(System.in);
		int balls=ap.nextInt();
		//int rounds=ap.nextInt();
		StdDraw.setXscale(-1.0, 1.0);
        StdDraw.setYscale(-1.0, 1.0);
        double[]rx=new double[balls];
        double[]ry=new double[balls];
        double[]vx=new double[balls];
        double[]vy=new double[balls];
        double radius=0.05;
        //init
        for(int i=0;i<balls;++i){
        	rx[i]=Math.random();
        	ry[i]=Math.random();
        	vx[i]=Math.random()*0.1;
        	vy[i]=Math.random()*0.1;
        }

        while (true) {
            for (int p = 0; p < balls; p++) {

                for (int q = p+1; q < balls; ++q) {

                    double distance = (rx[p] - rx[q]) * (rx[p] - rx[q]) + (ry[p] - ry[q]) * (ry[p] - ry[q]);
                    if (Math.sqrt(distance) < radius * 2) {
                        vx[p] = -vx[p];
                        vy[p] = -vy[p];
                        vx[q] = -vx[q];
                        vy[q] = -vy[q];
                    }


                }
                if (Math.abs(rx[p] + vx[p]) > 1.0 - radius) vx[p] = -vx[p];
                if (Math.abs(ry[p] + vy[p]) > 1.0 - radius) vy[p] = -vy[p];
                rx[p] = rx[p] + vx[p];
                ry[p] = ry[p] + vy[p];



            } // end for outter

            StdDraw.setPenColor(StdDraw.GRAY);
            StdDraw.filledSquare(0, 0, 1.0);
            for (int q = 0; q < balls; ++q) {

                // draw ball on the screen
                StdDraw.setPenColor(StdDraw.BLACK);
                StdDraw.filledCircle(rx[q], ry[q], radius);



            }
            // display and pause for 20 ms
            StdDraw.show(50);
         //   rounds--;
        } // end while
        
        
        
        
        
        
        
        
        
        
        
        
        
        
        
        
        
        
	}

}
