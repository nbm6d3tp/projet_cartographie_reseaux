package fr.internetcartographier.model.firebase;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import fr.internetcartographier.util.statistics.Statistic;
import fr.internetcartographier.util.statistics.Statistics;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.lang.reflect.Type;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;
import java.util.Optional;

/**
 * Class which manage the connection to database (Realtime Database - Firebase)
 */
public class DatabaseConnection {
	/**
	 * Web API Key
	 */
	private final static String KEY = "AIzaSyAlp6owp-Up0lpAw1R1eBTlUF8MbOnwfak";

	/**
	 * Save an user to database (used when an user wants to create an account/sign
	 * up)
	 *
	 * @param  email
	 * @param  password
	 * @return          info of the user if request succeeds, else null
	 */
	public static Optional<UserInfo> saveUser(String email, String password) {
		try {
			URL url = new URL("https://identitytoolkit.googleapis.com/v1/accounts:signUp?key=" + KEY);
			HttpURLConnection connection = (HttpURLConnection) url.openConnection();
			connection.setRequestMethod("POST");
			connection.setRequestProperty("Content-Type", "application/json");
			connection.setDoOutput(true);
			String requestBody = "{\"email\":\"" + email + "\",\"password\":\"" + password + "\"}";
			OutputStream outputStream = connection.getOutputStream();
			outputStream.write(requestBody.getBytes());
			outputStream.flush();
			outputStream.close();

			BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
			String line;
			StringBuilder response = new StringBuilder();
			while ((line = reader.readLine()) != null) {
				response.append(line);
			}

			reader.close();

			Gson gson = new Gson();
			Response responseJson = gson.fromJson(response.toString(), Response.class);

			URL urlUpdate = new URL("https://identitytoolkit.googleapis.com/v1/accounts:update?key=" + KEY);
			connection = (HttpURLConnection) urlUpdate.openConnection();
			connection.setRequestMethod("POST");
			connection.setRequestProperty("Content-Type", "application/json");
			connection.setDoOutput(true);
			String requestBodyUpdate = "{\"localId\":\"" + responseJson.localId + "\"}";
			outputStream = new DataOutputStream(connection.getOutputStream());
			outputStream.write(requestBodyUpdate.getBytes());
			outputStream.flush();
			outputStream.close();

			connection.disconnect();
			return Optional.of(new UserInfo(responseJson.localId, email));
		} catch (MalformedURLException e) {
			throw new IllegalStateException(e);
		} catch (ProtocolException e) {
			throw new IllegalStateException(e);
		} catch (IOException e) {
			throw new IllegalStateException(e);
		}
	}

	/**
	 * Get info of an user from database (used when an user wants to login)
	 *
	 * @param  email
	 * @param  password
	 * @return          info of the user if request succeeds, else null
	 */
	public static Optional<UserInfo> getUser(String email, String password) {
		try {
			URL url = new URL("https://identitytoolkit.googleapis.com/v1/accounts:signInWithPassword?key=" + KEY);
			HttpURLConnection connection = (HttpURLConnection) url.openConnection();
			connection.setRequestMethod("POST");
			connection.setRequestProperty("Content-Type", "application/json");
			connection.setDoOutput(true);
			String requestBody = "{\"email\":\"" + email + "\",\"password\":\"" + password + "\"}";
			OutputStream outputStream = connection.getOutputStream();
			outputStream.write(requestBody.getBytes());
			outputStream.flush();
			outputStream.close();
			int responseCode = connection.getResponseCode();

			BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
			String line;
			StringBuilder response = new StringBuilder();
			while ((line = reader.readLine()) != null) {
				response.append(line);
			}

			reader.close();
			connection.disconnect();

			Gson gson = new Gson();
			Response responseJson = gson.fromJson(response.toString(), Response.class);
			return Optional.of(new UserInfo(responseJson.localId, email));
		} catch (MalformedURLException e) {
			throw new IllegalStateException(e);
		} catch (ProtocolException e) {
			throw new IllegalStateException(e);
		} catch (IOException e) {
			throw new IllegalStateException(e);
		}
	}

	/**
	 * Save a statistic to database
	 *
	 * @param idUser
	 * @param statistics
	 */
	public static void saveHistory(String idUser, Statistics statistics) {
		try {
			URL url = new URL("https://internet-cartographier-default-rtdb.europe-west1.firebasedatabase.app/" + idUser
					+ ".json");
			HttpURLConnection connection = (HttpURLConnection) url.openConnection();
			connection.setRequestMethod("POST");
			connection.setRequestProperty("Content-Type", "application/json");
			connection.setDoOutput(true);

			Gson gson = new Gson();
			StatisticDatabase statisticDatabase = new StatisticDatabase();
			for (Statistic<?> statistic : statistics) {
				statisticDatabase.putEntry(statistic.getName(), statistic.getResultInString());
			}

			Type typeObject = new TypeToken<HashMap<String, String>>() {
			}.getType();
			String requestBody = gson.toJson(statisticDatabase.getMap(), typeObject);
			OutputStream outputStream = connection.getOutputStream();
			outputStream.write(requestBody.getBytes());
			outputStream.flush();
			outputStream.close();

			int responseCode = connection.getResponseCode();
			System.out.println("Response Code: " + responseCode);

			BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
			String line;
			StringBuilder response = new StringBuilder();
			while ((line = reader.readLine()) != null) {
				response.append(line);
			}
			reader.close();
			connection.disconnect();
		} catch (IOException e) {
			throw new IllegalStateException(e);
		}
	}

	/**
	 * Get list of histories of an user from database
	 *
	 * @param idUser
	 * @param statistics
	 */
	public static List<StatisticDatabase> getHistories(String idUser) {
		try {
			URL url = new URL("https://internet-cartographier-default-rtdb.europe-west1.firebasedatabase.app/" + idUser
					+ ".json");
			HttpURLConnection connection = (HttpURLConnection) url.openConnection();
			connection.setRequestMethod("GET");
			connection.setRequestProperty("Content-Type", "application/json");
			connection.setDoOutput(true);

			int responseCode = connection.getResponseCode();
			System.out.println("Response Code: " + responseCode);
			BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
			String line;
			StringBuilder response = new StringBuilder();
			while ((line = reader.readLine()) != null) {
				response.append(line);
			}
			System.out.println(response);
			reader.close();
			connection.disconnect();
			Gson gson = new Gson();

			List<StatisticDatabase> statisticDatabases = new ArrayList<>();
			Type statisticType = new TypeToken<HashMap<String, HashMap<String, String>>>() {
			}.getType();
			HashMap<String, HashMap<String, String>> statisticsMap = gson.fromJson(response.toString(), statisticType);
			if (statisticsMap == null) {
				return statisticDatabases;
			}
			for (Entry<String, HashMap<String, String>> entry : statisticsMap.entrySet()) {
				HashMap<String, String> val = entry.getValue();
				StatisticDatabase statisticDatabase = new StatisticDatabase();
				for (Entry<String, String> innerEntry : val.entrySet()) {
					String innerKey = innerEntry.getKey();
					String innerVal = innerEntry.getValue();
					statisticDatabase.putEntry(innerKey, innerVal);
				}
				statisticDatabases.add(statisticDatabase);
			}
			return statisticDatabases;
		} catch (IOException e) {
			throw new IllegalStateException(e);
		}
	}
}
