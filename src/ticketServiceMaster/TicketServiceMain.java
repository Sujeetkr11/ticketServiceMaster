package ticketServiceMaster;

import java.util.ArrayList;
import java.util.Date;
import java.util.Scanner;
import java.util.Timer;
import java.io.*; 

public class TicketServiceMain implements TicketService {
	
	static ArrayList <String> seatsAvailable = new ArrayList<String>();
	static ArrayList <String> seatsForOnHold = new ArrayList<String>();
    static Timer timer = new Timer();
    static int seconds = 0;
    static TicketServiceMain tsm = new TicketServiceMain();
    static SeatHold sh = new SeatHold();
	
	public static void main(String args[]) throws Exception

	{
	
	int numSeatsAvailable;
	int numSeats;
	
	Scanner in = new Scanner(System.in);
	
	seatsAvailable.clear();
	seatsForOnHold.clear();
	numSeatsAvailable = tsm.numSeatsAvailable();
	System.out.println();
	System.out.println ("Total Seats Available is "  + numSeatsAvailable);	
	System.out.println();
	System.out.println("Please enter your email id");
	String email = in.nextLine(); 
	int errorCnt = 0;
	 
	System.out.println("Please enter the number of seats to reserve");
	
	try {
		numSeats = in.nextInt();
		if ((numSeats <= numSeatsAvailable) && (numSeats > 0)) {		
			sh = tsm.findAndHoldSeats(numSeats, email);
			System.out.println("Your on hold seats are " + sh.SeatOnHold.get(email));
			System.out.println("Please reserve the seats in 10 seconds");
			myTimeOut(sh,email);
			System.exit(0);

		} else {
			System.out.println("Please enter the required seats less than available seats " + numSeatsAvailable);
			errorCnt = errorCnt + 1;
			if (errorCnt < 2) {
				tsm.main(null);
			} else {
				System.out.println("Maximum number of attempts exhausted. Try next time!");
				System.exit(0);
			}
		}
	}
	catch(Exception e) {
		System.out.println("Please enter the valid number");
		errorCnt = errorCnt + 1;
		if (errorCnt < 2) {
			tsm.main(null);
		} else {
			System.out.println("Maximum number of attempts exhausted. Try next time!");
			System.exit(0);
		}
		
	}
		
 }
	
	

	private static void myTimeOut(SeatHold sh, String email) throws Exception {
		// TODO Auto-generated method stub
		 Scanner in = new Scanner(System.in);
		 
		 int ttlSeats = sh.SeatOnHold.get(email).size();
		 long beforeResponse = new Date().getTime();
		 System.out.println("Please enter Yes to reserve it...");
		 String userResponse = in.nextLine(); 
		 long afterResponse  = new Date().getTime();
		 
		 long delayReponse = afterResponse - beforeResponse;
		 
		 if ((delayReponse <= 10000) && (userResponse.equalsIgnoreCase("yes"))) {
			 //call the reserve
			 tsm.reserveSeats(ttlSeats,email);
		 } else {
			 System.out.println("Reservation Timedout!! Please try again");
			 System.exit(0);
		 }
	
	}


	@Override
	public int numSeatsAvailable() throws Exception {
		// TODO Auto-generated method stub
		// TODO Auto-generated method stub
		String driverpath=System.getProperty("user.dir");
		//read data from file
		File file = new File(driverpath+"//availableSeats.txt");
		String st; 
		int fileReadCount = 0; 
		BufferedReader br;
		
		br = new BufferedReader(new FileReader(file));
		
		try {
		    while ((st = br.readLine()) != null) {
			    System.out.print(st + " "); 
			    seatsAvailable.add(st);
			    fileReadCount = fileReadCount + 1; 
	    }
		    
		} catch (FileNotFoundException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}   
	    br.close();
		
	    return fileReadCount;
	}

	@Override
	public SeatHold findAndHoldSeats(int numSeats, String customerEmail) {
		//TODO Auto-generated method stub
		SeatHold sh = new SeatHold();
		for (int i = 0; i < numSeats; i++) {
			seatsForOnHold.add(seatsAvailable.get(i));
		}
		sh.SeatOnHold.put(customerEmail, seatsForOnHold);
		return sh;
		
	}

	@Override
	public String reserveSeats(int seatHoldId, String customerEmail) throws Exception {
		// TODO Auto-generated method stub
		String confirmation_Nbr; 
		String driverpath=System.getProperty("user.dir");
		
		File availablefile = new File(driverpath+"//availableSeats.txt");
		File reservedfile = new File(driverpath+"//reservedSeats.txt");
		
		confirmation_Nbr = customerEmail + new Date().getTime();
		
		BufferedWriter writer = new BufferedWriter(new FileWriter(reservedfile,true));
		writer.append(confirmation_Nbr + " " + sh.SeatOnHold.get(customerEmail).toString());
		writer.newLine();
		writer.close();
		
		int maxCount = sh.SeatOnHold.get(customerEmail).size();
		
		for (int i=0; i< maxCount ; i++) {
			seatsAvailable.remove(sh.SeatOnHold.get(customerEmail).get(i));
		}
		
		BufferedWriter writer1 = new BufferedWriter(new FileWriter(availablefile));

		for (int j = 0; j < seatsAvailable.size(); j++) {
			writer1.write(seatsAvailable.get(j));
			writer1.newLine();
		}
		writer1.close();
		
		
		System.out.println("Your seats are booked and confirmation-nbr is " + confirmation_Nbr);
		
		
		return null;
	}
		
}
