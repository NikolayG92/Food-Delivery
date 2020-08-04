package softuni.delivery.model.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.List;

@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "categories")
public class Category extends BaseEntity{

    @Column(name = "name", nullable = false)
    private String name;
    @Column(name = "image_url")
    private String imageUrl;
    @OneToMany(mappedBy = "category",  cascade = CascadeType.MERGE,
            orphanRemoval = true,
            fetch = FetchType.EAGER)
    private List<Product> products;
    @ManyToOne
    @JoinColumn(name = "restaurant_id", referencedColumnName = "id")
    private Restaurant restaurant;

}
