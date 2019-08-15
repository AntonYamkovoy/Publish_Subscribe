package cs.tcd.ie;

import java.net.DatagramPacket;

public interface PacketContent {
	public String toString();
	public DatagramPacket toDatagramPacket();
}
//17331565 Anton Yamkovoy