package arjun.naik.list;

import java.io.File;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.ImageView;

public class RemoteCoverImage extends ImageView {
	
	private static final String TAG = "RemoteCoverImage";
	private String uriLocal;
	private String uriRemote;
	private HTTPThread uriThread = null;
	
	public RemoteCoverImage(Context context, AttributeSet attrs) {
		super(context, attrs, 0);
	}

	public RemoteCoverImage(Context context, AttributeSet attrs, int defStyle){
		super(context, attrs, defStyle);
	}
	
	public void setLocalURI(String localURI){
		String[] split_urls = localURI.split("/");
		String last_file_name = split_urls[split_urls.length - 1];
		uriLocal = Environment.getExternalStorageDirectory() + "/.remote-image-view-cache/" + last_file_name;
	}
	
	public void setRemoteURI(String remoteURI){
		if(remoteURI.startsWith("http")){
			uriRemote = remoteURI;
		}
	}
	
	public void loadImage(){
		
		File localImage = new File(uriLocal);
		
		if(localImage.exists()){
			setFromLocal();
		}
		else{
			localImage.getParentFile().mkdirs();
			queue();
		}
		
	}
	
	@Override
	public void finalize(){
		if(uriThread != null){
			HTTPQueue queue = HTTPQueue.getInstance();
			queue.dequeue(uriThread);
		}
	}
	
	public void queue(){
		if(uriThread == null){
			uriThread = new HTTPThread(uriRemote,uriLocal, imageHandler);
			HTTPQueue queue = HTTPQueue.getInstance();
			queue.enqueue(uriThread, HTTPQueue.PRIORITY_LOW);
		}
		setImageResource(R.drawable.ic_launcher);
	}
	
	private void setFromLocal(){
		uriThread = null;
		Drawable d = Drawable.createFromPath(uriLocal);
		if(d != null){
			setImageDrawable(d);
		}
	}
	
	private Handler imageHandler = new Handler(){
		@Override
		public void handleMessage(Message msg){
			setFromLocal();
		}
	};
	
}