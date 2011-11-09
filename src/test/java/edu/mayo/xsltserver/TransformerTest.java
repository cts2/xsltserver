package edu.mayo.xsltserver;

import java.io.ByteArrayOutputStream;
import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.*;
import org.junit.Test;
import org.springframework.core.io.ClassPathResource;

public class TransformerTest {
	
	Transformer transformer = new Transformer();
	
	@Test
	public void testTransformWithParams() throws Exception {
		
		ByteArrayOutputStream os = new ByteArrayOutputStream();
		
		Map<String,String> params = new HashMap<String,String>();
		
		params.put("param1", "test");
		
		transformer.transform(
				new ClassPathResource("testxml.xml").getInputStream(), 
				new ClassPathResource("paramtest.xsl").getInputStream(), 
				os, 
				params);
		
		assertTrue(os.toString().contains("Parm1: test"));
	}

}
