package core;

public interface Logger {
	public void log(String text);
	public void warn(String text);
	public void error(String text);
	public void success(String text);
}
