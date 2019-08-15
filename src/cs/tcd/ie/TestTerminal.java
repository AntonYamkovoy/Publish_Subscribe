package cs.tcd.ie;
//17331565 Anton Yamkovoy
import java.net.DatagramSocket;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.*;
import tcdIO.*;








public class TestTerminal {
	String commandType;
	String topic;
	boolean ack = true;
	 
	TestTerminal(){  
		
		frameSetup();
	}
	
	
	
		public static void main(String[] args) {    
		   TestTerminal t =  new TestTerminal();   
		   
		    
		}   
		
		
		public void frameSetup() {
			JLabel labelAck = new JLabel();
			labelAck.setBounds(441,75,65,30);
			labelAck.setText("ack on? :");
			
			JFrame f=new JFrame("Subscriber Terminal"); 
			JButton sendButton=new JButton("Send"); 
			
			
			sendButton.setBounds(240,50,65,30); 
			JButton ackB = new JButton("ack");
			ackB.setBounds(441,50,65,30); 
			JLabel label = new JLabel();		
			label.setText("Command type:");
			label.setBounds(20, 15, 100, 30);
			JLabel label2 = new JLabel();		
			label2.setText("<Topic>:");
			label2.setBounds(130, 15, 100, 30);
						
			JTextField textfield= new JTextField();
			textfield.setBounds(20, 50, 100, 30);
			JTextField textfield2= new JTextField();
			textfield2.setBounds(125, 50, 100, 30);
			
			
			
		
			 JPanel container = new JPanel();
		        JScrollPane jsp = new JScrollPane(container);
		        container.setPreferredSize(new Dimension(500, 250));
		        container.setLayout(null);

		    

		        f.getContentPane().add(jsp);
			f.getContentPane().add(textfield);
			f.getContentPane().add(label);
			f.getContentPane().add(labelAck);
			f.getContentPane().add(label2);
			f.getContentPane().add(textfield2);
			f.getContentPane().add(sendButton); 
			f.getContentPane().add(ackB);
			f.getContentPane().setLayout(null);
			
			JScrollPane scrollPane = new JScrollPane();
			scrollPane.setBounds(20, 133, 285, 263);
			f.getContentPane().add(scrollPane);
			
			JTextArea textArea = new JTextArea();
			scrollPane.setViewportView(textArea);
			
			JLabel lblNewLabel = new JLabel("Latest command sent: ");
			lblNewLabel.setBounds(27, 107, 278, 14);
			f.getContentPane().add(lblNewLabel);
			
			List list = new List();
			
			list.setBounds(441, 133, 110, 133);
			f.getContentPane().add(list);
			
			JLabel lblSubscriptionsList = new JLabel("Subscriptions List");
			lblSubscriptionsList.setBounds(441, 116, 122, 14);
			f.getContentPane().add(lblSubscriptionsList);
			f.setSize(600,600);    
			// textArea .append to add text
			f.setVisible(true);    
			
			f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);   
			
			
			
			
			
			
			sendButton.addActionListener(new ActionListener() {
		        
				@Override
				public void actionPerformed(ActionEvent arg0) {
						String commandType = textfield.getText();
						String topic = textfield2.getText();
						lblNewLabel.setText("Latest command sent: "+commandType+" "+topic);	
						
						
				}          
		      });
			 
			
			ackB.addActionListener(new ActionListener() {
	        
			@Override
			public void actionPerformed(ActionEvent arg0) {
					ack = !ack;
					labelAck.setText("ack on?: "+(ack == true ? "1": "0"));	
					//list.add("+1");
					
					
				}          
			});
			
		}
 }