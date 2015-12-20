package utils;

import entities.Song;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
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

        EntityTransaction transaction = entityManager.getTransaction();
        try {
            transaction.begin();
            entityManager.persist(song);
            transaction.commit();
        } catch (Exception e) {
            if (transaction.isActive()) {
                transaction.rollback();
            }
        }
        return song;
    }

    public void removeSong(Long id) {
        Song song = findSong(id);
        EntityTransaction transaction = entityManager.getTransaction();
        try {
            transaction.begin();
            if (song != null) {
                entityManager.remove(song);
            }
            transaction.commit();
        } catch (Exception e) {
            if (transaction.isActive()) {
                transaction.rollback();
            }
        }
    }

    public Song updateSong(Long id,  String newTitle, String newType, Integer newLength, Double newRating) {
        Song song = entityManager.find(Song.class, id);
        EntityTransaction transaction = entityManager.getTransaction();
        try {
            transaction.begin();
            if (song != null) {
                song.setTitle(newTitle);
                song.setType(newType);
                song.setLength(newLength);
                song.setRating(newRating);
            }
            transaction.commit();
        } catch (Exception e) {
            if (transaction.isActive()) {
                transaction.rollback();
            }
        }
        return song;
    }

    public Song findSong(Long id) {
        Song s;
        EntityTransaction transaction = entityManager.getTransaction();
        try {
            transaction.begin();
            s = entityManager.find(Song.class, id);
            transaction.commit();
            return s;
        } catch (Exception e) {
            if (transaction.isActive()) {
                transaction.rollback();
            }
        }
        return null;
    }

    public Collection<Song> findAllSongs() {
        Query query = entityManager.createQuery("SELECT e FROM Song e");
        return (Collection<Song>) query.getResultList();
    }
}
