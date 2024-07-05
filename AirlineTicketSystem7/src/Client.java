import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

public class Client implements Runnable {
    ReentrantLock lock;
    Condition condition;
    Socket clientSocket = null;
    int clientID;
    int portNumber;
    int serverPortNumber;
    int processOrder;
    int requestedTicket;
    ObjectOutputStream oos;
    ObjectInputStream ois;
    Message receivedMsg = null;

    public Client(ReentrantLock lock, Condition condition, int clientID, int portNumber, int serverPortNumber, int requestedTicket, int processOrder) {
        this.lock = lock;
        this.condition = condition;
        this.clientID = clientID;
        this.portNumber = portNumber;
        this.serverPortNumber = serverPortNumber;
        this.processOrder = processOrder;
        this.requestedTicket = requestedTicket;
    }

    public String makeReservation(int ticketID, String startLocation, String endLocation, String travelDate) {
        return WriterThread(ticketID, startLocation, endLocation, travelDate);
    }

    public String cancelReservation(int ticketID) {
        return CancelThread(ticketID);
    }

    public String queryReservation(int ticketID) {
        return ReaderThread(ticketID);
    }

    private String WriterThread(int ticketID, String startLocation, String endLocation, String travelDate) {
        lock.lock();
        try {
            System.out.println("Writer" + clientID + " is requesting ticket ID " + ticketID);
            clientSocket = new Socket("localhost", serverPortNumber);
            Message requestedMsg = new Message();
            requestedMsg.setSenderID(clientID);
            requestedMsg.setSenderPortNumber(portNumber);
            requestedMsg.setContent(ticketID + "," + startLocation + "," + endLocation + "," + travelDate);
            requestedMsg.setType(Message.Type.SERVICE_MAKE_REZERVATION);
            oos = new ObjectOutputStream(clientSocket.getOutputStream());
            oos.writeObject(requestedMsg);
            ois = new ObjectInputStream(clientSocket.getInputStream());
            receivedMsg = (Message) ois.readObject();
            System.out.println("Writer" + clientID + " received a message from server: " + receivedMsg.getContent());
            System.out.println("--------------------------------------------------");
            return receivedMsg.getContent().toString();
        } catch (Exception e) {
            e.printStackTrace();
            return "Error: " + e.getMessage();
        } finally {
            lock.unlock();
        }
    }

    private String CancelThread(int ticketID) {
        lock.lock();
        try {
            System.out.println("CancelWriter" + clientID + " is requesting cancellation for ticket ID " + ticketID);
            clientSocket = new Socket("localhost", serverPortNumber);
            Message requestedMsg = new Message();
            requestedMsg.setSenderID(clientID);
            requestedMsg.setSenderPortNumber(portNumber);
            requestedMsg.setContent(String.valueOf(ticketID));
            requestedMsg.setType(Message.Type.SERVICE_CANCEL_REZERVATION);
            oos = new ObjectOutputStream(clientSocket.getOutputStream());
            oos.writeObject(requestedMsg);
            ois = new ObjectInputStream(clientSocket.getInputStream());
            receivedMsg = (Message) ois.readObject();
            System.out.println("CancelWriter" + clientID + " received a message from server: " + receivedMsg.getContent());
            System.out.println("--------------------------------------------------");
            return receivedMsg.getContent().toString();
        } catch (Exception e) {
            e.printStackTrace();
            return "Error: " + e.getMessage();
        } finally {
            lock.unlock();
        }
    }

    private String ReaderThread(int ticketID) {
        lock.lock();
        try {
            System.out.println("Reader" + clientID + " is querying ticket ID " + ticketID);
            clientSocket = new Socket("localhost", serverPortNumber);
            Message requestedMsg = new Message();
            requestedMsg.setSenderID(clientID);
            requestedMsg.setSenderPortNumber(portNumber);
            requestedMsg.setContent(String.valueOf(ticketID));
            requestedMsg.setType(Message.Type.SERVICE_READ_REZERVATION_LIST);
            oos = new ObjectOutputStream(clientSocket.getOutputStream());
            oos.writeObject(requestedMsg);
            ois = new ObjectInputStream(clientSocket.getInputStream());
            receivedMsg = (Message) ois.readObject();
            System.out.println("Reader" + clientID + " received a message from server.");
            Flight flight = (Flight) receivedMsg.getContent();
            System.out.println("*****Ticket Numbers*****");
            StringBuilder result = new StringBuilder();
            for (int j = 0; j < flight.getTicketList().length; j++) {
                String ticketNumber = flight.getTicketList()[j].getTicketNumber();
                Boolean ticketState = flight.getTicketList()[j].isTicketState();
                if (ticketState == false) {
                    result.append(ticketNumber).append(" : Available *--* ");
                } else {
                    result.append(ticketNumber).append(" : Reserved *--* ");
                }
            }
            result.append("\n--------------------------------------------------");
            return result.toString();
        } catch (Exception e) {
            e.printStackTrace();
            return "Error: " + e.getMessage();
        } finally {
            lock.unlock();
        }
    }

    @Override
    public void run() {
        if (processOrder == 0) {
            queryReservation(requestedTicket);
        } else if (processOrder == 1) {
            // Varsayılan parametreler geçici olarak gönderildi
            makeReservation(requestedTicket, "Istanbul", "Samsun", "10.07.2024");
        } else if (processOrder == 2) {
            makeReservation(requestedTicket, "Istanbul", "Samsun", "10.07.2024");
            cancelReservation(requestedTicket);
            queryReservation(requestedTicket);
        }
    }
}
