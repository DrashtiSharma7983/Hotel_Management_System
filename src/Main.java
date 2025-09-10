import java.sql.*;
import java.util.Scanner;

public class Main {
    private static final String url = "jdbc:mysql://localhost:3306/hotel_db";
    private static final String username = "root";
    private static final String password = "adminmysql1234";

    public static void main(String[] args) throws ClassNotFoundException, SQLException , InterruptedException{

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            System.out.println("Driver loaded!!");
        } catch (ClassNotFoundException e) {
            System.out.println(e.getMessage());
        }

        try {
            Connection connection = DriverManager.getConnection(url, username, password);
//            Statement statement = connection.createStatement();
            while (true) {
                System.out.println("!!!Welcome to Hotel Room Reservation System!!!");
                Scanner scanner = new Scanner(System.in);
                System.out.println("1. New Registration");
                System.out.println("2. View Reservations");
                System.out.println("3. Get Room No.");
                System.out.println("4. Update Reservation");
                System.out.println("5. Delete Reservation");
                System.out.println("0. Exit");
                System.out.println("Choose an option");

                int choice = scanner.nextInt();
                switch (choice) {
                    case 1:
                        newRoom(connection, scanner);
                        break;
                    case 2:
                        viewReservations(connection);
                        break;
                    case 3:
                        getRoomNumber(connection,scanner);
                        break;
                    case 4:
                        updateReservation(connection,scanner);
                        break;
                    case 5:
                        deleteReservation(connection,scanner);
                        break;
                    case 0:
                        exit();
                        scanner.close();
                        return;
                    default:
                        System.out.println("Please choose valid option!! Try again !!");
                }
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }


    }

    private static void newRoom(Connection connection, Scanner scanner) {
        try {

            System.out.println("Enter guest name");
            String guestName = scanner.next();
            scanner.nextLine();
            System.out.println("Enter room number");
            int roomNumber = scanner.nextInt();
            System.out.println("Enter contact number");
            String contactNumber = scanner.next();


            String query = "INSERT INTO reservations (guest_name, room_number, contact_number) VALUES(?, ?, ?)";

            try (PreparedStatement preparedStatement = connection.prepareStatement(query)){
                preparedStatement.setString(1,guestName);
                preparedStatement.setInt(2,roomNumber);
                preparedStatement.setString(3,contactNumber);

                int rowsAffected = preparedStatement.executeUpdate();
                if (rowsAffected > 0) {
                    System.out.println("New registration Successfull!!");
                } else
                    System.out.println("Registration failed!!");
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void viewReservations(Connection connection) {

        String query = "SELECT reservation_id, guest_name, room_number, contact_number, reservation_date FROM reservations";
        try(PreparedStatement pst = connection.prepareStatement(query)){
            ResultSet resultSet = pst.executeQuery();

            System.out.println("Current Reservations:");
            System.out.println("+----------------+-----------------+---------------+----------------------+-------------------------+");
            System.out.println("| Reservation ID | Guest           | Room Number   | Contact Number      | Reservation Date        |");
            System.out.println("+----------------+-----------------+---------------+----------------------+-------------------------+");

            while(resultSet.next()){
                int id = resultSet.getInt("reservation_id");
                String name = resultSet.getString("guest_name");
                int roomNo = resultSet.getInt("room_number");
                String contactNo = resultSet.getString("contact_number");
                String reservationDate = resultSet.getTimestamp("reservation_date").toString();
//                System.out.println(id);
//                System.out.println(name);
//                System.out.println(roomNo);
//                System.out.println(contactNo);
                // Format and display the reservation data in a table-like format
                System.out.printf("| %-14d | %-15s | %-13d | %-20s | %-19s   |\n",
                        id, name, roomNo, contactNo, reservationDate);
            }

        }catch(Exception e){

        }

    }

    private static void getRoomNumber(Connection connection, Scanner scanner){
        System.out.println("Enter reservation ID:");
        int reservationId = scanner.nextInt();
        System.out.println("Enter guest name:");
        String name = scanner.next();
        scanner.nextLine();
//        String query = "select room_number from reservations where reservation_id=reservationId and guest_name = name";
        String query = "SELECT room_number FROM reservations WHERE reservation_id = ? AND guest_name = ?";
        try(PreparedStatement pst = connection.prepareStatement(query)){
            pst.setInt(1,reservationId);
            pst.setString(2,name);

            ResultSet rslt = pst.executeQuery();

            if(rslt.next()){
                int roomNum = rslt.getInt("room_number");
                System.out.println("Room no is : "+ roomNum);
            }
        }catch(SQLException e){
            e.printStackTrace();
        }

    }

    private static void updateReservation(Connection connection, Scanner scanner){

        System.out.println("Enter reservation id...r");
        int id = scanner.nextInt();
        System.out.println("Enter new guest name");
        String newGuestName = scanner.next();
        scanner.nextLine();
        System.out.println("Enter new room number");
        int newRoomNumber = scanner.nextInt();
        System.out.println("Enter new contact number");
        String newContactNumber = scanner.next();


        String query = "UPDATE reservations SET guest_name = ?, room_number = ?, contact_number = ? WHERE reservation_id = ?";

        try(PreparedStatement pst = connection.prepareStatement(query)){

            pst.setString(1,newGuestName);
            pst.setInt(2,newRoomNumber);
            pst.setString(3,newContactNumber);
            pst.setInt(4,id);
            int rowAffected = pst.executeUpdate();
            if(rowAffected>0){
                System.out.println("Record Updated!");
            }
            else
                System.out.println("Something went wrong!!");

        }catch(SQLException e){
            e.printStackTrace();
        }

    }

    private static void deleteReservation(Connection connection, Scanner scanner){
        System.out.println("Enter reservation Id which you want to delete!!");
        int id = scanner.nextInt();
        String query = "delete FROM reservations WHERE reservation_id = ? ";

        try(PreparedStatement pst = connection.prepareStatement(query)){
            pst.setInt(1,id);
            int rslt = pst.executeUpdate();

            if(rslt>0){
                System.out.println("Record Deleted!!");
            }
            else
                System.out.println("Record not found");
        }catch(SQLException e){
            System.out.println(e.getMessage());
        }



    }

    public static void exit() throws InterruptedException{
        System.out.print("Exiting System! ");
        int i = 5;
        while(i!=0){
            System.out.print(".");
            Thread.sleep(1000);
            i--;
        }
        System.out.println();
        System.out.println("Thank you for using Hotel Management System!!");
    }

}
