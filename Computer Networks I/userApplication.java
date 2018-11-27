package virtualModem;

import java.io.File;

import java.io.FileNotFoundException;

import java.io.FileOutputStream;

import java.io.IOException;

import java.nio.file.Files;

import java.nio.file.Paths;

import java.util.ArrayList;

import java.util.List;


import ithakimodem.Modem;

public class userApplication {

public static void main(String[] args) throws IOException { userApplication user = new userApplication(); user.newModem();

}

public void newModem() throws IOException{

//Ithaki connection

Modem modem;

modem = new Modem(8000);

modem.setSpeed(80000);

modem.setTimeout(2000);

modem.open("ithaki");

for (;;) {

try {

int k=modem.read();

if (k==-1) break;

System.out.print((char)k);

} catch (Exception x) { break;

}

}

//Echo Request for packages

String cmd_package = "E0118\r";

byte[] bytes_package = cmd_package.getBytes(); //PSTART DD-MM-YYYY HH-MM-SS PC PSTOP PC=Packet Counter



long begin = System.currentTimeMillis();

int i=0,k;

int size=500;

String echo_msg="";

String echo_times="";


//Files creation for package response times

File out_echo = new File("/Users/Thanasis/Desktop/diktua erg/echo.txt");

File out_times = new File("/Users/Thanasis/Desktop/diktua erg/times.csv");

FileOutputStream out_echo_str = new FileOutputStream(out_echo);

FileOutputStream out_times_str = new FileOutputStream(out_times);

while (i<size && (System.currentTimeMillis()-begin)<240000){ String message ="";
long startTime = System.currentTimeMillis();

modem.write(bytes_package);

for (;;){

try{

k = modem.read();

if (k == -1){

break;

}

message += (char)k;

}

catch (Exception x){

break;

}

if (message.indexOf("PSTOP")>-1){

long endTime = System.currentTimeMillis();

long response = endTime - startTime;

echo_msg += message + "\r";

echo_times += String.valueOf(response) + "\r";

}

}

i++;

}

//Files to be created

try{

out_echo_str.write(echo_msg.getBytes());

out_times_str.write(echo_times.getBytes());

out_echo_str.close();

out_times_str.close();

}catch (Exception x){

System.out.println(x);

}



//Image request code

String cmd_image = "M5061\r"; //DIR = Direction and CAM=FIX (default) - CAM=PTZ byte[] bytes_image = cmd_image.getBytes();
modem.write(bytes_image);

//	Image Request int i1,j;
File image = new File("/Users/Thanasis/Desktop/diktua erg/E1.jpg"); try{

FileOutputStream stream = new FileOutputStream(image); int flag=0;

i1=modem.read(); for (;;){
j=i1;

i1=modem.read(); //i1-1

if (i1 == -1){

break;

}

if (j==255 && i1==216){

flag=1;

}

if (flag==1){

try{

stream.write(j);

System.out.print((char)j);

}

catch(IOException e){

System.out.println("No written data");

}

}

if(j==255 && i1==217){

break;

}

}

}catch(FileNotFoundException e){

}

//Image request code with errors

String cmd_image_errors = "G4309\r"; //DIR = Direction and CAM=FIX (default) - CAM=PTZ byte[] bytes_image_errors = cmd_image_errors.getBytes(); modem.write(bytes_image_errors);

//	Image Request with errors int l,m;

File image_errors = new File("/Users/Thanasis/Desktop/diktua erg/E2.jpg"); try{

FileOutputStream stream = new FileOutputStream(image_errors); int flag=0;

l=modem.read(); for (;;){
m=l;

l=modem.read(); //l-1 if (l == -1){
break;

}

if (m==255 && l==216){

flag=1;

}

if (flag==1){

try{

stream.write(m);

System.out.print((char)m);

}

catch(IOException e){

System.out.println("No written data");

}

}

if(m==255 && l==217){

break;

}

}

}catch(FileNotFoundException e){

}

//GPS request code

String gps_code =

"P0220T=225734403738T=225730403730T=225760403750T=222730403130T=222739403135T=225731403731\r"; byte[] bytes_gps = gps_code.getBytes();
modem.write(bytes_gps);

//Creation of image GPS

List <Byte> GPSWithTList = new ArrayList <Byte>(); do{

k=modem.read();

GPSWithTList.add((byte)k);

}while( k != -1);

int size_list = GPSWithTList.size();

byte[] gpsIMG = new byte[size_list];

for (j=0; j<size_list;j++){

gpsIMG[j] = GPSWithTList.get(j);

}

try{

Files.write(Paths.get("/Users/Thanasis/Desktop/diktua erg/M1.jpg"), gpsIMG); }catch(Exception x){

System.out.println(x);

}


//ARQ request code

int packets = 5000;

//Files creation

File outputARQ = new File("/Users/Thanasis/Desktop/diktua erg/arq_msgs.txt");

File output_timeARQ = new File("/Users/Thanasis/Desktop/diktua erg/arq_times.csv"); File out_triesARQ = new File("/Users/Thanasis/Desktop/diktua erg/arq_packages.csv");

FileOutputStream outFileStreamARQ;

FileOutputStream outTimeStreamARQ;

FileOutputStream outTriesStreamARQ;

String ACK = "Q3473\r";

String NACK = "R1581\r";

String tx_msg = ACK;

String rx_msg = "";

String messages = "";

String times = "";

String tries = "";

long duration, t1, t2;

int req = 0;

i = 0;

begin = System.currentTimeMillis();

boolean correct;

//correct works as a flag

while (i<packets && (System.currentTimeMillis()-begin)<240000){ correct = false;

long startTime = System.currentTimeMillis();

req = 0;

t1 = System.currentTimeMillis();

//if is false, check again until true

while (!correct){

try{

modem.write(tx_msg.getBytes());
 
Αθανάσιος Παπάζογλου ΑΕΜ : 8324	5 of 6

 

}catch (Exception x){

System.out.println("Error passing the message");

}

req++;

rx_msg="";

duration = System.currentTimeMillis();

//if true write

while(true){

try{

if ((k=modem.read())==-1){

break;

}

rx_msg += (char)k;

System.out.print((char)k);

}catch(Exception x){

break;

}

if (rx_msg.indexOf("PSTOP")>-1){

//check if it is correct

correct = check(rx_msg);

if (correct){

long endTime = System.currentTimeMillis();

long response = duration - startTime;

tx_msg = ACK;

t2 = System.currentTimeMillis();

times += String.valueOf(t2-t1) + "\r";

messages += rx_msg + "\n";

tries += String.valueOf(req) + "\r";

}

else{

tx_msg = NACK;

}

break;

}

}

}

i++;

}

//Files creation

try{

outFileStreamARQ = new FileOutputStream(outputARQ); outTimeStreamARQ = new FileOutputStream(output_timeARQ); outTriesStreamARQ = new FileOutputStream(out_triesARQ);

outFileStreamARQ.write(messages.getBytes());

outTimeStreamARQ.write(times.getBytes());

outTriesStreamARQ.write(tries.getBytes());

outFileStreamARQ.close();

outTimeStreamARQ.close();

outTriesStreamARQ.close();

}catch(IOException e){

System.out.println(e);

}

modem.close();


}

public static boolean check(String packet){

int left = packet.indexOf("<");

int right = packet.indexOf(">");

int FCS = Integer.parseInt(packet.substring(right+2, right+5)); int temp = (int) packet.charAt(left + 1); int i;

for (i = left+2; i<right; i++){

temp = temp^(int) packet.charAt(i);

}

if (temp==FCS){

return true;

}

return false;

}

}
