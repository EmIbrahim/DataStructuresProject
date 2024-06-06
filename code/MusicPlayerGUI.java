package music;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.logging.Level;
import java.util.logging.Logger;

public class MusicPlayerGUI extends JFrame {
    private CardLayout cardLayout;
    private JPanel mainPanel;
    private Playlist playlist;
    private User currentUser;
    private Playlist mega;
    private Hash<String, User> userMap;
    private ArrayList<Song> allSongs;

    public MusicPlayerGUI() throws Exception {
        this.playlist = new Playlist();
        setTitle("Music Player");
        setSize(600, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // Apply modern look and feel
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }

        cardLayout = new CardLayout();
        mainPanel = new JPanel(cardLayout);

        // Initialize data
        try {
            allSongs = Music.loadSongsFromFile("songsfinal.txt");
            mega = new Playlist("Mega Playlist");
            Music.megamaker(allSongs, mega);
            userMap = Music.loadUsersFromFile("users.txt");
        } catch (Exception e) {
            e.printStackTrace();
        }

        // Add panels
        mainPanel.add(new LoginPanel(), "Login");
        mainPanel.add(new RegistrationPanel(), "Register");
        mainPanel.add(new MainMenuPanel(), "MainMenu");

        add(mainPanel);
        cardLayout.show(mainPanel, "Login");
        mainPanel.add(new SearchSongsPanel(playlist), "SearchSongs");
        mainPanel.add(new ModifyPlaylistPanel(playlist), "ModifyPlaylist");
        mainPanel.add(new ViewPlaylistsPanel(currentUser), "ViewPlaylists");   
    }

   private class LoginPanel extends JPanel {
    public LoginPanel() {
        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        gbc.insets = new Insets(10, 10, 10, 10);

        // Set background color
        setBackground(new Color(21, 64, 33));
        setBorder(BorderFactory.createLineBorder(new Color(30, 215, 96), 2));
        setBorder(BorderFactory.createLineBorder(new Color(30, 215, 96), 8, rootPaneCheckingEnabled));
        
        JLabel titleLabel = new JLabel("Login");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        titleLabel.setForeground(Color.white);  // Set text color
        add(titleLabel, gbc);
        
        gbc.gridy++;
        JLabel usernameLabel = new JLabel("Username:");
        usernameLabel.setForeground(Color.white);  // Set text color
        add(usernameLabel, gbc);

        gbc.gridy++;
        JTextField usernameField = new JTextField(20);
        usernameField.setBackground(new Color(62, 102, 63));  // Dark background
        usernameField.setForeground(Color.white);  // White text
        usernameField.setBorder(BorderFactory.createLineBorder(new Color(30, 215, 96)));  // Border color
        add(usernameField, gbc);

        gbc.gridy++;
        JLabel passwordLabel = new JLabel("Password:");
        passwordLabel.setForeground(Color.WHITE);  // Set text color
        add(passwordLabel, gbc);

        gbc.gridy++;
        JPasswordField passwordField = new JPasswordField(20);
        
        passwordField.setBackground(new Color(62, 102, 63));  // Dark background
        passwordField.setForeground(new Color(30, 215, 96));  // White text
        passwordField.setBorder(BorderFactory.createLineBorder(new Color(30, 215, 96)));  // Border color
        add(passwordField, gbc);

        gbc.gridy++;
        gbc.gridwidth = 1;
        JButton loginButton = new JButton("Login");
        loginButton.setBackground(new Color(30, 215, 96));  // Spotify green
        loginButton.setForeground(Color.BLACK);  // Black text
        loginButton.setFocusPainted(false);
        add(loginButton, gbc);

        loginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String username = usernameField.getText();
                String password = new String(passwordField.getPassword());

                try {
                    if (userMap.containsKey(username)) {
                        User user = userMap.get(username);
                        if (user != null && user.authenticate(password)) {
                            currentUser = user;
                            cardLayout.show(mainPanel, "MainMenu");
                        } else {
                            JOptionPane.showMessageDialog(null, "Invalid username or password.");
                        }
                    } else {
                        JOptionPane.showMessageDialog(null, "Invalid username or password.");
                    }
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        });

        gbc.gridx++;
        JButton registerButton = new JButton("Register");
        registerButton.setBackground(new Color(30, 215, 96));  // Spotify green
        registerButton.setForeground(Color.BLACK);  // Black text
        registerButton.setFocusPainted(false);
        add(registerButton, gbc);

        registerButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                cardLayout.show(mainPanel, "Register");
            }
        });
    }
}

    private class SearchSongsPanel extends JPanel {
    private JTextField searchField;
    private JTextArea resultsArea;
    private Playlist playlist;

    public SearchSongsPanel(Playlist playlist) {
        this.playlist = mega;
        setLayout(new BorderLayout());
        setBackground(new Color(21, 64, 33));

        searchField = new JTextField();
        resultsArea = new JTextArea();
        resultsArea.setEditable(false);
        resultsArea.setBackground(new Color(62, 102, 63));
        resultsArea.setForeground(Color.WHITE);
        resultsArea.setBorder(BorderFactory.createLineBorder(new Color(30, 215, 96)));
        JScrollPane scrollPane = new JScrollPane(resultsArea);

        JButton searchButton = createButton("Search");
        JButton backButton = createButton("Back");

        searchButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String searchQuery = searchField.getText().toLowerCase();
                if (searchQuery.isEmpty()) {
                    JOptionPane.showMessageDialog(null, "Search query cannot be empty.");
                } else {
                    try {
                        searchSongs(searchQuery);
                    } catch (Exception ex) {
                        Logger.getLogger(MusicPlayerGUI.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            }
        });

        backButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                cardLayout.show(mainPanel, "MainMenu");
            }
        });

        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.setBackground(new Color(21, 64, 33));
        topPanel.add(searchField, BorderLayout.CENTER);
        topPanel.add(searchButton, BorderLayout.EAST);

        add(topPanel, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);
        add(backButton, BorderLayout.SOUTH);
    }

    private JButton createButton(String text) {
        JButton button = new JButton(text);
        button.setBackground(new Color(30, 215, 96));
        button.setForeground(Color.BLACK);
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        return button;
    }

    private void searchSongs(String query) throws Exception {
        StringBuilder results = new StringBuilder();
        ArrayList<Song> resultSongs = new ArrayList<>();

        resultSongs.addAll(playlist.searchBySong(query));
        resultSongs.addAll(playlist.searchByArtist(query));
        resultSongs.addAll(playlist.searchByGenre(query));

        if (resultSongs.isEmpty()) {
            results.append("No results found.");
        } else {
            for (int i = 0; i < resultSongs.Length(); i++) {
                Song song = resultSongs.get(i);
                results.append(i + 1).append(". ").append(song.getSongName())
                        .append(" by ").append(song.getArtistName()).append("\n");
            }
        }

        resultsArea.setText(results.toString());
    }
}



    private class RegistrationPanel extends JPanel {
        public RegistrationPanel() {
            setLayout(new GridBagLayout());
            GridBagConstraints gbc = new GridBagConstraints();
            gbc.gridx = 0;
            gbc.gridy = 0;
            gbc.gridwidth = 2;
            gbc.insets = new Insets(10, 10, 10, 10);
            setBackground(new Color(21, 64, 33));
            setBorder(BorderFactory.createLineBorder(new Color(30, 215, 96), 2));
            setBorder(BorderFactory.createLineBorder(new Color(30, 215, 96), 8, rootPaneCheckingEnabled));
            JLabel titleLabel = new JLabel("Registration");
            titleLabel.setForeground(Color.white);
            titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
            add(titleLabel, gbc);

            gbc.gridy++;
            JLabel usernameLabel = new JLabel("Username:");
            usernameLabel.setForeground(Color.white);
            add(usernameLabel, gbc);

            gbc.gridy++;
            JTextField usernameField = new JTextField(20);
            add(usernameField, gbc);

            gbc.gridy++;
            JLabel passwordLabel = new JLabel("Password:");
            passwordLabel.setForeground(Color.white);
            add(passwordLabel, gbc);

            gbc.gridy++;
            JPasswordField passwordField = new JPasswordField(20);
            passwordField.setForeground(new Color(30, 215, 96));
            add(passwordField, gbc);

            gbc.gridy++;
            gbc.gridwidth = 1;
            JButton createAccountButton = new JButton("Create Account");
            add(createAccountButton, gbc);

            createAccountButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    String username = usernameField.getText();
                    String password = new String(passwordField.getPassword());

                    try {
                       if (userMap.containsKey(username)) {
                            JOptionPane.showMessageDialog(null, "Username already exists.");
                        } else {
                            User newUser = new User(username, password);
                            userMap.put(username, newUser);
                            Music.saveUsersToFile(userMap, "users.txt");
                            JOptionPane.showMessageDialog(null, "Account created successfully!");
                            cardLayout.show(mainPanel, "Login");
                        }
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                }
            });

            gbc.gridx++;
            JButton backButton = new JButton("Back");
            add(backButton, gbc);

            backButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    cardLayout.show(mainPanel, "Login");
                }
            });
        }
    }

   private class MainMenuPanel extends JPanel {
    public MainMenuPanel() throws Exception {
        setLayout(new GridBagLayout());
        setBackground(new Color(21, 64, 33)); // Dark background
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.insets = new Insets(10, 10, 10, 10);

        JButton createPlaylistButton = createButton("Create Playlist");
        JButton viewPlaylistsButton = createButton("View Playlists");
        JButton searchSongsButton = createButton("Search Songs");
        JButton logoutButton = createButton("Logout");

        add(createPlaylistButton, gbc);
        gbc.gridy++;
        add(viewPlaylistsButton, gbc);
        gbc.gridy++;
        add(searchSongsButton, gbc);
        gbc.gridy++;
        add(logoutButton, gbc);

        createPlaylistButton.addActionListener(e -> {
            String playlistName = JOptionPane.showInputDialog("Enter playlist name:");
            if (playlistName != null && !playlistName.trim().isEmpty()) {
                Playlist newPlaylist = new Playlist(playlistName);
                currentUser.addPlaylist(newPlaylist);
                JOptionPane.showMessageDialog(null, "Playlist created successfully!");
            } else {
                JOptionPane.showMessageDialog(null, "Playlist name cannot be empty.");
            }
        });

        viewPlaylistsButton.addActionListener(e -> {
            try {
                mainPanel.add(new ViewPlaylistsPanel(currentUser), "ViewPlaylists");
                cardLayout.show(mainPanel, "ViewPlaylists");
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        });

        searchSongsButton.addActionListener(e -> {
            try {
                mainPanel.add(new SearchSongsPanel(playlist), "SearchSongs");
                cardLayout.show(mainPanel, "SearchSongs");
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        });

        logoutButton.addActionListener(e -> {
            currentUser = null;
            cardLayout.show(mainPanel, "Login");
        });
    }

    private JButton createButton(String text) {
        JButton button = new JButton(text);
        button.setBackground(new Color(30, 215, 96));
        button.setForeground(Color.BLACK);
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        return button;
    }
}
   private class ViewPlaylistsPanel extends JPanel {
    private User currentUser;

    public ViewPlaylistsPanel(User currentUser) throws Exception {
        this.currentUser = currentUser;
        setLayout(new GridBagLayout());
        setBackground(new Color(21, 64, 33));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.insets = new Insets(10, 10, 10, 10);

        JTextArea playlistsArea = new JTextArea();
        playlistsArea.setEditable(false);
        playlistsArea.setBackground(new Color(62, 102, 63));
        playlistsArea.setForeground(Color.WHITE);
        playlistsArea.setBorder(BorderFactory.createLineBorder(new Color(30, 215, 96)));
        JScrollPane scrollPane = new JScrollPane(playlistsArea);

        JButton backButton = createButton("Back");
        JButton selectButton = createButton("Select Playlist");
        JButton modifyButton = createButton("Modify Playlist");

        backButton.addActionListener(e -> cardLayout.show(mainPanel, "MainMenu"));

        selectButton.addActionListener(e -> {
            String playlistName = JOptionPane.showInputDialog("Enter the name of the playlist to select:");
            if (playlistName != null && !playlistName.trim().isEmpty()) {
                try {
                    Playlist selectedPlaylist = currentUser.getPlaylist(playlistName);
                    if (selectedPlaylist != null) {
                        mainPanel.add(new PlayPlaylistPanel(selectedPlaylist), "PlayPlaylist");
                        cardLayout.show(mainPanel, "PlayPlaylist");
                    } else {
                        JOptionPane.showMessageDialog(null, "Playlist not found.");
                    }
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            } else {
                JOptionPane.showMessageDialog(null, "Playlist name cannot be empty.");
            }
        });

        modifyButton.addActionListener(e -> {
            String playlistName = JOptionPane.showInputDialog("Enter the name of the playlist to modify:");
            if (playlistName != null && !playlistName.trim().isEmpty()) {
                try {
                    Playlist playlist = currentUser.getPlaylist(playlistName);
                    if (playlist != null) {
                        mainPanel.add(new ModifyPlaylistPanel(playlist), "ModifyPlaylist");
                        cardLayout.show(mainPanel, "ModifyPlaylist");
                    } else {
                        JOptionPane.showMessageDialog(null, "Playlist not found.");
                    }
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            } else {
                JOptionPane.showMessageDialog(null, "Playlist name cannot be empty.");
            }
        });

        gbc.gridy++;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;
        add(scrollPane, gbc);

        gbc.gridy++;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;
        JPanel buttonPanel = new JPanel(new GridLayout(1, 2, 10, 10));
        buttonPanel.setBackground(new Color(21, 64, 33));
        buttonPanel.add(selectButton);
        buttonPanel.add(modifyButton);
        add(buttonPanel, gbc);

        gbc.gridy++;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;
        add(backButton, gbc);

        displayPlaylists(playlistsArea);
    }

    private JButton createButton(String text) {
        JButton button = new JButton(text);
        button.setBackground(new Color(30, 215, 96));
        button.setForeground(Color.BLACK);
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        return button;
    }

    private void displayPlaylists(JTextArea playlistsArea) throws Exception {
        if (currentUser != null) {
            StringBuilder playlistsText = new StringBuilder();
            ArrayList<Playlist> playlists = currentUser.getPlaylists();
            for (int i = 0; i < playlists.Length(); i++) {
                Playlist playlist = playlists.get(i);
                playlistsText.append(playlist.getPlaylistName()).append("\n");
                DoublyLinkedList<Song> songs = playlist.getSongs();
                for (int j = 0; j < songs.length(); j++) {
                    Song song = songs.get(j);
                    playlistsText.append("  ").append(song.getSongName()).append(" by ").append(song.getArtistName()).append("\n");
                }
                playlistsText.append("\n");
            }
            playlistsArea.setText(playlistsText.toString());
        }
    }
}



    private class PlayPlaylistPanel extends JPanel {
    private Playlist playlist;
    private Node<Song> curr;
    private JLabel songLabel;

    public PlayPlaylistPanel(Playlist playlist) {
        this.playlist = playlist;
        this.curr = playlist.getSongs().getHead(); // Assuming there's a method to get the head of the list
        setLayout(new BorderLayout());
        setBackground(new Color(21, 64, 33));

        songLabel = new JLabel("", JLabel.CENTER);
        songLabel.setForeground(Color.WHITE);
        updateSongLabel();

        JButton prevButton = createButton("Previous");
        JButton playButton = createButton("Play");
        JButton nextButton = createButton("Next");
        JButton backButton = createButton("Back");

        prevButton.addActionListener(e -> prevSong());

        playButton.addActionListener(e -> playCurrentSong());

        nextButton.addActionListener(e -> nextSong());

        backButton.addActionListener(e -> cardLayout.show(mainPanel, "ViewPlaylists"));

        JPanel controlPanel = new JPanel();
        controlPanel.setBackground(new Color(21, 64, 33));
        controlPanel.add(prevButton);
        controlPanel.add(playButton);
        controlPanel.add(nextButton);
        controlPanel.add(backButton);

        add(songLabel, BorderLayout.CENTER);
        add(controlPanel, BorderLayout.SOUTH);
    }

    private JButton createButton(String text) {
        JButton button = new JButton(text);
        button.setBackground(new Color(30, 215, 96));
        button.setForeground(Color.BLACK);
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        return button;
    }

    private void updateSongLabel() {
        if (curr != null && curr.getData() != null) {
            Song currentSong = curr.getData();
            songLabel.setText("Playing: " + currentSong.getSongName() + " by " + currentSong.getArtistName());
        } else {
            songLabel.setText("No songs in playlist.");
        }
    }

    private void playCurrentSong() {
        if (curr != null && curr.getData() != null) {
            Song currentSong = curr.getData();
            JOptionPane.showMessageDialog(null, "Playing: " + currentSong.getSongName() + " by " + currentSong.getArtistName());
        } else {
            JOptionPane.showMessageDialog(null, "No song selected.");
        }
    }

    private void nextSong() {
        if (curr != null && curr.getNext() != null) {
            curr = curr.getNext();
            updateSongLabel();
        }
    }

    private void prevSong() {
        if (curr != null && curr.getPrev() != null) {
            curr = curr.getPrev();
            updateSongLabel();
        }
    }
}

    private class ModifyPlaylistPanel extends JPanel {
    private Playlist playlist;
    private JTextArea playlistArea;

    public ModifyPlaylistPanel(Playlist playlist) {
        this.playlist = playlist;
        setLayout(new BorderLayout());
        setBackground(new Color(21, 64, 33));

        playlistArea = new JTextArea();
        playlistArea.setEditable(false);
        playlistArea.setBackground(new Color(62, 102, 63));
        playlistArea.setForeground(Color.WHITE);
        playlistArea.setBorder(BorderFactory.createLineBorder(new Color(30, 215, 96)));
        JScrollPane scrollPane = new JScrollPane(playlistArea);

        JButton addSongButton = createButton("Add Song");
        JButton removeSongButton = createButton("Remove Song");
        JButton backButton = createButton("Back");

        addSongButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    addSongToPlaylist();
                } catch (Exception ex) {
                    Logger.getLogger(MusicPlayerGUI.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });

        removeSongButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    removeSongFromPlaylist();
                } catch (Exception ex) {
                    Logger.getLogger(MusicPlayerGUI.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });

        backButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                cardLayout.show(mainPanel, "ViewPlaylists");
            }
        });

        JPanel buttonPanel = new JPanel(new GridLayout(1, 3, 10, 10));
        buttonPanel.setBackground(new Color(21, 64, 33));
        buttonPanel.add(addSongButton);
        buttonPanel.add(removeSongButton);
        buttonPanel.add(backButton);

        add(scrollPane, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);

        displayPlaylist();
    }

    private JButton createButton(String text) {
        JButton button = new JButton(text);
        button.setBackground(new Color(30, 215, 96));
        button.setForeground(Color.BLACK);
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        return button;
    }

    private void displayPlaylist() {
        StringBuilder playlistText = new StringBuilder();
        for (int i = 0; i < playlist.getSongs().length(); i++) {
            Song song = playlist.getSongs().get(i);
            playlistText.append(i + 1).append(". ").append(song.getSongName())
                    .append(" by ").append(song.getArtistName()).append("\n");
        }
        playlistArea.setText(playlistText.toString());
    }

    private void addSongToPlaylist() throws Exception {
        Song selectedSong = selectSongFromList(allSongs);
        if (selectedSong != null) {
            playlist.addSong(selectedSong);
            displayPlaylist();
            JOptionPane.showMessageDialog(null, "Song added to the playlist!");
        }
    }

    private void removeSongFromPlaylist() throws Exception {
        String songNumberStr = JOptionPane.showInputDialog("Enter the number of the song to remove:");
        try {
            int songNumber = Integer.parseInt(songNumberStr);
            if (songNumber >= 1 && songNumber <= playlist.getSongs().length()) {
                playlist.deleteSong(playlist.getSongs().get(songNumber - 1));
                displayPlaylist();
                JOptionPane.showMessageDialog(null, "Song removed from the playlist!");
            } else {
                JOptionPane.showMessageDialog(null, "Invalid song number.");
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(null, "Please enter a valid number.");
        }
    }
}
private Song selectSongFromList(ArrayList<Song> songs) throws Exception {
    final int pageSize = 35; // Adjust the page size as needed
    StringBuilder sb = new StringBuilder();
    sb.append("Available Songs:\n");

    int totalPages = (int) Math.ceil((double) songs.Length() / pageSize);
    int currentPage = 1;

    while (currentPage <= totalPages) {
        sb.append("Page ").append(currentPage).append(":\n");
        int startIndex = (currentPage - 1) * pageSize;
        int endIndex = Math.min(startIndex + pageSize, songs.Length());

        for (int i = startIndex; i < endIndex; i++) {
            sb.append(i + 1).append(". ").append(songs.get(i).getSongName())
                    .append(" by ").append(songs.get(i).getArtistName()).append("\n");
        }

        // Show pagination options
        sb.append("\n");
        if (currentPage < totalPages) {
            sb.append("Enter 'n' for Next Page\n");
        }
        if (currentPage > 1) {
            sb.append("Enter 'p' for Previous Page\n");
        }
        sb.append("Enter song number or 'q' to Quit\n");

        String input = JOptionPane.showInputDialog(null, sb.toString(), "Select Song - Page " + currentPage, JOptionPane.PLAIN_MESSAGE);
        if (input == null || input.equalsIgnoreCase("q")) {
            break; // Quit
        } else if (input.equalsIgnoreCase("n")) {
            currentPage++; // Next page
            sb.setLength(0); // Clear StringBuilder for the next page
        } else if (input.equalsIgnoreCase("p")) {
            currentPage--; // Previous page
            sb.setLength(0); // Clear StringBuilder for the next page
        } else {
            try {
                int songNumber = Integer.parseInt(input);
                int songIndex = (currentPage - 1) * pageSize + songNumber - 1;
                if (songIndex >= 0 && songIndex < songs.Length()) {
                    return songs.get(songIndex);
                } else {
                    JOptionPane.showMessageDialog(null, "Invalid song number.");
                }
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(null, "Please enter a valid number.");
            }
        }
    }
    return null;
}
 
    public static void main(String[] args) throws Exception {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                try {
                    new MusicPlayerGUI().setVisible(true);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }
}
