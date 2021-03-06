/*
    GNU GENERAL LICENSE
    Copyright (C) 2014 - 2018 Lobo Evolution

    This program is free software; you can redistribute it and/or
    modify it under the terms of the GNU General Public
    License as published by the Free Software Foundation; either
    verion 3 of the License, or (at your option) any later version.

    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
    General License for more details.

    You should have received a copy of the GNU General Public
    along with this program.  If not, see <http://www.gnu.org/licenses/>.
    

    Contact info: ivan.difrancesco@yahoo.it
 */
package org.loboevolution.main;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLStreamHandlerFactory;
import java.util.ArrayList;
import java.util.Collection;
import java.util.EventListener;
import java.util.EventObject;
import java.util.LinkedList;
import java.util.Properties;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

import org.loboevolution.clientlet.Clientlet;
import org.loboevolution.clientlet.ClientletRequest;
import org.loboevolution.clientlet.ClientletResponse;
import org.loboevolution.clientlet.ClientletSelector;
import org.loboevolution.security.GenericLocalPermission;
import org.loboevolution.ua.ConnectionProcessor;
import org.loboevolution.ua.NavigationEvent;
import org.loboevolution.ua.NavigationListener;
import org.loboevolution.ua.NavigationVetoException;
import org.loboevolution.ua.NavigatorErrorListener;
import org.loboevolution.ua.NavigatorExceptionEvent;
import org.loboevolution.ua.NavigatorExtension;
import org.loboevolution.ua.NavigatorExtensionContext;
import org.loboevolution.ua.NavigatorWindow;
import org.loboevolution.ua.UserAgent;
import org.loboevolution.util.EventDispatch2;

/**
 * Encapsulates a browser extension or plugin.
 */
public class Extension implements Comparable<Object>, NavigatorExtensionContext {

	/** The Constant ATTRIBUTE_EXTENSION_CLASS. */
	private static final String ATTRIBUTE_EXTENSION_CLASS = "extension.class";

	/** The Constant ATTRIBUTE_EXTENSION_PRIORITY. */
	private static final String ATTRIBUTE_EXTENSION_PRIORITY = "extension.priority";

	/** The Constant EXTENSION_PROPERTIES_FILE. */
	private static final String EXTENSION_PROPERTIES_FILE = "lobo-extension.properties";

	/** The Constant PRIMARY_EXTENSION_FILE_NAME. */
	private static final String PRIMARY_EXTENSION_FILE_NAME = "primary.jar";

	/**
	 * The minimum priority.
	 */
	public static final int LOW_PRIORITY = 0;

	/**
	 * The highest priority, only available to the default platform extension.
	 */
	public static final int PRIMARY_EXTENSION_PRIORITY = 10;

	/**
	 * The highest priority allowed for non-primary platform extensions.
	 */
	public static final int HIGH_PRIORITY = 9;

	/**
	 * The default priority.
	 */
	public static final int NORMAL_PRIORITY = 5;

	/** The priority. */
	private final int priority;

	/** The ext root. */
	private final File extRoot;

	/** The jar file. */
	private final JarFile jarFile;

	/** The ext class name. */
	private final String extClassName;

	/** The ext id. */
	private final String extId;

	/** The is primary. */
	private final boolean isPrimary;

	/** The is library. */
	private final boolean isLibrary;
	
	/** The class loader. */
	private ClassLoader classLoader;

	/** The platform extension. */
	private NavigatorExtension platformExtension;

	// TODO: Move these collections to ExtensionManager.
	// More efficient. Consider removal of extensions.

	/** The clientlet selectors. */
	private final Collection<ClientletSelector> clientletSelectors;

	/** The connection processors. */
	private final Collection<ConnectionProcessor> connectionProcessors;

	/** The navigation listeners. */
	private final Collection<NavigationListener> navigationListeners;

	/** The event. */
	private final EventDispatch2 EVENT = new NavigatorErrorEventDispatch();

	/**
	 * Instantiates a new extension.
	 *
	 * @param extRoot
	 *            the ext root
	 * @throws IOException
	 *             Signals that an I/O exception has occurred.
	 */
	public Extension(File extRoot) throws IOException {
		this.clientletSelectors = new LinkedList<ClientletSelector>();
		this.connectionProcessors = new ArrayList<ConnectionProcessor>();
		this.navigationListeners = new ArrayList<NavigationListener>();
		this.extRoot = extRoot;
		InputStream propsInputStream;
		if (extRoot.isDirectory()) {
			this.isPrimary = false;
			this.jarFile = null;
			this.extId = extRoot.getName();
			File propsFile = new File(extRoot, EXTENSION_PROPERTIES_FILE);
			propsInputStream = propsFile.exists() ? new FileInputStream(propsFile) : null;
		} else {
			JarFile jarFile = new JarFile(extRoot);
			this.isPrimary = extRoot.getName().equalsIgnoreCase(PRIMARY_EXTENSION_FILE_NAME);
			this.jarFile = jarFile;
			String name = extRoot.getName();
			int dotIdx = name.lastIndexOf('.');
			this.extId = dotIdx == -1 ? name : name.substring(0, dotIdx);
			JarEntry jarEntry = jarFile.getJarEntry(EXTENSION_PROPERTIES_FILE);
			propsInputStream = jarEntry == null ? null : jarFile.getInputStream(jarEntry);
		}
		this.isLibrary = propsInputStream == null;
		if (!this.isLibrary) {
			Properties mattribs = new Properties();
			try {
				mattribs.load(propsInputStream);
			} finally {
				propsInputStream.close();
			}
			String extClassName = mattribs.getProperty(ATTRIBUTE_EXTENSION_CLASS);
			if (extClassName == null) {
				throw new IOException("Property " + ATTRIBUTE_EXTENSION_CLASS + " missing in "
						+ EXTENSION_PROPERTIES_FILE + ", part of " + extRoot + ".");
			}
			this.extClassName = extClassName;
			String priorityText = mattribs.getProperty(ATTRIBUTE_EXTENSION_PRIORITY);
			if (priorityText != null) {
				int tp = Integer.parseInt(priorityText.trim());
				if (tp < LOW_PRIORITY) {
					tp = LOW_PRIORITY;
				} else if (tp > HIGH_PRIORITY) {
					tp = HIGH_PRIORITY;
				}
				this.priority = tp;
			} else {
				this.priority = NORMAL_PRIORITY;
			}
		} else {
			this.extClassName = null;
			this.priority = PRIMARY_EXTENSION_PRIORITY;
		}
	}

	/**
	 * Gets the id.
	 *
	 * @return the id
	 */
	public String getId() {
		return this.extId;
	}

	/**
	 * Gets the code source.
	 *
	 * @return the code source
	 * @throws MalformedURLException
	 *             the malformed url exception
	 */
	public URL getCodeSource() throws MalformedURLException {
		return this.extRoot.toURI().toURL();
	}

	/**
	 * Checks if is primary extension.
	 *
	 * @return true, if is primary extension
	 */
	public boolean isPrimaryExtension() {
		return this.isPrimary;
	}

	/**
	 * Checks if is library only.
	 *
	 * @return true, if is library only
	 */
	public boolean isLibraryOnly() {
		return this.isLibrary;
	}

	/**
	 * Inits the class loader.
	 *
	 * @param parentClassLoader
	 *            the parent class loader
	 * @throws MalformedURLException
	 *             the malformed url exception
	 * @throws ClassNotFoundException
	 *             the class not found exception
	 * @throws IllegalAccessException
	 *             the illegal access exception
	 * @throws InstantiationException
	 *             the instantiation exception
	 */
	public void initClassLoader(ClassLoader parentClassLoader)
			throws MalformedURLException, ClassNotFoundException, IllegalAccessException, InstantiationException {
		URL url = this.extRoot.toURI().toURL();
		URL[] urls = new URL[] { url };
		ExtensionClassLoader classLoader = new ExtensionClassLoader(urls, parentClassLoader);
		String extClassName = this.extClassName;
		NavigatorExtension pe = null;
		if (extClassName != null) {
			Class<?> extClass = classLoader.loadClass(extClassName);
			pe = (NavigatorExtension) extClass.newInstance();
		}
		synchronized (this) {
			this.classLoader = classLoader;
			this.platformExtension = pe;
		}
	}

	/**
	 * Gets the class loader.
	 *
	 * @return the class loader
	 */
	public ClassLoader getClassLoader() {
		synchronized (this) {
			return this.classLoader;
		}
	}

	/**
	 * Gets the navigator extension.
	 *
	 * @return the navigator extension
	 */
	public NavigatorExtension getNavigatorExtension() {
		synchronized (this) {
			return this.platformExtension;
		}
	}

	/**
	 * Inits the extension.
	 */
	public void initExtension() {
		Thread currentThread = Thread.currentThread();
		ClassLoader prevClassLoader = currentThread.getContextClassLoader();
		ClassLoader loader = this.classLoader;
		if (loader != null) {
			currentThread.setContextClassLoader(loader);
		}
		try {
			NavigatorExtension pe = this.platformExtension;
			if (pe != null) {
				pe.init(this);
			}
		} finally {
			currentThread.setContextClassLoader(prevClassLoader);
		}
	}

	/**
	 * Inits the extension window.
	 *
	 * @param wcontext
	 *            the wcontext
	 */
	public void initExtensionWindow(NavigatorWindow wcontext) {
		Thread currentThread = Thread.currentThread();
		ClassLoader prevClassLoader = currentThread.getContextClassLoader();
		ClassLoader loader = this.classLoader;
		if (loader != null) {
			currentThread.setContextClassLoader(loader);
		}
		try {
			NavigatorExtension pe = this.platformExtension;
			if (pe != null) {
				pe.windowOpening(wcontext);
			}
		} finally {
			currentThread.setContextClassLoader(prevClassLoader);
		}
	}

	/**
	 * Shutdown extension window.
	 *
	 * @param wcontext
	 *            the wcontext
	 */
	public void shutdownExtensionWindow(NavigatorWindow wcontext) {
		Thread currentThread = Thread.currentThread();
		ClassLoader prevClassLoader = currentThread.getContextClassLoader();
		ClassLoader loader = this.classLoader;
		if (loader != null) {
			currentThread.setContextClassLoader(loader);
		}
		try {
			NavigatorExtension pe = this.platformExtension;
			if (pe != null) {
				pe.windowClosing(wcontext);
			}
		} finally {
			currentThread.setContextClassLoader(prevClassLoader);
		}
	}

	/**
	 * Close.
	 *
	 * @throws IOException
	 *             Signals that an I/O exception has occurred.
	 */
	public void close() throws IOException {
		if (this.jarFile != null) {
			this.jarFile.close();
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.loboevolution.ua.NavigatorExtensionContext#addClientletSelector(org.
	 * loboevolution .clientlet.ClientletSelector)
	 */
	@Override
	public void addClientletSelector(ClientletSelector cs) {
		SecurityManager sm = System.getSecurityManager();
		if (sm != null) {
			sm.checkPermission(GenericLocalPermission.EXT_GENERIC);
		}
		synchronized (this) {
			this.clientletSelectors.add(cs);
		}
	}

	/**
	 * Gets the clientlet.
	 *
	 * @param request
	 *            the request
	 * @param response
	 *            the response
	 * @return the clientlet
	 */
	public Clientlet getClientlet(ClientletRequest request, ClientletResponse response) {
		// Need to set the class loader in thread context, otherwise
		// some library classes may not be found.
		Thread currentThread = Thread.currentThread();
		ClassLoader prevClassLoader = currentThread.getContextClassLoader();
		ClassLoader loader = this.classLoader;
		if (loader != null) {
			currentThread.setContextClassLoader(loader);
		}
		try {
			synchronized (this) {
				for (ClientletSelector cs : this.clientletSelectors) {
					Clientlet c = cs.select(request, response);
					if (c != null) {
						return c;
					}
				}
			}
			return null;
		} finally {
			currentThread.setContextClassLoader(prevClassLoader);
		}
	}

	/**
	 * Gets the last resort clientlet.
	 *
	 * @param request
	 *            the request
	 * @param response
	 *            the response
	 * @return the last resort clientlet
	 */
	public Clientlet getLastResortClientlet(ClientletRequest request, ClientletResponse response) {
		Thread currentThread = Thread.currentThread();
		ClassLoader prevClassLoader = currentThread.getContextClassLoader();
		ClassLoader loader = this.classLoader;
		if (loader != null) {
			currentThread.setContextClassLoader(loader);
		}
		try {
			synchronized (this) {
				for (ClientletSelector cs : this.clientletSelectors) {
					Clientlet c = cs.lastResortSelect(request, response);
					if (c != null) {
						return c;
					}
				}
			}
			return null;
		} finally {
			currentThread.setContextClassLoader(prevClassLoader);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.loboevolution.ua.NavigatorExtensionContext#addNavigatorErrorListener(
	 * org .loboevolution.ua.NavigatorErrorListener)
	 */
	@Override
	public void addNavigatorErrorListener(NavigatorErrorListener listener) {
		EVENT.addListener(listener);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.loboevolution.ua.NavigatorExtensionContext#removeNavigatorErrorListener
	 * (org.loboevolution.ua.NavigatorErrorListener)
	 */
	@Override
	public void removeNavigatorErrorListener(NavigatorErrorListener listener) {
		EVENT.removeListener(listener);
	}

	/**
	 * Handle error.
	 *
	 * @param event
	 *            the event
	 * @return True only if the event was dispatched to at least one listener.
	 */
	public boolean handleError(NavigatorExceptionEvent event) {
		// Expected in GUI thread.
		return EVENT.fireEvent(event);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.loboevolution.ua.NavigatorExtensionContext#addURLStreamHandlerFactory(
	 * java .net.URLStreamHandlerFactory)
	 */
	@Override
	public void addURLStreamHandlerFactory(URLStreamHandlerFactory factory) {
		// TODO: Since extensions are intialized in parallel,
		// this is not necessarily done in order of priority.
		org.loboevolution.main.PlatformStreamHandlerFactory.getInstance().addFactory(factory);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.loboevolution.ua.NavigatorExtensionContext#getUserAgent()
	 */
	@Override
	public UserAgent getUserAgent() {
		return org.loboevolution.request.UserAgentImpl.getInstance();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Comparable#compareTo(java.lang.Object)
	 */
	@Override
	public int compareTo(Object o) {
		// Reverse order based on priority.
		Extension other = (Extension) o;
		int diff = other.priority - this.priority;
		if (diff != 0) {
			return diff;
		}
		return this.extRoot.compareTo(other.extRoot);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		return this.priority | this.extRoot.hashCode();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object other) {
		if (!(other instanceof Extension)) {
			return false;
		}
		return ((Extension) other).extRoot.equals(this.extRoot);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "ExtensionInfo[extRoot=" + this.extRoot + ",isLibrary=" + this.isLibrary + "]";
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.loboevolution.ua.NavigatorExtensionContext#addConnectionProcessor(org.
	 * loboevolution.ua.ConnectionProcessor)
	 */
	@Override
	public void addConnectionProcessor(ConnectionProcessor processor) {
		SecurityManager sm = System.getSecurityManager();
		if (sm != null) {
			sm.checkPermission(GenericLocalPermission.EXT_GENERIC);
		}
		synchronized (this) {
			this.connectionProcessors.add(processor);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.loboevolution.ua.NavigatorExtensionContext#addNavigationListener(org.
	 * loboevolution.ua.NavigationListener)
	 */
	@Override
	public void addNavigationListener(NavigationListener listener) {
		SecurityManager sm = System.getSecurityManager();
		if (sm != null) {
			sm.checkPermission(GenericLocalPermission.EXT_GENERIC);
		}
		synchronized (this) {
			this.navigationListeners.add(listener);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.loboevolution.ua.NavigatorExtensionContext#removeClientletSelector(org.
	 * loboevolution.clientlet.ClientletSelector)
	 */
	@Override
	public void removeClientletSelector(ClientletSelector selector) {
		SecurityManager sm = System.getSecurityManager();
		if (sm != null) {
			sm.checkPermission(GenericLocalPermission.EXT_GENERIC);
		}
		synchronized (this) {
			this.clientletSelectors.remove(selector);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.loboevolution.ua.NavigatorExtensionContext#removeConnectionProcessor(
	 * org .loboevolution.ua.ConnectionProcessor)
	 */
	@Override
	public void removeConnectionProcessor(ConnectionProcessor processor) {
		SecurityManager sm = System.getSecurityManager();
		if (sm != null) {
			sm.checkPermission(GenericLocalPermission.EXT_GENERIC);
		}
		synchronized (this) {
			this.connectionProcessors.remove(processor);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.loboevolution.ua.NavigatorExtensionContext#removeNavigationListener(org
	 * .loboevolution.ua.NavigationListener)
	 */
	@Override
	public void removeNavigationListener(NavigationListener listener) {
		SecurityManager sm = System.getSecurityManager();
		if (sm != null) {
			sm.checkPermission(GenericLocalPermission.EXT_GENERIC);
		}
		synchronized (this) {
			this.navigationListeners.remove(listener);
		}
	}

	/**
	 * Dispatch before navigate.
	 *
	 * @param event
	 *            the event
	 * @throws NavigationVetoException
	 *             the navigation veto exception
	 */
	public void dispatchBeforeNavigate(NavigationEvent event) throws NavigationVetoException {
		// Should not be public
		Thread currentThread = Thread.currentThread();
		ClassLoader prevClassLoader = currentThread.getContextClassLoader();
		ClassLoader loader = this.classLoader;
		if (loader != null) {
			currentThread.setContextClassLoader(loader);
		}
		try {
			NavigationListener[] listeners;
			Collection<NavigationListener> nv = this.navigationListeners;
			synchronized (this) {
				if (nv.isEmpty()) {
					return;
				}
				listeners = nv.toArray(NavigationListener.EMPTY_ARRAY);
			}
			for (NavigationListener listener : listeners) {
				listener.beforeNavigate(event);
			}
		} finally {
			currentThread.setContextClassLoader(prevClassLoader);
		}
	}

	/**
	 * Dispatch before local navigate.
	 *
	 * @param event
	 *            the event
	 * @throws NavigationVetoException
	 *             the navigation veto exception
	 */
	public void dispatchBeforeLocalNavigate(NavigationEvent event) throws NavigationVetoException {
		Thread currentThread = Thread.currentThread();
		ClassLoader prevClassLoader = currentThread.getContextClassLoader();
		ClassLoader loader = this.classLoader;
		if (loader != null) {
			currentThread.setContextClassLoader(loader);
		}
		try {
			NavigationListener[] listeners;
			Collection<NavigationListener> nv = this.navigationListeners;
			synchronized (this) {
				if (nv.isEmpty()) {
					return;
				}
				listeners = nv.toArray(NavigationListener.EMPTY_ARRAY);
			}
			for (NavigationListener listener : listeners) {
				listener.beforeLocalNavigate(event);
			}
		} finally {
			currentThread.setContextClassLoader(prevClassLoader);
		}
	}

	/**
	 * Dispatch before window open.
	 *
	 * @param event
	 *            the event
	 * @throws NavigationVetoException
	 *             the navigation veto exception
	 */
	public void dispatchBeforeWindowOpen(NavigationEvent event) throws NavigationVetoException {
		// Should not be public
		Thread currentThread = Thread.currentThread();
		ClassLoader prevClassLoader = currentThread.getContextClassLoader();
		ClassLoader loader = this.classLoader;
		if (loader != null) {
			currentThread.setContextClassLoader(loader);
		}
		try {
			NavigationListener[] listeners;
			Collection<NavigationListener> nv = this.navigationListeners;
			synchronized (this) {
				if (nv.isEmpty()) {
					return;
				}
				listeners = nv.toArray(NavigationListener.EMPTY_ARRAY);
			}
			for (NavigationListener listener : listeners) {
				listener.beforeWindowOpen(event);
			}
		} finally {
			currentThread.setContextClassLoader(prevClassLoader);
		}
	}

	/**
	 * Dispatch pre connection.
	 *
	 * @param connection
	 *            the connection
	 * @return the URL connection
	 */
	public URLConnection dispatchPreConnection(URLConnection connection) {
		// Should not be public
		Thread currentThread = Thread.currentThread();
		ClassLoader prevClassLoader = currentThread.getContextClassLoader();
		ClassLoader loader = this.classLoader;
		if (loader != null) {
			currentThread.setContextClassLoader(loader);
		}
		try {
			ConnectionProcessor[] processors;
			Collection<ConnectionProcessor> cp = this.connectionProcessors;
			synchronized (this) {
				if (cp.isEmpty()) {
					return connection;
				}
				processors = cp.toArray(ConnectionProcessor.EMPTY_ARRAY);
			}
			for (ConnectionProcessor processor : processors) {
				connection = processor.processPreConnection(connection);
			}
			return connection;
		} finally {
			currentThread.setContextClassLoader(prevClassLoader);
		}
	}

	/**
	 * Dispatch post connection.
	 *
	 * @param connection
	 *            the connection
	 * @return the URL connection
	 */
	public URLConnection dispatchPostConnection(URLConnection connection) {
		// Should not be public
		Thread currentThread = Thread.currentThread();
		ClassLoader prevClassLoader = currentThread.getContextClassLoader();
		ClassLoader loader = this.classLoader;
		if (loader != null) {
			currentThread.setContextClassLoader(loader);
		}
		try {
			ConnectionProcessor[] processors;
			synchronized (this) {
				processors = this.connectionProcessors.toArray(ConnectionProcessor.EMPTY_ARRAY);
			}
			for (ConnectionProcessor processor : processors) {
				connection = processor.processPostConnection(connection);
			}
			return connection;
		} finally {
			currentThread.setContextClassLoader(prevClassLoader);
		}
	}

	/**
	 * The Class NavigatorErrorEventDispatch.
	 */
	private static class NavigatorErrorEventDispatch extends EventDispatch2 {

		/*
		 * (non-Javadoc)
		 * 
		 * @see org.loboevolution.util.EventDispatch2#dispatchEvent(java.util.
		 * EventListener, java.util.EventObject)
		 */
		@Override
		protected void dispatchEvent(EventListener listener, EventObject event) {
			((NavigatorErrorListener) listener).errorOcurred((NavigatorExceptionEvent) event);
		}
	}
}
