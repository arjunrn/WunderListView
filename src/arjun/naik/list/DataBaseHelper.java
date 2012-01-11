package arjun.naik.list;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import android.content.Context;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DataBaseHelper extends SQLiteOpenHelper {
	
	private static final String TAG = "SQLiteOpenHelper";
	private static String DB_PATH = "/data/data/arjun.naik.list/databases";
	private static String DB_NAME = "flipkartdb.sqlite";
	private SQLiteDatabase myDataBase;
	private final Context myContext;
	
	public DataBaseHelper(Context context){
		super(context, DB_NAME, null, 1);
		this.myContext = context;
		Log.d(TAG, "DataBaseHelper Constructor is called");
	}
	
	public void createDataBase(){
		boolean dbExist = checkDataBase();
		
		if(dbExist){
			Log.d(TAG, "createDataBase - DB already exists");
		}else{
			Log.d(TAG, "createDataBase - DB does not exist");
			this.getReadableDatabase();
			try{
				Log.d(TAG, "before copyDataBase()");
				copyDataBase();
			}catch(IOException e){
				Log.e(TAG, "createDataBase - IO Exception while copying DB");
				throw new Error("Error Copying Database");
				
			}
			
		}
		
	}
	
	private boolean checkDataBase(){
		
		SQLiteDatabase checkDB = null;
		
		try{
			String myPath = DB_PATH + DB_NAME;
			checkDB = SQLiteDatabase.openDatabase(myPath, null, SQLiteDatabase.OPEN_READONLY);
			
		}catch(SQLiteException e){
			Log.e(TAG, "There was a problem opening when CheckingExists");
		}
		
		if(checkDB != null){
			checkDB.close();
		}
		
		return checkDB != null ? true : false;
		
	}
	
	private void copyDataBase() throws IOException{
		
		InputStream myInput = myContext.getAssets().open(DB_NAME);
		
		String outFileName = DB_PATH + DB_NAME;
		
		OutputStream myOutput = new FileOutputStream(outFileName);
		
		byte[] buffer = new byte[1024];
		int length;
		while((length = myInput.read(buffer)) > 0){
			myOutput.write(buffer, 0, length);
		}
		
		myOutput.flush();
		myOutput.close();
		myInput.close();
		
	}
	
	public SQLiteDatabase openDataBase() throws SQLException{
		String myPath = DB_PATH + DB_NAME;
		myDataBase = SQLiteDatabase.openDatabase(myPath, null, SQLiteDatabase.OPEN_READONLY);
		return myDataBase;
	}
	
	@Override
	public synchronized void close(){
		if(myDataBase != null){
			myDataBase.close();
		}
		super.close();
	}
	
	@Override
	public void onCreate(SQLiteDatabase arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onUpgrade(SQLiteDatabase arg0, int arg1, int arg2) {
		// TODO Auto-generated method stub

	}

}
