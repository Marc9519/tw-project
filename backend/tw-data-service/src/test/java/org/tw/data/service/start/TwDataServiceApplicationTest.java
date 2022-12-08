
package org.tw.data.service.start;

import static org.junit.Assert.assertNotNull;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;
import org.tw.data.service.services.TwService;

@SpringBootTest
@RunWith(SpringRunner.class)
@TestPropertySource("classpath:testApplication.properties")
public class TwDataServiceApplicationTest {

	@Autowired
	private TwService twService;

	@Test
	public void testContextLoads() {
		assertNotNull(twService);
	}

}