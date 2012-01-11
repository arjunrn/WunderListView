package arjun.naik.list;

import java.io.IOException;

import android.app.Activity;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

public class WunderListViewActivity extends Activity {
    
	private static final String TAG = "WunderListViewActivity";
	Context context;
	BookDBAdapter bookAdapter;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.main);
        
        context = this;
        
        bookAdapter = new BookDBAdapter(context);
        
        try {
			bookAdapter.open();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			Log.d(TAG, "Exception while opening DB");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			Log.d(TAG, "Exception while opening DB");
		}
        
        Cursor first_book = bookAdapter.ExampleSelect(Integer.toString(10));
        Log.d(TAG, "Cursor length : " + first_book.getCount());
        first_book.moveToFirst();
        StringBuilder book_names = new StringBuilder();
        
        while(first_book.isAfterLast() == false){
	        
	        String book_title = first_book.getString(0);
	        book_names.append(book_title);
	        book_names.append("\n");
	        first_book.moveToNext();
        
        }
        
        TextView test_view = (TextView) findViewById(R.id.testdbtv);
        
        test_view.setText(book_names.toString());
        
        
    }
    
    @Override
    public void onDestroy(){
    	super.onDestroy();
    	bookAdapter.close();
    }
    
}