package music;


public class User {
    private String username;
    private String password;
    private DoublyLinkedList<Song> playlist;
    private ArrayList<Playlist> playlists;

    public ArrayList<Playlist> getPlaylists() {
        return playlists;
    }
    
    public User(String username, String password) {
        this.username = username;
        this.password = password;
        playlist = new DoublyLinkedList();
        playlists = new ArrayList<>();
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

  
      public boolean authenticate(String passwordAttempt) {
        return this.password.equals(passwordAttempt);
    }
        public void addPlaylist(Playlist playlist) {
        playlists.add(playlist);
    }

   public void displayPlaylists() throws Exception {
        for (int i = 0; i < playlists.Length(); i++) {
            System.out.println((i + 1) + ". " + playlists.get(i).toString());
        }
    }

    public Playlist getPlaylist(String playlistName) throws Exception {
        for (int i = 0; i < playlists.Length(); i++) {
            if (playlists.get(i).toString().equalsIgnoreCase(playlistName)) {
                return playlists.get(i);
            }
        }
        return null;
    }
    
    
    
}
