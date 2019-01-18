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
package org.exist.eclipse.xquery.debug.core.model;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;

import org.eclipse.core.resources.IMarker;
import org.eclipse.core.resources.IMarkerDelta;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.debug.core.DebugEvent;
import org.eclipse.debug.core.DebugException;
import org.eclipse.debug.core.DebugPlugin;
import org.eclipse.debug.core.ILaunch;
import org.eclipse.debug.core.model.IBreakpoint;
import org.eclipse.debug.core.model.IDebugTarget;
import org.eclipse.debug.core.model.ILineBreakpoint;
import org.eclipse.debug.core.model.IMemoryBlock;
import org.eclipse.debug.core.model.IProcess;
import org.eclipse.debug.core.model.IStackFrame;
import org.eclipse.debug.core.model.IThread;
import org.eclipse.debug.core.model.IValue;
import org.exist.eclipse.xquery.debug.core.launching.IXQueryConstants;

/**
 * @author <a href="mailto:shabanovd@gmail.com">Dmitriy Shabanov</a>
 *
 */
public class XQueryDebugTarget extends XQueryDebugElement implements IDebugTarget {

	private ILaunch fLaunch;
	private IProcess fProcess;
	private Socket fRequestSocket;
	private PrintWriter fRequestWriter;
	private BufferedReader fRequestReader;
	private Socket fEventSocket;
	private BufferedReader fEventReader;
	private XQueryThread fThread;
	private IThread[] fThreads;
	private EventDispatchJob fEventDispatch;
	private String fName;
	private boolean fSuspended;
	
	public XQueryDebugTarget(ILaunch launch, int requestPort, int eventPort) throws CoreException {
		super(null);
		
		fLaunch = launch;
		fTarget = this;
		fProcess = null; //TODO: any replacement
		try {
			fRequestSocket = new Socket("localhost", requestPort);
			fRequestWriter = new PrintWriter(fRequestSocket.getOutputStream());
			fRequestReader = new BufferedReader(new InputStreamReader(fRequestSocket.getInputStream()));
			
			fEventSocket = new Socket("localhost", eventPort);
			fEventReader = new BufferedReader(new InputStreamReader(fEventSocket.getInputStream()));
		} catch (UnknownHostException e) {
			abort("Unable to connect to eXist-db", e);
		} catch (IOException e) {
			abort("Unable to connect to eXist-db", e);
		}
		
		fThread = new XQueryThread(this);
		fThreads = new IThread[] {fThread};
		fEventDispatch = new EventDispatchJob();
		fEventDispatch.schedule();
		DebugPlugin.getDefault().getBreakpointManager().addBreakpointListener(this);
		
	}

	@Override
	public String getName() throws DebugException {
		if (fName == null) {
			fName = "XQuery script";
			try {
				fName = getLaunch().getLaunchConfiguration().getAttribute(IXQueryConstants.ATTR_XQUERY_SCRIPT, "XQuery");
			} catch (CoreException e) {
			}
		}
		return fName;
	}

	@Override
	public IProcess getProcess() {
		return fProcess;
	}

	@Override
	public IThread[] getThreads() throws DebugException {
		return fThreads;
	}

	@Override
	public boolean hasThreads() throws DebugException {
		return true;
	}

	@Override
	public boolean supportsBreakpoint(IBreakpoint breakpoint) {
		if (breakpoint.getModelIdentifier().equals(IXQueryConstants.ID_XQUERY_DEBUG_MODEL)) {
			try {
				String script = getLaunch().getLaunchConfiguration().getAttribute(IXQueryConstants.ATTR_XQUERY_SCRIPT, (String)null);
				if (script != null) {
					IMarker marker = breakpoint.getMarker();
					if (marker != null) {
						IPath p = new Path(script);
						return marker.getResource().getFullPath().equals(p);
					}
				}
			} catch (CoreException e) {
			}
		}
		return false;
	}
	
	@Override
	public IDebugTarget getDebugTarget() {
		return this;
	}
	
	@Override
	public ILaunch getLaunch() {
		return fLaunch;
	}

	@Override
	public boolean canTerminate() {
		return getProcess().canTerminate();
	}

	@Override
	public boolean isTerminated() {
		return getProcess().isTerminated();
	}

	@Override
	public void terminate() throws DebugException {
		synchronized (fRequestSocket) {
			fRequestWriter.println("exit");
			fRequestWriter.flush();
		}
	}

	@Override
	public boolean canResume() {
		return !isTerminated() && isSuspended();
	}

	@Override
	public boolean canSuspend() {
		return !isTerminated() && isSuspended();
	}

	@Override
	public boolean isSuspended() {
		return fSuspended;
	}

	@Override
	public void resume() throws DebugException {
		sendRequest("resume");
	}
	
	private void resumed(int detail) {
		fSuspended = false;
		fThread.fireResumeEvent(detail);
	}

	private void suspended(int detail) {
		fSuspended = true;
		fThread.fireSuspendEvent(detail);
	}

	@Override
	public void suspend() throws DebugException {
	}

	@Override
	public void breakpointAdded(IBreakpoint breakpoint) {
		if (supportsBreakpoint(breakpoint)) {
			try {
				if (breakpoint.isEnabled()) {
					try {
						sendRequest("set "+((ILineBreakpoint) breakpoint).getLineNumber());
					} catch (CoreException e) {
					}
				}
			} catch (CoreException e) {
			}
		}
		
	}

	@Override
	public void breakpointChanged(IBreakpoint breakpoint, IMarkerDelta delta) {
		if (supportsBreakpoint(breakpoint)) {
			try {
				if (breakpoint.isEnabled()) {
					breakpointAdded(breakpoint);
				} else {
					breakpointRemoved(breakpoint, null);
				}
			} catch (CoreException e) {
			}
		}
	}

	@Override
	public void breakpointRemoved(IBreakpoint arg0, IMarkerDelta arg1) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean canDisconnect() {
		return false;
	}

	@Override
	public void disconnect() throws DebugException {
	}

	@Override
	public boolean isDisconnected() {
		return false;
	}

	@Override
	public IMemoryBlock getMemoryBlock(long startAddress, long length) throws DebugException {
		return null;
	}

	@Override
	public boolean supportsStorageRetrieval() {
		return false;
	}
	
	private void started() {
		fireCreationEvent();
		installDeferredBreakpoints();
		try {
			resume();
		} catch (DebugException e) {
		}
	}
	
	private void installDeferredBreakpoints() {
		IBreakpoint[] breakpoints = DebugPlugin.getDefault().getBreakpointManager().getBreakpoints(IXQueryConstants.ID_XQUERY_DEBUG_MODEL);
		for (int i = 0; i < breakpoints.length; i++) {
			breakpointAdded(breakpoints[i]);
		}
	}
	
	private void terminated() {
		fSuspended = false;
		DebugPlugin.getDefault().getBreakpointManager().removeBreakpointListener(this);
		fireTermonateEvent();
	}
	
	protected IStackFrame[] getStackFrames() throws DebugException {
		synchronized (fRequestSocket) {
			fRequestWriter.println("stack");
			fRequestWriter.flush();
			try {
				String framesData = fRequestReader.readLine();
				if (framesData != null) {
					String[] frames = framesData.split("#");
					IStackFrame[] theFrames = new IStackFrame[frames.length];
					for (int i = 0; i < frames.length; i++) {
						String data = frames[i];
						theFrames[frames.length - i - 1] = new XQueryStackFrame(fThread, data, i);
					}
					return theFrames;
				}
			} catch (IOException e) {
				abort("Unable to retrieve stack frames", e);
			}
		}
		return new IStackFrame[0];
	}
	
	protected void step() throws DebugException {
		sendRequest("step");
	}
	
	protected IValue getVariableValue(XQueryVariable variable) throws DebugException {
		synchronized (fRequestSocket) {
			fRequestWriter.println("var "+variable.getIdentifier() + " " + variable.getName());
			fRequestWriter.flush();
			try {
				String value = fRequestReader.readLine();
				return new XQueryValue(this, value);
			} catch (IOException e) {
				abort("Unable to retrieve value for variable "+variable.getName(), e);
			}
		}
		return null;
	}
	
	public IValue[] getDataStack() throws DebugException {
		synchronized (fRequestSocket) {
			fRequestWriter.println("data");
			fRequestWriter.flush();
			try {
				String valueString = fRequestReader.readLine();
				if (valueString != null && valueString.length() > 0) {
					String[] values = valueString.split("\\|");
					IValue[] theValues = new IValue[values.length];
					for (int i = 0; i < values.length; i++) {
						String value = values[values.length - i - 1];
						theValues[i] = new XQueryValue(this, value);
					}
				}
			} catch (IOException e) {
				abort("Unable to retrieve data stack", e);
			}
		}
		return new IValue[0];
	}
	
	private void sendRequest(String request) throws DebugException {
		synchronized (fRequestSocket) {
			fRequestWriter.println(request);
			fRequestWriter.flush();
			try {
				// wait for "ok"
				fRequestReader.readLine();
			} catch (IOException e) {
				abort("Request failed: "+request, e);
			}
		}
	}
	
	private void breakpointHit(String event) {
		//determine which breakpoint was hit, and set the thread's breakpoint
		int lastSpace = event.lastIndexOf(" ");
		if (lastSpace > 0) {
			String line = event.substring(lastSpace + 1);
			int lineNumber = Integer.parseInt(line);
			IBreakpoint[] breakpoints = DebugPlugin.getDefault().getBreakpointManager().getBreakpoints(IXQueryConstants.ID_XQUERY_DEBUG_MODEL);
			for (int i = 0; i < breakpoints.length; i++) {
				IBreakpoint breakpoint = breakpoints[i];
				if (supportsBreakpoint(breakpoint)) {
					if (breakpoint instanceof ILineBreakpoint) {
						ILineBreakpoint lineBreakpoint = (ILineBreakpoint) breakpoint;
						try {
							if (lineBreakpoint.getLineNumber() == lineNumber) {
								fThread.setBreakpoints(new IBreakpoint[]{breakpoint});
							}
						} catch (CoreException e) {
						}
					}
				}
			}
		}
		suspended(DebugEvent.BREAKPOINT);
	}

	class EventDispatchJob extends Job {

		public EventDispatchJob() {
			super("XQuery Event Dispatch");
			setSystem(true);
		}

		@Override
		protected IStatus run(IProgressMonitor monitor) {
			String event = "";
			while (!isTerminated() && event != null) {
				try {
					event = fEventReader.readLine();
					if (event != null) {
						fThread.setBreakpoints(null);
						fThread.setStepping(false);
						if (event.equals("started")) {
							started();
						} else if (event.equals("terminated")) {
							terminated();
						} else if (event.equals("resumed")) {
							if (event.endsWith("step")) {
								fThread.setStepping(true);
								resumed(DebugEvent.STEP_OVER);
							} else if (event.endsWith("client")) {
								resumed(DebugEvent.CLIENT_REQUEST);
							}
						} else if (event.equals("suspended")) {
							if (event.endsWith("step")) {
								suspended(DebugEvent.STEP_END);
							} else if (event.endsWith("client")) {
								suspended(DebugEvent.CLIENT_REQUEST);
							} else if (event.indexOf("breakpoint") >= 0) {
								breakpointHit(event);
							}
						}
					}
				} catch (IOException e) {
					terminated();
				}
			}
			return Status.OK_STATUS;
		}
		
	}
}
