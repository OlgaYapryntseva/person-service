package telran.java48.person.service;


import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import lombok.RequiredArgsConstructor;
import telran.java48.person.dao.PersonRepository;
import telran.java48.person.dto.AddressDto;
import telran.java48.person.dto.CityPopulationDto;
import telran.java48.person.dto.PersonDto;
import telran.java48.person.exception.PersonNotFoundException;
import telran.java48.person.model.Address;
import telran.java48.person.model.Person;

@Service
@RequiredArgsConstructor
public class PersonServiceImpl implements PersonService {

	final PersonRepository personRepository;
	final ModelMapper modelMapper;

	@Override
	@Transactional
	public Boolean addPerson(PersonDto personDto) {
		if (personRepository.existsById(personDto.getId())) {
			return false;
		}
		personRepository.save(modelMapper.map(personDto, Person.class));
		return true;
	}

	@Override
	public PersonDto findPersonById(Integer id) {
		Person person = personRepository.findById(id).orElseThrow(PersonNotFoundException::new);
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
		return personRepository.findByAddressCity(city)
				.stream()
				.map(p -> modelMapper.map(p, PersonDto.class))
				.collect(Collectors.toList());
	}

	@Override
	public Iterable<PersonDto> findPersonsByName(String name) {
		return personRepository.findByName(name)
				.stream()
				.map(p -> modelMapper.map(p,  PersonDto.class))
				.collect(Collectors.toList());
	}

	@Override
	public Iterable<PersonDto> findPersonsBetweenAge(Integer minAge, Integer maxAge) {
		return personRepository.findByBirthDateBetween(LocalDate.now().minusYears(maxAge), LocalDate.now().minusYears(minAge))
				.stream()
				.map(p -> modelMapper.map(p, PersonDto.class))
				.collect(Collectors.toList());
	}


	@Override
	public Iterable<CityPopulationDto> getCitiesPopulation() {
		List<String> cityList = new ArrayList<>();
		personRepository.findAll().forEach(p -> cityList.add(p.getAddress().getCity()));
		Map<String, Long> map =	cityList.stream().collect(
		                Collectors.groupingBy(c -> c, Collectors.counting()));
		return 	 map.entrySet().stream()
				.sorted(Map.Entry.<String, Long>comparingByValue().reversed())
				.map(p -> new CityPopulationDto (p.getKey(),p.getValue()))		
				.collect(Collectors.toList());
	}

}