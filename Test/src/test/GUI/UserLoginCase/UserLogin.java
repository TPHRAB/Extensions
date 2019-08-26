package test.GUI.UserLoginCase;

public interface UserLogin {
	public abstract boolean login(String username, String password);

	public abstract void regist(User user);
}
