/*
 *  eXist Open Source Native XML Database
 *  Copyright (C) 2001-2009 The eXist Project
 *  http://exist-db.org
 *  
 *  This program is free software; you can redistribute it and/or
 *  modify it under the terms of the GNU Lesser General Public License
 *  as published by the Free Software Foundation; either version 2
 *  of the License, or (at your option) any later version.
 *  
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU Lesser General Public License for more details.
 *  
 *  You should have received a copy of the GNU Lesser General Public License
 *  along with this program; if not, write to the Free Software
 *  Foundation, Inc., 675 Mass Ave, Cambridge, MA 02139, USA.
 *  
 *  $Id:$
 */
package org.exist.eclipse.xquery.debug.core;

import org.eclipse.core.runtime.Plugin;

/**
 * @author <a href="mailto:shabanovd@gmail.com">Dmitriy Shabanov</a>
 * 
 */
public class DebugXQueryPlugin extends Plugin {

	private static DebugXQueryPlugin fgDefault;

	public DebugXQueryPlugin() {
		super();
		fgDefault = this;
	}

	public static DebugXQueryPlugin getDefault() {
		return fgDefault;
	}

	/**
	 * Returns a symbolic id of the plugin instance.
	 * 
	 * @return Id
	 */
	public static String getId() {
		return getDefault().getBundle().getSymbolicName();
	}
}