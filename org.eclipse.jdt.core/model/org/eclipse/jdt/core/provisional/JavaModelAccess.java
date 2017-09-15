/*******************************************************************************
 * Copyright (c) 2017 GK Software AG, and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * This is an implementation of an early-draft specification developed under the Java
 * Community Process (JCP) and is made available for testing and evaluation purposes
 * only. The code is not compatible with any specification of the JCP.
 *
 * Contributors:
 *     Stephan Herrmann - initial API and implementation
 *******************************************************************************/
package org.eclipse.jdt.core.provisional;

import org.eclipse.jdt.core.IClasspathAttribute;
import org.eclipse.jdt.core.IClasspathEntry;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.IPackageFragmentRoot;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jdt.internal.core.JavaProject;

/**
 * Provisional API for use by JDT/UI, which may possibly be removed in a future version.
 */
public class JavaModelAccess {

	/**
	 * In a Java 9 project, a classpath entry can be filtered using a {@link IClasspathAttribute#LIMIT_MODULES} attribute,
	 * in which case {@link IJavaProject#findPackageFragmentRoots(IClasspathEntry)} will not contain all roots physically
	 * present in the container.
	 * This provisional API can be used to bypass the filter and get really all roots to which the given entry is resolved.
	 * 
	 * @param javaProject the Java project to search in
	 * @param entry a classpath entry of the Java project
	 * @return the unfiltered array of package fragment roots to which the classpath entry resolves
	 */
	public static IPackageFragmentRoot[] getUnfilteredPackageFragmentRoots(IJavaProject javaProject, IClasspathEntry entry) {
		try {
			JavaProject internalProject = (JavaProject) javaProject; // cast should be safe since IJavaProject is @noimplement
			IClasspathEntry[] resolvedEntries = internalProject.resolveClasspath(new IClasspathEntry[]{ entry });
			return internalProject.computePackageFragmentRoots(resolvedEntries, false /* not exported roots */, false /* ignore limit-modules! */, null /* no reverse map */);
		} catch (JavaModelException e) {
			// according to comment in JavaProject.findPackageFragmentRoots() we assume that this is caused by the project no longer existing
			return new IPackageFragmentRoot[] {};
		}
	}
}