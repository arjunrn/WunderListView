package arjun.naik.list;

import java.io.IOException;

import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

public class BookDBAdapter {
	
	//private static final String TAG = "BookDBAdapter";
	private DataBaseHelper bDBHelper;
	private static SQLiteDatabase bDb;
	
	private final Context adapterContext;
	
	public BookDBAdapter(Context context){
		this.adapterContext = context;
	}
	
	public BookDBAdapter open() throws SQLException, IOException {
		bDBHelper = new DataBaseHelper(adapterContext);
		
		bDBHelper.createDataBase();
		
		try{
			bDb = bDBHelper.openDataBase();
		}catch (SQLException sqle){
			throw sqle;
		}
		
		return this;
	}
	
	
	public Cursor GetFirstBooks(String searchTerm){
		return bDb.query("books", new String[]{"_id","title","discount_price","image_url"}, "_id IN (SELECT book_id FROM book_titles WHERE book_title MATCH ?)",
			new String[]{searchTerm}, null, null, null);
	}
	
	public Cursor GetBookDetails(String book_id){
		return bDb.query("books", null, "_id=?", new String[]{book_id}, null, null, null);
	}
	
	public void close(){
		bDBHelper.close();
	}
	
}
