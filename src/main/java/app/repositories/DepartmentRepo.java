package app.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import app.entities.Department;

public interface DepartmentRepo extends JpaRepository<Department, String> {

	void deleteByDepartmentCode(String departmentCode);

	Department findByDepartmentName(String departmentName);

	Department findByDepartmentCode(String departmentCode);

}
