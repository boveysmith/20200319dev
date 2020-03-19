/**
 * This software code is only used for the authorized 
 * person to maintain and two development. It is not 
 * allowed to be issued to the outside world. It is 
 * necessary to strictly observe the confidentiality 
 * agreement we have signed.
 * copyright by 1sdk.cn
 *
 */
package Files;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.prefs.BackingStoreException;
import java.util.prefs.Preferences;

/**
 * @author huangxianbo
 * @date 2019/12/19
 * @version V1.0
 */
public class Registery {
	private static Registery instance = null;
	private Registery() {}
	synchronized public static Registery getInstance() {
		if (instance == null) {
			instance = new Registery();
		}
		return instance;
	}

	// 把相应的值储存到变量中去
	public void writeValue(String[] keys, String[] values) {
		if (keys.length > values.length)
			return;
		// HKEY_LOCAL_MACHINE\Software\JavaSoft\prefs下写入注册表myprefer.(myprefer如果大些，注册表中会加“/”)
		Preferences pre = Preferences.systemRoot().node("/myprefer");
		for (int i = 0; i < keys.length; i++) {
			pre.put(keys[i], values[i]);
		}
	}

	public void writeValue(String key, String value) {
		// HKEY_LOCAL_MACHINE\Software\JavaSoft\prefs下写入注册表myprefer
		Preferences pre = Preferences.systemRoot().node("/myprefer");
		pre.put(key, value);
	}

	/***
	 * 
	 * 根据key获取value
	 *
	 */
	public String getValue(String key) {
		Preferences pre = Preferences.systemRoot().node("/myprefer");
		return pre.get(key, null);
	}

	/***
	 * 
	 * 清除注册表
	 *
	 * @throws BackingStoreException
	 */
	public void clearValue() throws BackingStoreException {
		Preferences pre = Preferences.systemRoot().node("/myprefer");
		pre.clear();
	}

//	public static void main(String[] args) throws BackingStoreException {
//		String[] keys = { "version", "initial", "creator" };
//		String[] values = { "1.3", "ini.mp3", "caokai1818@sina.com" };
//		Registery.getInstance().writeValue(keys, values);
//		System.out.println(Registery.getInstance().getValue("version"));
//		// 可以读取任意路径下的
//		try {
//			Process ps = null;
//			ps = Runtime.getRuntime().exec("reg query HKEY_LOCAL_MACHINE\\SOFTWARE\\JavaSoft\\Prefs\\myprefer");
//			ps.getOutputStream().close();
//			InputStreamReader i = new InputStreamReader(ps.getInputStream());
//			String line;
//			BufferedReader ir = new BufferedReader(i);
//			while ((line = ir.readLine()) != null) {
//				System.out.println(line);
//			}
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
//	}
}
