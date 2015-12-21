package utils;

import entities.Cover;
import entities.Song;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.Query;
import java.util.Collection;

public class CoverService {
    protected EntityManager entityManager;

    public CoverService(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    public Cover createCover(String title, String type, Integer length, Double rating, Song song) {
        Cover cover = new Cover();
        cover.setTitle(title);
        cover.setType(type);
        cover.setLength(length);
        cover.setRating(rating);
        cover.setSong(song);

        EntityTransaction transaction = entityManager.getTransaction();
        try {
            transaction.begin();
            entityManager.persist(cover);
            transaction.commit();
        } catch (Exception e) {
            if (transaction.isActive()) {
                transaction.rollback();
            }
        }
        return cover;
    }

    public void removeCover(Long id) {
        Cover cover = findCover(id);
        EntityTransaction transaction = entityManager.getTransaction();
        try {
            transaction.begin();
            if (cover != null) {
                entityManager.remove(cover);
            }
            transaction.commit();
        } catch (Exception e) {
            if (transaction.isActive()) {
                transaction.rollback();
            }
        }
    }

    public Cover updateCover(Long id, String newTitle, String newType, Integer newLength, Double newRating) {
        Cover cover = entityManager.find(Cover.class, id);
        EntityTransaction transaction = entityManager.getTransaction();
        try {
            transaction.begin();
            if (cover != null) {
                cover.setTitle(newTitle);
                cover.setType(newType);
                cover.setLength(newLength);
                cover.setRating(newRating);
            }
            transaction.commit();
        } catch (Exception e) {
            if (transaction.isActive()) {
                transaction.rollback();
            }
        }
        return cover;
    }

    public Cover findCover(Long id) {
        Cover cover;
        EntityTransaction transaction = entityManager.getTransaction();
        try {
            transaction.begin();
            cover = entityManager.find(Cover.class, id);
            transaction.commit();
            return cover;
        } catch (Exception e) {
            if (transaction.isActive()) {
                transaction.rollback();
            }
        }
        return null;
    }

    public Collection<Cover> findAllCovers() {
        Query query = entityManager.createQuery("SELECT e FROM Cover e");
        return (Collection<Cover>) query.getResultList();
    }
}
