package core.xml.model;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public class Employee {
	private int empId;
	@NotBlank(message = "{employee.name.required}")
	@Size(max = 255, message = "{employee.name.maxLength}")
	private String empName;
	@NotNull(message = "{employee.age.required}")
	@Min(value = 0, message = "{employee.age.min}")
	@Max(value = 120, message = "{employee.age.max}")
	private Integer age;

	public int getEmpId() {
		return empId;
	}

	public void setEmpId(int empId) {
		this.empId = empId;
	}

	public String getEmpName() {
		return empName;
	}

	public void setEmpName(String empName) {
		this.empName = empName;
	}

	public Integer getAge() {
		return age;
	}

	public void setAge(Integer age) {
		this.age = age;
	}

	@Override
	public String toString() {
		return "Employee [empId=" + empId + ", empName=" + empName + ", age="
				+ age + "]";
	}
}
