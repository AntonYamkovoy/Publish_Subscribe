package cs.tcd.ie;
//17331565 Anton Yamkovoy
import java.net.DatagramSocket;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.*;
import tcdIO.*;








public class SubTerminal {
	String commandType;
	String topic;
	boolean ack = true;
	JFrame f;
	JLabel labelAck;
	JButton sendButton;
	JButton ackB;
	JLabel cmdLabel;
	JLabel topicLabel;
	JTextField topicTextField;
	JTextField cmdTextField;
	JPanel container;
	JScrollPane jsp;
	JLabel lblNewLabel;
	List subList;
	JLabel lblSubscriptionsList;
	JLabel lblSubscriptionFeed;
	TextArea textArea;
	
	Subscriber owner;
	 
	SubTerminal(){  
		f=new JFrame("Subscriber Terminal"); 
		frameSetup(f);
	}
	
	
	public static void main(String[] args) {    
		   SubTerminal t =  new SubTerminal();  
		   
		   
		   
		    
	}
	
	public void println(String string) {
		this.textArea.append(string);
		
	}
	
	
	
	
	  
		
		
	public void frameSetup(Frame f) {
			
			
			labelAck = new JLabel();
			labelAck.setBounds(441,75,65,30);
			labelAck.setText("ack on? :");
			
			
			sendButton=new JButton("Send"); 
			sendButton.setBounds(240,50,65,30); 
			
			
			ackB = new JButton("ack");
			ackB.setBounds(441,50,65,30); 
			
			cmdLabel = new JLabel();		
			cmdLabel.setText("Command type:");
			cmdLabel.setBounds(20, 15, 100, 30);
			
			topicLabel = new JLabel();		
			topicLabel.setText("<Topic>:");
			topicLabel.setBounds(130, 15, 100, 30);
						
			
			cmdTextField= new JTextField();
			cmdTextField.setBounds(20, 50, 100, 30);
			topicTextField= new JTextField();
			topicTextField.setBounds(125, 50, 100, 30);
			
			
			
		
			container = new JPanel();
		    jsp = new JScrollPane(container);
		    container.setPreferredSize(new Dimension(500, 250));
		    container.setLayout(null);

		    

		    ((JFrame) f).getContentPane().add(jsp);
			((JFrame) f).getContentPane().add(cmdTextField);
			((JFrame) f).getContentPane().add(cmdLabel);
			((JFrame) f).getContentPane().add(labelAck);
			((JFrame) f).getContentPane().add(topicLabel);
			((JFrame) f).getContentPane().add(topicTextField);
			((JFrame) f).getContentPane().add(sendButton); 
			((JFrame) f).getContentPane().add(ackB);
			((JFrame) f).getContentPane().setLayout(null);
			
			lblNewLabel = new JLabel("Latest command sent: ");
			lblNewLabel.setBounds(27, 107, 278, 14);
			((JFrame) f).getContentPane().add(lblNewLabel);
			
			subList = new List();
			
			subList.setBounds(441, 133, 110, 133);
			((JFrame) f).getContentPane().add(subList);
			
			lblSubscriptionsList = new JLabel("Subscriptions List");
			lblSubscriptionsList.setBounds(441, 116, 122, 14);
			((JFrame) f).getContentPane().add(lblSubscriptionsList);
			
			lblSubscriptionFeed = new JLabel("Subscription feed");
			lblSubscriptionFeed.setBounds(24, 150, 122, 14);
			((JFrame) f).getContentPane().add(lblSubscriptionFeed);
			
			textArea = new TextArea();
			textArea.setBounds(24, 170, 281, 229);
			((JFrame) f).getContentPane().add(textArea);
			f.setSize(600,600);    
			// textArea .append to add text
			f.setVisible(true);    
			
			((JFrame) f).setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);   
			
			
			
			
			
			
			sendButton.addActionListener(new ActionListener() {
		        
				@Override
				public void actionPerformed(ActionEvent arg0) {
					synchronized(owner) {
						String commandType = cmdTextField.getText();
						String topic = topicTextField.getText();
						lblNewLabel.setText("Latest command sent: "+commandType+" "+topic);	
						try {
							owner.subscribe();
						} catch (Exception e) {
							e.printStackTrace();
						}				
						sendButton.setEnabled(false);
						owner.notify();
					}
						
						
				}          
		      });
			 
			
			ackB.addActionListener(new ActionListener() {
	        
			@Override
			public void actionPerformed(ActionEvent arg0) {
					ack = !ack;
					labelAck.setText("ack on?: "+(ack == true ? "true": "false"));	
				
					
					
					
				}          
			});
			
		}
 }