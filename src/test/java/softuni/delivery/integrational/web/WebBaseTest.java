package softuni.delivery.integrational.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import softuni.delivery.DeliveryApplication;
import softuni.delivery.integrational.BaseTest;

@AutoConfigureMockMvc
@SpringBootTest
@ContextConfiguration(classes = DeliveryApplication.class)
public class WebBaseTest extends BaseTest {

    @Autowired
    protected
    MockMvc mockMvc;
}
