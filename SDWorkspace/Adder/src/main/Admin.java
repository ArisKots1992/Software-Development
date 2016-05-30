package main;

public class Admin {
	private static String username = "admin";
	private static String password = "123"; //"admin1234_i_have_a_terrible_password";

	public Admin() {}

	public static String get_username() {
		return username;
	}

	public static String get_password() {
		return password;
	}
}
