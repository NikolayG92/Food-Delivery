package softuni.delivery.service;

import softuni.delivery.model.entity.Category;
import softuni.delivery.model.entity.Restaurant;
import softuni.delivery.model.service.CategoryServiceModel;

import java.util.List;

public interface CategoryService {

    List<CategoryServiceModel> findCategoriesByRestaurantId(String id);

    void addCategory(CategoryServiceModel category);

    CategoryServiceModel findByNameAndRestaurantId(String category, String id);

    List<Category> findAllCategories();

    Category findById(String id);


    void deleteProductById(String id);
}
