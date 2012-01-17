package arjun.naik.list;

import java.io.File;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.widget.ImageView;

public class RemoteCoverImage extends ImageView {
	
	private static final String TAG = "RemoteCoverImage";
	private String uriLocal;
	private String uriRemote;
	private HTTPThread uriThread = null;
	private boolean imageLoaded = false;
	private Drawable defaultBookCover;
	
	public RemoteCoverImage(Context context, AttributeSet attrs) {
		super(context, attrs, 0);
		defaultBookCover = context.getResources().getDrawable(R.drawable.ic_launcher);
	}

	public RemoteCoverImage(Context context, AttributeSet attrs, int defStyle){
		super(context, attrs, defStyle);
	}
	
	public void setLocalURI(String localURI){
		String[] split_urls = localURI.split("/");
		String last_file_name = split_urls[split_urls.length - 1];
		uriLocal = Environment.getExternalStorageDirectory() + "/.remote-image-view-cache/" + last_file_name;
		imageLoaded = false;
	}
	
	public void setRemoteURI(String remoteURI){
		if(remoteURI.startsWith("http")){
			uriRemote = remoteURI;
			imageLoaded = false;
		}
	}
	
	public void loadImage(Boolean remoteLoad){
		
		if(imageLoaded) return;
		
		File localImage = new File(uriLocal);
		
		if(localImage.exists()){
			setFromLocal();
		}
		else{
			if(!remoteLoad){
				setImageDrawable(defaultBookCover);
				return;
			}
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
			imageLoaded = true;
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