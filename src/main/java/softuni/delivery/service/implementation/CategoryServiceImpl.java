package softuni.delivery.service.implementation;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import softuni.delivery.exceptions.ActiveOrderException;
import softuni.delivery.model.entity.Category;
import softuni.delivery.model.entity.Product;
import softuni.delivery.model.entity.Restaurant;
import softuni.delivery.model.service.CategoryServiceModel;
import softuni.delivery.repository.CategoryRepository;
import softuni.delivery.repository.OrderRepository;
import softuni.delivery.repository.ProductRepository;
import softuni.delivery.repository.RestaurantRepository;
import softuni.delivery.service.CategoryService;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository categoryRepository;
    private final ModelMapper modelMapper;
    private final RestaurantRepository restaurantRepository;
    private final ProductRepository productRepository;
    private final OrderRepository orderRepository;

    public CategoryServiceImpl(CategoryRepository categoryRepository, ModelMapper modelMapper, RestaurantRepository restaurantRepository, ProductRepository productRepository, OrderRepository orderRepository) {
        this.categoryRepository = categoryRepository;
        this.modelMapper = modelMapper;
        this.restaurantRepository = restaurantRepository;
        this.productRepository = productRepository;
        this.orderRepository = orderRepository;
    }

    @Override
    public List<CategoryServiceModel> findCategoriesByRestaurantId(String id) {

        return this.categoryRepository.findAllByRestaurant(this.restaurantRepository
        .findById(id).orElse(null))
                .stream()
                .map(category -> {
                    CategoryServiceModel categoryServiceModel =
                            this.modelMapper
                                    .map(category, CategoryServiceModel.class);
                    return categoryServiceModel;
                }).collect(Collectors.toList());
    }

    @Override
    public void addCategory(CategoryServiceModel categoryServiceModel) {

        Category category = this.modelMapper
                .map(categoryServiceModel, Category.class);
        this.categoryRepository
                .saveAndFlush(category);
    }

    @Override
    public CategoryServiceModel findByNameAndRestaurantId(String category, String id) {

        Restaurant restaurant = this.restaurantRepository
                .findById(id).orElse(null);

           return this.modelMapper
        .map(this.categoryRepository.findByNameAndRestaurant(category,
                    restaurant), CategoryServiceModel.class);

    }

    @Override
    public List<Category> findAllCategories() {

        return this.categoryRepository.findAll();
    }

    @Override
    public Category findById(String id) {

        return this.categoryRepository.findById(id)
                .orElse(null);
    }


    @Override
    public void deleteProductById(String id) {

        if(this.orderRepository
        .findAllByProducts(this.productRepository
        .findById(id).orElse(null)).size() > 0){
            throw new ActiveOrderException("This product is in active orders!");
        }
           this.productRepository
                   .findById(id)
                   .ifPresent(product -> {
                       this.categoryRepository
                               .findById(product.getCategory().getId())
                   .ifPresent(category -> {
                       category.getProducts().remove(product);
                   });
                   });
           this.productRepository.deleteById(id);
    }

}
