package telran.java48.person.dao;


import java.time.LocalDate;
import java.util.List;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import telran.java48.person.dto.CityPopulationDto;
import telran.java48.person.model.Person;


public interface PersonRepository extends CrudRepository<Person, Integer> {
	//@Query("select p from Person p where p.address.city=:cityName")
    List<Person> findByAddressCity(@Param("cityName") String city);
	
	//@Query("select p from Person p where p.name=?1")
	List<Person> findByName(String name);
	
	List<Person> findByBirthDateBetween(LocalDate minYear, LocalDate maxYear);

	@Query("select new telran.java48.person.dto.CityPopulationDto(p.address.city, count(p)) from Person p group by p.address.city order by count(p) asc")
    List<CityPopulationDto> getCityPopulation();
	
	@Query("select c from Child c")
	List<Person> findByType();
	
	@Query("select e from Employee e where e.salary >= :min and e.salary <= :max")
	List<Person> findBySalaryBetween(@Param("min") Integer minSalary,@Param("max") Integer maxSalary);
	
}
