/**
//17331565 Anton Yamkovoy
 * 
 */
package cs.tcd.ie;

import java.net.DatagramSocket;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetSocketAddress;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Queue;

import tcdIO.*;

/**
 *
 * Broker class
 * 
 * receives subs/unsubs from subscribers.
 * receives publish request from publisher
 * forwards messages from publisher to subs.
 *
 */



public class Broker extends Node {
	static final int DEFAULT_PUBLISHER_PORT = 50000;	
	static final int DEFAULT_SRC_PORT = 50001;
	static final int DEFAULT_DST_PORT = 50002;
	static final String DEFAULT_DST_NODE = "localhost";	
	
	BrokerTerminal terminal;
	InetSocketAddress dstAddress;
	ArrayList<SubscriberTopicsList> subscriberTopicsLists;
	Queue<String> queue;
	int windowSize = 1;
	int delay;
	int timeoutTime;
	
	
	
	/**
	 * Constructor
	 * 	 
	 * Attempts to create socket at given port and create an InetSocketAddress for the destinations
	 */
	Broker(BrokerTerminal terminal, String dstHost, int dstPort, int srcPort) {
		try {
			this.terminal= terminal;
			dstAddress= new InetSocketAddress(dstHost, dstPort);
			socket= new DatagramSocket(srcPort);
			listener.go();
			subscriberTopicsLists = new ArrayList<SubscriberTopicsList>();
			queue = new LinkedList<String>();
			
			
		}
		catch(java.lang.Exception e) {e.printStackTrace();}
	}

	SubscriberTopicsList findSubscriberTopics(int subscriberId) {
	    for (SubscriberTopicsList s : subscriberTopicsLists) {
	    	if(s.port == subscriberId) {
	    		return s;
	    	}   	
	    }
	    return null;
	}
	
 
	

	
	/**
	 * Receive packets from subscriber with request to sub/unsub
	 * or receive publish distribution requests from publisher	
	 * @throws IOException 
	 */
	public synchronized void onReceipt(DatagramPacket packet) throws IOException {
		
		terminal.println("Length: "+packet.getLength()+"\n");
		String msg = getStringFromPacket(packet);
		terminal.println(msg+"\n");
		if(msg.contains("OK"))
		{
			this.notify();
			return;
		}
		String[] contents = msg.split("\\|");
		this.notify();
		
		if(contents[0].equals("s")|| contents[0].equals("u")) {
			String type = contents[0];
			String topic = contents[1];
			int subscriberId = Integer.parseInt(contents[2]);
			SubscriberTopicsList subObj = findSubscriberTopics(subscriberId); 
			if(subObj == null) {
				subObj = new SubscriberTopicsList(subscriberId);
				subscriberTopicsLists.add(subObj);
					
			}
			if(contents[0].equals("s")) {
				if(subObj.findTopic(topic) == -1) {
					subObj.topics.add(topic);
					terminal.println("Subscriber "+subscriberId+ " subscribed to "+topic+ "\n");
					if(terminal.ack ==true) {
						sendAck(subscriberId,-1, DEFAULT_SRC_PORT );
					}
					
				}
				
			}
			else if(contents[0].equals("u")) {
				int index = subObj.findTopic(topic);
				if(index != -1) {
					//topic is present
					subObj.topics.remove(index);
					terminal.println("Subscriber "+subscriberId+ " unsubscribed from "+topic+ "\n");
					if(terminal.ack ==true) {
						sendAck(subscriberId,-1, DEFAULT_SRC_PORT);
					}
					
				} 
			}
			// case where type isn't sub/unsub
			
			terminal.println("Sent n/ack to subscriber"+ subscriberId+"\n");



		}
		else if(contents[0].equals("p")) {
			String type = contents[0];
			String topic = contents[1];
			String message = contents[2];
			int sequenceNumber = Integer.parseInt(contents[3]);
			queue.add(topic+"|"+sequenceNumber+"|"+message);
			if(terminal.ack == true) {
				sendAck(DEFAULT_PUBLISHER_PORT,sequenceNumber, DEFAULT_SRC_PORT);
				terminal.println("Sent ack to publisher\n");
			}

			
		}
		else  {
			if(terminal.ack == true) {
				sendNack(packet.getPort(),-1, DEFAULT_SRC_PORT);
			}
		}
		
	}

	
	/**
	 * Finds the subscribers which are subscribed to the newest publish in the queue, and sends them the message and sequence number.
	 * 
	 */
	public synchronized void start() throws Exception {		
		
		while(true) {
			while(!queue.isEmpty()) 
			{
				String dataPacket = queue.remove();
				String[] info = dataPacket.split("\\|");
				String infoTopic = info[0];
				
				for(int i=0; i< subscriberTopicsLists.size(); i++) {
					SubscriberTopicsList subObj = subscriberTopicsLists.get(i);
					if(subObj.findTopic(infoTopic ) != -1) {
						byte[] data= null;
						DatagramPacket packet= null;	
						
						data = dataPacket.getBytes();
						terminal.println("Sending message to subs: "+dataPacket);
						
						InetSocketAddress address = new InetSocketAddress("localhost", subObj.port);
						packet= new DatagramPacket(data, data.length, address);
						socket.send(packet);

						terminal.println("publisher's message sent.");						
					}
				}
			
			}
			terminal.println("Waiting for publish requests...\n");
			this.wait();
		}
	}


	/**
	 * Test method
	 * 
	 * Sends a packet to a given address
	 */
	public static void main(String[] args) {
		try {					
			BrokerTerminal terminal= new BrokerTerminal();		
			(new Broker(terminal, DEFAULT_DST_NODE, DEFAULT_DST_PORT, DEFAULT_SRC_PORT)).start();
			terminal.println("Program completed");
		} catch(java.lang.Exception e) {e.printStackTrace();}
	}
}