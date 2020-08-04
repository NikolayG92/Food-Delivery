package softuni.delivery.model.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.List;
import java.util.Set;

@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "towns")
public class Town extends BaseEntity{

    @Column(name = "name", nullable = false, unique = true)
    private String name;
    @Column(name = "image_url")
    private String imageUrl;
    @OneToMany(mappedBy = "town",  cascade = CascadeType.ALL,
    orphanRemoval = true,
    fetch = FetchType.EAGER)
    private List<Restaurant> restaurants;

}
