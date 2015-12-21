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

	public static void main(String[] args) throws TwilioRestException {
		TwilioRestClient client = new TwilioRestClient(getAccountSID(), getAuthToken()); 

		List<NameValuePair> params = new ArrayList<NameValuePair>(); 
		params.add(new BasicNameValuePair("To", "+14437974588")); 
		params.add(new BasicNameValuePair("From", "+15128174588")); 
		params.add(new BasicNameValuePair("Body", "John Wall #NBAVote")); 
		
		MessageFactory messageFactory = client.getAccount().getMessageFactory(); 
		Message message = messageFactory.create(params);
		
		System.out.println(message.getSid()); 
	} 

	private static String getAccountSID() {
		return loadAPICredentials()[0];
	}

	public static String getAccountSIDByPhoneNumber(String phoneNumber){
		return loadAPICredentialsByPhoneNumber(phoneNumber)[0];
	}

	private static String getAuthToken(){
		return loadAPICredentials()[1];
	}

	public static String getAuthTokenByPhoneNumber(String phoneNumber){
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

		phoneNumber = phoneNumber.trim().replaceAll("-", "").replaceAll("+", "").replaceAll(" ", "");
		Pattern pattern = Pattern.compile("\\d+");

		if (pattern.matcher(phoneNumber).matches()){
			try {
				FileReader fileReader = new FileReader(CONFIG_FILE_PATH);
				BufferedReader bufferedReader = new BufferedReader(fileReader);
				boolean apiCredentialsFound = false;
				int currentAPICredential = 0;

				while((line = bufferedReader.readLine()) != null) {
					if (apiCredentialsFound && currentAPICredential < 2 && currentAPICredential >= 0){
						apiCredentials[currentAPICredential++] = (line == null) ? line : line.trim();
					} else {
						line = line.trim().replaceAll("-", "").replaceAll("+", "").replaceAll(" ", "");
						apiCredentialsFound = pattern.matcher(line).matches() && line.equals(phoneNumber);
					}
				}
			} catch (Exception e){
				return apiCredentials;
			}
		}
		return apiCredentials;
	}
}