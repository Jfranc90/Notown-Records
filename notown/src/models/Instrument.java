package models;

public class Instrument 
{
	private int iid;
	private String iname;
	private String musicalKey;
	
	public Instrument(int iid, String iname, String musicalKey) 
	{
		super();
		this.iid = iid;
		this.iname = iname;
		this.musicalKey = musicalKey;
	}

	public int getIid() {
		return iid;
	}

	public void setIid(int iid) {
		this.iid = iid;
	}

	public String getIname() {
		return iname;
	}
	

	public void setIname(String iname) {
		this.iname = iname;
	}

	public String getMusicalKey() {
		return musicalKey;
	}

	public void setMusicalKey(String musicalKey) {
		this.musicalKey = musicalKey;
	}
	
	@Override
	public String toString()
	{
		return this.iid + " instrument name: " + this.iname + " musical key:" + this.musicalKey;
	}
}
