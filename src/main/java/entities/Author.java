package entities;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Author {
    private Long id;
    private String firstName;
    private String lastName;
    private Integer age;
    private String genre;

    private List<Cover> coverList = new ArrayList<>();

    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public String getGenre() {
        return genre;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }

    @ManyToMany(mappedBy="authorList", cascade = CascadeType.REMOVE)
    public List<Cover> getCoverList() {
        return coverList;
    }

    public void setCoverList(List<Cover> coverList) {
        this.coverList = coverList;
    }

}
