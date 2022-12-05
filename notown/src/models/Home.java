package models;

import java.util.ArrayList;

public class Home 
{
	private String address;
	private String phoneN;
	private ArrayList<Musician> musicians = new  ArrayList<Musician>();
	
	public Home(String address, String phoneN) 
	{
		this.address = address;
		this.phoneN = phoneN;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getPhoneN() {
		return phoneN;
	}

	public void setPhoneN(String phoneN) {
		this.phoneN = phoneN;
	}

	public void addMusician(Musician m)
	{
		musicians.add(m);
	}
	
	public void removeMusician(Musician m)
	{
		musicians.remove(m);
	}
	
	@Override
	public String toString() {
		return "Home Address: " + this.address + " Phone Number: " + this.phoneN;
	}
}
