package softuni.delivery.service.implementation;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import softuni.delivery.model.binding.order.AddProductToCartBindingModel;
import softuni.delivery.model.entity.Product;
import softuni.delivery.model.service.ProductServiceModel;
import softuni.delivery.model.view.ProductViewModel;
import softuni.delivery.model.view.RestaurantViewModel;
import softuni.delivery.repository.ProductRepository;
import softuni.delivery.service.CategoryService;
import softuni.delivery.service.ProductService;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProductServiceImpl implements ProductService {
    private final ProductRepository productRepository;
    private final ModelMapper modelMapper;
    private final CategoryService categoryService;

    public ProductServiceImpl(ProductRepository productRepository, ModelMapper modelMapper, CategoryService categoryService) {
        this.productRepository = productRepository;
        this.modelMapper = modelMapper;
        this.categoryService = categoryService;
    }

    @Override
    public void addProduct(ProductServiceModel productServiceModel) {
        Product product = this.modelMapper
                .map(productServiceModel, Product.class);

        if(product.getImageUrl() == null){
            product.setImageUrl("/img/no-image-available.jpg");
        }
        this.productRepository.saveAndFlush(product);
    }

    @Override
    public List<ProductViewModel> findAllByCategoryId(String id) {

        List<ProductViewModel> products =  this.categoryService.findById(id).getProducts()
                .stream()
                .map(product -> {
                    ProductViewModel productViewModel =
                            this.modelMapper
                                    .map(product, ProductViewModel.class);

                    if(product.getDescription().length() > 0){
                        productViewModel.setDescription(String.format
                                ("/%s/", product.getDescription()));
                    }else{
                        productViewModel.setDescription(null);
                    }

                    return productViewModel;
                }).collect(Collectors.toList());

        Collections.sort(products, Comparator.comparing(ProductViewModel::getPrice));

        return products;
    }

    @Override
    public ProductServiceModel getProductById(String id) {

        return this.modelMapper
                .map(this.productRepository.findById(id).orElse(null), ProductServiceModel.class);
    }

    @Override
    public ProductServiceModel findByName(String name) {

        return this.modelMapper
        .map(this.productRepository
                .findByName(name), ProductServiceModel.class);
    }




}
