package android.bigplan.lego.activity.voice;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;

import android.bigplan.lego.app.AppApplication;
import android.media.MediaPlayer;
import android.media.MediaRecorder;

public class Media {
	// 录音文件播放
	private MediaPlayer myPlayer;
	// 录音
	private MediaRecorder myRecorder;
	// 音频文件保存地址

	public String name;//存储名字
	private File saveFilePath;
	private String pathsend;

	public String getPathsend() {
		return pathsend;
	}



	public Media()
	{
//		if (Environment.getExternalStorageState().equals(
//				Environment.MEDIA_MOUNTED)) {
//			try {
//				sendpath = Environment.getExternalStorageDirectory()
//						.getCanonicalPath().toString()
////						+ "/MessageMediaReceive";
//						+ "/lazy/media/send";
//				File files = new File(sendpath);
//				if (!files.exists()) {
//					boolean mkdir = files.mkdirs();
//				}
//
//
//			} catch (IOException e) {
//				e.printStackTrace();
//			}
//		}
//		if (Environment.getExternalStorageState().equals(
//				Environment.MEDIA_MOUNTED)) {
//			try {
//				receivepath = Environment.getExternalStorageDirectory()
//						.getCanonicalPath().toString()
////						+ "/MessageMediaReceive";
//				+ "/lazy/media/accept";
//				File files = new File(receivepath);
//				if (!files.exists()) {
//					boolean mkdir = files.mkdirs();
//				}
//
//
//			} catch (IOException e) {
//				e.printStackTrace();
//			}
//		}
	}
	//开始录制
	public void startRecord()
	{
		myRecorder = new MediaRecorder();
		// 从麦克风源进行录音
		myRecorder.setAudioSource(MediaRecorder.AudioSource.DEFAULT);
		// 设置输出格式
		myRecorder.setOutputFormat(MediaRecorder.OutputFormat.DEFAULT);
		// 设置编码格式
//		myRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.DEFAULT);
		myRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AAC);
//		myRecorder.setVideoEncoder(MediaRecorder.VideoEncoder.H264);//会崩溃
		//保存路径
		this.name="LAZY"+Tools.getRandomId()
				+ new SimpleDateFormat(
						"yyyyMMddHHmmss").format(System
						.currentTimeMillis())
				+ ".aac";
		pathsend = AppApplication.sendpath+"/"+name;
		saveFilePath = new File(pathsend);
		myRecorder.setOutputFile(saveFilePath
				.getAbsolutePath());
		try {
			saveFilePath.createNewFile();
			myRecorder.prepare();
		} catch (IOException e) {
			e.printStackTrace();
		}
		// 开始录音
		myRecorder.start();
	}
	//停止录制并保存
	public void stopRecord()
	{
		if (saveFilePath.exists() && saveFilePath != null) {
			myRecorder.stop();
			myRecorder.release();	
		}
	}
	//退出
	public void destroy()
	{
		// 释放资源
		if (myPlayer.isPlaying()) {
			myPlayer.stop();
			myPlayer.release();
		}
		myPlayer.release();
		myRecorder.release();
	}
	//开始播放

	public void startPlay(String path0)
	{
		myPlayer = new MediaPlayer();
		try {
			myPlayer.reset();
			myPlayer.setDataSource(path0);
			if (!myPlayer.isPlaying()) {

				myPlayer.prepare();
				myPlayer.start();
			} else {
				myPlayer.pause();
			}

		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	//停止播放
	public void stopPlay()
	{
		if (myPlayer.isPlaying()) {
			myPlayer.stop();
		}
	}
	
}
