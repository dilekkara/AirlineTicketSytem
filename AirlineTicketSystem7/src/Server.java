import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {
    public static Flight flight = null;
    public static Ticket ticket = null;

    public static void main(String[] args) throws IOException, ClassNotFoundException {
        fillMyInfo();
        int serverPortNumber = 8095;
        ServerSocket serverSocket = new ServerSocket(serverPortNumber);
        Message receivedMsg = null;
        ObjectInputStream ois = null;
        ObjectOutputStream oos = null;
        System.out.println("Server is listening on port " + serverPortNumber);
        boolean listening = true;
        while (listening) {
            Socket connectionSocket = serverSocket.accept();
            ois = new ObjectInputStream(connectionSocket.getInputStream());
            receivedMsg = (Message) ois.readObject();
            ClientHandler thread = new ClientHandler(connectionSocket, receivedMsg, flight);
            thread.start();
        }
        serverSocket.close();
    }

    public static void fillMyInfo() {
        flight = new Flight();
        flight.setFlightID(1);
        flight.setFlightDate("10.07.2024");
        flight.setRoute("İstanbul-Samsun");
        Ticket[] ticketList = new Ticket[5];
        for (int i = 1; i < 6; i++) {
            ticket = new Ticket();
            ticket.setTicketID(i);
            if (i % 2 == 0) {
                ticket.setTicketNumber(i + ".numara-Cam Kenarı");
            } else {
                ticket.setTicketNumber(i + ".numara-Koridor");
            }
            ticket.setTicketState(false);
            ticket.setTicketHolder(0);
            ticketList[i - 1] = ticket;
        }
        flight.setTicketList(ticketList);
    }
}
