package de.honoka.test.various.old.p4;

import de.honoka.sdk.util.code.HonokaComparator;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class StreamTest {
	
	enum Status {
		BUSY,
		FREE
	}
	
	static class Employee {
		
		int id;
		
		String name;
		
		int age;
		
		int salary;
		
		Status status;
		
		public Employee(int id, String name, int age, int salary, Status status) {
			this.id = id;
			this.name = name;
			this.age = age;
			this.salary = salary;
			this.status = status;
		}
		
		@Override
		public String toString() {
			return "Employee{" +
					"id=" + id +
					", name='" + name + '\'' +
					", age=" + age +
					", salary=" + salary +
					", status=" + status +
					'}';
		}
	}
	
	static class SalesPerson {
		
		Employee employee;
		
		String city;
		
		String joinYear;
		
		public SalesPerson(Employee employee, String city, String joinYear) {
			this.employee = employee;
			this.city = city;
			this.joinYear = joinYear;
		}
		
		@Override
		public String toString() {
			return "SalesPerson{" +
					"employee=" + employee +
					", city='" + city + '\'' +
					", joinYear='" + joinYear + '\'' +
					'}';
		}
	}
	
	private List<SalesPerson> sales = null;
	
	@Test
	public void test() {
		Employee emma = new Employee(001, "Emma", 41, 20000, Status.FREE);
		Employee Mary = new Employee(002, "Mary", 39, 18000, Status.BUSY);
		Employee Allen = new Employee(003, "Allen", 33, 15000, Status.BUSY);
		Employee Olivia = new Employee(004, "Olivia", 52, 32000, Status.FREE);
		Employee Natasha = new Employee(005, "Natasha", 27, 13000, Status.BUSY);
		Employee Kevin = new Employee(006, "Kevin", 25, 10000, Status.FREE);
		Employee Haivent = new Employee(007, "Haivent", 25, 12000, Status.FREE);
		
		sales = Arrays.asList(
				new SalesPerson(emma, "ChengDu", "2005"),
				new SalesPerson(Mary, "ShangHai", "2010"),
				new SalesPerson(Allen, "ShangHai", "2005"),
				new SalesPerson(Olivia, "ShenZhen", "2006"),
				new SalesPerson(Natasha, "ChengDu", "2008"),
				new SalesPerson(Kevin, "ChengDu", "2005"),
				new SalesPerson(Haivent, "BeiJing", "2005")
		);
		
		//?????? reduce ?????????????????????
		System.out.println(sales.stream().count());
		//????????? 2005 ???????????????????????????????????????????????????
		sales.stream().filter(s -> s.joinYear.equals("2005"))
				.sorted((HonokaComparator<SalesPerson>) (o1, o2) -> {
			return o1.employee.salary > o2.employee.salary ? o1 : o2;
		}).forEach(System.err::println);
		//??????????????????????????????????????????????????????
		sales.stream().collect(Collectors.groupingBy(p -> p.city))
				.forEach((city, group) -> System.out.println(city));
		//????????????????????????????????????????????????
		sales.stream().sorted((HonokaComparator<SalesPerson>) (o1, o2) -> {
			char o1C, o2C;
			o1C = o1.employee.name.charAt(0);
			o2C = o2.employee.name.charAt(0);
			if(o1C > o2C) return o1;
			if(o1C < o2C) return o2;
			return null;
		}).forEach(p -> System.err.println(p.employee.name));
		//??????????????????????????????
		System.out.println(sales.stream().anyMatch(p -> p.city.equals("ChengDu")));
		//??????????????????????????????????????????
		System.err.println(sales.stream().filter(p -> p.city.equals("ChengDu"))
				.collect(Collectors.summingInt(p -> p.employee.salary)));
		//????????????????????????????????????
		System.out.println(sales.stream().mapToInt(p -> p.employee.salary)
				.max().getAsInt());
		//??????????????????????????????????????????
		System.err.println(sales.stream().min(
				(HonokaComparator<SalesPerson>) (o1, o2) -> {
			if(o1.employee.salary == o2.employee.salary)
				return null;
			return o1.employee.salary > o2.employee.salary ? o1 : o2;
		}).orElse(null));
		//?????????????????????????????????
		sales.stream().collect(Collectors.groupingBy(p -> p.city))
				.forEach((city, list) -> {
			System.out.print(city + "\t");
			int sum = 0;
			for(SalesPerson p : list) {
				sum += p.employee.salary;
			}
			System.out.println(sum);
		});
	}
}
