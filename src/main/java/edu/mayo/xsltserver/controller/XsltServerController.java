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
package edu.mayo.xsltserver.controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.security.cert.X509Certificate;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import edu.mayo.xsltserver.FileService;
import edu.mayo.xsltserver.Transformer;

/**
 * Handles requests for the application home page.
 *
 * @author <a href="mailto:kevin.peterson@mayo.edu">Kevin Peterson</a>
 */
@Controller
public class XsltServerController {

	@Resource
	private Transformer transformer;
	
	@Resource
	private FileService fileService;
	
	private SSLSocketFactory sslSocketFactory;
	
    final static TrustManager[] TRUST_ALL_CERTS = new TrustManager[] { new X509TrustManager() {

		@Override
		public void checkClientTrusted(X509Certificate[] chain, String authType)
				throws java.security.cert.CertificateException {
		}

		@Override
		public void checkServerTrusted(X509Certificate[] chain, String authType)
				throws java.security.cert.CertificateException {	
		}

		@Override
		public X509Certificate[] getAcceptedIssuers() {
			return null;
		}
		
    } };

	public XsltServerController() {
		super();
		try {
			final SSLContext sslContext = SSLContext.getInstance("SSL");
			sslContext.init(null, TRUST_ALL_CERTS,
					new java.security.SecureRandom());
			// Create an ssl socket factory with our all-trusting manager
			this.sslSocketFactory = sslContext.getSocketFactory();
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * Simply selects the home view to render by returning its name.
	 *
	 * @param request the request
	 * @param response the response
	 * @param xmlurl the xmlurl
	 * @param xmlname the xmlname
	 * @param xslturl the xslturl
	 * @param xsltname the xsltname
	 * @param encoding the encoding
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	@RequestMapping(value = "/transform", method = RequestMethod.GET)
	@ResponseBody
	public void transform(
			HttpServletRequest request,
			HttpServletResponse response,
			@RequestParam(required = false) URL xmlurl,
			@RequestParam(required = false) String xmlname,
			@RequestParam(required = false) URL xslturl,
			@RequestParam(required = false) String xsltname,
			@RequestParam(defaultValue = "application/xml") String encoding)
			throws IOException {
		
		Map<String, String> params = new HashMap<String, String>();
		
		@SuppressWarnings("unchecked")
		Enumeration<String> names = request.getParameterNames();
			
		while(names.hasMoreElements()){
			String name = names.nextElement();
			if(name.equals("xmlurl") ||
					name.equals("xmlname") ||
					name.equals("xslturl") ||
					name.equals("xsltname") ||
					name.equals("encoding")){
				continue;
			} else {
				params.put(name, request.getParameter(name));
			}
		}
		
		response.setContentType(encoding);
		
		if(xmlurl != null && StringUtils.hasText(xmlname)){
			throw new UserInputException("You may specify a 'xslturl' param OR a 'xsltname, but not both");
		}

		if(xslturl != null && StringUtils.hasText(xsltname)){
			throw new UserInputException("You may specify a 'xmlurl' param OR a 'xmlname, but not both");
		}
		
		InputStream xsltInputStream;
		if(StringUtils.hasText(xsltname)){
			File file = fileService.getFile(xsltname);
			if(! file.exists()){
				throw new UserInputException("The requested XSLT file: " + xsltname + " does not exist.");
			}
			
			xsltInputStream = new FileInputStream(file);
		} else {
			xsltInputStream = this.createInputStreamFromUrl(xslturl);
		}
		
		InputStream xmlInputStream;
		if(StringUtils.hasText(xmlname)){
			File file = fileService.getFile(xmlname);
			if(! file.exists()){
				throw new UserInputException("The requested XML file: " + xmlname + " does not exist.");
			}
			
			xmlInputStream = new FileInputStream(file);
		} else {
			xmlInputStream = this.createInputStreamFromUrl(xmlurl);
		}

		transformer.transform(
				xmlInputStream, 
				xsltInputStream,
				response.getOutputStream(), 
				params);
	}
	
	protected InputStream createInputStreamFromUrl(URL url) throws IOException {
		final URLConnection urlCon = url.openConnection();
	    
		if(urlCon instanceof HttpsURLConnection){
			( (HttpsURLConnection) urlCon ).setSSLSocketFactory( sslSocketFactory );
		}

		return urlCon.getInputStream();
	}

	/**
	 * Handle null pointer exception.
	 *
	 * @param response the response
	 * @param ex the ex
	 * @return the model and view
	 */
	@ExceptionHandler(UserInputException.class)
	public ModelAndView handleNullPointerException(
			HttpServletResponse response,
			UserInputException ex) {
		
		ModelAndView mav = new ModelAndView("inputError");
		mav.addObject("message", ex.getLocalizedMessage());
		
		response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
		
		return mav;
	}

}
