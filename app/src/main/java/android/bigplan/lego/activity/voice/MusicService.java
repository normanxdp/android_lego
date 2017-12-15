package android.bigplan.lego.activity.voice;

import java.util.Timer;
import java.util.TimerTask;

import android.app.Service;
import android.bigplan.lego.util.Logger;
import android.content.Intent;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.media.MediaPlayer.OnPreparedListener;
import android.os.Binder;
import android.os.IBinder;


public class MusicService extends Service {

	private MediaPlayer player;
	private String strPath;
	private String mediapath="";
	private Timer timer;
	private TimerTask ff;



	static CallBackVoiceListener mCllBackListener;

	public interface CallBackVoiceListener {
		public void callBack(int duration, int currentPosition);
	}

	public static void setCallBackVoiceListener(CallBackVoiceListener callBackListener) {
		mCllBackListener = callBackListener;
	}

	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		return new MyBinder();
	}

	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		super.onCreate();
		player = new MediaPlayer();
	}

	@Override
	public void onStart(Intent intent, int startId) {
		super.onStart(intent, startId);
		mediapath = intent.getStringExtra("mediapath");
		Logger.v("syso",mediapath);
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		mediapath = intent.getStringExtra("mediapath");
		Logger.v("syso",mediapath);
		return super.onStartCommand(intent, flags, startId);
	}

	class MyBinder extends Binder implements MusicInterface{

		@Override
		public void play() {
			MusicService.this.play();

		}

		@Override
		public void pause() {
			MusicService.this.pause();

		}

		@Override
		public void continuePlay() {
			MusicService.this.continuePlay();

		}

		@Override
		public void stop() {
			MusicService.this.stop();

		}

		@Override
		public void seekTo(int progress) {
			MusicService.this.seekTo(progress);

		}

	}

	private void play() {
		//重置player
		player.reset();
		try {
			//设置要播放的资源

//			if (Environment.getExternalStorageState().equals( Environment.MEDIA_MOUNTED)) {
//				strPath = Environment.getExternalStorageDirectory() .toString();
//			}
//
//			boolean sdCardExist = Environment.getExternalStorageState().equals(android.os.Environment.MEDIA_MOUNTED); //判断sd卡是否存在
//			if (sdCardExist)
//			{
//				File	sdDir = Environment.getExternalStorageDirectory();//获取跟目录
//			}

//			String  lujing= strPath+"/"+"lazy/media/AND615620160628000130.aac";
//			System.out.println(lujing);
			player.setDataSource(mediapath);
//			//同步准备
//			player.prepare();
			//异步准备
			player.prepareAsync();
			//异步准备必须要设置监听，获知系统什么时候加载资源完毕
			player.setOnCompletionListener(new OnCompletionListener() {

				@Override
				public void onCompletion(MediaPlayer mp) {
					mp.seekTo(0);
					System.out.println("音乐播放完啦");
					if(ff!=null)
					ff.cancel();
				}
			});
			player.setOnPreparedListener(new OnPreparedListener() {

				@Override
				public void onPrepared(MediaPlayer mp) {
					//开始播放
					player.start();
					addSeekBar();
				}
			});
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
//		//开始播放
//		player.start();
//		addSeekBar();
	}

	private void pause() {
		player.pause();
	}

	private void continuePlay() {
		player.start();
	}

	private void stop() {
		//一旦停止，就不能再start了，必须重置、加载资源、准备，然后才能start
		player.stop();
	}

	private void seekTo(int progress) {
		//改变播放进度至指定的毫秒值
		player.seekTo(progress);

	}

	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		//释放资源，一旦释放，这个player就废了，要用就要重新new
		player.release();
		player = null;
		if(ff!=null)
		ff.cancel();
		if(timer!=null)
		timer.cancel();
	}

	public void addSeekBar(){

		//设置计时器
		if(timer==null) {
			timer = new Timer();
		}
//		if(ff==null) {
			ff = new TimerTask() {

				//ps:这是子线程
				@Override
				public void run() {
					int duration = player.getDuration();
					int currentPosition = player.getCurrentPosition();
					if (mCllBackListener != null) {
						mCllBackListener.callBack(duration, currentPosition);

					}
				}
				//延迟5毫秒启动，每500毫秒运行一次run方法
			};
//		}
		timer.schedule(ff, 5, 1000);
	}

}
