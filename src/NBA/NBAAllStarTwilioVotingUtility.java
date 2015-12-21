package NBA;

import java.util.ArrayList;

import TwilioAPI.TwilioUtility;

public final class NBAAllStarTwilioVotingUtility {
	public static final String [] EASTERN_CONFERENCE_PLAYERS = new String [] 
			{"John Wall", "Bradley Beal", "Marcin Gortat", "Gary Neal", "Jared Dudley",
					"Kris Humphries", "Ramon Sessions", "Garrett Temple"};
	public static final String [] WESTERN_CONFERENCE_PLAYERS = new String []
			{"Kobe Bryant", "Trevor Ariza"};
	public static final String NBA_VOTING_TEXT_MESSAGE_NUMBER = "";
	
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
	
	public static void sendNBATexts(String twitterHandle) {
		for (String text: getNBATexts()){
			TwilioUtility.sendTextMessage(NBA_VOTING_TEXT_MESSAGE_NUMBER, text);
		}
	}
}
