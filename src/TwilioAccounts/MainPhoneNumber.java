package TwilioAccounts;

import NBA.NBAAllStarTwilioVotingUtility;
import TwilioAPI.TwilioUtility;

public final class MainPhoneNumber {

	public static final String PHONE_NUMBER = TwilioUtility.getPersonalPhoneNumber(); //Enter your phone number here

	public static String getPhoneNumber() {
		return PHONE_NUMBER;
	}

	public static void sendNBATexts(){
		NBAAllStarTwilioVotingUtility.sendNBATexts(PHONE_NUMBER);
	}

	public static void sendText(String toNumber, String text){
		TwilioUtility.sendTextMessage(toNumber, text);
	}
}
