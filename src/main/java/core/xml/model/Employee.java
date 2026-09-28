package core.xml.model;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class Employee {
	private int empId;
	@NotBlank(message = "Tên nhân viên không được để trống")
	@Size(max = 255, message = "Tên nhân viên không được dài quá 255 ký tự")
	private String empName;
	@Min(value = 0, message = "Tuổi không được là số âm")
	@Max(value = 120, message = "Tuổi phải nhỏ hơn hoặc bằng 120")
	private int age;

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

	public int getAge() {
		return age;
	}

	public void setAge(int age) {
		this.age = age;
	}

	@Override
	public String toString() {
		return "Employee [empId=" + empId + ", empName=" + empName + ", age="
				+ age + "]";
	}
}
