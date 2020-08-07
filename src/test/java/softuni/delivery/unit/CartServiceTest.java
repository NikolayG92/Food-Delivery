package softuni.delivery.unit;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import softuni.delivery.model.view.CartViewModel;
import softuni.delivery.model.view.ProductViewModel;
import softuni.delivery.service.CartService;
import softuni.delivery.service.ProductService;

import javax.servlet.http.HttpSession;

import java.math.BigDecimal;
import java.util.ArrayList;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

public class CartServiceTest {


    @MockBean
    ProductService productService;

    @Autowired
    CartService cartService;

    CartViewModel cart;

    @Before
    public void initCart(){
        cart = new CartViewModel();
        cart.setProducts(new ArrayList<>());
    }


    @Test
    public void findTotalPrice_shouldReturnCorrectPrice(){
        ProductViewModel productViewModel = new ProductViewModel();
        productViewModel.setTotalPrice(BigDecimal.valueOf(5));

        ProductViewModel productViewModel1 = new ProductViewModel();
        productViewModel1.setTotalPrice(BigDecimal.valueOf(10));

        cart.getProducts().add(productViewModel);
        cart.getProducts().add(productViewModel1);

        assertEquals(BigDecimal.valueOf(15), cartService.findTotalPrice(cart.getProducts()));
    }
}
