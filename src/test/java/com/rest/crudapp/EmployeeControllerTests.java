package com.rest.crudapp;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.rest.crudapp.exception.ResourceNotFoundException;
import com.rest.crudapp.model.Employee;
import com.rest.crudapp.repository.EmployeeRepository;
import com.rest.crudapp.service.EmployeeService;

@WebMvcTest
public class EmployeeControllerTests {

	@Autowired
	private MockMvc mockMvc;

	@MockBean
	private EmployeeService service;

	@MockBean
	private EmployeeRepository repository;

	@Autowired
	private ObjectMapper objectMapper;

	Employee employee1 = new Employee(1L, "Amar", "Kumar", "amk@gmail.com");
	Employee employee2 = new Employee(1L, "Rajesh", "Kumar", "rk@gmail.com");
	Employee employee3 = new Employee(1L, "Santosh", "Kumar", "sk@gmail.com");

	public Employee getEmployee() {
		return Employee.builder().firstName("Ankur").lastName("Veer").emailId("ank@gmail.com").build();
	}

	@Test
	public void createEmployeeTest() throws Exception {
		String content = objectMapper.writeValueAsString(getEmployee());
		mockMvc.perform(post("http://localhost:9292/api/v1/employee").contentType("application/json").content(content))
				.andExpect(status().isCreated());

	}

	@Test
	public void getEmployeeByIdTest() throws Exception {
		Long employeeId = 1L;
		when(service.getEmployeeById(employeeId)).thenReturn(getEmployee());
		mockMvc.perform(get("http://localhost:9292/api/v1/employee/{id}", employeeId).contentType("application/json"))
				.andExpect(status().isOk()).andExpect(jsonPath("$.firstName", is(getEmployee().getFirstName())))
				.andExpect(jsonPath("$.lastName", is(getEmployee().getLastName())))
				.andExpect(jsonPath("$.emailId", is(getEmployee().getEmailId())));
	}

	@Test
	public void getAllEmployeesTest() throws Exception {
		List<Employee> listOfEmployees = new ArrayList<>();
		listOfEmployees.add(employee1);
		listOfEmployees.add(employee2);
		listOfEmployees.add(employee3);

		when(service.getAllEmployees()).thenReturn(listOfEmployees);
		mockMvc.perform(get("http://localhost:9292/api/v1/employees").contentType("application/json"))
				.andExpect(status().isOk()).andExpect(jsonPath("$[2].firstName", is("Santosh")));
	}

	@Test
	public void updateEmployeeResourceNotFoundTest() throws Exception {
		Employee updatedEmployee = Employee.builder().id(5L).firstName("Sachin").lastName("Tendulekar")
				.emailId("sachin@gmail.com").build();
		when(repository.findById(updatedEmployee.getId())).thenThrow(new ResourceNotFoundException("Not found."));
		mockMvc.perform(put("http://localhost:9292/api/v1/employee/{id}", updatedEmployee.getId())
				.contentType("application/json").content(this.objectMapper.writeValueAsString(updatedEmployee)))
			    .andExpect(status().isNotFound())
				.andExpect(result -> assertTrue(result.getResolvedException() instanceof ResourceNotFoundException));

	}

	@Test
	public void deleteEmployeeById_success() throws Exception {
		when(repository.findById(employee2.getId())).thenReturn(Optional.of(employee2));

		mockMvc.perform(
				delete("http://localhost:9292/api/v1/employee/{id}", employee2.getId()).contentType("application/json"))
				.andExpect(status().isOk());
	}

	@Test
	public void deletePatientById_notFound() throws Exception {
		when(repository.findById(5L)).thenReturn(null);

		mockMvc.perform(delete("http://localhost:9292/api/v1/employee/{id}", 5L).contentType("application/json"))
				.andExpect(status().isNotFound())
				.andExpect(result -> assertTrue(result.getResolvedException() instanceof ResourceNotFoundException));
	}
}
