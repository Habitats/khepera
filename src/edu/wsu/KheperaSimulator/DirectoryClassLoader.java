/**
 * @(#)DirectoryClassLoader.java 1.1 2002/10/13
 *
 * Copyright Wright State University. All Rights Reserved.
 *
 * This file is part of the WSU Khepera Simulator.
 *
 * This file may be distributed under the terms of the Q Public License
 * as defined by Trolltech AS of Norway and appearing in the file
 * WSU_Khepera_Sim_license.txt included in the packaging of this file.
 *
 * This file is provided AS IS with NO WARRANTY OF ANY KIND, INCLUDING THE
 * WARRANTY OF DESIGN, MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE.
 *
 * For information on the Q Public License see:
 * 		http://www.opensource.org/licenses/qtpl.php
 *
 * For information on the WSU Khepera Simulator see:
 * 		http://gozer.cs.wright.edu and follow the links.
 *
 * Contact robostaff@gozer.cs.wright.edu if any conditions of this licensing are
 * not clear to you.
 */

package edu.wsu.KheperaSimulator;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.lang.ClassLoader;

/**
 * A <code>DirectoryClassLoader</code> is a utility class that provides the ablility to dynamically load a class from a directory in the file system.
 * 
 * @author Brian Potchik
 * @version 1.1 2002/10/13
 */
public class DirectoryClassLoader extends ClassLoader {

	/**
	 * path of the class file to load
	 */
	private String path;

	/**
	 * Allocate a new <code>DirectoryClassLoader</code> that can load classes from the directory identified by the string parameter.
	 * 
	 * @param path
	 *            the location of the file
	 */
	public DirectoryClassLoader(String path) {
		this.path = path;
	}

	/**
	 * Load the class with the specified classname without the '.class' extension.
	 * 
	 * @param className
	 *            the name of the class to load
	 * @return A new defined <code>Class</code>
	 */
	@Override
	public Class findClass(String className) {
		byte[] b = loadClassData(className);
		return defineClass(className, b, 0, b.length);
	} // findClass

	/**
	 * Load the class from a directory
	 * 
	 * @param name
	 *            the name of the class to load
	 * @return A <tt>byte[]</tt> consisting of the class data.
	 */
	private byte[] loadClassData(String name) {
		try {
			File file = new File(path + name + ".class");
			byte[] b = new byte[(int) file.length()];
			BufferedInputStream in = new BufferedInputStream(new FileInputStream(file));
			in.read(b);
			return b;
		} catch (Exception e) {
			return null;
		}
	} // loadClassData
} // DirectoryClassLoader