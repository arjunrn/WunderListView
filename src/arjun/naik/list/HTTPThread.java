package arjun.naik.list;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.ref.SoftReference;
import java.net.MalformedURLException;
import java.net.URL;

import android.os.Handler;
import android.util.Log;

public class HTTPThread extends Thread {

	public static final int STATUS_PENDING = 0;
	public static final int STATUS_RUNNING = 1;
	public static final int STATUS_FINISHED = 2;
	private static final String TAG = "HTTPThread";
	
	private boolean imageError = false;
	private Exception imageException = null;
	private String imageURL;
	private String imageLocalURI;
	private int imageStatus = STATUS_PENDING;
	private SoftReference<Handler> imageHandler;
	
	public HTTPThread(String url, String local, Handler handler){
	
		imageURL = url;
		imageLocalURI = local;
		imageHandler = new SoftReference<Handler>(handler);
	
	}
	
	public void start(){
		if(getStatus() == STATUS_PENDING){
			synchronized (this) {
				imageStatus = STATUS_RUNNING;
			}
			super.start();
		}
	}
	
	public void run(){
		
		Log.d(TAG, "RUN on :" + imageURL);
		try{
			URL request = new URL(imageURL);
			InputStream is = (InputStream) request.getContent();
			FileOutputStream fos = new FileOutputStream(imageLocalURI);
			try{
				byte[] buffer = new byte[4096];
				int l;
				while((l = is.read(buffer)) != -1){
					fos.write(buffer, 0 , l);
				}
			}
			catch(IOException e){
				Log.e(TAG, "There was a IO error while fetching the image");
			}
			finally{
				Log.d(TAG, "Completed copying for : " + imageURL);
				is.close();
				fos.flush();
				fos.close();
			}
		}
		catch(MalformedURLException e){
			Log.e(TAG, "Not a proper URL!! WTF");
			e.printStackTrace();
		}
		catch(IOException e){
			Log.e(TAG, "MORE fucking IOExceptions");
			e.printStackTrace();
		}
		
		synchronized(this){
			imageStatus = STATUS_FINISHED;
		}
		
		Handler handler = getHandler();
		if(handler != null){
			handler.sendEmptyMessage(STATUS_FINISHED);
		}
		
	}
	
	public int getStatus(){
		synchronized(this){
			return imageStatus;
		}
	}
	
	public Exception getException(){
		return imageException;
	}
	
	public boolean hasError(){
		return imageError;
	}
	
	public void setHandler(Handler handler){
		imageHandler = new SoftReference<Handler>(handler);
	}
	
	public Handler getHandler(){
		if(imageHandler != null){
			return imageHandler.get();
		}
		return null;
	}
	
	@Override
	public long getId(){
		return imageURL.hashCode();
	}
}
