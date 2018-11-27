package serverThread;
import java.io.*;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.ArrayList;
import java.util.Scanner;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.SourceDataLine;


public class userApplication {

	private Scanner in;
	private Scanner in_echo;
	private Scanner out_echo;
	private Scanner servPort;
	private Scanner clPort;

	public static void main(String[] args) throws IOException {
		(new userApplication()).options();
	}
	
	//Creation of menu for choosing the desirable option
	public void options(){
		for(;;){
			int choice;
			in = new Scanner(System.in);
			System.out.print("1.Echo Request Code \r\n");
			System.out.print("2.Image Request Code\r\n");
			System.out.print("3.Sound Request Code(DPCM)\r\n");
			System.out.print("4.Sound Request Code(AQDPCM)\r\n");
			System.out.print("5.Ithakicopter TCP\r\n");
			System.out.print("6.Exit\r\n");
			System.out.print("Choose an application: ");
			try{
				choice = in.nextInt();
				if(choice == 1){
					echo();
				}else if(choice == 2){
					image();
				}else if(choice == 3){
					soundDPCM();
				}else if(choice == 4){
					soundAQDPCM();
				}else if(choice == 5){
					ithakicopter();	
				}else if(choice == 6){
					return;
				}else{
					System.out.println("Wrong Number\r\n");
				}
			}catch(Exception x){
				System.out.println("Wrong Number\r\n");
			}
	}
	}//end of Menu

	public void echo() throws IOException{
		int cmd_package,serverPort,clientPort;
		int countPackets=0,counter=0;
		int option_echo;
		
		float counterInt=0;
		long timeStart=0,timeEnd=0,timeDelta=0;
		long startInt=0,endInt=0,deltaInt=0,sumInt=0;
		
		String echoMessage="";
		String packetInfo="";
		
		ArrayList<Long> samples = new ArrayList<Long>();
		ArrayList<Float> counters = new ArrayList<Float>();
		ArrayList<Long> sums = new ArrayList<Long>();
		
		//Echo Request for packages
		System.out.println("Enter Echo Reauest Code EXXXX");
		in_echo = new Scanner(System.in);
		cmd_package = in_echo.nextInt();
		out_echo = new Scanner(System.in);
		System.out.println("Choose mode: \r\n");
		System.out.println("1.Echo with delay");
		System.out.println("2.Echo without delay");
		option_echo = out_echo.nextInt();
		//Options for different echos
		if(option_echo == 1){
			packetInfo = "E" + Integer.toString(cmd_package) +"\r";
		}else if(option_echo == 2){
			packetInfo = "E0000\r";
		}
		
		byte[] txbuffer = packetInfo.getBytes();
		DatagramSocket socketClientSend = new DatagramSocket();
		//SERVER PORT
		System.out.println("Enter server listening port :");
		servPort = new Scanner(System.in);
		serverPort = servPort.nextInt();
		byte[] hostIP = {(byte)155,(byte)207,(byte)18,(byte)208};
		InetAddress hostAddress = InetAddress.getByAddress(hostIP);
		DatagramPacket packetClientToServer = new DatagramPacket(txbuffer,txbuffer.length,hostAddress,serverPort);
		//CLIENT PORT
		System.out.println("Enter Client listening port ");
		clPort = new Scanner(System.in);
		clientPort = clPort.nextInt();
		DatagramSocket socketClientReceive = new DatagramSocket(clientPort);
		byte[] rxbuffer = new byte[2048];
		DatagramPacket packetServerToClient = new DatagramPacket(rxbuffer,rxbuffer.length);

		timeStart = System.currentTimeMillis();
		while((timeDelta/1000)<6*60){
			socketClientSend.send(packetClientToServer);
			startInt = System.currentTimeMillis();
			endInt = 0;
			deltaInt = 0;
			socketClientReceive.setSoTimeout(3200);
			for (;;){
				try{
					socketClientReceive.receive(packetServerToClient);
					endInt = System.currentTimeMillis();
					deltaInt = endInt - startInt;
					echoMessage = new String(rxbuffer,0,packetServerToClient.getLength());
					System.out.println(new String(rxbuffer));
					System.out.println(new String(rxbuffer));
					break;
				}catch (Exception x){
					System.out.println(x);
				}
			}
			
			System.out.print(deltaInt + "\r\n");
			countPackets++;
			samples.add(deltaInt);
			counter++;
			timeEnd = System.currentTimeMillis();
			timeDelta = timeEnd - timeStart;
		}
		
		for (int i=0;i<samples.size();i++){
			int j=i;
			while ((sumInt/1000<8) && (j<samples.size())){
				sumInt += samples.get(j);
				counterInt++;
				j++;
			}
			counterInt = counterInt/8;
			counters.add(counterInt);
			sums.add(sumInt);
			counterInt = 0;
			sumInt = 0;
		}
		
		System.out.print("\r\n");
		System.out.print("Loop Time: "+ timeDelta);
		System.out.print("\r\n");
		
		for(int i = 0 ; i < countPackets ; i++){
			System.out.print("Sample " + i + " "+ samples.get(i) +"\r\n");
		}
		
		//Files creation
		BufferedWriter bw = null;
		File out_times = new File("/Users/Thanasis/Desktop/diktya erg/times.csv");
		FileWriter fw = new FileWriter(out_times);
		bw = new BufferedWriter(fw);
		for(int i = 0 ; i < countPackets ; i++){
			bw.write("" + samples.get(i));
			bw.newLine();
		}
		try{
			if(bw != null) bw.close();
		}catch(Exception y){
			System.out.println(y);
		}
		
		BufferedWriter sw = null;
		File out_sums = new File("/Users/Thanasis/Desktop/diktya erg/sums.txt");
		FileWriter kw = new FileWriter(out_sums);
		sw = new BufferedWriter(kw);
		for(int i = 0 ; i < countPackets ; i++){
			sw.write("" + samples.get(i));
			sw.newLine();
		}
		try{
			if(sw != null) sw.close();
		}catch(Exception y){
			System.out.println(y);
		}
		
	}//end of function Echo()
	
	public void image() throws IOException{
		int cmd_img,option,serverPort,clientPort;
		String packetInfo="";
		String fileName = "/Users/Thanasis/Desktop/diktya erg/image.jpg";
		
		System.out.println("Enter Image Request Code MXXXX: ");
		Scanner in = new Scanner(System.in);
		cmd_img = in.nextInt();
		Scanner ch = new Scanner(System.in);
		System.out.println("Choose mode: \r\n");
		System.out.println("1.Image CAM1");
		System.out.println("2.Image CAM2");
		option = ch.nextInt();
		if(option == 1){
			packetInfo = "M" + Integer.toString(cmd_img) +"\r";
		}else if(option == 2){
			packetInfo = "M" + Integer.toString(cmd_img) + " " + "CAM=PTZ" + "\r";
		}
		
		byte[] txbuffer = packetInfo.getBytes();
		DatagramSocket socketClientSendImg = new DatagramSocket();
		System.out.println("Enter server listening port :");
		Scanner servPortImg = new Scanner(System.in);
		serverPort = servPortImg.nextInt();
		byte[] hostIPImg = {(byte)155,(byte)207,(byte)18,(byte)208};
		InetAddress hostAddressImg = InetAddress.getByAddress(hostIPImg);
		
		DatagramPacket packetClientToServerImg = new DatagramPacket(txbuffer,txbuffer.length,hostAddressImg,serverPort);
		System.out.println("Enter Client listening port ");
		Scanner clPortImg = new Scanner(System.in);
		clientPort = clPortImg.nextInt();
		DatagramSocket socketClientReceiveImg = new DatagramSocket(clientPort);
		
		byte[] rx_bytes_img = new byte[2048];
		DatagramPacket packetServerToClientImg = new DatagramPacket(rx_bytes_img,rx_bytes_img.length);
		socketClientSendImg.send(packetClientToServerImg);
		socketClientReceiveImg.setSoTimeout(3200);
		FileOutputStream outputStream = new FileOutputStream(fileName);
		for(;;){
			try{
				socketClientReceiveImg.receive(packetClientToServerImg);
				if (rx_bytes_img == null){
					break;
				}
				for(int i = 0 ; i <= 127 ; i++){
					outputStream.write(rx_bytes_img[i]);
				}
			}catch (IOException ex) {
				System.out.println("error writing file");
				break;
			}
		}
		outputStream.close();
	}//end of function Image()

	
	public void soundDPCM() throws IOException, LineUnavailableException{
		int cmd_sound,option,serverPort,clientPort,rx;
		int numPackets=999,beta=4,mask1=15,mask2=240;
		int nibble1,nibble2,sub1,sub2,sample1 = 0,sample2 = 0,counter = 0;
		String packetInfo="";
		ArrayList<Integer> subs = new ArrayList<Integer>();
		ArrayList<Integer> samples = new ArrayList<Integer>();
		
		System.out.println("Enter sound request code :");
		Scanner in = new Scanner(System.in);
		cmd_sound = in.nextInt();
		Scanner ch = new Scanner(System.in);
		System.out.println("Choose mode: \r\n");
		System.out.println("1.Song DPCM");
		System.out.println("2.Frequency DPCM");
		option = ch.nextInt();
		if(option == 1){
			packetInfo = "V" + Integer.toString(cmd_sound) + "F999";
		}else if(option == 2){
			packetInfo = "V" + Integer.toString(cmd_sound) + "T999";
		}
		
		byte [] txbuffer = packetInfo.getBytes();
		DatagramSocket socketClientSend = new DatagramSocket();
		System.out.println("Enter Server Listening Port: ");
		Scanner sPort = new Scanner(System.in);
		serverPort = sPort.nextInt();
		byte[] hostIP = { (byte)155,(byte)207,(byte)18,(byte)208 };
		InetAddress hostAddress = InetAddress.getByAddress(hostIP);
		
		DatagramPacket packetClientToServer = new DatagramPacket(txbuffer,txbuffer.length,hostAddress,serverPort);
		System.out.println("Enter Client Listening Port: ");
		Scanner cPort = new Scanner(System.in);
		clientPort = cPort.nextInt();
		DatagramSocket socketClientReceive = new DatagramSocket(clientPort);
		byte[] rxbuffer = new byte[128];
		DatagramPacket packetServetToClient = new DatagramPacket(rxbuffer,rxbuffer.length);
		socketClientSend.send(packetClientToServer);
		byte[] song = new byte[256*numPackets];
		
		for(int i = 1;i < numPackets;i++){
			try{
				socketClientReceive.receive(packetServetToClient);
				for (int j = 0;j <= 127;j++){
					rx = rxbuffer[j];
					nibble1 = rx & mask1;
					nibble2 = ((rx & mask2)>>4);
					sub1 = (nibble1-8);
					subs.add(sub1);
					sub1 = sub1*beta;
					sub2 = (nibble2-8);
					subs.add(sub2);
					sub2 = sub2*beta;
					sample1 = sample2 + sub1;
					samples.add(sample1);
					sample2 = sample1 + sub2;
					samples.add(sample2);
					song[counter] = (byte)sample1;
					song[counter + 1] = (byte)sample2;
					counter += 2;
				}
			}catch (Exception x){
				System.out.println("Error");
			}
			System.out.println(i);
		}
		
		System.out.println("Playing is starting.....");
		AudioFormat pcm = new AudioFormat(8000,8,1,true,false);
		SourceDataLine playsong = AudioSystem.getSourceDataLine(pcm);
		playsong.open(pcm,32000);
		playsong.start();
		playsong.write(song,0,256*numPackets);
		playsong.stop();
		playsong.close();
		
		BufferedWriter bw = null;
		try{
		File file = new File("/Users/Thanasis/Desktop/diktya erg/DPCMsubF.txt");
		if(!file.exists()){
			file.createNewFile();
		}
		FileWriter fw = new FileWriter(file);
		bw = new BufferedWriter(fw);
		for(int i = 0 ; i < subs.size() ; i += 2){
			bw.write("" + subs.get(i) + " " + subs.get(i+1));	
			bw.newLine();
		}
		}catch(Exception x){
			System.out.println(x);
		}
		
		try{
			if(bw != null){
				bw.close();
			}
		}catch(Exception x){
			System.out.println(x);
		}
		
		BufferedWriter mw = null;
		try{
			File file = new File("/Users/Thanasis/Desktop/diktya erg/DPCMsamplesF.txt");
			if(!file.exists()){
			file.createNewFile();
		}
		FileWriter fw = new FileWriter(file);
		mw = new BufferedWriter(fw);
		for(int i = 0 ; i < samples.size() ; i += 2){
			mw.write("" + samples.get(i) + " " + samples.get(i+1));
			mw.newLine();
		}
		}catch(Exception x){
			System.out.println(x);
		}finally{
		try{
			if(mw != null){
				mw.close();
			}
		}catch(Exception x){
			System.out.println(x);
		}
		}
		
	}//end of function soundDPCM()
	
	
	public void soundAQDPCM() throws IOException, LineUnavailableException{
		int soundRequestCode,option,serverPort,clientPort,numPackets = 999,rx;
		String packetInfo = "";
		int nibble1,nibble2,sub1,sub2,sample1 = 0,sample2 = 0,counter = 4,mean,beta,hint = 0;
		
		ArrayList<Integer> means = new ArrayList<Integer>();
		ArrayList<Integer> betas = new ArrayList<Integer>();
		ArrayList<Integer> subs = new ArrayList<Integer>();
		ArrayList<Integer> samples = new ArrayList<Integer>();
		
		System.out.println("Enter Sound Request Code VXXXX: ");
		Scanner in = new Scanner(System.in);
		soundRequestCode = in.nextInt();
		Scanner ch = new Scanner(System.in);
		System.out.println("Choose mode: \r\n");
		System.out.println("1.Song AQDPCM");
		System.out.println("2.Frequency AQDPCM");
		option = ch.nextInt();
		if(option == 1){
			packetInfo = "V" + Integer.toString(soundRequestCode) + "AQF999";
		}else if(option == 2){
			packetInfo = "V" + Integer.toString(soundRequestCode) + "AQT999";
		}
		
		byte[] txbuffer = packetInfo.getBytes();
		DatagramSocket socketClientSend = new DatagramSocket();
		System.out.println("Enter Server Listening Port: ");
		Scanner sPort = new Scanner(System.in);
		serverPort = sPort.nextInt();
		byte[] hostIP = { (byte)155,(byte)207,(byte)18,(byte)208 };
		InetAddress hostAddress = InetAddress.getByAddress(hostIP);
		
		DatagramPacket packetClientToServer = new DatagramPacket(txbuffer,txbuffer.length,hostAddress,serverPort);
		System.out.println("Enter Client Listening Port: ");
		Scanner cPort = new Scanner(System.in);
		clientPort = cPort.nextInt();
		DatagramSocket socketClientReceive = new DatagramSocket(clientPort);
		byte[] rxbuffer = new byte[132];
		DatagramPacket packetServetToClient = new DatagramPacket(rxbuffer,rxbuffer.length);
		socketClientSend.send(packetClientToServer);
		
		byte[] meanB = new byte[4];
		byte[] betta = new byte[4];
		byte sign;
		byte[] song = new byte[256*2*numPackets];
		
		for(int i = 1;i < numPackets;i++){
			System.out.println(i);
			try{
				socketClientReceive.receive(packetServetToClient);
				sign = (byte)( ( rxbuffer[1] & 0x80) !=0 ? 0xff : 0x00); //converting byte[2] to integer
				meanB[3] = sign;
				meanB[2] = sign;
				meanB[1] = rxbuffer[1];
				meanB[0] = rxbuffer[0];
				mean = ByteBuffer.wrap(meanB).order(ByteOrder.LITTLE_ENDIAN).getInt();
				means.add(mean);
				sign = (byte)( ( rxbuffer[3] & 0x80) !=0 ? 0xff : 0x00);
				betta[3] = sign;
				betta[2] = sign;
				betta[1] = rxbuffer[3];
				betta[0] = rxbuffer[2];
				beta = ByteBuffer.wrap(betta).order(ByteOrder.LITTLE_ENDIAN).getInt();
				betas.add(beta);
				for (int j = 4;j <= 131;j++){
					rx = rxbuffer[j];
					nibble1 = (int)(rx & 0x0000000F);
					nibble2 = (int)((rxbuffer[j] & 0x000000F0)>>4);
					sub1 = (nibble2-8);
					subs.add(sub1);
					sub2 = (nibble1-8);
					subs.add(sub2);
					sub1 = sub1*beta;
					sub2 = sub2*beta;
					sample1 = hint + sub1 + mean;
					samples.add(sample1);
					sample2 = sub1 + sub2 + mean;
					hint = sub2;
					samples.add(sample2);
					counter += 4;
					song[counter] = (byte)(sample1 & 0x000000FF);
					song[counter + 1] = (byte)((sample1 & 0x0000FF00)>>8);
					song[counter + 2] = (byte)(sample2 & 0x000000FF);
					song[counter + 3] = (byte)((sample2 & 0x0000FF00)>>8);
				}
			}catch(Exception x){
				System.out.println(x);
			}
		}
		
		System.out.println("Playing is starting.....");
		AudioFormat aqpcm = new AudioFormat(8000,16,1,true,false);
		SourceDataLine playsong = AudioSystem.getSourceDataLine(aqpcm);
		playsong.open(aqpcm,32000);
		playsong.start();
		playsong.write(song,0,256*2*numPackets);
		playsong.stop();
		playsong.close();
		
		BufferedWriter bw = null;
		try{
			File file = new File("/Users/Thanasis/Desktop/diktya erg/AQDPCMsubsF2.txt");
			if(!file.exists()){
				file.createNewFile();
			}
			FileWriter fw = new FileWriter(file);
			bw = new BufferedWriter(fw);
			for(int i = 0 ; i < subs.size() ; i += 2){
					bw.write("" + subs.get(i) + " " + subs.get(i+1));
					bw.newLine();
			}
		}catch(IOException ioe){
			ioe.printStackTrace();
		}finally{
		try{
			if(bw != null) bw.close();
		}catch(Exception ex){
			System.out.println(ex);
		}
		}

		BufferedWriter mw = null;
		try{
			File file = new File("/Users/Thanasis/Desktop/diktya erg/AQDPCMsamplesF2.txt");
			if(!file.exists()){
					file.createNewFile();
			}
			FileWriter fw = new FileWriter(file);
			mw = new BufferedWriter(fw);
			for(int i = 0 ; i < samples.size() ; i += 2){
					mw.write("" + samples.get(i) + " " + samples.get(i+1));
					mw.newLine();
			}
		}catch(IOException ioe){
			ioe.printStackTrace();
		}finally{
		try{
			if(mw != null) mw.close();
		}catch(Exception ex){
			System.out.println(ex);
		}
		}
		
		BufferedWriter pw = null;
		try{
			File file = new File("/Users/Thanasis/Desktop/diktya erg/AQDPCMmeanF2.txt");
			if(!file.exists()){
					file.createNewFile();
			}
			FileWriter fw = new FileWriter(file);
			pw = new BufferedWriter(fw);
			for(int i = 0 ; i < means.size() ; i += 2){
				pw.write("" + means.get(i));
				pw.newLine();
			}
		}catch(IOException ioe){
			ioe.printStackTrace();
		}finally{
		try{
			if(pw != null) pw.close();
		}catch(Exception ex){
			System.out.println(ex);
		}
		}
		
		BufferedWriter kw = null;
		try{
			File file = new File("/Users/Thanasis/Desktop/diktya erg/AQDPCMbetasF2.txt");
			if(!file.exists()){
				file.createNewFile();
			}
			FileWriter fw = new FileWriter(file);
			kw = new BufferedWriter(fw);
			for(int i = 0 ; i < betas.size() ; i ++){
				kw.write("" + betas.get(i));
				kw.newLine();
			}
		}catch(IOException ioe){
			ioe.printStackTrace();
		}finally{
		try{
			if(kw != null) kw.close();
		}catch(Exception ex){
			System.out.println(ex);
		}
		}	
	}//end of function SoundAQDPC()
	
	
	public void ithakicopter() throws IOException{
		int serverPort = 38048,clientPort = 48038;
		int copterRequestCode;
		String message = "",packetInfo = "";
		ArrayList<String> messages = new ArrayList<String>();
		
		byte[] hostIP = { (byte)155,(byte)207,(byte)18,(byte)208 };
		System.out.println("Enter Copter Request Code QXXXX: ");
		Scanner in = new Scanner(System.in);
		copterRequestCode = in.nextInt();
		packetInfo = "Q" + Integer.toString(copterRequestCode) +"\r";
		
		byte[] txbuffer = packetInfo.getBytes();
		DatagramSocket socketClientSend = new DatagramSocket();
		InetAddress hostAddress = InetAddress.getByAddress(hostIP);
		DatagramPacket packetClientToServer = new DatagramPacket(txbuffer,txbuffer.length,hostAddress,serverPort);
		DatagramSocket socketClientReceive = new DatagramSocket(clientPort);
		byte[] rxbuffer = new byte[5000];
		DatagramPacket packetServerToClient = new DatagramPacket(rxbuffer,rxbuffer.length);
		
		for (int i = 1;i <= 60 ; i++){
			socketClientSend.send(packetClientToServer);
			socketClientReceive.receive(packetServerToClient);
			message = new String(rxbuffer,0,packetServerToClient.getLength());
			messages.add(message);
			System.out.println(new String(rxbuffer));
		}
		
		BufferedWriter bw = null;
		try{
			File file = new File("Ithakicopter2.txt");
			if(!file.exists()){
				file.createNewFile();
			}
			FileWriter fw = new FileWriter(file);
			bw = new BufferedWriter(fw);
			for(int i = 0 ; i < messages.size(); i++){
				bw.write("" + messages.get(i));
				bw.newLine();
			}
		}catch(IOException ioe){
			ioe.printStackTrace();
		}finally{
		try{
			if(bw != null) bw.close();
		}catch(Exception ex){
			System.out.println(ex);
		}
		}
	}//end of function ithakicopter()
	
}//end class userApplication
