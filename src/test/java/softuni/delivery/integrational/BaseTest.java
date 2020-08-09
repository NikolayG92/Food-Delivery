package softuni.delivery.integrational;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;
import softuni.delivery.DeliveryApplication;

@SpringBootTest(classes = DeliveryApplication.class)
@ExtendWith(MockitoExtension.class)
public class BaseTest {

    @BeforeEach
    public void setupTest(){
        MockitoAnnotations.initMocks(this);
        this.beforeEach();
    }

    protected void beforeEach(){

    }
}
