import java.util.*;
import java.net.*;
class PingClient{
   public static void main(String[] args) throws Exception
   {
      // Get command line argument.
      if (args.length != 2) {
         System.out.println("Required arguments: host, port");
         return;
      }
	  //extract arguments from command line
	  String host  = args[0];
      int port = Integer.parseInt(args[1]);
      InetAddress serverhost = InetAddress.getByName(host);
	  long time,time1,rtt,min,max,total;
	  int packetsdropped=0;
	  double average;
      // Create a datagram socket for receiving and sending UDP packets
      // through the port specified on the command line.
      DatagramSocket socketsend = new DatagramSocket(port);
	  //set the timeout to 1 second
	  socketsend.setSoTimeout(1000);
	  String message;
	  //intialsie min and max ping
	  min = Long.MAX_VALUE;
	  max = Long.MIN_VALUE;
	  total =0;

      // Processing loop.
      for(int i =0;i<10;i++){
		 //get current time
		 time = new Date().getTime();
		 //create message
		 message = "PING " + i + " " + time +" "+ "\r\n";
		 System.out.println("sent: " + message);
		 // Send ping.
         DatagramPacket ping = new DatagramPacket(message.getBytes(), message.length(),serverhost , port);
         socketsend.send(ping);
         // Create a datagram packet to hold incomming UDP packet.
         DatagramPacket response = new DatagramPacket(new byte[1024], 1024);

         // Block until the you receive a UDP packet.
		 try{
			socketsend.receive(response);
		 }catch(Exception e){
			System.out.println("packet dropped\n");
			//if no packet was recieved increment dropped
			packetsdropped++;
			continue;
		 }
		 //a packet was recieved calculate it ping
		 time1 = new Date().getTime();
		 rtt = time1-time;
		 //update max and min
		 if(min > rtt){
			 min = rtt;
		 }
		 if(max<rtt){
			 max = rtt;
		 }
		 total += rtt;
		 
		 System.out.println("packet " + i + " ping = " + rtt + " ms\n");
      }
	  //calculate average
	  if(packetsdropped!=10){
		average = total/(10-packetsdropped); 
		System.out.println("average ping "+ average +" ms");
	  }
	  System.out.println(packetsdropped +" packets were dropped");
	  System.out.println("max ping "+ max +" ms");
	  System.out.println("min ping "+ min +" ms");
	  socketsend.close();
   }

}