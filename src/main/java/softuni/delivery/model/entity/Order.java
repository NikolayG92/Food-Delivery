package softuni.delivery.model.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import javax.validation.constraints.DecimalMin;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Set;

@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "orders")
public class Order extends BaseEntity{

    @ManyToMany(cascade = CascadeType.MERGE,
    fetch = FetchType.EAGER)
    private Set<Product> products;
    @Column(name = "in_pending", nullable = false)
    private boolean inPending;
    @Column(name = "accepted", nullable = false)
    private boolean accepted;
    @Column(name = "ordered_on", nullable = false)
    @DateTimeFormat(pattern = "yyyy-MM-dd'T'hh:MM:ss")
    private LocalDateTime orderedOn;
    @ManyToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private User user;
    @ManyToOne
    @JoinColumn(name = "address_id", referencedColumnName = "id")
    private Address address;
    @Column(name = "total_price")
    private BigDecimal totalPrice;
    @ManyToOne
    @JoinColumn(name = "restaurant_id", referencedColumnName = "id")
    private Restaurant restaurant;
}
