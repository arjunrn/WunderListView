package arjun.naik.list;

import android.app.Activity;
import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.widget.TextView;

public class BookDetail extends Activity {
	Context context;
	BookDBAdapter bookAdapter;
	
	@Override
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		context = this;
	
		setContentView(R.layout.book_detail);
		
		Bundle extras = getIntent().getExtras();
		String bookId = extras.getString("bookid");
		
		bookAdapter = new BookDBAdapter(context);
		Cursor bookDetailCursor = bookAdapter.GetBookDetails(bookId);
		bookDetailCursor.moveToFirst();
		
		String bookTitle = bookDetailCursor.getString(bookDetailCursor.getColumnIndex("title"));
		
		TextView bookTitleTV = (TextView)findViewById(R.id.book_detail_title);
		bookTitleTV.setText(bookTitle);
		
		String cover_url = bookDetailCursor.getString(bookDetailCursor.getColumnIndex("image"));
		RemoteCoverImage bookCover = (RemoteCoverImage)findViewById(R.id.book_detail_cover);
		bookCover.setRemoteURI(cover_url);
		bookCover.setLocalURI(cover_url);
		bookCover.loadImage(true);
		
		String original_price = bookDetailCursor.getString(bookDetailCursor.getColumnIndex("original_price"));
		TextView originalPriceTV = (TextView)findViewById(R.id.book_detail_op);
		originalPriceTV.setText(original_price);
		
		String discount_price = bookDetailCursor.getString(bookDetailCursor.getColumnIndex("discount_price"));
		TextView discountPriceTV = (TextView)findViewById(R.id.book_detail_dp);
		discountPriceTV.setText(discount_price);
		
		String book_category = bookDetailCursor.getString(bookDetailCursor.getColumnIndex("delivery_time"));
		TextView bookCategoryTV = (TextView)findViewById(R.id.book_detail_category);
		bookCategoryTV.setText(book_category);
		
		String book_author = bookDetailCursor.getString(bookDetailCursor.getColumnIndex("author"));
		TextView bookAuthor = (TextView)findViewById(R.id.book_author);
		bookAuthor.setText(book_author);
		
		String book_publisher = bookDetailCursor.getString(bookDetailCursor.getColumnIndex("publisher"));
		TextView publisher = (TextView)findViewById(R.id.publisher);
		publisher.setText(book_publisher);
		
		String book_isbn = bookDetailCursor.getString(bookDetailCursor.getColumnIndex("isbn"));
		TextView isbnTV = (TextView)findViewById(R.id.book_isbn);
		isbnTV.setText(book_isbn);
		
	}
	
	@Override
	public void onDestroy(){
		super.onDestroy();
	}
	
}
