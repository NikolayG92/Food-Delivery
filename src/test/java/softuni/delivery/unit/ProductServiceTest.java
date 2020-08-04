package softuni.delivery.unit;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import softuni.delivery.BaseTest;
import softuni.delivery.exceptions.UserNotFoundException;
import softuni.delivery.model.entity.Category;
import softuni.delivery.model.entity.Product;
import softuni.delivery.model.entity.User;
import softuni.delivery.model.service.ProductServiceModel;
import softuni.delivery.model.service.UserServiceModel;
import softuni.delivery.model.view.ProductViewModel;
import softuni.delivery.repository.CategoryRepository;
import softuni.delivery.repository.ProductRepository;
import softuni.delivery.service.ProductService;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;

public class ProductServiceTest extends BaseTest {

    @MockBean
    ProductRepository productRepository;

    @MockBean
    CategoryRepository categoryRepository;

    @Autowired
    ProductService productService;

    @Test
    public void findByName_whenProductExists_shouldReturnCorrectProduct(){
        Product product = new Product();
        product.setName("Pizza");
        when(productRepository.findByName("Pizza"))
                .thenReturn(product);


        ProductServiceModel productServiceModel = productService.findByName("Pizza");


        Assertions.assertEquals("Pizza", productServiceModel.getName());
    }

    @Test
    public void findById_whenProductExists_shouldReturnCorrectProduct(){
        Product product = new Product();
        product.setName("Pizza");
        product.setId("1");

        when(productRepository.findById("1"))
                .thenReturn(Optional.of(product));


        ProductServiceModel productServiceModel = productService.getProductById("1");

        Assertions.assertEquals(product.getId(), productServiceModel.getId());
    }

/*
    @Test
    public void findByCategoryId_whenCategoryExists_shouldReturnAllProducts(){
        Category category = new Category();
        category.setName("Pizza");
        category.setId("1");

        Product product1 = new Product();
        product1.setName("Margarita");
        product1.setCategory(category);
        product1.setId("1");
        Product product2 = new Product();
        product2.setName("Quattro Formaggi");
        product2.setCategory(category);
        product2.setId("2");

        when(categoryRepository.findById("1"))
                .thenReturn(Optional.of(category));


        assertEquals(2,  productService.findAllByCategoryId("1").size());
    }
*/

}
