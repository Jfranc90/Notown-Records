package models;

public class Song 
{
	private String title;
	private String author;
	private int sid;
	private int uid;
	
	public Song(String title, String author, int sid, int uid) 
	{
		super();
		this.title = title;
		this.author = author;
		this.sid = sid;
		this.uid = uid;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getAuthor() {
		return author;
	}

	public void setAuthor(String author) {
		this.author = author;
	}

	public int getSid() {
		return sid;
	}

	public void setSid(int sid) {
		this.sid = sid;
	}

	public int getUid() {
		return uid;
	}

	public void setUid(int uid) {
		this.uid = uid;
	}
	
	@Override
	public String toString()
	{
		return this.title +  " written by " + this.author;
	}
}
