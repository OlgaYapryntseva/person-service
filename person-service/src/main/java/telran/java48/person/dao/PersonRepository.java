package telran.java48.person.dao;


import java.time.LocalDate;
import java.util.List;
import org.springframework.data.repository.CrudRepository;
import telran.java48.person.model.Person;


public interface PersonRepository extends CrudRepository<Person, Integer> {
	
    List<Person> findByAddressCity(String city);
	
	List<Person> findByName(String name);
	
	List<Person>  findByBirthDateBetween(LocalDate minYear, LocalDate maxYear);
	

}
