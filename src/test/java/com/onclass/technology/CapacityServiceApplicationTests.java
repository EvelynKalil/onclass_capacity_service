package com.onclass.technology;

import com.onclass.capacity.domain.api.TechnologyServicePort;
import com.onclass.capacity.domain.spi.TechnologyPersistencePort;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

@SpringBootTest(classes = CapacityServiceApplication.class)
class CapacityServiceApplicationTests {

	@MockBean
	private CapacityPersistencePort technologyPersistencePort;

	@Autowired
	private CapacityServicePort technologyServicePort;

	@Test
	void contextLoads() {
		// just checks Spring context starts with our beans wired
	}
}
