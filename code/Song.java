package music;


public class Song implements Comparable<Song> {
    private String songName;
    private String artistName;
    private String genre;

    public Song(String songName, String artistName, String genre) {
        this.songName = songName;
        this.artistName = artistName;
        this.genre = genre;
    }

    // Getters and setters

    public String getSongName() {
        return songName;
    }

    public String getArtistName() {
        return artistName;
    }

    public String getGenre() {
        return genre;
    }

    @Override
    public int compareTo(Song other) {
        return this.songName.compareTo(other.songName);
    }
}
