/*
 * Copyright: (c) 2004-2011 Mayo Foundation for Medical Education and 
 * Research (MFMER). All rights reserved. MAYO, MAYO CLINIC, and the
 * triple-shield Mayo logo are trademarks and service marks of MFMER.
 *
 * Except as contained in the copyright notice above, or as used to identify 
 * MFMER as the author of this software, the trade names, trademarks, service
 * marks, or product names of the copyright holder shall not be used in
 * advertising, promotion or otherwise in connection with this software without
 * prior written authorization of the copyright holder.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package edu.mayo.xsltserver;

import java.io.File;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Map;
import java.util.Map.Entry;

import javax.annotation.Resource;
import javax.xml.transform.Source;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.URIResolver;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;

import org.springframework.stereotype.Component;

/**
 * The XSLT Transformation logic.
 *
 * @author <a href="mailto:kevin.peterson@mayo.edu">Kevin Peterson</a>
 */
@Component
public class Transformer {
	
	@Resource
	private FileService fileService;

	/**
	 * Transform.
	 *
	 * @param xmlInputStream the xml input stream
	 * @param xsltInputStream the xslt input stream
	 * @param outputStream the output stream
	 * @param parameters the parameters
	 */
	public void transform(
			InputStream xmlInputStream, 
			InputStream xsltInputStream, 
			OutputStream outputStream,
			Map<String,String> parameters){
		  try
	      {
	         // Source XML File
	         StreamSource xmlFile = new StreamSource(xmlInputStream);
	         
	         // Source XSLT Stylesheet
	         StreamSource xsltFile = new StreamSource(xsltInputStream);
	         TransformerFactory xsltFactory = TransformerFactory.newInstance();
	         
	         final URIResolver decoratedResolver = xsltFactory.getURIResolver();
	         
	         xsltFactory.setURIResolver(new URIResolver(){

					@Override
					public Source resolve(String href, String base)
							throws TransformerException {
						Source source = decoratedResolver.resolve(href, fileService.getStorageDirectory() + File.separator);
						
						return source;
					}
		        	 
		     });
	         
	         javax.xml.transform.Transformer transformer = xsltFactory.newTransformer(xsltFile);

	         if(parameters != null){
		         for(Entry<String, String> entry : parameters.entrySet()){
		        	 transformer.setParameter(entry.getKey(), entry.getValue());
		         }
	         }

	         // Send transformed output to the console
	         StreamResult resultStream = new StreamResult(outputStream);

	         // Apply the transformation
	         transformer.transform(xmlFile, resultStream);
	      }
	      catch(Exception ex)
	      {
	         throw new RuntimeException(ex);
	      } 
	}
}
