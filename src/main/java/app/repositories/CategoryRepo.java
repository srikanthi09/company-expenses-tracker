package app.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import app.entities.Category;

public interface CategoryRepo extends JpaRepository<Category, String> {

	List<Category> findByCategoryCode(String categoryCode);

	Category findByCategoryName(String categoryName);

	void deleteByCategoryCode(String categoryCode);
}
