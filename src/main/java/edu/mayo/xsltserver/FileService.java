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
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Component;

/**
 * Class responsible for file based persistence of the XML and XSLT artifacts.
 *
 * @author <a href="mailto:kevin.peterson@mayo.edu">Kevin Peterson</a>
 */
@Component
public class FileService implements InitializingBean {
	
	private static final String DEFAULT_STORAGE_DIR = 
			System.getProperty("user.home") + File.separator + ".xsltserver" + File.separator + "files";
	
	private String storageDirectory = DEFAULT_STORAGE_DIR;
	
	/* (non-Javadoc)
	 * @see org.springframework.beans.factory.InitializingBean#afterPropertiesSet()
	 */
	@Override
	public void afterPropertiesSet() throws Exception {
		new File(storageDirectory).mkdirs();
	}

	/**
	 * Store an artifact to disk.
	 *
	 * @param name the name
	 * @param bytes the bytes
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	public void store(String name, byte[] bytes) throws IOException {
		FileUtils.writeByteArrayToFile(
				new File(storageDirectory + File.separator + name), bytes);
	}

	public List<String> getAllFileNames() {
		List<String> returnList = new ArrayList<String>();
		
		for(File file : FileUtils.listFiles(new File(storageDirectory), null, false)){
			returnList.add(file.getName());
		}
		
		return returnList;
	}

	/**
	 * Delete.
	 *
	 * @param fileName the file name
	 */
	public void delete(String fileName) {
		FileUtils.deleteQuietly(new File(storageDirectory + File.separator + fileName));
	}

	/**
	 * Gets the file.
	 *
	 * @param fileName the file name
	 * @return the file
	 */
	public File getFile(String fileName) {
		return new File(storageDirectory + File.separator + fileName);
	}

	protected String getStorageDirectory() {
		return storageDirectory;
	}

	protected void setStorageDirectory(String storageDirectory) {
		this.storageDirectory = storageDirectory;
	}

}
