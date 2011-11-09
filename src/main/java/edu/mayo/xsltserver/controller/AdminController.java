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
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

import edu.mayo.xsltserver.FileService;

/**
 * The Admin Controller.
 *
 * @author <a href="mailto:kevin.peterson@mayo.edu">Kevin Peterson</a>
 */
@Controller
public class AdminController {

	@Resource
	private FileService fileService;

	/**
	 * List files.
	 *
	 * @return the model and view
	 * @throws Exception the exception
	 */
	@RequestMapping(value = "/admin")
	public ModelAndView listFiles() throws Exception {

		List<String> files = this.fileService.getAllFileNames();

		return new ModelAndView("admin", "files", files);
	}

	/**
	 * upload.
	 *
	 * @param request the request
	 * @param response the response
	 * @return the redirect view
	 * @throws Exception the exception
	 */
	@RequestMapping(value = "/admin/files", method=RequestMethod.POST)
	public RedirectView upload(HttpServletRequest request,
			HttpServletResponse response) throws Exception {

		MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
		MultipartFile multipartFile = multipartRequest.getFile("file");

		String fileName = multipartFile.getOriginalFilename();
		fileService.store(fileName, multipartFile.getBytes());

		RedirectView view = new RedirectView("../admin");
		view.setExposeModelAttributes(false);

		return view;
	}

	/**
	 * download.
	 *
	 * @param response the response
	 * @param fileName the file name
	 * @throws Exception the exception
	 */
	@RequestMapping(value = "/admin/file/{fileName}")
	public void download(
			HttpServletResponse response, 
			@PathVariable String fileName) throws Exception {

		File file = this.fileService.getFile(fileName);

		response.setContentType("text/xml");
		response.setHeader("Content-Disposition", "attachment; filename=\""
				+ file.getName() + "\"");

		FileCopyUtils.copy(new FileInputStream(file), response.getOutputStream());
	}

	/**
	 * delete.
	 *
	 * @param fileName the file name
	 * @return the redirect view
	 * @throws Exception the exception
	 */
	@RequestMapping(value = "/admin/file/{fileName}", method = RequestMethod.DELETE)
	public RedirectView delete(@PathVariable String fileName) throws Exception {

		this.fileService.delete(fileName);

		RedirectView view = new RedirectView("../../admin");
		view.setExposeModelAttributes(false);

		return view;
	}
}
