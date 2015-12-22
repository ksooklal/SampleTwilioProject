package NBA;

import java.util.ArrayList;

import TwilioAPI.TwilioUtility;

public final class NBAAllStarTwilioVotingUtility {
	
	public static final String [] EASTERN_CONFERENCE_PLAYERS = new String [] 
			{"John Wall", "Bradley Beal", "Marcin Gortat", "Gary Neal", "Jared Dudley",
					"Kris Humphries", "Ramon Sessions", "Garrett Temple"};
	
	public static final String [] WESTERN_CONFERENCE_PLAYERS = new String []
			{"Kobe Bryant", "Trevor Ariza"};
	
	/** Unfortunately Twilio does not support texting to short code phone numbers, so this 
	 	class and logic is all theoretical. **/
	public static final String NBA_VOTING_TEXT_MESSAGE_NUMBER = "69622";
	
	public static ArrayList<String> get2016EasternConferenceTexts(){
		ArrayList<String> texts = new ArrayList<String>();
		for (String player: EASTERN_CONFERENCE_PLAYERS){
			texts.add(player + " #NBAVote");
		}
		return texts;
	}
	
	public static ArrayList<String> get2016WesternConferenceTexts(){
		ArrayList<String> texts = new ArrayList<String>();
		for (String player: WESTERN_CONFERENCE_PLAYERS){
			texts.add(player + " #NBAVote");
		}
		return texts;
	}
	
	public static ArrayList<String> getNBATexts(){
		ArrayList<String> easternTexts = get2016EasternConferenceTexts();
		easternTexts.addAll(get2016WesternConferenceTexts());
		return easternTexts;
	}
	
	public static void sendNBATexts(String phoneNumber) {
		for (String text: getNBATexts()){
			TwilioUtility.sendTextMessage(phoneNumber, NBA_VOTING_TEXT_MESSAGE_NUMBER, text);
		}
	}
}
