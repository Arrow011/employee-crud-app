package com.rest.crudapp.controller;

import java.util.List;
import javax.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import com.rest.crudapp.exception.ResourceNotFoundException;
import com.rest.crudapp.model.Employee;
import com.rest.crudapp.model.UserInfo;
import com.rest.crudapp.service.EmployeeService;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class EmployeeController {

	private final EmployeeService employeeService;

	@GetMapping("/employees")
	@ResponseStatus(HttpStatus.OK)
	public List<Employee> getAllEmployees() {
		return employeeService.getAllEmployees();
	}

	@GetMapping("/employee/{id}")
	@ResponseStatus(HttpStatus.OK)
	@PreAuthorize("hasAuthority('ROLE_USER')")
	public Employee getEmployeeByID(@PathVariable("id") Long id) throws ResourceNotFoundException {
		return employeeService.getEmployeeById(id);
	}

	@PostMapping("/employee")
	@ResponseStatus(HttpStatus.CREATED)
	public Employee createEmployee(@Valid @RequestBody Employee employee) {
		return employeeService.createEmployee(employee);
	}

	@PutMapping("/employee/{id}")
	@ResponseStatus(HttpStatus.OK)
	public Employee updateEmployeeDetail(@PathVariable("id") Long id, @Valid @RequestBody Employee emp)
			throws ResourceNotFoundException {
		return employeeService.updateEmployeeDetails(id,emp);
	}

	@DeleteMapping("/employee/{id}")
	@ResponseStatus(HttpStatus.OK)
	@PreAuthorize("hasAuthority('ROLE_ADMIN')")
	public String deleteEmployee(@PathVariable("id") Long id) throws ResourceNotFoundException {
		return employeeService.removeEmployee(id);
	}
	
	@PostMapping("/newUser")
	public String addNewUser(@RequestBody UserInfo userInfo) {
		return employeeService.addUser(userInfo);
	}

}
