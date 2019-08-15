package cs.tcd.ie;
//17331565 Anton Yamkovoy
import java.net.DatagramPacket;
import java.sql.Timestamp;

public class Message {
	public Timestamp timeSentAt;
	public DatagramPacket packet;
	public int recieverAddress;
	public boolean gotAck;
	public boolean readyToResend;
	public int sequenceNumber;
	public boolean gotNack;
	
	public Message(Timestamp timeSentAt, DatagramPacket packet, int recieverAddress, int sequenceNumber) {
		
		
		this.timeSentAt = timeSentAt;
		this.packet = packet;
		this.recieverAddress = recieverAddress;
		this.gotAck = false;
		this.gotNack = false;
		this.readyToResend = false;
		this.sequenceNumber = sequenceNumber;
	}
	
	
	
	
}
