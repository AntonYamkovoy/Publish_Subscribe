package cs.tcd.ie;

import java.net.DatagramSocket;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.*;
import tcdIO.*;


//17331565 Anton Yamkovoy





public class BrokerTerminal {
	
	boolean ack = true;
	JLabel labelAck;
	JButton ackB;
	JPanel container;
	JScrollPane jsp;
	JScrollPane scrollPane;
	JTextArea textArea;
	List subList;
	List pubList;
	JLabel lblTranscript;
	JLabel lblSubscriberList;
	JLabel lblPublisherList;
	
	
	 
	BrokerTerminal(){  
		JFrame f=new JFrame("Broker Terminal");
		frameSetup(f);
		
	}
	
	public void println(String s) {
		this.textArea.append(s);
	}
	
	
	
		public static void main(String[] args) {    
			BrokerTerminal t =  new BrokerTerminal();   
		   
		    
		}   
		
		
		public void frameSetup(JFrame f) {
			labelAck = new JLabel();
			labelAck.setBounds(441,75,133,30);
			labelAck.setText("ack on? :");
			
			
			ackB = new JButton("ack");
			ackB.setBounds(441,50,65,30);
			
			
			
		
			container = new JPanel();
		    jsp = new JScrollPane(container);
		    container.setPreferredSize(new Dimension(500, 250));
		    container.setLayout(null);

		    

		    f.getContentPane().add(jsp);
			f.getContentPane().add(labelAck);
			f.getContentPane().add(ackB);
			f.getContentPane().setLayout(null);
			
		    scrollPane = new JScrollPane();
			scrollPane.setBounds(20, 133, 285, 263);
			f.getContentPane().add(scrollPane);
			
			textArea = new JTextArea();
			scrollPane.setViewportView(textArea);
			
			subList = new List();
			
			subList.setBounds(441, 133, 110, 88);
			f.getContentPane().add(subList);
			
			lblTranscript = new JLabel("transcript");
			lblTranscript.setBounds(20, 108, 71, 14);
			f.getContentPane().add(lblTranscript);
			
			pubList = new List();
			pubList.setBounds(441, 244, 110, 132);
			f.getContentPane().add(pubList);
			
			lblSubscriberList = new JLabel("Subscriber List");
			lblSubscriberList.setBounds(438, 116, 100, 14);
			f.getContentPane().add(lblSubscriberList);
			
			lblPublisherList = new JLabel("Publisher list");
			lblPublisherList.setBounds(441, 227, 80, 14);
			f.getContentPane().add(lblPublisherList);
			f.setSize(600,600);    
			// textArea .append to add text
			f.setVisible(true);    
			
			f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			 
			
			ackB.addActionListener(new ActionListener() {
	        
			@Override
			public void actionPerformed(ActionEvent arg0) {
					ack = !ack;
					labelAck.setText("ack on?: "+(ack == true ? "true": "false"));	
					//list.add("+1");
					
					
				}          
			});
			
		}
 }