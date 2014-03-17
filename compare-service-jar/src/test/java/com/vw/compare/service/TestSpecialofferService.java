package com.vw.compare.service;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.junit4.AbstractJUnit4SpringContextTests;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;


/**
 * Class to run the service as a JUnit test. 
 *
 */

@RunWith(SpringJUnit4ClassRunner.class)
@TestExecutionListeners({
		DependencyInjectionTestExecutionListener.class		
	 })
@ContextConfiguration(locations = {
		"classpath:spring-test.xml"
		 })

public class TestSpecialofferService extends AbstractJUnit4SpringContextTests  {

	
	
	/**
	 * The service being tested, injected by Spring.
	 *
	 */
	@Autowired
	@Qualifier("SpecialofferService")
	private CompareService service;

	



	
}
