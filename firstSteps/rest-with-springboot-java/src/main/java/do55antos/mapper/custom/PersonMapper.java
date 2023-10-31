package do55antos.mapper.custom;

import java.util.Date;

import org.springframework.stereotype.Service;

import do55antos.data_vo_v2.PersonVOV2;
import do55antos.model.Person;

@Service
public class PersonMapper {
	
	public PersonVOV2 convertEntityToVo(Person person) {
		PersonVOV2 vo = new PersonVOV2();
		vo.setId(person.getId());
		vo.setFirstName(person.getFirstName());
		vo.setLastName(person.getLastName());
		vo.setAdress(person.getAdress());
		vo.setGender(person.getGender());
		vo.setBirthDay(new Date());
		
		return vo;
	}
	
	public Person convertVoToEntity(PersonVOV2 person) {
		Person entity = new Person();
		entity.setId(person.getId());
		entity.setFirstName(person.getFirstName());
		entity.setLastName(person.getLastName());
		entity.setAdress(person.getAdress());
		entity.setGender(person.getGender());		
		return entity;
	}
}
