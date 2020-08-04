package softuni.delivery.model.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "addresses")
public class Address extends BaseEntity{

    @Column(name = "street", nullable = false)
    private String street;
    @Column(name = "number", nullable = false)
    private int number;
    @Column(name = "entrance")
    private String entrance;
    @Column(name = "floor")
    private int floor;
    @Column(name = "apartment_number")
    private int apartmentNumber;
    @ManyToOne
    private User user;
    @ManyToOne
    private Town town;
  /*  @OneToMany(mappedBy = "address",  cascade = CascadeType.ALL,
            orphanRemoval = true,
            fetch = FetchType.EAGER)
    private List<Order> orders;*/
}
