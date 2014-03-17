package com.vw.compare.service;

import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.junit4.AbstractJUnit4SpringContextTests;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;

/**
 * Test class to test the methods of the LegacyofferService.
 * 
 */

@RunWith(SpringJUnit4ClassRunner.class)
@TestExecutionListeners({ DependencyInjectionTestExecutionListener.class })
@ContextConfiguration(locations = { "classpath:serviceContext.xml" })
public class TestLegacyofferService extends AbstractJUnit4SpringContextTests {

	/**
	 * The service being tested, injected by Spring.
	 * 
	 */	
//	@Autowired
//    LegacyofferService legacyofferService;	
//
//	@Test
//	public void testGetRTIXML() throws CompareServiceException {
//		Offers offers = null;
//		offers = legacyofferService.getRTIXML("48335");
//		assertNotNull(offers);
//
//	}
//	
//	@Test
//	public void testGetDAAXML() throws CompareServiceException {
//		Offers offers = null;
//		offers = legacyofferService.getDAAXML("215");
//		assertNotNull(offers);
//
//	}
//	
//	@Test
//	public void testProcessExternalRequest() throws CompareServiceException {
//		String str = null;
//		str = legacyofferService.processExternalRequest("ext_2010_lease_jetta.xml");
//		assertNotNull(str);
//
//	}

}
