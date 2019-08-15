package cs.tcd.ie;

import java.net.DatagramSocket;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.*;
import tcdIO.*;

//17331565 Anton Yamkovoy






public class PublisherTerminal {
	String commandType;
	String topic;
	boolean ack = true;
	String message;
	int serialNum;
	JTextField textFieldMessage;
	JLabel labelAck;
	JButton sendButton;
	JButton ackB;
	JLabel label;
	JLabel label2;
	JTextField textFieldType;
	JTextField textFieldTopic;
	JPanel container;
	JScrollPane jsp ;
	JScrollPane scrollPane;
	JTextArea textArea;
	List list;
	JLabel lblMessage;
	JLabel lblBrokerList;
	JLabel lblPublishRequestsSent;
	
	Publisher owner;
	
	JFrame f = new JFrame("Publisher Terminal");
	public JTextField timeoutTextField;
	 
	PublisherTerminal(){  
		JFrame f = new JFrame("Publisher Terminal");
		frameSetup(f);
		
		JLabel lblTimeout = new JLabel("Timeout:");
		lblTimeout.setBounds(441, 283, 55, 14);
		f.getContentPane().add(lblTimeout);
		
		timeoutTextField = new JTextField();
		timeoutTextField.setBounds(441, 303, 86, 20);
		f.getContentPane().add(timeoutTextField);
		timeoutTextField.setColumns(10);
		this.serialNum = 0;
	}
	
	
	public void println(String s) {
		this.textArea.append(s);
		
	}
	public static void main(String[] args) {
		JFrame f = new JFrame("Publisher Terminal");
		PublisherTerminal t = new PublisherTerminal();
	}
	
	
	
		   
		
		
		public void frameSetup(JFrame f) {
			labelAck = new JLabel();
			labelAck.setBounds(441,75,133,30);
			labelAck.setText("ack on? :");
			
			
			sendButton=new JButton("Publish"); 
			
			
			sendButton.setBounds(337,50,86,30); 
			ackB = new JButton("ack");
			ackB.setBounds(441,50,65,30); 
			label = new JLabel();		
			label.setText("Command type:");
			label.setBounds(20, 15, 100, 30);
			label2 = new JLabel();		
			label2.setText("<Topic>:");
			label2.setBounds(130, 15, 100, 30);
						
			textFieldType= new JTextField();
			textFieldType.setBounds(20, 50, 100, 30);
			textFieldTopic= new JTextField();
			textFieldTopic.setBounds(125, 50, 100, 30);
			
			
			
		
			 container = new JPanel();
		      jsp = new JScrollPane(container);
		      container.setPreferredSize(new Dimension(500, 250));
		      container.setLayout(null);

		    

		        f.getContentPane().add(jsp);
			f.getContentPane().add(textFieldType);
			f.getContentPane().add(label);
			f.getContentPane().add(labelAck);
			f.getContentPane().add(label2);
			f.getContentPane().add(textFieldTopic);
			f.getContentPane().add(sendButton); 
			f.getContentPane().add(ackB);
			f.getContentPane().setLayout(null);
			
			scrollPane = new JScrollPane();
			scrollPane.setBounds(20, 133, 285, 263);
			f.getContentPane().add(scrollPane);
			
			textArea = new JTextArea();
			scrollPane.setViewportView(textArea);
			
			List list = new List();
			
			list.setBounds(441, 133, 110, 133);
			f.getContentPane().add(list);
			
			lblMessage = new JLabel("Message");
			lblMessage.setBounds(240, 23, 65, 14);
			f.getContentPane().add(lblMessage);
			
			textFieldMessage = new JTextField();
			textFieldMessage.setBounds(235, 50, 86, 30);
			f.getContentPane().add(textFieldMessage);
			textFieldMessage.setColumns(10);
			
			lblBrokerList = new JLabel("Broker List");
			lblBrokerList.setBounds(441, 116, 96, 14);
			f.getContentPane().add(lblBrokerList);
			
			lblPublishRequestsSent = new JLabel("Publish requests sent");
			lblPublishRequestsSent.setBounds(20, 116, 138, 14);
			f.getContentPane().add(lblPublishRequestsSent);
			f.setSize(600,600);    
			// textArea .append to add text
			f.setVisible(true);    
			
			f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);   
			
			
			
			
			
			
			sendButton.addActionListener(new ActionListener() {
		        
				@Override
				public void actionPerformed(ActionEvent arg0) {
					synchronized(owner) {
						String commandType = textFieldType.getText();
						String topic = textFieldTopic.getText();
						String message = textFieldMessage.getText();
						
						textArea.append(serialNum++ +" "+commandType+" "+topic+" "+message+"\n");
						try {
							owner.publish();
						} catch (Exception e) {
							e.printStackTrace();
						}
						
						//sendButton.setEnabled(false);
						owner.notify();
					}
						
						
						
				}          
		      });
			 
			
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