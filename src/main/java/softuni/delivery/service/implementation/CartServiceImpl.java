package softuni.delivery.service.implementation;

import org.modelmapper.ModelMapper;
import softuni.delivery.model.view.CartViewModel;
import softuni.delivery.model.view.ProductViewModel;
import softuni.delivery.service.CartService;
import softuni.delivery.service.ProductService;

import java.math.BigDecimal;
import java.util.List;


public class CartServiceImpl implements CartService {

    private final ProductService productService;
    private final ModelMapper modelMapper;

    public CartServiceImpl(ProductService productService, ModelMapper modelMapper) {
        this.productService = productService;
        this.modelMapper = modelMapper;
    }


    public BigDecimal findTotalPrice(List<ProductViewModel> products) {

        final BigDecimal[] totalPrice = {BigDecimal.ZERO};
        products.forEach(product -> {
            totalPrice[0] = totalPrice[0].add(product.getPrice().
                    multiply(BigDecimal.valueOf(product.getQuantity())));
        });

        return totalPrice[0];
    }

    @Override
    public ProductViewModel findByName(String name) {
        return this.modelMapper
        .map(this.productService
                .findByName(name), ProductViewModel.class);
    }

    @Override
    public void removeProduct(ProductViewModel product, CartViewModel cart) {

        List<ProductViewModel> products = cart.getProducts();
        int indexOfProduct = 0;
        for (int i = 0; i < products.size(); i++) {
            if(products.get(i).getName().equals(product.getName())){
                indexOfProduct = i;
            }
        }

        products.remove(indexOfProduct);
        cart.setProducts(products);
    }

    @Override
    public void addProductToCart(ProductViewModel productViewModel, CartViewModel cartViewModel) {

    }
}
