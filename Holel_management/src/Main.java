import java.sql.DriverManager;
import java.util.Scanner;
import java.sql.Connection;
import java.sql.Statement;
import java.sql.ResultSet;
import java.sql.SQLException;
public class Main {
	
	private static void reserveRoom(Connection conn, Scanner sc){
		try {
			System.out.println("Enter guest name: ");
			String name = sc.next();
			sc.nextLine();
			
			System.out.println("Enter Room no.: ");
			int roomNumber = sc.nextInt();
			System.out.println("Enter Contact no.: ");
			String contactNumber = sc.next();
			
			String sql = "INSERT INTO hotel_db.reservation (guest_name, room_num, contact_num) " +
                    "VALUES ('" + name + "', " + roomNumber + ", '" + contactNumber + "')";
			try{
				Statement stmt = conn.createStatement();
			    int affectedRows = stmt.executeUpdate(sql);
			    if(affectedRows>0) {
				    System.out.println("Reservation Successful.");
			    }else {
				    System.out.println("Reservation failed.");
			    }
		    }
			finally {
				
			}
		}
		
		catch(SQLException e) {
			System.out.println(e.getMessage());
		}
	}
	
	private static void viewReservation(Connection conn) throws SQLException {
        String sql = "SELECT reservation_id, guest_name, room_num, contact_num, reservation_date FROM hotel_db.reservation";

        try (Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            System.out.println("Current Reservations:");
            System.out.println("+----------------+-----------------+---------------+----------------------+-------------------------+");
            System.out.println("| Reservation ID | Guest           | Room Number   | Contact Number      | Reservation Date        |");
            System.out.println("+----------------+-----------------+---------------+----------------------+-------------------------+");

            while (rs.next()) {
                int reservationId = rs.getInt("reservation_id");
                String guestName = rs.getString("guest_name");
                int roomNumber = rs.getInt("room_num");
                String contactNumber = rs.getString("contact_num");
                String reservationDate = rs.getTimestamp("reservation_date").toString();

                // Format and display the reservation data in a table-like format
                System.out.printf("| %-14d | %-15s | %-13d | %-20s | %-19s   |\n",
                        reservationId, guestName, roomNumber, contactNumber, reservationDate);
            }

            System.out.println("+----------------+-----------------+---------------+----------------------+-------------------------+");
        }
    }
	
	private static void getRoomNumber(Connection conn, Scanner sc) throws SQLException {
		System.out.println("Enter Reservation ID");
		int reservationId = sc.nextInt();
		System.out.println("Enter Guest name: ");
		String guestName = sc.next();
//		sc.nextLine();
		
		String sql = "SELECT room_num FROM hotel_db.reservation " +
                "WHERE reservation_id = " + reservationId +
                " AND guest_name = '" + guestName + "'";
		try {
			Statement stmt = conn.createStatement();
			ResultSet rs = stmt.executeQuery(sql);
			if(rs.next()) {
		    	int roomNumber = rs.getInt("room_num");
		    	System.out.println("Room number of the Reservation ID "+reservationId+" and guest is "+guestName+" is "+roomNumber);
		    }else {
		    	System.out.println("Reservation not found for the given ID"+reservationId+ "and guest name" +guestName);
		    }
		}
		catch(SQLException e) {
			e.printStackTrace();
		}
		
	}
	
	private static void updateReservations(Connection conn, Scanner sc) throws SQLException {
		System.out.println("Eneter reservation Id to update");
		int reservationId = sc.nextInt();
		
		sc.nextLine();
		
		if(!reservationExists(conn, reservationId)) {
			System.out.println("reservation not found for the Reservation ID: "+reservationId);
		}
		
		System.out.println("Enter new guest name: ");
		String newguestName = sc.nextLine();
		
		System.out.println("Enter new room number");
		int newroom = sc.nextInt();
		
		System.out.println("Enter new contact number: ");
		String contactnum = sc.next();
		
		String sql = "UPDATE hotel_db.reservation SET guest_name ='"+newguestName+"',"+"room_num = "+newroom+", contact_num ='"+contactnum+"'"+"WHERE reservation_id = "+reservationId;
		
		try {
			Statement stmt = conn.createStatement();
			int rowaffect = stmt.executeUpdate(sql);
			
			if(rowaffect>0) {
				System.out.println("Reservation updated successfully..!");
			}else {
				System.out.println("Reservation update failed.");
			}
		}
		catch(SQLException e) {
			e.printStackTrace();
		}
	}
	
	public static void exit() throws InterruptedException {
        System.out.print("Exiting System");
        int i = 5;
        while(i!=0){
            System.out.print(".");
            Thread.sleep(1000);
            i--;
        }
        System.out.println();
        System.out.println("ThankYou For Using Hotel Reservation System!!!");
    }
	
	public static boolean reservationExists(Connection conn, int reservation_id) {
        try {
            String sql = "SELECT reservation_id FROM hotel_db.reservation WHERE reservation_id = " + reservation_id;

            try (Statement statement = conn.createStatement();
                 ResultSet resultSet = statement.executeQuery(sql)) {

                return resultSet.next(); // If there's a result, the reservation exists
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return false; // Handle database errors as needed
        }
    }

	 private static void deleteReservation(Connection conn, Scanner sc) {
	        try {
	            System.out.print("Enter reservation ID to delete: ");
	            int reservation_id = sc.nextInt();

	            if (!reservationExists(conn, reservation_id)) {
	                System.out.println("Reservation not found for the given ID.");
	                return;
	            }

	            String sql = "DELETE FROM hotel_db.reservation WHERE reservation_id = " + reservation_id;

	            try (Statement statement = conn.createStatement()) {
	                int affectedRows = statement.executeUpdate(sql);

	                if (affectedRows > 0) {
	                    System.out.println("Reservation deleted successfully!");
	                } else {
	                    System.out.println("Reservation deletion failed.");
	                }
	            }
	        } 
	        catch (SQLException e) {
	            e.printStackTrace();
	        }
	    }
	 
	 
	 
	public static void main(String[] args) throws ClassNotFoundException {
		final String url = "jdbc:mysql://localhost:3306/?user=root";
		final String username = "root";
		final String password = "4092";
		
		
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
			System.out.println("Driver Loades Sussessfully");
		}
		catch(ClassNotFoundException e){
			System.out.println(e.getMessage());
			System.out.println("Driver not loaded");
			
		}
		try {
			Connection conn = DriverManager.getConnection(url, username, password);
			while(true) {
				Scanner sc = new Scanner(System.in);
				System.out.println();
                System.out.println("HOTEL MANAGEMENT SYSTEM");
                
                System.out.println("1. Reserve a room");
                System.out.println("2. View Reservations");
                System.out.println("3. Get Room Number");
                System.out.println("4. Update Reservations");
                System.out.println("5. Delete Reservations");
                System.out.println("0. Exit");
                System.out.print("Choose an option: ");
                
				int choice = sc.nextInt();
				switch(choice) {
				case 1:
					reserveRoom(conn,sc);
					break;
				case 2:
					viewReservation(conn);
					break;
				case 3:
					getRoomNumber(conn,sc);
					break;
				case 4:
					updateReservations(conn,sc);
					break;
				case 5:
					deleteReservation(conn,sc);
					break;
				case 0:
					exit();
					sc.close();
					return;
				default:
					System.out.println("Invalid choice. Please select from above optoions");
					
					
				}
			}
			
		}
		catch(SQLException e) {
			System.out.println(e.getMessage());
			System.out.println("Connection failed");
		}
		catch(InterruptedException e) {
			throw new RuntimeException(e);
		}
		
		

	}

}
