import javax.swing.*;
import java.awt.*;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

public class FlightReservationSystemGUI extends JFrame {

    private ReentrantLock lock = new ReentrantLock();
    private Condition condition = lock.newCondition();
    private JTextField ticketIDField;
    private JComboBox<String> startLocationComboBox;
    private JComboBox<String> endLocationComboBox;
    private JTextField travelDateField;
    private JTextArea logArea;
    private int clientID = 1;  // Test için sabit bir client ID

    public FlightReservationSystemGUI() {
        setTitle("Flight Reservation System");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1000, 800); // Pencere boyutu ayarlandı
        setLocationRelativeTo(null); // Ortaya konumlandırıldı

        JPanel mainPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 10, 5, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // 81 ili içeren dizi
        String[] cities = {
            "Adana", "Adıyaman", "Afyonkarahisar", "Ağrı", "Amasya", "Ankara", "Antalya", "Artvin", "Aydın", "Balıkesir",
            "Bilecik", "Bingöl", "Bitlis", "Bolu", "Burdur", "Bursa", "Çanakkale", "Çankırı", "Çorum", "Denizli",
            "Diyarbakır", "Edirne", "Elazığ", "Erzincan", "Erzurum", "Eskişehir", "Gaziantep", "Giresun", "Gümüşhane",
            "Hakkari", "Hatay", "Isparta", "Mersin", "İstanbul", "İzmir", "Kars", "Kastamonu", "Kayseri", "Kırklareli",
            "Kırşehir", "Kocaeli", "Konya", "Kütahya", "Malatya", "Manisa", "Kahramanmaraş", "Mardin", "Muğla",
            "Muş", "Nevşehir", "Niğde", "Ordu", "Rize", "Sakarya", "Samsun", "Siirt", "Sinop", "Sivas", "Tekirdağ",
            "Tokat", "Trabzon", "Tunceli", "Şanlıurfa", "Uşak", "Van", "Yozgat", "Zonguldak", "Aksaray", "Bayburt",
            "Karaman", "Kırıkkale", "Batman", "Şırnak", "Bartın", "Ardahan", "Iğdır", "Yalova", "Karabük", "Kilis",
            "Osmaniye", "Düzce"
        };

        // Bilet ID
        JLabel ticketIDLabel = new JLabel("Bilet ID:");
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.EAST;
        mainPanel.add(ticketIDLabel, gbc);

        ticketIDField = new JTextField(20);
        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.WEST;
        mainPanel.add(ticketIDField, gbc);

        // Başlangıç Yeri
        JLabel startLocationLabel = new JLabel("Başlangıç Yeri:");
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.anchor = GridBagConstraints.EAST;
        mainPanel.add(startLocationLabel, gbc);

        startLocationComboBox = new JComboBox<>(cities);
        gbc.gridx = 1;
        gbc.gridy = 1;
        gbc.anchor = GridBagConstraints.WEST;
        mainPanel.add(startLocationComboBox, gbc);

        // Bitiş Yeri
        JLabel endLocationLabel = new JLabel("Bitiş Yeri:");
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.anchor = GridBagConstraints.EAST;
        mainPanel.add(endLocationLabel, gbc);

        endLocationComboBox = new JComboBox<>(cities);
        gbc.gridx = 1;
        gbc.gridy = 2;
        gbc.anchor = GridBagConstraints.WEST;
        mainPanel.add(endLocationComboBox, gbc);

        // Gidilen Tarih
        JLabel travelDateLabel = new JLabel("Gidilen Tarih:");
        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.anchor = GridBagConstraints.EAST;
        mainPanel.add(travelDateLabel, gbc);

        travelDateField = new JTextField(20);
        gbc.gridx = 1;
        gbc.gridy = 3;
        gbc.anchor = GridBagConstraints.WEST;
        mainPanel.add(travelDateField, gbc);

        // Butonlar
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 10, 10));

        JButton reserveButton = new JButton("Rezervasyon Yap");
        reserveButton.addActionListener(e -> new ReservationWorker("makeReservation").execute());
        buttonPanel.add(reserveButton);

        JButton cancelButton = new JButton("Rezervasyon İptal");
        cancelButton.addActionListener(e -> new ReservationWorker("cancelReservation").execute());
        buttonPanel.add(cancelButton);

        JButton queryButton = new JButton("Rezervasyon Sorgula");
        queryButton.addActionListener(e -> new ReservationWorker("queryReservation").execute());
        buttonPanel.add(queryButton);

        gbc.gridx = 0;
        gbc.gridy = 4;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        mainPanel.add(buttonPanel, gbc);

        // Log Alanı
        logArea = new JTextArea(15, 80);
        logArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(logArea);
        gbc.gridx = 0;
        gbc.gridy = 5;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        mainPanel.add(scrollPane, gbc);

        add(mainPanel);
    }

    private class ReservationWorker extends SwingWorker<String, Void> {
        private String action;

        public ReservationWorker(String action) {
            this.action = action;
        }

        @Override
        protected String doInBackground() throws Exception {
            int ticketID = Integer.parseInt(ticketIDField.getText());
            String startLocation = (String) startLocationComboBox.getSelectedItem();
            String endLocation = (String) endLocationComboBox.getSelectedItem();
            String travelDate = travelDateField.getText(); // Tarih alanı
            Client client = new Client(lock, condition, clientID, 8080, 8095, ticketID, 0);

            switch (action) {
                case "makeReservation":
                    return "Rezervasyon yapıldı: " + client.makeReservation(ticketID, startLocation, endLocation, travelDate);
                case "cancelReservation":
                    return "Rezervasyon iptal edildi: " + client.cancelReservation(ticketID);
                case "queryReservation":
                    return "Rezervasyon sorgulandı: " + client.queryReservation(ticketID);
                default:
                    return "Bilinmeyen işlem";
            }
        }

        @Override
        protected void done() {
            try {
                String result = get();
                logArea.append(result + "\n");
            } catch (Exception e) {
                e.printStackTrace();
                logArea.append("Bir hata oluştu: " + e.getMessage() + "\n");
            }
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            FlightReservationSystemGUI frame = new FlightReservationSystemGUI();
            frame.setVisible(true);
        });
    }
}
