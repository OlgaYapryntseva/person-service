package telran.java48.person.model;

import java.io.Serializable;
import java.time.LocalDate;
import javax.persistence.Entity;
import javax.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@EqualsAndHashCode(of = "id")
@Entity
@ToString
public class Person implements Serializable {
	private static final long serialVersionUID = -7307107779894340657L;

	@Id
	Integer id;
	@Setter
	String name;
	LocalDate birthDate;
	@Setter
//	@Embedded
	Address address;
}