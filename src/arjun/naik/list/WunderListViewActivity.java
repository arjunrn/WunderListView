package arjun.naik.list;

import java.io.IOException;

import android.app.Activity;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;

public class WunderListViewActivity extends Activity {
    
	private static final String TAG = "WunderListViewActivity";
	Context context;
	BookDBAdapter bookAdapter;
	ListView book_list;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.main);
        
        context = this;
        
        bookAdapter = new BookDBAdapter(context);
        
        try {
			bookAdapter.open();
		} catch (SQLException e) {
			Log.e(TAG, "Exception while opening DB");
			return;
		} catch (IOException e) {
			Log.e(TAG, "Exception while opening DB");
			return;
		}
        
        book_list = (ListView)findViewById(R.id.main_lv);
        
        int[] place_holders = new int[2];
        place_holders[0] = R.id.book_name;
        place_holders[1] = R.id.book_price;
        
        Cursor first_book = bookAdapter.ExampleSelect(Integer.toString(30));
        
        SimpleCursorAdapter booklistAdapter = new SimpleCursorAdapter(context,R.layout.book_item , first_book, new String[]{"title","original_price"}, place_holders);
        
        book_list.setAdapter(booklistAdapter);
        
    }
    
    @Override
    public void onDestroy(){
    	super.onDestroy();
    	bookAdapter.close();
    }
    
}