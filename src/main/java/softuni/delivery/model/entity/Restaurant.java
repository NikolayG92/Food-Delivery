package softuni.delivery.model.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.Set;

@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "restaurants")
public class Restaurant extends BaseEntity{

    @Column(name = "name", nullable = false, unique = true)
    private String name;
    @Column(name = "description", columnDefinition = "TEXT")
    private String description;
    @Column(name = "image_url")
    private String imageUrl;
    @ManyToOne(optional = false)
    @JoinColumn(name = "town_id", referencedColumnName = "id")
    private Town town;
    @OneToMany(mappedBy = "restaurant",  cascade = CascadeType.ALL,
            orphanRemoval = true,
            fetch = FetchType.EAGER)
    private Set<Category> category;
}
