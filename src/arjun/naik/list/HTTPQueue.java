package arjun.naik.list;

import java.util.ArrayList;
import java.util.HashMap;

import android.os.Handler;
import android.os.Message;
import android.util.Log;

public class HTTPQueue {
	public static final int PRIORITY_LOW = 0;
	public static final int PRIORITY_HIGH = 1;
	private static final String TAG = "HTTPQueue";
	
	private volatile static HTTPQueue singleInstance = null;
	
	private ArrayList<HTTPThread> imageQueue = new ArrayList<HTTPThread>();
	private HashMap<Long, Boolean> imageThreads = new HashMap<Long, Boolean>();
	private Handler imageQueuedHandler = null;
	
	private HTTPQueue(){
		
	}
	
	public static HTTPQueue getInstance(){
		if(singleInstance == null){
			singleInstance = new HTTPQueue();
		}
		return singleInstance;
	}
	
	public void enqueue(HTTPThread task){
		enqueue(task, PRIORITY_LOW);
	}
	
	public synchronized void enqueue(HTTPThread task, int priority){
		Boolean exists = imageThreads.get(task.getId());
		if(exists == null){
			if(imageQueue.size() == 0 || priority == PRIORITY_LOW){
				imageQueue.add(task);
			}
			else{
				imageQueue.add(1, task);
			}
		}
		runFirst();
	}
	
	public synchronized void dequeue(final HTTPThread task){
		imageThreads.remove(task.getId());
		imageQueue.remove(task);
	}
	
	public synchronized void finished(int result){
		if(imageQueuedHandler != null){
			imageQueuedHandler.sendEmptyMessage(result);
		}
	}
	
	public synchronized void runFirst(){
		
		if(imageQueue.size() > 0){
			HTTPThread task = imageQueue.get(0);
			if(task.getStatus() == HTTPThread.STATUS_PENDING ){
				Log.d(TAG, "STATUS is pending");
				imageQueuedHandler = task.getHandler();
				task.setHandler(imageHandler);
				task.start();
			}
			else if(task.getStatus() == HTTPThread.STATUS_FINISHED){
				Log.d(TAG, "Thread FINISHED");
				HTTPThread thread = imageQueue.remove(0);
				imageThreads.remove(thread.getId());
				runFirst();
			}
		}
		
	}
	
	private Handler imageHandler = new Handler(){
		@Override
		public void handleMessage(Message message){
			finished(message.what);
			runFirst();
		}
	};
	
}
