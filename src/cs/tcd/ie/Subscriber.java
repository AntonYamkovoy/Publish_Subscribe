package cs.tcd.ie;
//17331565 Anton Yamkovoy
import java.net.DatagramSocket;
import java.awt.Component;
import java.awt.Container;
import java.awt.List;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetSocketAddress;
import java.util.ArrayList;
import java.util.Arrays;

import javax.swing.JComponent;

import tcdIO.*;

/**
 *
 * Subscriber Class
 * 
 * receive messages on topic from broker
 * send topic to subscribe to / unsub from.
 * print out messages list recieved.
 * 
 */
public class Subscriber extends Node {
	
	
	
		private Subscriber owner;
	
	
	static final int DEFAULT_SRC_PORT = 50002;
	static final int DEFAULT_DST_PORT = 50001;
	static final String DEFAULT_DST_NODE = "localhost";	
	
	SubTerminal terminal;
	InetSocketAddress dstAddress;
	
	ArrayList<String> messagesReceived;
	ArrayList<String> topics;
	int subscriberPort;
	int delay;
	int timeoutTime;
	
	
	
	/**
	 * Constructor
	 * 	 
	 * Attempts to create socket at given port and create an InetSocketAddress for the destinations
	 * @wbp.parser.entryPoint
	 */
	Subscriber(SubTerminal terminal, String dstHost, int dstPort, int srcPort) {
		try {
			this.terminal= terminal;
			dstAddress= new InetSocketAddress(dstHost, dstPort);
			socket= new DatagramSocket(srcPort);
			listener.go();
			messagesReceived = new ArrayList<String>();
			subscriberPort = srcPort;
			topics = new ArrayList<String>();
			terminal.f.setName(String.valueOf(srcPort));
			

		   			

		}
		catch(java.lang.Exception e) {e.printStackTrace();}
	}

	
	/**
	 * Assume the receiving packet contains a message from a publisher via broker with a topic,message,sequence number
	 * @throws IOException 
	 */
	public synchronized void onReceipt(DatagramPacket packet) throws IOException {
		
		
		terminal.println("Length: "+packet.getLength()+"\n");
		String msg = getStringFromPacket(packet);
		terminal.println(msg+"\n");
		if(msg.contains("OK"))
		{
			this.notify();
			terminal.sendButton.setEnabled(true);
			return;
		}
		this.notify();
		String content= getStringFromPacket(packet);
		String[] contentArray = content.split("\\|");
		
		String topic = contentArray[0];
		String message = contentArray[2];
		String seqNumber = contentArray[1];
		String update = "#"+seqNumber+" "+topic+" "+message;
		messagesReceived.add(update);		
		terminal.println("Received message from broker:\n"+ update+"\n");
		if(terminal.ack ==true) {
			sendAck(DEFAULT_DST_PORT,Integer.parseInt(seqNumber),DEFAULT_SRC_PORT);
		}
		
		terminal.println("Send ack to broker\n");
	
		this.notify();

		
		
	}
	
	public void subscribe() throws Exception {
		byte[] data= null;
		String type = terminal.cmdTextField.getText();
		DatagramPacket packet= null;
		if(type.equals("Subscribe")) {
			type = "s";
		}
		else if(type.equals("Unsubscribe")) {
			type = "u";
		}
		String input = type+"|"+terminal.topicTextField.getText();
		data= (input +"|"+subscriberPort).getBytes();
		topics.add(terminal.topicTextField.getText());
		terminal.println("Sending request.");
		packet= new DatagramPacket(data, data.length, dstAddress);
		socket.send(packet);
		terminal.println("Sub/Unsub request sent.");
		
		
		
	}	
	



	
	/**
	 * Sender Method
	 * 
	 */

	public synchronized void start() throws Exception {
		
		while(true) {
				this.wait();				
		}
		
	}

	
	
	
	
	

	/**
	 * Test method
	 * 
	 * Sends a sub/unsub request to the broker, waits for packets from broker
	 */
	public static void main(String[] args) {
		if (args.length != 1) {
			System.out.println("sub requires port in command line");
			return;
		}
		int subscriberPort = Integer.parseInt(args[0]);
		
		try {
			
			SubTerminal terminal= new SubTerminal();		
			Subscriber sub = new Subscriber(terminal, DEFAULT_DST_NODE, DEFAULT_DST_PORT,subscriberPort);
			terminal.owner = sub;
			sub.start();
			
				
			
		} catch(java.lang.Exception e) {e.printStackTrace();}
	}
}