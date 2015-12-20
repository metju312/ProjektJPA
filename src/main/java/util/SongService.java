package util;

import entities.Song;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.util.Collection;

public class SongService {
    protected EntityManager entityManager;

    public SongService(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    public Song createSong( String title, String type, Integer length, Double rating) {
        Song song = new Song();
        song.setTitle(title);
        song.setType(type);
        song.setLength(length);
        song.setRating(rating);
        entityManager.persist(song);
        return song;
    }

    public void removeSong(Long id) {
        Song song = findSong(id);
        if (song != null) {
            entityManager.remove(song);
        }
    }

    public Song updateSong(Long id, long raise) {
        Song song = entityManager.find(Song.class, id);
        if (song != null) {
            song.setRating(song.getRating() + raise);
        }
        return song;
    }

    public Song findSong(Long id) {
        return entityManager.find(Song.class, id);
    }

    public Collection<Song> findAllSongs() {
        Query query = entityManager.createQuery("SELECT e FROM Song e");
        return (Collection<Song>) query.getResultList();
    }
}
