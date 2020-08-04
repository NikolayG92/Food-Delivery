package softuni.delivery.service;

import softuni.delivery.model.binding.order.AddProductToCartBindingModel;
import softuni.delivery.model.entity.Product;
import softuni.delivery.model.service.ProductServiceModel;
import softuni.delivery.model.view.ProductViewModel;

import java.util.List;

public interface ProductService {
    void addProduct(ProductServiceModel product);

    List<ProductViewModel> findAllByCategoryId(String id);

    ProductServiceModel getProductById(String id);

    ProductServiceModel findByName(String name);

}
