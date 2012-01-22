package arjun.naik.list;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

public class FirstRun extends Activity {
	DataBaseHelper dbHelper;
	Context context;
	SharedPreferences firstRunCheck;
	Handler firstRunHandler;
	
	@Override
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		context = this;
		firstRunCheck = getSharedPreferences("first_run", MODE_WORLD_READABLE);
		Boolean isFirstRun = firstRunCheck.getBoolean("first_check", true);
		
		if(isFirstRun){
			setContentView(R.layout.first_run);
			
			dbHelper = new DataBaseHelper(context);
			
			firstRunHandler = new Handler(){
				@Override
				public void handleMessage(Message message){
					SharedPreferences.Editor firstRunEditor = firstRunCheck.edit();
					firstRunEditor.putBoolean("first_check", false);
					firstRunEditor.commit();
					goToList();
				}
			};
			
			new Thread(new Runnable(){
				public void run() {
					dbHelper.createDataBase();
					firstRunHandler.sendEmptyMessage(0);
				}
			}).start();
		}
		else{
			goToList();
		}
		
	}
	
	protected void goToList(){
		Intent bookListIntent = new Intent(context, WunderListViewActivity.class);
		startActivity(bookListIntent);
		finish();
	}
	
	@Override
	public void onDestroy(){
		if(dbHelper != null){
			dbHelper.close();
		}
		super.onDestroy();
	}
	
}
