package com.urent.server.toy;

public class Hockeyplayer {
   private String name,hand;
   private int number;
   private int[]goals,assists;

public Hockeyplayer(String name, String hand, int number, int[] goals, int[] assists) {
	super();
	this.name = name;
	this.hand = hand;
	this.number = number;
	this.goals =new  int[number];
	for (int i=0;i<number;i++) this.goals[i]=goals[i];
	this.assists = assists;
}
public String getName() {
	return name;
}
public String getHand() {
	double a=Math.random();
	if(a<1.0/3.0){
		return "left handed";
	}
	else{
		if(a<2.0/3.0){
			return "right handed";
		}
		else{
			return "either way";
		}
	}
   
}
public int getNumber() {
	return number;
}
public int[] getAssists() {
	return assists;
}
public int[] getGoals() {
	return goals;
}
public int specificgoals(int x){
	return goals[x];
}
public int specificassists(int x){
	return assists[x];
}
public int specificpoints(int x){
	return goals[x]+assists[x];
}
public int totalassists(int x){
	int total=0;
	for(int i=0;i<number;++i){
		total=total+assists[x];
	}
	return total;
}
public int totalgoals(){
	int total=0;
	for(int i=0;i<number;++i){
		total=total+goals[i];
	}
	return total;
}
public int totalpoints(int x){
	int total=0;
	for(int i=0;i<number;++i){
		total=total+assists[x]+goals[x];
	}
	return total;
}
public static void main(String[] args) {
	int[]a1=new int[2];
	a1[0]=3;
	a1[1]=2;
	int[]a2=new int[2];
	a2[0]=7;
	a2[1]=1;
	Hockeyplayer h1=new Hockeyplayer("Jake"," ",2,a1,a2);
	System.out.println("Hockeyplaer "+h1.getName()+" is "+h1.getHand()+". He played "+ h1.getNumber()+" games");
	System.out.println("Total goals for all games are "+h1.totalgoals());
		
	
}



}