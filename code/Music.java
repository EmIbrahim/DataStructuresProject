
package music;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import java.util.Scanner;

public class Music {
  static Playlist mega = new Playlist("Mega Playlist");
     public static ArrayList<Song> allSongs;
/*    public static void main(String[] args) throws Exception {    //Uncomment if you want to run this on console
      Scanner scanner = new Scanner(System.in);

        
        allSongs = loadSongsFromFile("songsfinal.txt");
        
        megamaker(allSongs, mega);
        
        Hash<String, User> userMap;
        userMap = loadUsersFromFile("users.txt");
        
      //  User user1 = new User("admin", "admin");
        //userMap.put("admin", user1);
        
       

        while (true) {
            System.out.println("1. Create an account");
            System.out.println("2. Log in");
            System.out.println("3. Exit");
            
            System.out.print("Enter your choice: ");

            int choice = scanner.nextInt();
            scanner.nextLine();

            switch (choice) {
                case 1:
                    createUser(userMap, scanner);
                    break;
                case 2:
                    loginUser(userMap, scanner, allSongs);
                    break;
                case 3:
                    saveUsersToFile(userMap, "users.txt");
                    System.out.println("Exiting the program. Goodbye!");
                    System.exit(0);
                default:
                    System.out.println("Invalid choice. Please enter a valid option.");
            }
        }
        
    }*/
    
    public static void megamaker(ArrayList<Song> all, Playlist mega) throws Exception {
        for (int i = 0; i < all.Length(); i++) {
            Song selectedSong = all.get(i);
            mega.addSong(selectedSong);
        }
    }
    private static void createUser(Hash<String, User> userMap, Scanner scanner) throws Exception {
        System.out.print("Enter your username: ");
        String username = scanner.nextLine();

        if (userMap.containsKey(username)) {
            System.out.println("Username already exists. Please choose a different one.");
            return;
        }

        System.out.print("Enter your password: ");
        String password = scanner.nextLine();

        User newUser = new User(username, password);
        userMap.put(username, newUser);

        System.out.println("Account created successfully!");

        
        saveUsersToFile(userMap, "users.txt");
    }
    
public static void saveUsersToFile(Hash<String, User> userMap, String fileName) throws Exception {
    try (BufferedWriter writer = new BufferedWriter(new FileWriter(fileName))) {
        for (int i = 0; i < userMap.getTable().length; i++) {
            ArrayList<Pair<String, User>> bucket = userMap.getTable()[i];
            for (int j = 0; j < bucket.Length(); j++) {
                Pair<String, User> pair = bucket.get(j);
                User user = pair.getValue();
                writer.write(user.getUsername() + ", " + user.getPassword());

               
                ArrayList<Playlist> playlists = user.getPlaylists();
                if (!playlists.isEmpty()) {
                    writer.write(", ");
                    for (int k = 0; k < playlists.Length(); k++) {
                        Playlist playlist = playlists.get(k);
                        writer.write(playlist.getPlaylistName() + ":");

                        
                        Node<Song> current = playlist.getSongs().head;
                        while (current != null) {
                            writer.write(current.getData().getSongName() + "|" +
                                    current.getData().getArtistName() + "|" +
                                    current.getData().getGenre());
                            if (current.getNext() != null) {
                                writer.write("] ");
                            }
                            current = current.getNext();
                        }

                        if (k < playlists.Length() - 1) {
                            writer.write("; ");
                        }
                    }
                }

                writer.newLine();
            }
        }
    } catch (IOException e) {
        e.printStackTrace();
    }
}

public static Hash<String, User> loadUsersFromFile(String fileName) throws Exception {
    Hash<String, User> userMap = new Hash<>();

    try (BufferedReader br = new BufferedReader(new FileReader(fileName))) {
        String line;
        while ((line = br.readLine()) != null) {
            String[] parts = line.split(", ");
            if (parts.length >= 2) {
                String username = parts[0];
                String password = parts[1];
                User loadedUser = new User(username, password);

                
                if (parts.length >= 3) {
                    String[] playlistInfo = parts[2].split("; ");
                    for (String playlistString : playlistInfo) {
                        String[] playlistData = playlistString.split(":");
                        String playlistName = playlistData[0].trim();
                        Playlist playlist = new Playlist(playlistName);

                        
                        if (playlistData.length > 1) {
                            String[] songsStringArray = playlistData[1].split("\\]");
                            for (String songString : songsStringArray) {
                                String[] songInfo = songString.trim().split("\\|");
                                if (songInfo.length == 3) {
                                    String songName = songInfo[0].trim();

                                    String artistName = songInfo[1].trim();
                                    String genre = songInfo[2].trim();
                                    Scanner scanner;
                                    Song s = new Song(songName, artistName, genre);
                                    
                                    playlist.addSong(s);

                                }
                            }
                        }
                        loadedUser.addPlaylist(playlist);
                    }
                }

                userMap.put(username, loadedUser);
            }
        }
    } catch (IOException e) {
        e.printStackTrace();
    }

    return userMap;
}
    private static void loginUser(Hash<String, User> userMap, Scanner scanner, ArrayList<Song> allSongs) throws Exception {
        System.out.print("Enter your username: ");
        String username = scanner.nextLine();

        if (!userMap.containsKey(username)) {
            System.out.println("Username not found. Please create an account.");
            return;
        }

        System.out.print("Enter your password: ");
        String password = scanner.nextLine();

        User user = userMap.get(username);

        if (user != null && user.authenticate(password)) {
            System.out.println("Login successful!");
            
            performPlaylistOperations(user, scanner, allSongs);
        } else {
            System.out.println("Incorrect password. Please try again.");
        }
    }

    private static void performPlaylistOperations(User user, Scanner scanner, ArrayList<Song> allSongs) throws Exception {
        while (true) {
            System.out.println("1. Create a playlist");
            System.out.println("2. Select a playlist");
            System.out.println("3: Search for songs");
            System.out.println("4. Logout");
            System.out.print("Enter your choice: ");

            int choice = scanner.nextInt();
            scanner.nextLine();

            switch (choice) {
                case 1:
                    createPlaylist(user, scanner, allSongs);
                    break;
                case 2:
                    selectPlaylist(user, scanner);
                    break;
                case 3:
                    basicSearch(scanner);
                    break;
                case 4:
                    System.out.println("Logging out...");
                    return;
                default:
                    System.out.println("Invalid choice. Please enter a valid option.");
            }
        }
    }

    private static void createPlaylist(User user, Scanner scanner, ArrayList<Song> allSongs) {
        System.out.print("Enter the name of the new playlist: ");
        String playlistName = scanner.nextLine();
        Playlist newPlaylist = new Playlist(playlistName);
        user.addPlaylist(newPlaylist);
        System.out.println("Playlist created successfully!");
    }

    private static void selectPlaylist(User user, Scanner scanner) throws Exception {
        System.out.println("Your playlists:");
        user.displayPlaylists();
        System.out.print("Enter the name of the playlist: ");
        String playlistName = scanner.nextLine();

        Playlist selectedPlaylist = user.getPlaylist(playlistName);
        if (selectedPlaylist != null) {
           
            performPlaylistOperations(user, selectedPlaylist, scanner);
        } else {
            System.out.println("Playlist not found.");
        }
    }
    private static void basicSearch(Scanner scanner) throws Exception{
            boolean flag = true;
         while(flag){
        System.out.println("");
        System.out.println("");
        System.out.println("1) Search by Artist");
        System.out.println("2) Search by Song");
        System.out.println("3) Search by Genre");
        System.out.println("4) List All");
        System.out.println("5) Return to playlist selection");
        System.out.println("");
        System.out.print("Enter your choice: ");
        int ch = scanner.nextInt();
       
        scanner.nextLine();
     
        
        switch(ch){
            case 1:
                searchByArtist(mega, scanner);
                break;
            case 2:
                searchBySong(mega, scanner);
                break;
            case 3:
                searchByGenre(mega, scanner);
                break;
            case 4:
                displaySongs(allSongs);
                break;
            case 5:
                return;
            default:
                System.out.println("Incorrect option");
            
        }
    }
    }

    private static void performPlaylistOperations(User user, Playlist playlist, Scanner scanner) throws Exception {
        while (true) {
            System.out.println("");
            System.out.println("1. Add song to playlist");
            System.out.println("2. Delete song from playlist");
            System.out.println("3. Play playlist");
            System.out.println("4. Search by artist");
            System.out.println("5. Search by song");
            System.out.println("6. Search by genre");
            System.out.println("7. List All");
            System.out.println("8. Return to playlist selection");
            System.out.print("Enter your choice: ");
            
            int choice = scanner.nextInt();
            scanner.nextLine();  
            Song song = null;
            switch (choice) {
                case 1:
                    addSongToPlaylist(playlist, song);
                    break;
                case 2:
                    deleteSongFromPlaylist(playlist, scanner);
                    
                    break;
                case 3:
                    boolean flag = true;
                    playlist.playPlaylist(playlist.songs.head);
                     while(flag){
                    
                    System.out.println("1) Pause");
                    System.out.println("2) Next");
                    System.out.println("3) Previous");
                    System.out.println("4) Back");
                   
                    int ch = scanner.nextInt();
                    switch(ch){
                        case 1:
                            System.out.println("Pausing");
                            System.out.println("1) Play");
                            int c = scanner.nextInt();
                            if(c==1){System.out.println("Playing " ); }else{System.out.println("Wrong input");}
                            break;
                        case 2:
                            playlist.nextSong();
                           
                         break;
                        case 3:
                            playlist.prevSong();
                            
                            break;
                            case 4:
                           flag=false;
                           break;
                        default:
                            System.out.println("Incorrect Input");
                    }}
                    break;
                case 4:
                    searchByArtist(playlist, scanner);
                    break;
                case 5:
                    searchBySong(playlist, scanner);
                    break;
                case 6:
                    searchByGenre(playlist, scanner);
                    break;  
                case 7:
                    
                    searchAll(playlist, scanner);
                    break;
                case 8:
                    return; 
                default:
                    System.out.println("Invalid choice. Please enter a valid option.");
            }
        }
    }

    private static void addSongToPlaylist(Playlist playlist, Song song) throws Exception {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Available songs:");
        displaySongs(allSongs);
        System.out.print("Enter the number of the song to add to the playlist: ");
        int songNumber = scanner.nextInt();
        scanner.nextLine();  

        if (songNumber >= 1 && songNumber <= allSongs.Length()) {
            Song selectedSong = allSongs.get(songNumber - 1);
            playlist.addSong(selectedSong);
            System.out.println("Song added to the playlist!");
        } else {
            System.out.println("Invalid song number.");
        }
    }

    private static void deleteSongFromPlaylist(Playlist playlist, Scanner scanner) throws Exception {
        System.out.print("Enter the name of the song to delete: ");
        String songName = scanner.nextLine();

        ArrayList<Song> songsToDelete = playlist.searchBySong(songName);

        if (songsToDelete.isEmpty()) {
            System.out.println("Song not found in the playlist.");
        } else {
            
            System.out.println("Songs with the specified name:");
            for (int i = 0; i < songsToDelete.Length(); i++) {
                System.out.println((i + 1) + ". " + songsToDelete.get(i).getSongName());
            }

            System.out.print("Enter the number of the song to delete: ");
            int songNumber = scanner.nextInt();
            scanner.nextLine();

            if (songNumber >= 1 && songNumber <= songsToDelete.Length()) {
               
               playlist.deleteSong(songsToDelete.get(songNumber - 1));
                System.out.println("Song deleted from the playlist!");
            } else {
                System.out.println("Invalid song number.");
            }
        }
    }

    private static void searchByArtist(Playlist playlist, Scanner scanner) throws Exception {
        System.out.print("Enter artist name: ");
        String artistName = scanner.nextLine();

        ArrayList<Song> result = playlist.searchByArtist(artistName);

        if (result.isEmpty()) {
            System.out.println("No songs found by the specified artist.");
        } else {
            System.out.println("Songs by " + artistName + ":");
            displaySongs(result);
        }
    }

    private static void searchBySong(Playlist playlist, Scanner scanner) throws Exception {
        System.out.print("Enter song name: ");
        String songName = scanner.nextLine();

        ArrayList<Song> result = playlist.searchBySong(songName);

        if (result.isEmpty()) {
            System.out.println("No songs found with the specified name.");
        } else {
            System.out.println("Songs with the name " + songName + ":");
            displaySongs(result);
        }
    }

    private static void searchByGenre(Playlist playlist, Scanner scanner) throws Exception {
        System.out.print("Enter genre: ");
        String genre = scanner.nextLine();

        ArrayList<Song> result = playlist.searchByGenre(genre);

        if (result.isEmpty()) {
            System.out.println("No songs found in the specified genre.");
        } else {
            System.out.println("Songs in the genre " + genre + ":");
            displaySongs(result);
        }
    }
    
    public static void searchAll(Playlist playlist, Scanner scanner) throws Exception{
        System.out.println("All Songs in playlist: "+ playlist);
        ArrayList<Song> result = playlist.searchAll();

            displaySongs(result);
    }

    private static void displaySongs(ArrayList<Song> songs) throws Exception {
        for (int i = 0; i < songs.Length(); i++) {
            System.out.println((i + 1) + ". " + songs.get(i).getSongName() + " || " + songs.get(i).getArtistName() + " || "+ songs.get(i).getGenre());
        }
    }

    public static ArrayList<Song> loadSongsFromFile(String fileName) {
    ArrayList<Song> songs = new ArrayList<>();
    
    try (BufferedReader br = new BufferedReader(new FileReader(fileName))) {
    String line;
    while ((line = br.readLine()) != null) {
    String[] parts = line.split(", ");
    if (parts.length == 3) {
    String songName = parts[0];
    String artistName = parts[1];
    String genre = parts[2];
    songs.add(new Song(songName, artistName, genre));
    }
    }
    } catch (IOException e) {
    e.printStackTrace();
    }
    
    return songs;
    }
}
