package main;

import java.sql.*;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Locale;
import java.util.Properties;
import java.util.Scanner;

import models.Album;
import models.Home;
import models.Instrument;
import models.Musician;
import models.Song;

public class Notown {
	static final String DB_URL = "jdbc:postgresql://cs1.calstatela.edu:5432/cs4222s24";
	static Properties props = new Properties();
	static Scanner scan = new Scanner(System.in);
	static Connection conn = null;
	public static ArrayList<Home> homes = new ArrayList<Home>();
	public static ArrayList<Musician> musicians = new ArrayList<Musician>();
	public static ArrayList<Album> albums = new ArrayList<Album>();
	public static ArrayList<Song> songs = new ArrayList<Song>();
	public static ArrayList<Instrument> insts = new ArrayList<Instrument>();
	static final String INSERTION_QUERY = "INSERT INTO musician (ssn, name, p_number) VALUES (?,?,?);";
	static final String HOME_QUERY = "SELECT * FROM home";
	static final String HOME_INSERTION_QUERY = "INSERT INTO home (address, p_number) VALUES (?,?);";
	static final String MUSICIAN_QUERY = "SELECT m.ssn, m.name, m.p_number, h.address FROM musician m INNER JOIN home h ON m.p_number = h.p_number;";
	static final String MUSICIAN_REMOVAL_QUERY = "DELETE FROM musician WHERE ssn = ?";
	static final String ALBUM_LAST_ID_QUERY = "SELECT uid FROM ALBUM ORDER BY uid DESC LIMIT 1;";
	static final String ALBUM_INSERTION_QUERY = "insert into album(format, date, aid, title, uid, ssn) values (?,?,?,?,?,?);";
	static final String ALBUM_QUERY = "SELECT * FROM ALBUM";
	static final String SONG_QUERY = "SELECT a.format, a.date, a.aid, a.title, a.uid, a.ssn, s.title, s.author, s.sid\r\n"
			+ "FROM album A\r\n"
			+ "INNER JOIN song s\r\n"
			+ "ON a.uid = s.uid;";
	static final String ALBUM_REMOVAL_QUERY = "DELETE FROM album WHERE uid = ?";
	static final String SONG_LAST_ID_QUERY = "SELECT sid FROM song ORDER BY sid DESC LIMIT 1;";
	static final String SONG_INSERTION_QUERY = "INSERT INTO song(title, author, sid, uid) VALUES (?,?,?,?);";
	static final String SONG_REMOVAL_QUERY = "DELETE FROM song WHERE uid = ? and sid = ?";
	static final String INSTRUMENT_QUERY = "SELECT * FROM instrument;";
	
	
	public static void main(String[] args) {
		props.setProperty("user", "cs4222s24");
		props.setProperty("password", "dbJvejT0");
		props.setProperty("ssl", "true");
		props.setProperty("sslmode", "prefer");
		createConnection();
		
		
		System.out.println("Collecting current information from db...\n");
		getHomeList();
		getMusicianList();
		getAlbumList();
		getSongList();
		getInstrumentList();
		
		

		int selection = -1;

		boolean runner = true;
		while (runner) {
			System.out.print("Welcome to the main menu! The following selections are available for usage: \n"
					+ "1) Add a musician into the current database. \n"
					+ "2) Remove a musician in the current database.\n"
					+ "3) Add an Album. \n"
					+ "4) Remove an Album \n"
					+ "5) Add a song to an album\n"
					+ "6) Remove a song on an album\n"
					+ "7) Show information on the following database tables.\n"
					+ "8) Exit the program.\n"
					+ "Please provide you desired choice of action (1-8): \t");
			selection = scan.nextInt();
			scan.nextLine();
			switch (selection) {
			case 1:
				addMusicianMenu();
				break;
			case 2:
				removeMusician();
				break;
			case 3:
				addAlbumMenu();
				break;
			case 4:
				removeAlbum();
				break;
			case 5:
				addSongMenu();
				break;
			case 6:
				removeSong();
				break;
			case 7:
				System.out.println("\nHome Info");
				for (Home home : homes) {
					System.out.println(home.toString());
				}
				
				System.out.println("\nMusician Info");
				for(Musician m: musicians)
				{
					System.out.println(m.toString());
				}
				System.out.println("\n");
				
				System.out.println("\nAlbum Info");
				for(Album a: albums)
				{
					System.out.print(a.toString() + " ");
				}
				System.out.println("\n");
				
				System.out.println("\nSong Info");
				for(Song sg: songs)
				{
					System.out.println(sg.toString());
					
					for(Album als: albums)
					{
						if (sg.getUid() == als.getUid())
						{
							System.out.println("Withing Album -->" + als.getTitle());
						}
					}
				}
				
				System.out.println("\nInstrument Info");
				for (Instrument inst : insts) {
					System.out.println(inst.toString());
				}
				System.out.println("\n");
				break;
			case 8:
				try {
					conn.close();
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				runner = !runner;
				break;
			}
		}
		System.out.println("Program exited");

		try {
			conn.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private static void getInstrumentList() {
		// TODO Auto-generated method stub
				try {
					Statement st = conn.createStatement();
					ResultSet rs = st.executeQuery(INSTRUMENT_QUERY);
					int iid = 0;
					String name = "";
					String musicalKey = "";
					while (rs.next()) {
						iid = rs.getInt(1);
						name = rs.getString(2);
						musicalKey = rs.getString(3);
						insts.add(new Instrument(iid, name, musicalKey));
					}
					st.close();
					rs.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
		
	}

	private static void removeSong() {
		int uid = 0;
		int sid = 0;
		System.out.println("To remove a song form an album we must know the a couple of details:");
		System.out.println("1) The UID of the album: ");
		System.out.println("Current Albums available: ");
		for(Album a: albums)
		{
			System.out.println(a.getUid() + " " + a.getTitle());
		}
		System.out.println("UID: ");
		uid = Integer.parseInt(scan.nextLine());
		System.out.println("Please select the song you would like to remove using the SID");
		for(Song sg: songs)
		{
			if(sg.getUid() == uid)
			{
				System.out.println(sg.getTitle() + " sid: " + sg.getSid());
			}
		}
		System.out.println("SID: ");
		sid = Integer.parseInt(scan.nextLine());
		PreparedStatement pstmt;
		try {
			pstmt = conn.prepareStatement(SONG_REMOVAL_QUERY);
			pstmt.setInt(1,uid);
			pstmt.setInt(2, sid);
			pstmt.executeUpdate();
			pstmt.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally {
			int el = 0;
			Song ss = null;
			for(Song sg: songs)
			{
				if(sg.getUid() == uid && sg.getSid() == sid)
				{
					ss = new Song(sg.getTitle(), sg.getAuthor(), sg.getUid(), sg.getSid());
					break;
				}else {
					el++;
				}
			}
			songs.remove(el);
			for(Album als: albums)
			{
				if(als.getUid() == ss.getUid())
				{
					als.removeSong(ss);
				}
			}
			
			System.out.println("Removal Successful");
		}
	}

	private static void getSongList() {
				try {
					Statement st = conn.createStatement();
					ResultSet rs = st.executeQuery(SONG_QUERY);
					String format = "";
					LocalDate date;
					int albumID = 0;
					String title  = "";
					String ssn = "";
					int uid = 0;
					String songTitle = "";
					String author = "";
					int sid = 0;
					while (rs.next()) {
						format = rs.getString(1).strip();
						date = rs.getObject(2, LocalDate.class);
						albumID = rs.getInt(3);
						title = rs.getString(4).strip();
						uid = rs.getInt(5);
						ssn = rs.getString(6);
						songTitle = rs.getString(7).strip();
						author = rs.getString(8).strip();
						sid = rs.getInt(9);
						songs.add(new Song(songTitle, author, sid, uid));
						Album chAlbum = new Album(format, albumID, title, uid, ssn, date);
						
						boolean inAList = false;
						for(Album al: albums)
						{
							if(al.getUid() == chAlbum.getUid())
							{
								inAList = true;
							}
						}
						
						if(!inAList)
						{
							albums.add(chAlbum);
						}
					}
					st.close();
					rs.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
		
	}

	private static void addSongMenu() {
		String title = "";
		String author = "";
		int sid = 0;
		int uid = 0;
		
		System.out.println("The following details are required to add a song to an album:");
		System.out.println("Title of song: ");
		title = scan.nextLine();
		System.out.println("Author");
		author = scan.nextLine();
		sid = getLastSongID() + 1;
		System.out.println("UID of the album you would like to add this song to: ");
		System.out.println("Current Albums available: ");
		for(Album a: albums)
		{
			System.out.println(a.getUid() + " " + a.getTitle());
		}
		System.out.println("UID: ");
		uid = Integer.parseInt(scan.nextLine());
		addSong(title, author, sid, uid);
	}

	private static void addSong(String title, String author, int sid, int uid) {
		try {
			PreparedStatement st = conn.prepareStatement(SONG_INSERTION_QUERY);

			st.setString(1, title);
			st.setString(2, author);
			st.setInt(3, sid);
			st.setInt(4, uid);
			st.executeUpdate();
			st.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}finally {
			songs.add(new Song(title, author, sid, uid));
			for(Album al: albums)
			{
				if(al.getUid() == uid)
				{
					al.addSong(new Song(title, author, sid, uid));
				}
			}
		}
		
	}

	private static int getLastSongID() {
		int uid = 0;
		try {
			Statement st = conn.createStatement();
			ResultSet rs = st.executeQuery(SONG_LAST_ID_QUERY );
			while(rs.next()) {
				uid = rs.getInt(1);
			}
			st.close();
			rs.close();
		}catch(SQLException e) {
			System.err.println(e.getMessage());
		}
		return uid;
	}

	private static void getAlbumList() {
		// TODO Auto-generated method stub
		try {
			Statement st = conn.createStatement();
			ResultSet rs = st.executeQuery(ALBUM_QUERY);
			String format = "";
			LocalDate date;
			int albumID = 0;
			String title  = "";
			String ssn = "";
			int uid = 0;
			while (rs.next()) {
				format = rs.getString(1).strip();
				date = rs.getObject(2, LocalDate.class);
				albumID = rs.getInt(3);
				title = rs.getString(4).strip();
				uid = rs.getInt(5);
				ssn = rs.getString(6);
				albums.add(new Album(format, albumID, title, uid, ssn, date));
			}
			st.close();
			rs.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	private static void getMusicianList() {
		try {
			Statement st = conn.createStatement();
			ResultSet rs = st.executeQuery(MUSICIAN_QUERY);
			String addr = "";
			String pn = "";
			String ssn = "";
			String name = "";
			while (rs.next()) {
				ssn = rs.getString(1).strip();
				name = rs.getString(2).strip();
				pn = rs.getString(3).strip();
				addr = rs.getString(4).strip();
				musicians.add(new Musician(ssn, addr, pn, name));
			}
			st.close();
			rs.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
	}

	private static void addMusicianMenu() {
		System.out.print("Please provide a ssn (must be 9 digits and in format xxx-xx-xxxx ): ");
		String ssn = scan.nextLine().strip();
		System.out.print("Please provide the name of the musician: ");
		String name = scan.nextLine().strip();
		System.out.println("Please provide the phone number of the musician: ");
		System.out.println("You can use any of these numbers in relation to their address or input a new phone number");
		for(Home h: homes)
		{
			System.out.println(h.toString());
		}
		String pnumber = scan.nextLine().strip();
		//System.out.println("Preparing to add new musician to table....");
		//System.out.println(ssn + " " + name + " " + pnumber);
		addMusician(ssn,name,pnumber);

	}

	public static void removeMusician()
	{
		String choice = "";
		System.out.println("To remove a musician from the current database, please provide "
				+ " a Social Security Number (ssn) ");
		System.out.println("The current SSNs in the table are: ");
		for(Musician m: musicians)
		{
			System.out.println(m.getSsn());
		}
		System.out.println("SSN: ");
		choice = scan.nextLine();
		
		PreparedStatement pstmt;
		try {
			pstmt = conn.prepareStatement(MUSICIAN_REMOVAL_QUERY);
			pstmt.setString(1,choice);
			
			pstmt.executeUpdate();
			pstmt.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally {
			int el = 0;
			Musician rm = null;
			for(Musician m: musicians)
			{
				if(m.getSsn().equals(choice))
				{
					rm = new Musician(m.getSsn(),m.getHome().getAddress(), m.getHome().getPhoneN(), m.getName());
					break;
				}else {
					el++;
				}
			}
			musicians.remove(el);
			for(Home h: homes)
			{
				if(rm.getHome().getAddress().equals(h.getAddress()))
				{
					h.removeMusician(rm);
				}
			}
			
			System.out.println("Removal Successful");
		}
		
	}
	
	public static void addMusician(String ssn, String name, String p_number) {
		String address = "";
		if (ssn.length() < 11 || p_number.length() < 10) {
			System.out.println("Error in entry: \n" + "1) SSN must contain 9 digits \n"
					+ "2) Phone number must contain atleast 10 digits ");
			return;
		} else {
			boolean checkPhone = checkHomes(p_number);
			if(!checkPhone){
				System.out.println("Please provide an address: ");
				address = scan.nextLine();
				try {
					PreparedStatement homeSt = conn.prepareStatement(HOME_INSERTION_QUERY);
					homeSt.setString(1, address);
					homeSt.setString(2, p_number);
					homeSt.executeUpdate();
					homeSt.close();
					homes.add(new Home(address, p_number));
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
			}else {
				address = getAddress(p_number);
			}
			try {
				PreparedStatement st = conn.prepareStatement(INSERTION_QUERY);

				st.setString(1, ssn);
				st.setString(2, name);
				st.setString(3, p_number);
				st.executeUpdate();
				st.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}finally {
				musicians.add(new Musician(ssn, address, p_number, name));
				for(Home h: homes)
				{
					if(h.getAddress().equals(address)) {
						h.addMusician(new Musician(ssn, address, p_number, name));
					}
				}
			}
		}
	}

	private static String getAddress(String p_number) {
		String found = "";
		for (Home h : homes) {
			if (h.getPhoneN().equals(p_number)) {
				found = h.getAddress();
			}
		}
		return found;
	}

	private static boolean checkHomes(String p_number) {
		boolean found = false;
		for (Home h : homes) {
			if (h.getPhoneN().equals(p_number)) {
				found = true;
			}
		}
		return found;
	}

	public static void getHomeList() {
//		boolean checkHome = false;
		try {
			Statement st = conn.createStatement();
			ResultSet rs = st.executeQuery(HOME_QUERY);
			String addr = "";
			String pn = "";
			while (rs.next()) {
				addr = rs.getString(1).strip();
				pn = rs.getString(2).strip();
				homes.add(new Home(addr, pn));
//				for(Home h: homes)
//				{
//					if(h.getAddress().equals(addr) && h.getPhoneNumber().equals(pn))
//					{
//						checkHome = true;
//					}
//				}
//				if(homes.contains(pn))
//				{
//					homes.add(pn);
//				}
			}
			st.close();
			rs.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public static void addAlbumMenu()
	{
		String title = "";
		String format = "";
		String ssn = "";
		int albumID = 0;
		int uid = getLastUID() + 1;
		
		System.out.println("The following details are needed for this album:");
		System.out.println("Title: ");
		title = scan.nextLine().strip();
		System.out.println("Format: ");
		format = scan.nextLine().strip();
		System.out.println("Date (yyyy-MM-dd): ");
		
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd", Locale.ENGLISH);
		LocalDate date = LocalDate.parse(scan.nextLine().strip(), formatter);
		
		System.out.println("Album ID: ");
		albumID = Integer.parseInt(scan.nextLine().strip());
		
		while(Integer.toString(albumID).length() != 5)
		{
			System.out.println("Album ID: ");
			albumID = Integer.parseInt(scan.nextLine().strip());
		}
		
		System.out.println("The current SSNs in the table are: ");
		for(Musician m: musicians)
		{
			System.out.println(m.getSsn());
		}
		System.out.println("SSN of the Producer");
		ssn = scan.nextLine();
		
		addAlbum(format, date, albumID,title, uid, ssn);
	}
	
	private static void addAlbum(String format, LocalDate date, int albumID, String title, int uid, String ssn) {
		try {
			PreparedStatement st = conn.prepareStatement(ALBUM_INSERTION_QUERY);

			st.setString(1, format);
			st.setObject(2, date);
			st.setInt(3, albumID);
			st.setString(4,title);
			st.setInt(5, uid);
			st.setString(6, ssn);
			st.executeUpdate();
			st.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}finally {
			albums.add(new Album(format, albumID, title, uid, ssn, date));
		}
	}

	public static void removeAlbum()
	{
		int choice = 0;
		System.out.println("To remove an album from the current database, please provide "
				+ " the album's unique id.");
		System.out.println("The current Albums in the table are: ");
		for(Album a: albums)
		{
			System.out.println(a.getUid() + " " + a.getTitle());
		}
		System.out.println("UID: ");
		choice = Integer.parseInt(scan.nextLine());
		
		PreparedStatement pstmt;
		try {
			pstmt = conn.prepareStatement(ALBUM_REMOVAL_QUERY);
			pstmt.setInt(1,choice);
			
			pstmt.executeUpdate();
			pstmt.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally {
			int el = 0;
			//Album al = null;
			for(Album a: albums)
			{
				if(a.getUid() == choice)
				{
					break;
				}else {
					el++;
				}
			}
			albums.remove(el);
			System.out.println("Removal Successful");
		}
	}
	
	private static int getLastUID() {
		int id = 0;
		try {
			Statement st = conn.createStatement();
			ResultSet rs = st.executeQuery(ALBUM_LAST_ID_QUERY);
			while(rs.next()) {
				id = rs.getInt(1);
			}
			st.close();
			rs.close();
		}catch(SQLException e) {
			System.err.println(e.getMessage());
		}
		return id;
	}

	private static void createConnection() {
		try {
			conn = DriverManager.getConnection(DB_URL, props);
			if (conn != null) {
				System.out.println("Connection to the PostgreSQL server was successful!");
			} else {
				System.out.println("Connection failed!");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
}
