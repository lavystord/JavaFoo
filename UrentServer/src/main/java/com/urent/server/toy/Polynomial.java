package com.urent.server.toy;

import java.util.Iterator;
import java.util.LinkedList;

public class Polynomial {

	 private LinkedList<Double> list;

	/**
	 * Constructs a Polynomial with no terms yet.
	 */
	
	public Polynomial() {
		//
		// Set the instance variable (list) to be a new linked list of Double type
		//
		this.list = list; 
		list=new LinkedList<Double>();
		// FIXME
	}

	
	public String toString() {
		
	    if(list.size()==0){
	    	return "no list here";
	    }
	    
	    else{
	    	String result=""+list.getFirst();
		for(int i=1;i<list.size();++i){
			result=result+"+"+list.get(i)+"x^"+i;
		}
		return result;
	    }
		 // FIXME
	}

	public Polynomial addTerm(double coeff) {
		//
		// FIXME
		//
		this.list.add(coeff);
		return this;  // required by lab spec
	}

	public double evaluate(double x) {
		if (list.size()==0)
			return 0;
		else
			return calc(x,0);  // FIXME
	}
	public double calc(double x,int i){
		if(i==list.size()-1){
			return list.get(i);
		}
		else{
			return calc(x,i+1)*x+list.get(i);
		}
	}
	public Polynomial derivative() {
		
		Polynomial derive=new Polynomial();
		for(int i=1;i<this.list.size();++i){
			derive.addTerm(this.list.get(i)*i);
		}
		return derive;   // FIXME
	}
	
	public Polynomial sum(Polynomial another) {
		
		Polynomial sum=new Polynomial();
		int x,y=0;
		if(this.list.size()>another.list.size()){
			x=this.list.size();
			y=another.list.size();
		}
		else{
			y=this.list.size();
			x=another.list.size();
		}
		for(int i=0;i<y;++i){
			sum.addTerm(this.list.get(i)+another.list.get(i));
		}
		for(int i=y;i<x;++i){
			if(this.list.size()>another.list.size()){
				sum.addTerm(this.list.get(i));
			}
			else{
				sum.addTerm(another.list.get(i));
			}
	    }
		return sum;   // FIXME
	}

	/**
	 * This is the "equals" method that is called by
	 *    assertEquals(...) from your JUnit test code.
	 *    It must be prepared to compare this Polynomial
	 *    with any other kind of Object (obj).  Eclipse
	 *    automatically generated the code for this method,
	 *    after I told it to use the contained list as the basis
	 *    of equality testing.  I have annotated the code to show
	 *    what is going on.
	 */

	public boolean equals(Object obj) {
		// If the two objects are exactly the same object,
		//    we are required to return true.  The == operator
		//    tests whether they are exactly the same object.
		if (this == obj)
			return true;
		// "this" object cannot be null (or we would have
		//    experienced a null-pointer exception by now), but
		//    obj can be null.  We are required to say the two
		//    objects are not the same if obj is null.
		if (obj == null)
			return false;

		//  The two objects must be Polynomials (or better) to
		//     allow meaningful comparison.
		if (!(obj instanceof Polynomial))
			return false;

		// View the obj reference now as the Polynomial we know
		//   it to be.  This works even if obj is a BetterPolynomial.
		Polynomial other = (Polynomial) obj;

		//
		// If we get here, we have two Polynomial objects,
		//   this and other,
		//   and neither is null.  Eclipse generated code
		//   to make sure that the Polynomial's list references
		//   are non-null, but we can prove they are not null
		//   because the constructor sets them to some object.
		//   I cleaned up that code to make this read better.


		// A LinkedList object is programmed to compare itself
		//   against any other LinkedList object by checking
		//   that the elements in each list agree.

		return this.list.equals(other.list);
	}
	public static void main(String[] args) {
		
		
	}
}
