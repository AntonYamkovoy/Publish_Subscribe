package cs.tcd.ie;
//17331565 Anton Yamkovoy
import java.util.ArrayList;

public class SubscriberTopicsList {
	public int port;
	public ArrayList<String> topics;
	
	SubscriberTopicsList(int port) {
		this.port = port;
		topics = new ArrayList<String>();
		
	}
	
	int findTopic(String topic) {
		 for(int i=0; i < topics.size(); i++) {
			 if(topics.get(i).equals(topic)) {
				 return i; 
			 }
			 
		 }
		   
		   return -1;
	}

}
