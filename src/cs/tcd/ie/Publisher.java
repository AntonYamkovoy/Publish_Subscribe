package cs.tcd.ie;

import java.net.DatagramSocket;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetSocketAddress;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Arrays;

import java.time.Instant;
import javax.swing.Timer;

import tcdIO.*;
//17331565 Anton Yamkovoy
/**
 *
 * Publisher class
 * 
 * able to send topic and message to broker
 * message must have seqNumber;
 *
 */
public class Publisher extends Node {
	
	class TimerHandler implements ActionListener{
		public Publisher owner;
		public TimerHandler(Publisher owner_) {
			super();
			owner = owner_;
		}
		@Override
		public void actionPerformed(ActionEvent arg0) {
		    try {
				owner.onTimer();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		  }

	}
	
	static final int DEFAULT_SRC_PORT = 50000;
	static final int DEFAULT_DST_PORT = 50001;
	static final String DEFAULT_DST_NODE = "localhost";	
	PublisherTerminal terminal;
	InetSocketAddress dstAddress;
	int sequenceNumber;
	ArrayList<DatagramPacket> messages;
	int timeoutInterval = 10000;
	Timer t;
	ArrayList<Message> window;
	
	
	/**
	 * Constructor
	 * 	 
	 * Attempts to create socket at given port and create an InetSocketAddress for the destinations
	 */
	Publisher(PublisherTerminal terminal, String dstHost, int dstPort, int srcPort) {
		try {
			this.terminal= terminal;
			dstAddress= new InetSocketAddress(dstHost, dstPort);
			socket= new DatagramSocket(srcPort);
			listener.go();
			sequenceNumber = 0;
			window = new ArrayList<Message>();
			messages = new ArrayList<DatagramPacket>();
			terminal.timeoutTextField.setText("10000");
			t = new Timer(3000, new TimerHandler(this));
			t.setRepeats(false);
		}
		catch(java.lang.Exception e) {e.printStackTrace();}
		
	}
	
	public void removeMessageFromWindow(int sequenceNumber, int sender) {
		for(int i=0; i < window.size(); i++) {
			Message message = window.get(i);
			if(message.sequenceNumber == sequenceNumber && message.recieverAddress  == sender) {
				window.remove(message);
				break;
				
			}
			
			
			
		}
		
	}
	
	public void nackUpdate(int sequenceNumber, int sender) {
		for(int i=0; i < window.size(); i++) {
			Message message = window.get(i);
			if(message.sequenceNumber == sequenceNumber && message.recieverAddress  == sender) {
				message.gotNack = true;
				
			}
			
			
			
		}
		
	}	

	public synchronized void onTimer() throws Exception {
		this.timeoutInterval = Integer.parseInt(terminal.timeoutTextField.getText());
		Timestamp currentTime = new Timestamp(System.currentTimeMillis());
		Timestamp timeoutTime = new Timestamp(System.currentTimeMillis());
		int firstWrongMessageIndex = window.size();
		boolean needToResend = false;
		ArrayList<Message> messagesToSend = new ArrayList<Message>();
		for(int i=0; i < window.size(); i++) {
			Message message = window.get(i);
			timeoutTime.setTime(message.timeSentAt.getTime() + timeoutInterval);
			if(needToResend)
				messagesToSend.add(window.get(i));
			else if(currentTime.compareTo(timeoutTime) > 0) {
				firstWrongMessageIndex = i;
				needToResend = true;
				messagesToSend.add(window.get(i));
			}			
		}
		if(messagesToSend.size() != 0) {
			terminal.println("messagesToSend size = "+messagesToSend.size()+"\n");
			terminal.println("firstWrongMessage :"+firstWrongMessageIndex+"\n");
			
		} 
		
		for(int i = window.size()-1; i >= firstWrongMessageIndex; i--) {
			window.remove(window.get(i));
		}
		for(int i = 0; i < messagesToSend.size(); i++) {
			Message m = messagesToSend.get(i);
			publishResend(m);
		}
		//terminal.println("timer re-started\n");
		t.start();		
	}
	
	public synchronized void onReceipt(DatagramPacket packet) {
			
			String content =getStringFromPacket(packet);
			if(content.contains("OK")|| content.contains("NOK")) {			
				terminal.println(content.toString()+"\n");
				if(content.contains("NOK")) {
					String[] contents = content.split("\\|"); 
					int sequenceNumber = Integer.parseInt(contents[1]);
					int sender = Integer.parseInt(contents[2]);
					nackUpdate(sequenceNumber,sender);
					
				}
				else if(content.contains("OK")) {
					String[] contents = content.split("\\|"); 
					int sequenceNumber = Integer.parseInt(contents[1]);
					int sender = Integer.parseInt(contents[2]);
					removeMessageFromWindow(sequenceNumber,sender);
					
						
					
				} 
			}
			//terminal.sendButton.setEnabled(true);
			
			this.notify();
		
	}

	
	
	public synchronized void start() throws Exception {
		//terminal.println("timer started\n");
        t.start();	
		while(true) {
				
				this.wait();				
		}
		
	}
	
public void publishResend(Message message) throws Exception {
		
		terminal.println("re-sending publisher message: "+message.sequenceNumber+"\n");
		Timestamp timestamp = new Timestamp(System.currentTimeMillis());
		socket.send(message.packet); 
		window.add(new Message(timestamp,message.packet,message.recieverAddress,message.sequenceNumber));		
		terminal.println("publisher message resent.\n");
		
		
		
	}
	
	
	public void publish() throws Exception {
		
		byte[] data= null;
		DatagramPacket packet= null;
		String type = terminal.textFieldType.getText();
		if(type.equals("Publish")) {
			type = "p";
		}
	    String dataPacket =type+"|"+terminal.textFieldTopic.getText()+"|"+terminal.textFieldMessage.getText();
	    dataPacket +="|"+sequenceNumber;
		data= dataPacket.getBytes();			
		terminal.println("sending publisher message\n");
		packet= new DatagramPacket(data, data.length, dstAddress);
		socket.send(packet); 
		Timestamp timestamp = new Timestamp(System.currentTimeMillis());		
		window.add(new Message(timestamp,packet,DEFAULT_DST_PORT,sequenceNumber));
		sequenceNumber++;
		terminal.println("publisher message sent.\n");
		
		
		
	}


	/**
	 * Test method
	 * 
	 * Sends a packet to a given address
	 */
	public static void main(String[] args) {
		try {
			int publisherPort = Integer.parseInt(args[0]);
			PublisherTerminal terminal= new PublisherTerminal();		
			Publisher publisher = new Publisher(terminal, DEFAULT_DST_NODE, DEFAULT_DST_PORT, publisherPort);
			terminal.owner = publisher;
			publisher.start();
			
		} catch(java.lang.Exception e) {e.printStackTrace();}
	}
}