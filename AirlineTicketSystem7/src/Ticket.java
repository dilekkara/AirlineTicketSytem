import java.io.Serializable;

public class Ticket implements Serializable {
    private int ticketID;
    private String ticketNumber;
    private boolean ticketState;
    private int ticketHolder;
    private String startLocation; // Yeni alan
    private String endLocation; // Yeni alan
    private String travelDate; // Yeni alan

    public Ticket() {
    }

    public Ticket(int ticketID, String ticketNumber, boolean ticketState, int ticketHolder) {
        this.ticketID = ticketID;
        this.ticketNumber = ticketNumber;
        this.ticketState = ticketState;
        this.ticketHolder = ticketHolder;
    }

    // Yeni Constructor
    public Ticket(int ticketID, String ticketNumber, boolean ticketState, int ticketHolder, String startLocation, String endLocation, String travelDate) {
        this.ticketID = ticketID;
        this.ticketNumber = ticketNumber;
        this.ticketState = ticketState;
        this.ticketHolder = ticketHolder;
        this.startLocation = startLocation;
        this.endLocation = endLocation;
        this.travelDate = travelDate;
    }

    public int getTicketID() {
        return ticketID;
    }

    public void setTicketID(int ticketID) {
        this.ticketID = ticketID;
    }

    public String getTicketNumber() {
        return ticketNumber;
    }

    public void setTicketNumber(String ticketNumber) {
        this.ticketNumber = ticketNumber;
    }

    public boolean isTicketState() {
        return ticketState;
    }

    public void setTicketState(boolean ticketState) {
        this.ticketState = ticketState;
    }

    public int getTicketHolder() {
        return ticketHolder;
    }

    public void setTicketHolder(int ticketHolder) {
        this.ticketHolder = ticketHolder;
    }

    public String getStartLocation() {
        return startLocation;
    }

    public void setStartLocation(String startLocation) {
        this.startLocation = startLocation;
    }

    public String getEndLocation() {
        return endLocation;
    }

    public void setEndLocation(String endLocation) {
        this.endLocation = endLocation;
    }

    public String getTravelDate() {
        return travelDate;
    }

    public void setTravelDate(String travelDate) {
        this.travelDate = travelDate;
    }
}
