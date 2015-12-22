package TwilioAPI;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.*;
import java.util.regex.Pattern;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import com.twilio.sdk.*; 
import com.twilio.sdk.resource.factory.*; 
import com.twilio.sdk.resource.instance.*;

public class TwilioUtility {
	private static final String CONFIG_FILE_PATH = "resources/api.config";
	private static final String DEFAULT_TWILIO_NUMBER = getTwilioNumber(); //Enter your Twilio phone number here
	private static final Pattern ALL_NUMERIC_PATTERN = Pattern.compile("\\d+");

	public static boolean sendTextMessage(String fromNumber, String toNumber, String text){
		final String ACCOUNT_SID = getAccountSIDByPhoneNumber(fromNumber);
		final String AUTH_TOKEN = getAuthTokenByPhoneNumber(fromNumber);
		return sendTextMessage(fromNumber, toNumber, text, ACCOUNT_SID, AUTH_TOKEN);
	}

	public static String validatePhoneNumber(String phoneNumber){
		if (phoneNumber == null || phoneNumber.length() < 1 || phoneNumber.equals("+")){
			return null;
		}

		phoneNumber = phoneNumber.trim().replaceAll("-", "").replace("+", "").replaceAll(" ", "");

		return ALL_NUMERIC_PATTERN.matcher(phoneNumber).matches() ? "+" + phoneNumber : null;
	}

	public static boolean sendTextMessage(String fromNumber, String toNumber, String text, String accountSID, String authToken){
		try{
			fromNumber = validatePhoneNumber(fromNumber);
			toNumber = validatePhoneNumber(toNumber);
			if (text == null || fromNumber == null || toNumber == null || accountSID == null || authToken == null){
				return false;
			}
			TwilioRestClient client = new TwilioRestClient(accountSID, authToken); 
			List<NameValuePair> params = new ArrayList<NameValuePair>();

			params.add(new BasicNameValuePair("To", toNumber)); 
			params.add(new BasicNameValuePair("From", fromNumber)); 
			params.add(new BasicNameValuePair("Body", text)); 

			MessageFactory messageFactory = client.getAccount().getMessageFactory(); 
			Message message = messageFactory.create(params);

			return (message.getStatus() != null);
		} catch (Exception e){
			e.printStackTrace();
			return false;
		}
	}

	/** Sends a text message from the default Twilio Account in the 
	 	repository (add your own twilio Account phone number to the 
	 	class level final static String variable) **/
	public static boolean sendTextMessage(String toNumber, String text){
		return sendTextMessage(DEFAULT_TWILIO_NUMBER, toNumber, text, getAccountSID(), getAuthToken());
	}

	private static String getAccountSID() {
		return loadAPICredentials()[0];
	}

	private static String getAccountSIDByPhoneNumber(String phoneNumber){
		return loadAPICredentialsByPhoneNumber(phoneNumber)[0];
	}

	private static String getAuthToken(){
		return loadAPICredentials()[1];
	}

	private static String getAuthTokenByPhoneNumber(String phoneNumber){
		return loadAPICredentialsByPhoneNumber(phoneNumber)[1];
	}

	/** Finds the first possible API Credentials (ACCOUNT_SID and AUTH_TOKEN) and returns
	 	them as a pair of Strings in a String array 
		<br/>
	  	@return {ACCOUNT_SID, AUTH_TOKEN} 
	 **/
	@SuppressWarnings("resource")
	private static final String [] loadAPICredentials(){
		String line = null;
		String [] apiCredentials = new String [2];

		try {
			FileReader fileReader = new FileReader(CONFIG_FILE_PATH);
			BufferedReader bufferedReader = new BufferedReader(fileReader);
			boolean apiCredentialsFound = false;
			int currentAPICredential = 0;

			while((line = bufferedReader.readLine()) != null) {
				if (apiCredentialsFound && currentAPICredential < 2 && currentAPICredential >= 0){
					apiCredentials[currentAPICredential++] = (line == null) ? line : line.trim();
				} else {
					apiCredentialsFound = line != null && line.trim().indexOf("+") >= 0;
				}
			}
		} catch (Exception e){
			return apiCredentials;
		}
		return apiCredentials;
	}

	/** Finds the personal phone number in the config file (not included in public
		GitHub repository) associated with the phrase "Personal Phone Number:"
		
  		@return Phone Number
	 **/
	@SuppressWarnings("resource")
	public static final String getPersonalPhoneNumber(){
		String line = null;
		String personalPhoneNumber = null;

		try {
			FileReader fileReader = new FileReader(CONFIG_FILE_PATH);
			BufferedReader bufferedReader = new BufferedReader(fileReader);
			
			while((line = bufferedReader.readLine()) != null) {
				if (line != null && line.indexOf("Personal Phone Number:") >= 0){
					String [] lineSplit = line.split(":");
					personalPhoneNumber = (lineSplit != null && lineSplit.length > 1 && lineSplit[1] != null) ? lineSplit[1].trim() : null;
					return personalPhoneNumber;
				}
			}
		} catch (Exception e){
			return personalPhoneNumber;
		}
		return personalPhoneNumber;
	}
	
	@SuppressWarnings("resource")
	public static final String getTwilioNumber(){
		String line = null;
		String personalPhoneNumber = null;

		try {
			FileReader fileReader = new FileReader(CONFIG_FILE_PATH);
			BufferedReader bufferedReader = new BufferedReader(fileReader);
			
			while((line = bufferedReader.readLine()) != null) {
				if (line != null && line.indexOf("Twilio Phone Number:") >= 0){
					String [] lineSplit = line.split(":");
					personalPhoneNumber = (lineSplit != null && lineSplit.length > 1 && lineSplit[1] != null) ? lineSplit[1].trim() : null;
					return personalPhoneNumber;
				}
			}
		} catch (Exception e){
			return personalPhoneNumber;
		}
		return personalPhoneNumber;
	}
	
	/** Finds the API Credentials (ACCOUNT_SID and AUTH_TOKEN) that correspond to 
	 	given phone number and returns them as a pair of Strings in a String array 

		<br/>
  		@return {ACCOUNT_SID, AUTH_TOKEN} 
	 **/
	@SuppressWarnings("resource")
	private static final String [] loadAPICredentialsByPhoneNumber(String phoneNumber){
		String line = null;
		String [] apiCredentials = new String [2];

		if (phoneNumber == null || phoneNumber.trim().length() < 1){
			return apiCredentials;
		}

		phoneNumber = phoneNumber.trim().replaceAll("-", "").replace("+", "").replaceAll(" ", "");

		if (ALL_NUMERIC_PATTERN.matcher(phoneNumber).matches()){
			try {
				FileReader fileReader = new FileReader(CONFIG_FILE_PATH);
				BufferedReader bufferedReader = new BufferedReader(fileReader);
				boolean apiCredentialsFound = false;
				int currentAPICredential = 0;

				while((line = bufferedReader.readLine()) != null) {
					if (apiCredentialsFound && currentAPICredential < 2 && currentAPICredential >= 0){
						apiCredentials[currentAPICredential++] = (line == null) ? line : line.trim();
					} else {
						line = line.trim().replaceAll("-", "").replace("+", "").replaceAll(" ", "");
						apiCredentialsFound = ALL_NUMERIC_PATTERN.matcher(line).matches() && line.equals(phoneNumber);
					}
				}
			} catch (Exception e){
				return apiCredentials;
			}
		}
		return apiCredentials;
	}
}