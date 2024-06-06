package music;

public class Playlist implements Comparable<Playlist> {
    public DoublyLinkedList<Song> songs;
    private ArrayList<BST<Song>> artistBSTs;
    private ArrayList<BST<Song>> genreBSTs;
    private ArrayList<BST<Song>> songBSTs;
    private String playlistName;
    Node<Song> curr;
    public Playlist() {
        this.songs = new DoublyLinkedList<>();
        genreBSTs = new ArrayList<>();
        artistBSTs = new ArrayList<>();
        songBSTs = new ArrayList<>();
        
    }
    public Playlist(String name) {
    this.playlistName = name;
    this.songs = new DoublyLinkedList<>();
    this.genreBSTs = new ArrayList<>();
    artistBSTs = new ArrayList<>();
    songBSTs = new ArrayList<>();
}
    public boolean containsSong(Song song) {
        return songs.contains(song);
    }

public void checkPlayList(){
    System.out.println("checker" + songs.head.getData().getSongName());
    curr = songs.head;
    System.out.println("Current " + curr.getData().getSongName());
}

    public void addSong(Song song) throws Exception {
        songs.InsertInOrder(song);
        curr= songs.head;
     BST<Song> artistBST= findOrCreateArtistBST(song.getArtistName());
     artistBST.insert(song);
     BST<Song> songBST = findOrCreateSongBST(song.getSongName());
     songBST.insert(song);
     BST<Song> genreBST = findOrCreateGenreBST(song.getGenre());
        genreBST.insert(song);
    }
    
    private BST<Song> findOrCreateArtistBST(String artist) throws Exception{
        for(int i =0; i < artistBSTs.Length(); i++){
            BST<Song> bst = artistBSTs.get(i);
            if(bst.getRoot()!=null && bst.getRoot().data.getArtistName().equals(artist)){
                return bst;
            }
        }
        BST<Song> newArtistBST = new BST<>();
        artistBSTs.add(newArtistBST);
        return newArtistBST;
    }
    
    private BST<Song> findOrCreateSongBST(String songname) throws Exception{
        for(int i =0; i < songBSTs.Length(); i++){
            BST<Song> bst = songBSTs.get(i);
            if(bst.getRoot()!=null && bst.getRoot().data.getSongName().equals(songname)){
                return bst;
            }
        }
        BST<Song> newSongBST = new BST<>();
        songBSTs.add(newSongBST);
        return newSongBST;
    }
    
    private BST<Song> findOrCreateGenreBST(String genre) throws Exception {
        for (int i = 0; i < genreBSTs.Length(); i++) {
            BST<Song> bst = genreBSTs.get(i);
            if (bst.getRoot() != null && bst.getRoot().data.getGenre().equals(genre)) {
                return bst;
            }
        }

       
        BST<Song> newGenreBST = new BST<>();
        genreBSTs.add(newGenreBST);
        return newGenreBST;
    }

    public void deleteSong(Song song) throws Exception {
        songs.Delete(song);
       
    BST<Song> artistBST = findArtistBST(song.getArtistName());
    if (artistBST != null) {
        artistBST.delete(song);
    }

  
    BST<Song> songBST = findSongBST(song.getSongName());
    if (songBST != null) {
        songBST.delete(song);
    }

    BST<Song> genreBST = findGenreBST(song.getGenre());
    if (genreBST != null) {
        genreBST.delete(song);
    }
    }

    public void playPlaylist(Node<Song> current) {
        
        if(current!=null){
        System.out.println("Playing Playlist:");
        System.out.println(current.getData().getSongName()+ " by " + current.getData().getArtistName());
    }
        else {
            System.out.println("Playlist is empty");
        }
    }
    
    public void nextSong() {
        if (songs.isEmpty()) {
            System.out.println("Playlist is empty");
            return;
        }

        if (curr.next == null) {
           
            curr = songs.head;
        } else {
            curr = curr.next;
        }

        playPlaylist(curr);
    }

    public void prevSong() {
        if (songs.isEmpty()) {
            System.out.println("Playlist is empty");
            return;
        }

        if (curr.prev == null) {
            
            Node<Song> lastNode = songs.head;
            while (lastNode.next != null) {
                lastNode = lastNode.next;
            }
            curr = lastNode;
        } else {
            curr = curr.prev;
        }

        playPlaylist(curr);
    }
    public DoublyLinkedList<Song> getSongs() {
        return songs;
    }

    public ArrayList<Song> searchByArtist(String artist) throws Exception {
        
        ArrayList<Song> result = new ArrayList<>();
        
        BST<Song> artistBST = findArtistBST(artist);

        if(artistBST!=null){
            result = artistBST.inOrderTraversal();
        }
        return result;
    }

    public ArrayList<Song> searchBySong(String songName) throws Exception {
        ArrayList<Song> result = new ArrayList<>();
        BST<Song> songBST = findSongBST(songName);
        
        if(songBST!=null){
            result = songBST.inOrderTraversal();
        }
        return result;
    }

     public ArrayList<Song> searchByGenre(String genre) throws Exception {
        ArrayList<Song> result = new ArrayList<>();

        BST<Song> genreBST = findGenreBST(genre);
        if (genreBST != null) {
            result = genreBST.inOrderTraversal();
        }

        return result;
    }
     public ArrayList<Song> searchAll(){
         return songs.searchAll();
     }
     
     private BST<Song> findArtistBST(String artist) throws Exception{
         for(int i =0; i<artistBSTs.Length(); i++){
             BST<Song> bst = artistBSTs.get(i);
             if(bst.getRoot()!=null && bst.getRoot().data.getArtistName().equalsIgnoreCase(artist)){
                 return bst;
             }
         }
         return null;
     }
     
     private BST<Song> findSongBST(String songName) throws Exception{
         for(int i=0; i<songBSTs.Length(); i++){
             BST<Song> bst = songBSTs.get(i);
             if(bst.getRoot()!=null && bst.getRoot().data.getSongName().equalsIgnoreCase(songName)){
                 return bst;
             }
         }
         return null;
     }

   private BST<Song> findGenreBST(String genre) throws Exception {
        for (int i = 0; i < genreBSTs.Length(); i++) {
            BST<Song> bst = genreBSTs.get(i);
            if (bst.getRoot() != null && bst.getRoot().data.getGenre().equalsIgnoreCase(genre)) {
                return bst;
            }
        }
        return null;
    }
   
    public String getPlaylistName() {
        return playlistName;
    }

    @Override
    public int compareTo(Playlist other) {
        return this.playlistName.compareTo(other.playlistName);
    }

    @Override
    public String toString(){
        return playlistName;
    }
     public static Playlist fromString(String playlistString) {
        return new Playlist(playlistString);
    }
}
   
