package game.xlash.start;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.Field;

public class Start {
	
	public static Game game;
	
	public static void main(String[] args) {
		String os = System.getProperty("os.name").toLowerCase();
		String arch = System.getProperty("os.arch").toLowerCase();
		if(os.indexOf("win")>=0) {
			if(arch.indexOf("64")>=0) {
				load("jinput-dx8_64", ".dll");
				load("jinput-raw_64", ".dll");
			}else {
				load("jinput-dx8", ".dll");
				load("jinput-raw", ".dll");
				load("jinput-wintab", ".dll");
			}
		}else if(os.indexOf("mac")>=0) {
			load("jinput-osx", ".jnilib");
		}else if(os.indexOf("nix") >= 0 || os.indexOf("nux") >= 0 || os.indexOf("aix") >= 0 ) {
			if(arch.indexOf("64")>=0) {
				load("jinput-linux64", ".so");
			}else {
				load("jinput-linux", ".so");
			}
		}
		System.setProperty("java.library.path", System.getProperty("java.io.tmpdir")+"Sneks_temp/jinput_natives;"+System.getProperty("java.library.path"));
		System.out.println("Path: " + System.getProperty("java.library.path"));
		// try {
		// 	Field fieldSysPath = ClassLoader.class.getDeclaredField("sys_paths");
		// 	fieldSysPath.setAccessible(true);
		// 	fieldSysPath.set(null, null);
		// } catch (NoSuchFieldException e) {
		// 	e.printStackTrace();
		// } catch (SecurityException e) {
		// 	e.printStackTrace();
		// } catch (IllegalArgumentException e) {
		// 	e.printStackTrace();
		// } catch (IllegalAccessException e) {
		// 	e.printStackTrace();
		// }
		game = new Game();
	}
	
	public static void load(String file, String suff) {
		try {
			InputStream in = Start.class.getResource("/"+file+suff).openStream();
			String tempDir = System.getProperty("java.io.tmpdir")+"/Sneks_temp/jinput_natives";
			File directory = new File(tempDir);
			directory.mkdirs();
			File temp = new File(tempDir + "/" + file+suff);
			if(temp.exists()) {
				temp.delete();
			}
			temp.createNewFile();
			OutputStream out = new FileOutputStream(temp);
			byte[] buffer = new byte[1024];
            int readBytes;
            try {
                while ((readBytes = in.read(buffer)) != -1) {
                    out.write(buffer, 0, readBytes);
                }
            } finally {
                out.close();
                in.close();
            }
            System.out.println("Loaded " + file + suff);
		} catch (IOException e) {
			e.printStackTrace();
			System.exit(-1);
		}
	}

}
