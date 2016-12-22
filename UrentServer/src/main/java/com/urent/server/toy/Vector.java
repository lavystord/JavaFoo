package com.urent.server.toy;

public class Vector {
	 private double deltaX, deltaY;

	public Vector(double finalDeltaX, double finalDeltaY) {
		
		this.deltaX = finalDeltaX;
		this.deltaY = finalDeltaY;
	}

	public double getDeltaX() {
		return deltaX;
	}

	public double getDeltaY() {
		return deltaY;
	}
	public String toString(){
		return "["+this.getDeltaX()+" "+this.getDeltaY()+"]";
	}
	public double magnitude(){
		return Math.sqrt(this.getDeltaX()*this.getDeltaX()+this.getDeltaY()*this.getDeltaY());
	}
	public Vector deflectX(){
		return new Vector(-this.getDeltaX(),this.getDeltaY());
	}
	public Vector deflectY(){
		return new Vector(this.getDeltaX(),-this.getDeltaY());
	}
	public Vector plus(Vector a){
		return new Vector(this.deltaX+a.deltaX,this.deltaY+a.deltaY);
	}
	public Vector minus(Vector x){
		return new Vector(this.deltaX-x.deltaX,this.deltaY-x.deltaY);
	}
	public Vector scale(double factor){
		return new Vector(this.getDeltaX()*factor,this.getDeltaY()*factor);
	}
	public Vector rescale(double magnitude){
		double original=this.magnitude();
		double percent=magnitude/original;
		if(percent==0){
			return new Vector(magnitude,0);
		}
		else{
		return this.scale(percent);
		}
	}

	public static void main(String[] args) {
		Vector a = new Vector(2.5, 1.2);
		Vector b = new Vector(1.5, 2.3);
		Vector c=a.plus(b);
		System.out.println(c.getDeltaX());
		System.out.println(c.getDeltaY());
		c = a.minus(b);
		System.out.println(c.getDeltaX());
		System.out.println(c.getDeltaY());
	}
	
}
