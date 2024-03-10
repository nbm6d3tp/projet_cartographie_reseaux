package fr.internetcartographier.model.firebase;

/**
 * Model of information of an user
 */
public class UserInfo {
	/**
	 * Token id, necessary to make a request to database for informations relating
	 * to the user
	 */
	private String idToken;
	private String email;

	public UserInfo(String idToken, String email) {
		this.idToken = idToken;
		this.email = email;
	}

	@Override
	public String toString() {
		return "UserInfo [idToken=" + idToken + ", email=" + email;
	}

	public String getIdToken() {
		return idToken;
	}

	public void setIdToken(String idToken) {
		this.idToken = idToken;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}
}
