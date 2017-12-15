package android.bigplan.lego.app;


/**
 * 异常崩溃处理类
 * 
 * @ClassName: CrashHandler
 * @Description: 当程序发生未捕获异常时，由该类来接管程序并记录发送错误报告。
 * @author FingerArt FingerArt.me
 * @date 2016年1月2日 下午2:34:53
 *
 */
public class AndroidCrash {
	private static AndroidCrash androidCrash = new AndroidCrash();

    /** 错误日志文件名称 */
    static final String LOG_NAME = "/crash.txt";

    /**
     * @brief 构造函数
     * @details 获取系统默认的UncaughtException处理器，设置该CrashHandler为程序的默认处理器 。
     * param context 上下文
     */
	private AndroidCrash() {
		super();
	}
	
	public static AndroidCrash getInstance() {
		return androidCrash;
	}
	
	public void init(CrashHandler carshHandler) {
		CrashCatcher eh = CrashCatcher.getInstance();
		eh.init(carshHandler);
		Thread.setDefaultUncaughtExceptionHandler(eh);
	}
}
