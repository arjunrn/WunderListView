package arjun.naik.list;

import java.io.IOException;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.SQLException;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.CursorAdapter;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;
import android.widget.Toast;

public class WunderListViewActivity extends Activity {
    
	private static final String TAG = "WunderListViewActivity";
	Context context;
	BookDBAdapter bookAdapter;
	ListView book_list;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.main);
        Log.d(TAG, "main layout is set");
        
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
        
        Cursor first_book = bookAdapter.GetFirstBooks(Integer.toString(30));
        
        Log.d(TAG, "Before creating adapter");
        BookCursorAdapter booklistAdapter = new BookCursorAdapter(context,first_book);
        
        Log.d(TAG, "Going to set adapter");
        book_list.setAdapter(booklistAdapter);
        
        book_list.setOnItemClickListener(new OnItemClickListener() {

			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				Intent bookDetailIntent = new Intent(context, BookDetail.class);
				bookDetailIntent.putExtra("bookid", Long.toString(id));
				startActivity(bookDetailIntent);
			}
		
        });
        
    }
    
    @Override
    public void onDestroy(){
    	super.onDestroy();
    	bookAdapter.close();
    }
    
    private class BookCursorAdapter extends CursorAdapter{

		public BookCursorAdapter(Context context, Cursor c) {
			super(context, c);
			Log.d(TAG, "BookCursorAdapter constructor called");
			// TODO Auto-generated constructor stub
		}

		@Override
		public void bindView(View view, Context context, Cursor cursor) {
			
			BookHolder book_tag = (BookHolder)view.getTag();
			book_tag.book_title.setText(cursor.getString(cursor.getColumnIndex("title")));
			book_tag.book_cost.setText(cursor.getString(cursor.getColumnIndex("discount_price")));
			String image_url_hash = cursor.getString(cursor.getColumnIndex("image_url"));
			book_tag.book_url.setRemoteURI(image_url_hash);
			book_tag.book_url.setLocalURI(image_url_hash);
			book_tag.book_url.loadImage();
		
		}

		@Override
		public View newView(Context context, Cursor cursor, ViewGroup parent) {
			
			LayoutInflater inflater = getLayoutInflater();
			View book_item = inflater.inflate(R.layout.book_item, null);
			BookHolder book_tag = new BookHolder();
			book_tag.book_title = (TextView) book_item.findViewById(R.id.book_name);
			book_tag.book_cost = (TextView) book_item.findViewById(R.id.book_price);
			book_tag.book_url = (RemoteCoverImage) book_item.findViewById(R.id.remote_cover_image);
			
			book_item.setTag(book_tag);
			return book_item;
		
		}
    	
		private class BookHolder{
			RemoteCoverImage book_url;
			TextView book_title;
			TextView book_cost;
		}
		
    }
    
}