package models;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

public class Album 
{
	private String format = "";
	private int aid;
	private String title;
	private int uid;
	private String ssn;
	private LocalDate dt;
	private ArrayList<Song> songs = new ArrayList<Song>();
	
	public Album(String format, int aid, String title, int uid, String ssn, LocalDate dt) 
	{
		super();
		this.format = format;
		this.aid = aid;
		this.title = title;
		this.uid = uid;
		this.ssn = ssn;
		this.dt = dt;
	}

	public String getFormat() {
		return format;
	}

	public void setFormat(String format) {
		this.format = format;
	}

	public int getAid() {
		return aid;
	}

	public String getTitle() {
		return title;
	}


	public int getUid() {
		return uid;
	}

	public String getSsn() {
		return ssn;
	}

	public String getDt() {
		DateTimeFormatter myFormatObj = DateTimeFormatter.ofPattern("MM-dd-yyyy");
		String formattedDt = this.dt.format(myFormatObj);
		return formattedDt;
	}

	public void setDt(LocalDate dt) {
		this.dt = dt;
	}
	
	public void addSong(Song s) {
		this.songs.add(s);
	}
	
	public void removeSong(Song s)
	{
		this.songs.remove(s);
	}
	
	@Override
	public String toString()
	{
		return this.title + " AID: " + this.aid + " made " + this.dt + " " + " by producer with ssn " + this.ssn;
	}
}
