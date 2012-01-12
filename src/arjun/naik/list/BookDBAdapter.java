package arjun.naik.list;

import java.io.IOException;

import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

public class BookDBAdapter {
	
	private static final String TAG = "BookDBAdapter";
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
	
	public Cursor ExampleSelect(String id_num){
		String query = "SELECT _id,title FROM books LIMIT ?";
		if(bDb == null){
			Log.d(TAG,"bDB is null");
		}
		String[] params = new String[1];
		params[0] = id_num;
		return bDb.rawQuery(query, new String[]{id_num});
	}
	
	public void close(){
		bDBHelper.close();
	}
	
}
