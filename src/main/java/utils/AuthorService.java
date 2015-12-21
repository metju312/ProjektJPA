package utils;

import entities.Author;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.Query;
import java.util.Collection;

public class AuthorService {
    protected EntityManager entityManager;

    public AuthorService(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    public Author createAuthor(String firstName, String lastName, Integer age, String genre) {
        Author author = new Author();
        author.setFirstName(firstName);
        author.setLastName(lastName);
        author.setAge(age);
        author.setGenre(genre);

        EntityTransaction transaction = entityManager.getTransaction();
        try {
            transaction.begin();
            entityManager.persist(author);
            transaction.commit();
        } catch (Exception e) {
            if (transaction.isActive()) {
                transaction.rollback();
            }
        }
        return author;
    }

    public void removeAuthor(Long id) {
        Author author = findAuthor(id);
        EntityTransaction transaction = entityManager.getTransaction();
        try {
            transaction.begin();
            if (author != null) {
                entityManager.remove(author);
            }
            transaction.commit();
        } catch (Exception e) {
            if (transaction.isActive()) {
                transaction.rollback();
            }
        }
    }

    public Author updateAuthor(Long id, String firstName, String lastName, Integer age, String genre) {
        Author author = entityManager.find(Author.class, id);
        EntityTransaction transaction = entityManager.getTransaction();
        try {
            transaction.begin();
            if (author != null) {
                author.setFirstName(firstName);
                author.setLastName(lastName);
                author.setAge(age);
                author.setGenre(genre);
            }
            transaction.commit();
        } catch (Exception e) {
            if (transaction.isActive()) {
                transaction.rollback();
            }
        }
        return author;
    }

    public Author findAuthor(Long id) {
        Author s;
        EntityTransaction transaction = entityManager.getTransaction();
        try {
            transaction.begin();
            s = entityManager.find(Author.class, id);
            transaction.commit();
            return s;
        } catch (Exception e) {
            if (transaction.isActive()) {
                transaction.rollback();
            }
        }
        return null;
    }

    public Collection<Author> findAllAuthors() {
        Query query = entityManager.createQuery("SELECT e FROM Author e");
        return (Collection<Author>) query.getResultList();
    }
}
