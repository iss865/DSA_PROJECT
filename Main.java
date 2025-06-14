package workingdsa1;



import javax.swing.*;
import java.awt.*;
import java.util.*;

class User {
    String username, password, location, contact, gender;
    Stack<Donation> donationHistory = new Stack<>();
    Queue<Donation> claimedDonations = new LinkedList<>();
    ArrayList<Message> messages = new ArrayList<>();
    ArrayList<String> ratings = new ArrayList<>();

    public User(String username, String password, String location, String contact, String gender) {
        this.username = username;
        this.password = password;
        this.location = location;
        this.contact = contact;
        this.gender = gender;
    }
}

class Donation {
    String donorUsername, itemName, category, location;
    boolean isClaimed;

    public Donation(String donorUsername, String itemName, String category, String location) {
        this.donorUsername = donorUsername;
        this.itemName = itemName;
        this.category = category;
        this.location = location;
        this.isClaimed = false;
    }

    public String toString() {
        return "Item: " + itemName + ", Category: " + category + ", Location: " + location + ", Status: " + (isClaimed ? "Claimed" : "Available");
    }
}

class Message {
    String sender, receiver, content;
    Date timestamp;

    public Message(String sender, String receiver, String content) {
        this.sender = sender;
        this.receiver = receiver;
        this.content = content;
        this.timestamp = new Date();
    }

    public String toString() {
        return "[" + timestamp + "] " + sender + " to " + receiver + ": " + content;
    }
}

public class Main extends JFrame {
    ArrayList<User> users = new ArrayList<>();
    ArrayList<Donation> donations = new ArrayList<>();
    ArrayList<Message> allMessages = new ArrayList<>();
    User loggedInUser = null;

    public Main() {
        setTitle("Community Donation Exchange Platform");
        setSize(600, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new GridLayout(12, 1));

        JButton registerButton = new JButton("Register");
        JButton loginButton = new JButton("Login");
        JButton donateButton = new JButton("Post Donation");
        JButton viewButton = new JButton("View Donations");
        JButton claimButton = new JButton("Claim Donation");
        JButton messageButton = new JButton("Send Message");
        JButton viewMessagesButton = new JButton("View Messages");
        JButton ratingButton = new JButton("Rate User");
        JButton historyButton = new JButton("View Donation History");
        JButton claimedButton = new JButton("View Claimed Donations");
        JButton searchByLocationCategoryButton = new JButton("Search by Location & Category");
        JButton logoutButton = new JButton("Logout");

        buttonPanel.add(registerButton);
        buttonPanel.add(loginButton);
        buttonPanel.add(donateButton);
        buttonPanel.add(viewButton);
        buttonPanel.add(claimButton);
        buttonPanel.add(messageButton);
        buttonPanel.add(viewMessagesButton);
        buttonPanel.add(ratingButton);
        buttonPanel.add(historyButton);
        buttonPanel.add(claimedButton);
        buttonPanel.add(searchByLocationCategoryButton);
        buttonPanel.add(logoutButton);

        add(buttonPanel, BorderLayout.CENTER);

        registerButton.addActionListener(e -> registerUser());
        loginButton.addActionListener(e -> loginUser());
        donateButton.addActionListener(e -> {
            if (checkLogin()) postDonation();
        });
        viewButton.addActionListener(e -> viewDonations());
        claimButton.addActionListener(e -> {
            if (checkLogin()) claimDonation();
        });
        messageButton.addActionListener(e -> {
            if (checkLogin()) sendMessage();
        });
        viewMessagesButton.addActionListener(e -> {
            if (checkLogin()) viewMessages();
        });
        ratingButton.addActionListener(e -> {
            if (checkLogin()) rateUser();
        });
        historyButton.addActionListener(e -> {
            if (checkLogin()) viewDonationHistory();
        });
        claimedButton.addActionListener(e -> {
            if (checkLogin()) viewClaimedDonations();
        });
        searchByLocationCategoryButton.addActionListener(e -> searchDonationsByLocationAndCategory());
        logoutButton.addActionListener(e -> logout());

        setVisible(true);
    }

    private boolean checkLogin() {
        if (loggedInUser == null) {
            JOptionPane.showMessageDialog(this, "Please login to continue.");
            return false;
        }
        return true;
    }

    private void registerUser() {
        String username = JOptionPane.showInputDialog(this, "Enter username:");
        if (username == null) return;
        for (User u : users) {
            if (u.username.equals(username)) {
                JOptionPane.showMessageDialog(this, "Username already exists.");
                return;
            }
        }
        String password = JOptionPane.showInputDialog(this, "Enter password:");
        if (password == null) return;
        String location = JOptionPane.showInputDialog(this, "Enter location:");
        if (location == null) return;
        String contact = JOptionPane.showInputDialog(this, "Enter contact:");
        if (contact == null) return;
        String gender = JOptionPane.showInputDialog(this, "Enter gender:");
        if (gender == null) return;
        users.add(new User(username, password, location, contact, gender));
        JOptionPane.showMessageDialog(this, "User registered successfully.");
    }

    private void loginUser() {
        String username = JOptionPane.showInputDialog(this, "Enter username:");
        if (username == null) return;
        String password = JOptionPane.showInputDialog(this, "Enter password:");
        if (password == null) return;
        for (User u : users) {
            if (u.username.equals(username) && u.password.equals(password)) {
                loggedInUser = u;
                JOptionPane.showMessageDialog(this, "Login successful. Welcome, " + username + "!");
                return;
            }
        }
        JOptionPane.showMessageDialog(this, "Invalid credentials. Please try again or register.");
    }

    private void logout() {
        if (loggedInUser != null) {
            JOptionPane.showMessageDialog(this, "Logged out: " + loggedInUser.username);
            loggedInUser = null;
        } else {
            JOptionPane.showMessageDialog(this, "No user is currently logged in.");
        }
    }

    private void postDonation() {
        String item = JOptionPane.showInputDialog(this, "Enter item to donate:");
        if (item == null) return;
        String category = JOptionPane.showInputDialog(this, "Enter category:");
        if (category == null) return;
        String location = JOptionPane.showInputDialog(this, "Enter location:");
        if (location == null) return;
        Donation newDonation = new Donation(loggedInUser.username, item, category, location);
        donations.add(newDonation);
        loggedInUser.donationHistory.push(newDonation);
        JOptionPane.showMessageDialog(this, "Donation posted successfully.");
    }

    private void viewDonations() {
        String output = "Available Donations:\n";
        for (Donation d : donations) {
            if (!d.isClaimed) {
                output = output + d + "\n";
            }
        }
        JOptionPane.showMessageDialog(this, output.length() > 22 ? output : "No donations available.");
    }

    private void claimDonation() {
        String item = JOptionPane.showInputDialog(this, "Enter item name to claim:");
        if (item == null) return;
        for (Donation d : donations) {
            if (d.itemName.equalsIgnoreCase(item) && !d.isClaimed) {
                d.isClaimed = true;
                loggedInUser.claimedDonations.add(d);
                JOptionPane.showMessageDialog(this, "Successfully claimed: " + item);
                return;
            }
        }
        JOptionPane.showMessageDialog(this, "Donation not found or already claimed.");
    }

    private void viewDonationHistory() {
        String history = "Donation History (Stack):\n";
        Stack<Donation> temp = new Stack<>();
        while (!loggedInUser.donationHistory.isEmpty()) {
            Donation d = loggedInUser.donationHistory.pop();
            history = history + d + "\n";
            temp.push(d);
        }
        while (!temp.isEmpty()) {
            loggedInUser.donationHistory.push(temp.pop());
        }
        JOptionPane.showMessageDialog(this, history.length() > 27 ? history : "No donation history.");
    }

    private void viewClaimedDonations() {
        String claimed = "Claimed Donations (Queue):\n";
        Queue<Donation> temp = new LinkedList<>();
        while (!loggedInUser.claimedDonations.isEmpty()) {
            Donation d = loggedInUser.claimedDonations.poll();
            claimed = claimed + d + "\n";
            temp.add(d);
        }
        while (!temp.isEmpty()) {
            loggedInUser.claimedDonations.add(temp.poll());
        }
        JOptionPane.showMessageDialog(this, claimed.length() > 27 ? claimed : "No claimed donations.");
    }

    private void sendMessage() {
        String receiver = JOptionPane.showInputDialog(this, "Enter receiver username:");
        if (receiver == null) return;
        if (receiver.equals(loggedInUser.username)) {
            JOptionPane.showMessageDialog(this, "You cannot send a message to yourself.");
            return;
        }
        User receiverUser = null;
        for (User u : users) {
            if (u.username.equals(receiver)) {
                receiverUser = u;
                break;
            }
        }
        if (receiverUser == null) {
            JOptionPane.showMessageDialog(this, "Receiver does not exist.");
            return;
        }
        String content = JOptionPane.showInputDialog(this, "Enter your message:");
        if (content == null) return;
        Message msg = new Message(loggedInUser.username, receiver, content);
        loggedInUser.messages.add(msg);
        receiverUser.messages.add(msg);
        allMessages.add(msg);
        JOptionPane.showMessageDialog(this, "Message sent.");
    }

    private void viewMessages() {
        String output = "Your Messages:\n";
        for (Message m : loggedInUser.messages) {
            output = output + m + "\n";
        }
        JOptionPane.showMessageDialog(this, output.length() > 15 ? output : "No messages.");
    }

    private void rateUser() {
        String ratee = JOptionPane.showInputDialog(this, "Enter username to rate:");
        if (ratee == null) return;
        if (ratee.equals(loggedInUser.username)) {
            JOptionPane.showMessageDialog(this, "You cannot rate yourself.");
            return;
        }
        User rateeUser = null;
        for (User u : users) {
            if (u.username.equals(ratee)) {
                rateeUser = u;
                break;
            }
        }
        if (rateeUser == null) {
            JOptionPane.showMessageDialog(this, "User not found.");
            return;
        }
        String ratingStr = JOptionPane.showInputDialog(this, "Enter rating (1-5):");
        if (ratingStr == null) return;
        int rating = 0;
        try {
            rating = Integer.parseInt(ratingStr);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Invalid rating.");
            return;
        }
        if (rating < 1 || rating > 5) {
            JOptionPane.showMessageDialog(this, "Rating must be between 1 and 5.");
            return;
        }
        String ratingEntry = loggedInUser.username + " rated " + ratee + ": " + rating + " stars";
        loggedInUser.ratings.add(ratingEntry);
        JOptionPane.showMessageDialog(this, "Rating submitted.");
    }

    private void searchDonationsByLocationAndCategory() {
        String location = JOptionPane.showInputDialog(this, "Enter location to filter:");
        if (location == null) return;
        String category = JOptionPane.showInputDialog(this, "Enter category to filter:");
        if (category == null) return;
        boolean found = false;
        String results = "Donations in Location: " + location + ", Category: " + category + "\n";
        for (Donation d : donations) {
            if (d.location.equalsIgnoreCase(location) && d.category.equalsIgnoreCase(category) && !d.isClaimed) {
                results = results + d + "\n";
                found = true;
            }
        }
        JOptionPane.showMessageDialog(this, found ? results : "No matching donations found.");
    }

    public static void main(String[] args) {
        new Main();
    }
}
