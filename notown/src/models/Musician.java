package models;

public class Musician 
{
	private String ssn = "";
	private Home mHome;
	private String name = "";
	
	public Musician(String ssn, String address, String phoneN, String name)
	{
		this.ssn = ssn;
		this.mHome = new Home(address, phoneN);
		this.name = name;
	}

	public String getSsn() {
		return ssn;
	}

	public void setSsn(String ssn) {
		this.ssn = ssn;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	public Home getHome()
	{
		return this.mHome;
	}
	
	@Override
	public String toString()
	{
		return this.ssn + " " + this.name + " whose phone number is " + this.mHome.getPhoneN() + " and lives at " + this.mHome.getAddress(); 
	}
}
