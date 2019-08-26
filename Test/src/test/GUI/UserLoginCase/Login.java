package test.GUI.UserLoginCase;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;

public class Login implements UserLogin {
	private static File file = new File("user.txt");

	// 类加载的时候创建文件
	static {
		try {
			file.createNewFile();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public boolean login(String username, String password) {
		boolean flag = false;

		try {
			BufferedReader br = new BufferedReader(new FileReader(file));
			String line = null;
			while ((line = br.readLine()) != null) {
				String[] up = line.split("=");
				if (up[0].equals(username) && up[1].equals(password)) {
					flag = true;
					break;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return flag;
	}

	public void regist(User user) {
		BufferedWriter writer = null;
		try {
			writer = new BufferedWriter(new FileWriter(file, true));
			writer.write(user.getUsername() + "=" + user.getPassword() + "\r\n");
			writer.flush();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				writer.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

}