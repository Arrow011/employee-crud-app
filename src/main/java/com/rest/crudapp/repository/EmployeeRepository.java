package com.rest.crudapp.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.rest.crudapp.model.Employee;

public interface EmployeeRepository extends JpaRepository<Employee,Long>{

}
