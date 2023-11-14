package app.rest;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import app.dto.CategoryDto;
import app.dto.ExpenditureDto;
import app.entities.Category;
import app.entities.Department;
import app.entities.Expenditure;
import app.entities.PaymentMode;
import app.repositories.CategoryRepo;
import app.repositories.DepartmentRepo;
import app.repositories.ExpenditureRepo;
import app.repositories.PaymentModeRepo;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.validation.Valid;

@RestController
public class EntitesController {

	@Autowired
	ExpenditureRepo expRepo;

	@Autowired
	CategoryRepo categoryRepo;

	@Autowired
	PaymentModeRepo paymentmodeRepo;

	@Autowired
	DepartmentRepo departmentRepo;

	@GetMapping("/expenditures")
	public List<Expenditure> getAllProducts() {
		return expRepo.findAll();
	}

	// 1.1 Add expenditure
	@PostMapping("/addexpenditure")
	@Operation(description = "Add Expenditure", summary = "Add one expenditure")
	@ApiResponse(responseCode = "200", description = "Expenditure added successfully")
	@ApiResponse(responseCode = "400", description = "Bad request, Expenditure already exists")
	@ApiResponse(responseCode = "500", description = "Internal server error")
	@ApiResponse(responseCode = "401", description = "Unauthorized")
	public ResponseEntity<String> addExpenditure(@RequestBody Expenditure newExpenditure) {
		Optional<Expenditure> existingExpenditure = expRepo.findById(newExpenditure.getId());
		if (existingExpenditure.isPresent()) {
			return ResponseEntity.badRequest().body("Expenditure already exists.");
		}
		expRepo.save(newExpenditure);

		return ResponseEntity.ok("Expenditure added successfully!");
	}

	// 1.2 Delete one expenditure
	@DeleteMapping("/deleteexpenditure")
	@Operation(description = "delete expenditure", summary = "delete one expenditure")
	@ApiResponse(responseCode = "200", description = "paymentMode deleted successfully ")
	@ApiResponse(responseCode = "400", description = "Bad request, Already exist")
	@ApiResponse(responseCode = "500", description = "Internal server")
	@ApiResponse(responseCode = "401", description = "unauthorized")
	public String deleteExpenditure(@RequestParam Integer id) {
		Optional<Expenditure> optionalExpenditure = expRepo.findById(id);
		if (optionalExpenditure.isEmpty()) {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Id not found...!");
		}
		expRepo.deleteById(id);
		return "Deleted successfully...!";
	}

	// 1.3 Update one expenditure
	@PutMapping("/updateexpenditure/{id}")
	@Operation(description = "update expenditure", summary = "update one expenditure")
	@ApiResponse(responseCode = "200", description = "paymentMode deleted successfully ")
	@ApiResponse(responseCode = "400", description = "Bad request, Already exist")
	@ApiResponse(responseCode = "500", description = "Internal server")
	@ApiResponse(responseCode = "401", description = "unauthorized")
	public ResponseEntity<String> updateOneExpenditure(@PathVariable("id") int id,
			@RequestParam("paymentMode") String paymentMode) {
		Optional<Expenditure> optExpenditure = expRepo.findById(id);

		if (optExpenditure.isPresent()) {
			Expenditure expenditure = optExpenditure.get();
			expenditure.setPaymentMode(paymentMode);

			expRepo.save(expenditure);

			return ResponseEntity.ok("Expenditure updated successfully");
		} else {
			return ResponseEntity.badRequest().body("Invalid ID or Expenditure not found");
		}
	}

	// 2.1 Add payment Modes
	@PostMapping("/addpaymentmode")
	@Operation(description = "add paymentMode", summary = "Add one paymentMode")
	@ApiResponse(responseCode = "200", description = "paymentMode added successfully ")
	@ApiResponse(responseCode = "400", description = "Bad request, Already exist")
	@ApiResponse(responseCode = "500", description = "Internal server")
	@ApiResponse(responseCode = "401", description = "unauthorized")
	public String addPaymentMode(@RequestBody PaymentMode newPaymentMode) {
		PaymentMode exsistingPaymentmode = paymentmodeRepo.findByPaymentName(newPaymentMode.getPaymentName());
		if (exsistingPaymentmode != null)
			paymentmodeRepo.save(newPaymentMode);
		return "PaymentMode added successfully!";
	}

	// 2.2 Delete one payment Mode
	@Transactional
	@DeleteMapping("/deletepaymentmode/{paymentCode}")
	@Operation(description = "delete paymentMode", summary = "delete one paymentMode")
	@ApiResponse(responseCode = "200", description = "paymentMode deleted successfully ")
	@ApiResponse(responseCode = "400", description = "Bad request, Already exist")
	@ApiResponse(responseCode = "500", description = "Internal server")
	@ApiResponse(responseCode = "401", description = "unauthorized")
	public String deletePaymentMode(@PathVariable("paymentCode") String s) {
		paymentmodeRepo.findByPaymentCode(s);
		paymentmodeRepo.deleteById(s);

		return "Payment mode deleted successfully.";
	}

	// 2.3 Update one paymentMode
	@PutMapping("/updatepaymentmode/{paymentCode}")
	@Operation(description = "update paymentMode", summary = "updated one paymentMode")
	@ApiResponse(responseCode = "200", description = "paymentMode updated successfully ")
	@ApiResponse(responseCode = "400", description = "Bad request, Already exist")
	@ApiResponse(responseCode = "500", description = "Internal server")
	@ApiResponse(responseCode = "401", description = "unauthorized")
	public void updateOnePaymentmode(@PathVariable("paymentCode") String s,
			@RequestParam("paymentName") String paymentName) {
		PaymentMode optPaymentmode = paymentmodeRepo.findByPaymentCode(s);
//		if (optPaymentmode.isPresent()) {
//			PaymentMode p = optPaymentmode.get();
		if (paymentName != null) {
			optPaymentmode.setPaymentName(paymentName);
			paymentmodeRepo.save(optPaymentmode);
//			}
		}
	}

	// 3.1 Add new Department
	@PostMapping("/adddepartment")
	@Operation(description = "add department", summary = "Add one department")
	@ApiResponse(responseCode = "200", description = "department added successfully ")
	@ApiResponse(responseCode = "400", description = "Bad request, Already exist")
	@ApiResponse(responseCode = "500", description = "Internal server")
	@ApiResponse(responseCode = "401", description = "unauthorized")
	public String addDepartment(@Valid @RequestBody Department newDepartment) {
		Department exsistingDepartment = departmentRepo.findByDepartmentName(newDepartment.getDepartmentName());
		if (exsistingDepartment != null)
			departmentRepo.save(newDepartment);
		return "Department added successfully!";
	}

	// 3.2 Delete one department
	@Transactional
	@DeleteMapping("/deletedepartment/{departmentCode}")
	@Operation(description = "delete department", summary = "delete one department")
	@ApiResponse(responseCode = "200", description = "department deleted successfully ")
	@ApiResponse(responseCode = "400", description = "Bad request, Already exist")
	@ApiResponse(responseCode = "500", description = "Internal server")
	@ApiResponse(responseCode = "401", description = "unauthorized")
	public String deleteOneDepartment(@PathVariable("departmentCode") String departmentCode) {
		departmentRepo.findByDepartmentCode(departmentCode);
		departmentRepo.deleteById(departmentCode);
		return "department is successfully deleted";
	}

	// 3.3 Update one department
	@PutMapping("/updatedepartment/{departmentCode}")
	@Operation(description = "update department", summary = "update one department")
	@ApiResponse(responseCode = "200", description = "department updated successfully ")
	@ApiResponse(responseCode = "400", description = "Bad request, Already exist")
	@ApiResponse(responseCode = "500", description = "Internal server")
	@ApiResponse(responseCode = "401", description = "unauthorized")
	public void updateOneDepartment(@PathVariable("departmentCode") String d,
			@RequestParam("departmentName") String departmentName) {
		Optional<Department> optDepartment = departmentRepo.findById(d);
		if (optDepartment.isPresent()) {
			Department de = optDepartment.get();
			if (departmentName != null)
				de.setDepartmentName(departmentName);
			departmentRepo.save(de);
		}
	}

	// 4.1 Add new category
	@PostMapping("/categories")
	@Operation(description = "add category", summary = "Add one category")
	@ApiResponse(responseCode = "200", description = "category added successfully ")
	@ApiResponse(responseCode = "400", description = "Bad request, Already exist")
	@ApiResponse(responseCode = "500", description = "Internal server")
	@ApiResponse(responseCode = "401", description = "unauthorized")
	public String addNewExpense(@Valid @RequestBody Category newCategory) {
		categoryRepo.findByCategoryName(newCategory.getCategoryName());
		categoryRepo.save(newCategory);
		return "Category is added successfully!";
	}

	// 4.2 delete one category
	@DeleteMapping("/categories/{categoryCode}")
	@Operation(description = "delete category", summary = "delete category")
	@ApiResponse(responseCode = "200", description = "category deleted successfully ")
	@ApiResponse(responseCode = "400", description = "Bad request, Already exist")
	@ApiResponse(responseCode = "500", description = "Internal server")
	@ApiResponse(responseCode = "401", description = "unauthorized")
	@Transactional
	public String delOneCategory(@PathVariable("categoryCode") String categoryCode) {
		var optionalCategory = categoryRepo.findByCategoryCode(categoryCode);
		if (optionalCategory.isEmpty()) {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Category Code Not Found!");
		}
		categoryRepo.deleteByCategoryCode(categoryCode);
		return "Category is successfully deleted";
	}

	// 4.3 Update one category
	@PutMapping("/categories/{categoryCode}")
	@Operation(description = "update category", summary = "update one category")
	@ApiResponse(responseCode = "200", description = "category updated successfully ")
	@ApiResponse(responseCode = "400", description = "Bad request, Already exist")
	@ApiResponse(responseCode = "500", description = "Internal server")
	@ApiResponse(responseCode = "401", description = "unauthorized")
	public void updateOneCategory(@PathVariable("categoryCode") String categoryCode,
			@RequestParam("categoryName") String categoryName) {
		Optional<Category> optCategory = categoryRepo.findById(categoryCode);
		if (optCategory.isPresent()) {
			Category c = optCategory.get();

			if (categoryName != null) {
				c.setCategoryName(categoryName);
				categoryRepo.save(c);
			}
		}
	}

	// 5 Pagination by categoryCode and sort by id
	@GetMapping("/expenses/category/{categoryCode}")
	@Operation(description = "Pagination by categoryCode and sort by id", summary = "pagination by categoryCode")
	@ApiResponse(responseCode = "200", description = "paymentMode updated successfully ")
	@ApiResponse(responseCode = "400", description = "Bad request, Already exist")
	@ApiResponse(responseCode = "500", description = "Internal server")
	@ApiResponse(responseCode = "401", description = "unauthorized")
	public List<Expenditure> printPage(@PathVariable("categoryCode") String categoryCode) {
		var page = expRepo.findByCategoryCode(categoryCode, PageRequest.of(0, 5, Sort.by("Id").ascending()));
		List<Expenditure> list = new ArrayList<>();
		for (var p : page) {
			list.add(p);
		}
		return list;
	}

	// 6 Pagination by paymentMode and sort by id
	@GetMapping("/expenses/paymentmode/{code}")
	@Operation(description = "Pagination by paymentMode and sort by id", summary = "pagination by paymentMode")
	@ApiResponse(responseCode = "200", description = "paymentMode updated successfully ")
	@ApiResponse(responseCode = "400", description = "Bad request, Already exist")
	@ApiResponse(responseCode = "500", description = "Internal server")
	@ApiResponse(responseCode = "401", description = "unauthorized")
	public List<Expenditure> printPage1(@PathVariable("code") String code) {
		// Retrieve given page with 5 rows in the page
		var page = expRepo.findByPaymentMode(code, PageRequest.of(0, 5, Sort.by("id").ascending()));
		List<Expenditure> list = new ArrayList<>();
		for (var p : page) {
			list.add(p);
		}
		return list;
	}

	// 7 Pagination between 2 given dates and sorted by date in descending order
	@GetMapping("/pagination/expenses/{startDate}&{endDate}")
	@Operation(description = "Pagination by Start and End dates", summary = "pagination by Start and End dates ")
	@ApiResponse(responseCode = "200", description = "paymentMode updated successfully ")
	@ApiResponse(responseCode = "400", description = "Bad request, Already exist")
	@ApiResponse(responseCode = "500", description = "Internal server")
	@ApiResponse(responseCode = "401", description = "unauthorized")
	public ResponseEntity<?> getExpensesByStartAndEndDate1(@PathVariable("startDate") String startDateString,
			@PathVariable("endDate") String endDateString) {
		LocalDate startDate;
		LocalDate endDate;
		// Validate the startDate
		try {
			startDate = LocalDate.parse(startDateString);
		} catch (DateTimeParseException e) {
			return ResponseEntity.badRequest().body("Invalid startDate format. Please use yyyy-MM-dd.");
		}
		// Validate the endDate
		try {
			endDate = LocalDate.parse(endDateString);
		} catch (DateTimeParseException e) {
			return ResponseEntity.badRequest().body("Invalid endDate format. Please use yyyy-MM-dd.");
		}
		if (startDate.isAfter(endDate)) {
			return ResponseEntity.badRequest().body("Start date cannot be after end date.");
		}
		var page = expRepo.findAll(PageRequest.of(0, 5, Sort.by("expenditureDate").ascending()));
		List<Expenditure> list = new ArrayList<>();
		for (var p : page) {
			list.add(p);
		}
		return ResponseEntity.ok(list);
	}

	// 8 expenses summary(total amount) for each category in a given month
	@GetMapping("expenditurebymonth/expenditureDate{month}")
	@Operation(description = "expenses by totalamount for each category", summary = "expenses by total amount")
	@ApiResponse(responseCode = "200", description = "paymentMode updated successfully ")
	@ApiResponse(responseCode = "400", description = "Bad request, Already exist")
	@ApiResponse(responseCode = "500", description = "Internal server")
	@ApiResponse(responseCode = "401", description = "unauthorized")
	public List<ExpenditureDto> getExpendituresByExpenditureDate(@PathVariable("month") int month) {
		return expRepo.findAmountByMonth(month);
	}

	// 9 expenses of a department between given dates
	@GetMapping("/expenses/{departmentCode}/{startDate}&{endDate}")
	@Operation(description = "expenses of department b/w given dates", summary = "expenses of department b/w dates")
	@ApiResponse(responseCode = "200", description = "paymentMode updated successfully ")
	@ApiResponse(responseCode = "400", description = "Bad request, Already exist")
	@ApiResponse(responseCode = "500", description = "Internal server")
	@ApiResponse(responseCode = "401", description = "unauthorized")
	public List<Expenditure> getExpensesByStartAndEndDate(@PathVariable("departmentCode") String departmentCode,
			@PathVariable("startDate") String startdate, @PathVariable("endDate") String enddate) {
		LocalDate startDate;
		LocalDate endDate;
		List<Expenditure> result;
		try {
			startDate = LocalDate.parse(startdate);
			endDate = LocalDate.parse(enddate);
			result = expRepo.findMaxExpenseDateAndDepartmentCodeBetweenTwoDates(departmentCode, startDate, endDate);
		} catch (DateTimeParseException e) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
					"Invalid startDate format. Please use yyyy-MM-dd.");
		}
		return result;
	}

	// 10 expenses authorized by an employee
	@GetMapping("/expenditure/{authorizedBy}")
	@Operation(description = "expenses authorized by an employee", summary = "expenses authorized by employee")
	@ApiResponse(responseCode = "200", description = "paymentMode updated successfully ")
	@ApiResponse(responseCode = "400", description = "Bad request, Already exist")
	@ApiResponse(responseCode = "500", description = "Internal server")
	@ApiResponse(responseCode = "401", description = "unauthorized")
	public List<Expenditure> getExpendituresAuthorizedBy(@PathVariable("authorizedBy") String authorizedBy) {
		return expRepo.findAllByauthorizedBy(authorizedBy);
	}

	// 11 list all expenses where description contains the given string
	@GetMapping("/expenditures/{description}")
	@Operation(description = "expenses where description contains given string", summary = "Description contains given string")
	@ApiResponse(responseCode = "200", description = "paymentMode updated successfully ")
	@ApiResponse(responseCode = "400", description = "Bad request, Already exist")
	@ApiResponse(responseCode = "500", description = "Internal server")
	@ApiResponse(responseCode = "401", description = "unauthorized")
	public List<Expenditure> getExpensesByDescription(@PathVariable("description") String description) {
		return expRepo.findAllExpensesByDescriptionContaining(description);
	}

	// 12 List all expenses where amount is between the given min and max values
	@CrossOrigin()
	@GetMapping("/expendituresbyamount")
	@Operation(description = "expenses where amount is b/w given min & max", summary = "expenses amount b/w given min & max")
	@ApiResponse(responseCode = "200", description = "paymentMode updated successfully ")
	@ApiResponse(responseCode = "400", description = "Bad request, Already exist")
	@ApiResponse(responseCode = "500", description = "Internal server")
	@ApiResponse(responseCode = "401", description = "unauthorized")
	public List<Expenditure> getExpensesByMinMaxPrice(@RequestParam int minprice, @RequestParam int maxprice) {
		List<Expenditure> ex = expRepo.findByAmount(minprice, maxprice);
		return ex;
	}

	// 13 List all categories
	@CrossOrigin()
	@GetMapping("/listcategories")
	@Operation(description = "Get categories", summary = "Get all cattegories")
	@ApiResponse(responseCode = "200", description = "paymentMode updated successfully ")
	@ApiResponse(responseCode = "400", description = "Bad request, Already exist")
	@ApiResponse(responseCode = "500", description = "Internal server")
	@ApiResponse(responseCode = "401", description = "unauthorized")
	public List<Category> getAllcategories() {
		return categoryRepo.findAll();
	}

	// 14 department and total amount spent on department
	@GetMapping("/expenditure/departmentamount")
	@Operation(description = "total amount spent on department", summary = "total amount spent on department")
	@ApiResponse(responseCode = "200", description = "paymentMode updated successfully ")
	@ApiResponse(responseCode = "400", description = "Bad request, Already exist")
	@ApiResponse(responseCode = "500", description = "Internal server")
	@ApiResponse(responseCode = "401", description = "unauthorized")
	public List<ExpenditureDto> departmentalExpenditure() {
		return expRepo.groupByDepartmentCode();
	}

	// 15 category and total amount spent in the category
	@GetMapping("/categoryandtotal")
	@Operation(description = "total amount spent on category", summary = "total spent on category")
	@ApiResponse(responseCode = "200", description = "paymentMode updated successfully ")
	@ApiResponse(responseCode = "400", description = "Bad request, Already exist")
	@ApiResponse(responseCode = "500", description = "Internal server")
	@ApiResponse(responseCode = "401", description = "unauthorized")
	public List<CategoryDto> categoryExpenditure() {
		return expRepo.groupByCategory();
	}

}
