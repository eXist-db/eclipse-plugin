package org.exist.eclipse.query.internal;

import org.eclipse.ui.IStartup;

/**
 * The query plugin is started directly after loading the workbench. The reason
 * for this is, that the auto plugin can be informed concerning open/closed
 * connections.
 * 
 * @author Markus Tanner
 */
public class Startup implements IStartup {

	@Override
	public void earlyStartup() {
	}
}
