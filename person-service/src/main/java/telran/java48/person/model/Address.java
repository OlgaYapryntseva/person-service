package telran.java48.person.model;

import java.io.Serializable;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.Embeddable;


@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@EqualsAndHashCode
@Embeddable
@ToString
public class Address implements Serializable {
	private static final long serialVersionUID = 9066705325384188825L;
	
	String city;
	String street;
	Integer building;
}
