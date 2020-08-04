package softuni.delivery.model.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.aspectj.weaver.ast.Or;
import org.springframework.stereotype.Controller;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Set;

@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "products")
public class Product extends BaseEntity{

    @Column(name = "name", nullable = false, unique = true)
    private String name;
    @Column(name = "size", nullable = false)
    private int size;
    @Column(name = "description", columnDefinition = "TEXT")
    private String description;
    @Column(name = "price", nullable = false)
    private BigDecimal price;
    @Column(name = "image_url")
    private String imageUrl;
    @ManyToOne
    @JoinColumn(name = "category_id", referencedColumnName = "id")
    private Category category;


}
