package softuni.delivery.unit;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import softuni.delivery.service.CartService;
import softuni.delivery.service.ProductService;

import javax.servlet.http.HttpSession;

import static org.junit.Assert.assertEquals;

public class CartServiceTest {

    private final HttpSession httpSession;
    @MockBean
    ProductService productService;

    @Autowired
    CartService cartService;


    public CartServiceTest(HttpSession httpSession) {
        this.httpSession = httpSession;
    }

   /* @Test
    void */
}
