package com.rest.crudapp.service;

import java.util.List;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import com.rest.crudapp.exception.ResourceNotFoundException;
import com.rest.crudapp.model.Employee;
import com.rest.crudapp.model.UserInfo;
import com.rest.crudapp.repository.EmployeeRepository;
import com.rest.crudapp.repository.UserInfoRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class EmployeeService {

	private final EmployeeRepository repository;
	
	private final UserInfoRepository userInfoRepository;
	
	private final PasswordEncoder encoder;

	public List<Employee> getAllEmployees() {
		return repository.findAll();
	}

	public Employee getEmployeeById(Long id) throws ResourceNotFoundException {
		return repository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Employee not found for this id : " + id));
	}

	public Employee updateEmployeeDetails(Long id, Employee emp) throws ResourceNotFoundException {
		repository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Employee not found for this id : " + id));
		return repository.save(Employee.builder().firstName(emp.getFirstName()).lastName(emp.getLastName()).emailId(emp.getEmailId())
				.build()); 
	}
	
	public Employee createEmployee(Employee emp) {
		return repository.save(emp);
	}
	
	public String removeEmployee(Long id) throws ResourceNotFoundException {
		Employee employee = repository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Employee not found for this id : " + id));
		repository.delete(employee);
		return "Employee removed successfully!";
	}
	
	public String addUser(UserInfo userInfo) {
		userInfo.setPassword(encoder.encode(userInfo.getPassword()));
		userInfoRepository.save(userInfo);
		return "User added to system!";
	}
}
