package android.bigplan.lego.app;

public abstract class CrashHandler {
	public abstract void handleCarsh(Thread thread, Throwable ex);
}
