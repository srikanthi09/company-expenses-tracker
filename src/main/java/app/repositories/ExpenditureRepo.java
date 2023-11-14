package app.repositories;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import app.dto.CategoryDto;
import app.dto.ExpenditureDto;
import app.entities.Expenditure;

public interface ExpenditureRepo extends JpaRepository<Expenditure, Integer> {

	// 2
	void deleteAllBypaymentMode(String paymentCode);

	// 3
	void deleteAllByDepartmentCode(String departmentCode);

	// 4
	void deleteAllByCategoryCode(String categoryCode);

	// 5
	List<Expenditure> findByCategoryCode(String code, PageRequest pageRequest);

	// 6
	List<Expenditure> findByPaymentMode(String code, PageRequest pageRequest);

	// 8
	@Query("select e.categoryCode as categoryCode,sum(e.amount) as totalAmount from Expenditure e where month(e.expenditureDate)=:month group by e.categoryCode")
	List<ExpenditureDto> findAmountByMonth(@Param("month") int month);

	// 9
	@Query("FROM Expenditure e WHERE e.departmentCode = :departmentCode AND (e.expenditureDate BETWEEN :startDate AND :endDate)")
	List<Expenditure> findMaxExpenseDateAndDepartmentCodeBetweenTwoDates(@Param("departmentCode") String departmentCode,
			@Param("startDate") LocalDate startDate, @Param("endDate") LocalDate endDate);

	// 10
	List<Expenditure> findAllByauthorizedBy(String authorizedBy);

	// 11
	List<Expenditure> findAllExpensesByDescriptionContaining(String description);

	// 12
	@Query("from Expenditure where amount between :minprice and :maxprice")
	List<Expenditure> findByAmount(@Param("minprice") int minprice, @Param("maxprice") int maxprice);

	// 14
	@Query("select e.department.departmentName as departmentName,e.departmentCode as departmentCode, sum(e.amount) as totalAmount from Expenditure e group by e.department.departmentName,e.departmentCode")
	List<ExpenditureDto> groupByDepartmentCode();

	// 15
	@Query("select e.category.categoryName as categoryName,e.categoryCode as categoryCode, sum(e.amount) as totalAmount from Expenditure e group by e.category.categoryName,e.categoryCode")
	List<CategoryDto> groupByCategory();
}
