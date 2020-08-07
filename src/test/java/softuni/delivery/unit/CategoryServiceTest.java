package softuni.delivery.unit;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import softuni.delivery.BaseTest;
import softuni.delivery.model.entity.*;
import softuni.delivery.model.service.CategoryServiceModel;
import softuni.delivery.model.view.ProductViewModel;
import softuni.delivery.repository.CategoryRepository;
import softuni.delivery.repository.ProductRepository;
import softuni.delivery.repository.RestaurantRepository;
import softuni.delivery.service.CategoryService;
import softuni.delivery.service.ProductService;

import java.util.*;


import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class CategoryServiceTest extends BaseTest {

    @MockBean
    CategoryRepository categoryRepository;

    @Autowired
    CategoryService categoryService;

    @Autowired
    ModelMapper modelMapper;

    @MockBean
    RestaurantRepository restaurantRepository;

    @MockBean
    ProductRepository productRepository;

    @Autowired
    ProductService productService;


    @Test
    public void findAllCategories_ifNotEmptyShouldReturnCollection(){

        List<Category> categories = new ArrayList<>();

        categories.add(new Category());
        categories.add(new Category());

        when(categoryRepository.findAll())
                .thenReturn(categories);

        List<Category> allCategories = categoryService.findAllCategories();

        assertEquals(2, allCategories.size());
    }

    @Test
    public void findById_whenCategoryExists_shouldReturnIt(){
        Category category = new Category();
        category.setId("1");

        when(categoryRepository.findById("1"))
                .thenReturn(Optional.of(category));
        Category newCategory = categoryService.findById("1");
        assertEquals(category.getId(), newCategory.getId());
    }

    @Test
    public void addCategory_shouldAddCategory(){
        CategoryServiceModel category = new CategoryServiceModel();
        category.setName("categoryName");
        category.setId("1");

        when(categoryRepository.findById("1"))
                .thenReturn(Optional.empty());

        categoryService.addCategory(category);
        verify(categoryRepository)
                .saveAndFlush(any());

    }

    @Test
    public void findCategoriesByRestaurantId_shouldReturnAllCategoriesByRestaurant(){
        Restaurant restaurant = new Restaurant();
        restaurant.setId("1");

        restaurant.setCategory(new HashSet<>());
        restaurant.getCategory().add(new Category());
        restaurant.getCategory().add(new Category());
        Set<Category> categories = restaurant.getCategory();
        when(categoryRepository.findAllByRestaurant(restaurant))
                .thenReturn(categories);
        when(restaurantRepository.findById("1"))
                .thenReturn(Optional.of(restaurant));

        List<CategoryServiceModel> categoryList = categoryService.findCategoriesByRestaurantId("1");

        assertEquals(2, categoryList.size());
    }


}
