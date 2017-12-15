package android.bigplan.lego.app;

import java.lang.Thread.UncaughtExceptionHandler;

public class CrashCatcher implements UncaughtExceptionHandler {
	private static CrashCatcher carshCatcher = new CrashCatcher();
	private CrashHandler carshHandler;
	private static final String TAG = CrashCatcher.class.getSimpleName();
	
	/**
	 * 获取实例
	 */
	public static CrashCatcher getInstance() {
		return carshCatcher;
	}
	
	/**
	 * 初始化
	 */
	public void init( CrashHandler carshHandler) {
		this.carshHandler = carshHandler;
	}
	
	/**
	 * 单例
	 */
	private CrashCatcher() {
		super();
	}

	/**
	 * 当UncaughtException发生时会转入该函数来处理
	 */
	@Override
	public void uncaughtException(Thread thread, Throwable ex) {
		if(carshHandler != null) {
			carshHandler.handleCarsh(thread, ex);
		}
    }

}
