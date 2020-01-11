package utility;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

public class File {
	
	private static final String PATH_SEPERATOR = "/";
	
	private final String path;
	private final String name;
	
	public File(String path) {
		this.path = path;
		String[] dirs = path.split(PATH_SEPERATOR);
		this.name = dirs[dirs.length - 1];
	}

	public File(String... paths) {
		String parts = "";
		for (String part : paths) {
			parts += (PATH_SEPERATOR + part);
		}
		path = parts;
		String[] dirs = parts.split(PATH_SEPERATOR);
		this.name = dirs[dirs.length - 1];
	}
	
	public String getPath() {
		return path;
	}

	@Override
	public String toString() {
		return getPath();
	}

	public InputStream getInputStream() {
		return Class.class.getResourceAsStream(path);
	}

	public BufferedReader getReader() {
		InputStreamReader isr = new InputStreamReader(getInputStream());
		BufferedReader reader = new BufferedReader(isr);
		return reader;
		
	}

	public String getName() {
		return name;
	}
}
