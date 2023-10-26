package telran.java48.person.service;

import java.time.LocalDate;
import java.util.stream.Collectors;
import org.modelmapper.ModelMapper;
import org.springframework.boot.CommandLineRunner;
import org.springframework.data.convert.TypeMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import lombok.RequiredArgsConstructor;
import telran.java48.person.dao.PersonRepository;
import telran.java48.person.dto.AddressDto;
import telran.java48.person.dto.ChildDto;
import telran.java48.person.dto.CityPopulationDto;
import telran.java48.person.dto.EmployeeDto;
import telran.java48.person.dto.PersonDto;
import telran.java48.person.exception.PersonNotFoundException;
import telran.java48.person.model.Address;
import telran.java48.person.model.Child;
import telran.java48.person.model.Employee;
import telran.java48.person.model.Person;


@Service
@RequiredArgsConstructor
public class PersonServiceImpl implements PersonService, CommandLineRunner {

	final PersonRepository personRepository;
	final ModelMapper modelMapper;	  

	@Override
	@Transactional
	public Boolean addPerson(PersonDto personDto) {
		if (personRepository.existsById(personDto.getId())) {
			return false;
		}
		if (personDto instanceof ChildDto) {
			personRepository.save(modelMapper.map(personDto, Child.class));
		} else if (personDto instanceof EmployeeDto) {
			personRepository.save(modelMapper.map(personDto, Employee.class));
		} else if (personDto instanceof PersonDto) {
			personRepository.save(modelMapper.map(personDto, Person.class));
		}
		return true;
	}

	@Override
	public PersonDto findPersonById(Integer id) {
		Person person = personRepository.findById(id).orElseThrow(PersonNotFoundException::new);
		if (person instanceof Child) {
			return modelMapper.map(person, ChildDto.class);
		}
		if (person instanceof Employee) {
			return modelMapper.map(person, EmployeeDto.class);
		}
		return modelMapper.map(person, PersonDto.class);
	}

	@Override
	@Transactional
	public PersonDto removePerson(Integer id) {
		Person person = personRepository.findById(id).orElseThrow(PersonNotFoundException::new);
		personRepository.delete(person);
		return modelMapper.map(person, PersonDto.class);
	}

	@Override
	@Transactional
	public PersonDto updatePersonName(Integer id, String name) {
		Person person = personRepository.findById(id).orElseThrow(PersonNotFoundException::new);
		person.setName(name);
//		personRepository.save(person);
		return modelMapper.map(person, PersonDto.class);
	}

	@Override
	@Transactional
	public PersonDto updatePersonAddress(Integer id, AddressDto address) {
		Person person = personRepository.findById(id).orElseThrow(PersonNotFoundException::new);
		person.setAddress(modelMapper.map(address, Address.class));
//		personRepository.save(person);
		return modelMapper.map(person, PersonDto.class);
	}

	@Override
	public Iterable<PersonDto> findPersonsByCity(String city) {
		return personRepository.findByAddressCity(city).stream().map(p -> modelMapper.map(p, PersonDto.class))
				.collect(Collectors.toList());
	}

	@Override
	@Transactional(readOnly = true)
	public Iterable<PersonDto> findPersonsByName(String name) {
		return personRepository.findByName(name).stream().map(p -> modelMapper.map(p, PersonDto.class))
				.collect(Collectors.toList());
	}

	@Override
	@Transactional(readOnly = true)
	public Iterable<PersonDto> findPersonsBetweenAge(Integer minAge, Integer maxAge) {
		return personRepository
				.findByBirthDateBetween(LocalDate.now().minusYears(maxAge), LocalDate.now().minusYears(minAge)).stream()
				.map(p -> modelMapper.map(p, PersonDto.class)).collect(Collectors.toList());
	}

	@Override
	public Iterable<CityPopulationDto> getCitiesPopulation() {
		/*
		 * List<String> cityList = new ArrayList<>();
		 * personRepository.findAll().forEach(p ->
		 * cityList.add(p.getAddress().getCity())); Map<String, Long> map =
		 * cityList.stream().collect( Collectors.groupingBy(c -> c,
		 * Collectors.counting())); return map.entrySet().stream()
		 * .sorted(Map.Entry.<String, Long>comparingByValue().reversed()) .map(p -> new
		 * CityPopulationDto (p.getKey(),p.getValue())) .collect(Collectors.toList());
		 */
		return personRepository.getCityPopulation();
	}

	@Override
	@Transactional
	public void run(String... args) throws Exception {
		if (personRepository.count() == 0) {
			Person person = new Person(1000, "John", LocalDate.of(1985, 4, 11),
					new Address("Tel Aviv", "Ben Gvirol", 87));
			Child child = new Child(2000, "Mosche", LocalDate.of(2018, 7, 5), new Address("Ashkelon", "Bar Kohva", 21),
					"Shalom");
			Employee employee = new Employee(3000, "Sarah", LocalDate.of(1995, 11, 23),
					new Address("Rehovot", "Herzl", 7), "Motorola", 20_000);
			personRepository.save(person);
			personRepository.save(child);
			personRepository.save(employee);
		}
	}

	@Override
	@Transactional
	public Iterable<PersonDto> getAllChildren() {
		return personRepository.findByType().stream().map(p -> modelMapper.map(p, ChildDto.class))
				.collect(Collectors.toList());
	}

	@Override
	@Transactional
	public Iterable<PersonDto> getEmployeesBySalary(Integer minSalary, Integer maxSalary) {
		return personRepository.findBySalaryBetween(minSalary, maxSalary).stream()
				.map(p -> modelMapper.map(p, EmployeeDto.class)).collect(Collectors.toList());
	}

}