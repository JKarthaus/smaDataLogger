/*
 * yasdi4j -- Java Binding for YASDI
 * Copyright (c) 2008 Michael Denk <code@michaeldenk.de>
 *
 * Permission is hereby granted, free of charge, to any person
 * obtaining a copy of this software and associated documentation
 * files (the "Software"), to deal in the Software without
 * restriction, including without limitation the rights to use,
 * copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the
 * Software is furnished to do so, subject to the following
 * conditions:
 *
 * The above copyright notice and this permission notice shall be
 * included in all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND,
 * EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES
 * OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND
 * NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT
 * HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY,
 * WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING
 * FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR
 * OTHER DEALINGS IN THE SOFTWARE.
 */

package de.michaeldenk.yasdi4j;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * <code>YasdiMaster</code> is the high level interface to the YASDI master
 * library functions.
 * <p>
 * A typical invocation looks like this: After getting the instance of this
 * class, we have to initialize the master (this is to remind you, that you have
 * to call {@link #shutdown()} when you're done). initialize and shutdown may
 * only be called once. Once the master is shut down it is in a state where it
 * can't be used anymore, and it cannot be initialized again.<blockquote>
 * 
 * <pre>
 * YasdiMaster master = YasdiMaster.getInstance();
 * master.initialize();
 * </pre>
 * 
 * </blockquote>
 * <p>
 * After the initialization we have to set one or more drivers online (don't
 * forget to set them offline when you're done). For simplicity we only set the
 * first driver online.<blockquote>
 * 
 * <pre>
 * YasdiDriver[] drivers = master.getDrivers();
 * master.setDriverOnline(drivers[0]);
 * </pre>
 * 
 * </blockquote>
 * <p>
 * Now we can start the device detection. Let's say we have only one device.
 * <blockquote>
 * 
 * <pre>
 * master.detectDevices(1);
 * ... // do something with the devices
 * </pre>
 * 
 * </blockquote>
 * <p>
 * We're finished, clean up. <blockquote>
 * 
 * <pre>
 * master.setDriverOffline(drivers[0]);
 * master.shutdown();
 * </pre>
 * 
 * </blockquote>
 * 
 * @author Michael Denk <code@michaeldenk.de>
 */

public class YasdiMaster {

	static {
		System.loadLibrary("yasdi");
		System.loadLibrary("yasdimaster");
		System.loadLibrary("yasdi4j");
	}

	/**
	 * The different states of the master.
	 */
	public enum State {
		NOT_INITIALIZED, INITIALIZED, DEVICE_DETECTION, SHUTDOWN
	};

	@SuppressWarnings(value = "unused")
	private String configFile;

	private State state;

	private YasdiDriver[] drivers;
	private List<YasdiDriver> activeDrivers;
	private YasdiDevice[] devices;

	private List<YasdiDeviceListener> deviceListeners;

	private static YasdiMaster instance = new YasdiMaster();

	/**
	 * Constructs a new YasdiMaster that is initially not initialized.
	 */
	private YasdiMaster() {
		state = State.NOT_INITIALIZED;
		activeDrivers = new ArrayList<YasdiDriver>();
		drivers = new YasdiDriver[0];
		devices = new YasdiDevice[0];
		deviceListeners = new ArrayList<YasdiDeviceListener>();
	}

	/**
	 * Returns the only instance of <code>YasdiMaster</code>.
	 */
	public static YasdiMaster getInstance() {
		return instance;
	}

	/**
	 * Initializes the master with the INI file <tt>yasdi.ini</tt>. This is the
	 * same as calling {@link #initialize(String) initialize("yasdi.ini")}.
	 */
	public void initialize() throws IOException {
		initialize("yasdi.ini");
	}

	/**
	 * Initializes the master with the INI file <tt>configFile</tt>
	 * 
	 * @param configFile
	 * @throws IOException
	 *             if <tt>configFile</tt> doesn't exist or is not readable.
	 */
	public void initialize(String configFile) throws IOException {
		if (state.equals(State.SHUTDOWN)) {
			throw new IllegalStateException("Master is already shut down");
		}
		if (!state.equals(State.NOT_INITIALIZED)) {
			throw new IllegalStateException("Master is already initialized");
		}
		if (configFile == null) {
			throw new NullPointerException();
		}
		this.configFile = configFile;

		// initializes drivers
		c_initialize(configFile);

		state = State.INITIALIZED;
	}

	/**
	 * Shuts down the master. This method should be called when you're done
	 * using the master. When the master is shut down, you can't use it anymore
	 * and you can't initialize it again. Thus this is the final method to call.
	 */
	public void shutdown() {
		ensureInitialized();
		state = State.SHUTDOWN;
		deviceListeners.clear();
		for (YasdiDriver d : activeDrivers) {
			c_setDriverOffline(d.getId());
		}
		activeDrivers.clear();
		devices = new YasdiDevice[0];
		drivers = new YasdiDriver[0];
		c_shutdown();
	}

	/**
	 * Resets the master. After calling this method, the master is in the same
	 * state as after calling initialize.
	 */
	public void reset() {
		ensureInitialized();
		state = State.INITIALIZED;
		deviceListeners.clear();
		for (YasdiDriver d : activeDrivers) {
			c_setDriverOffline(d.getId());
		}
		activeDrivers.clear();
		devices = new YasdiDevice[0];
		c_reset();
	}

	/**
	 * Returns the number of available drivers.
	 */
	public int getNrDrivers() {
		return drivers.length;
	}

	/**
	 * Returns a copy of the array of available drivers.
	 */
	public YasdiDriver[] getDrivers() {
		YasdiDriver[] d = new YasdiDriver[drivers.length];
		System.arraycopy(drivers, 0, d, 0, d.length);
		return d;
	}

	/**
	 * Sets the driver <tt>d</tt> online.
	 * 
	 * @throws IOException
	 *             if the driver can't be set online
	 */
	public void setDriverOnline(YasdiDriver d) throws IOException {
		ensureInitialized();
		if (c_setDriverOnline(d.getId())) {
			activeDrivers.add(d);
		} else {
			throw new IOException("Unable to set driver " + d.getName()
					+ " online");
		}
	}

	/**
	 * Sets the driver <tt>d</tt> offline.
	 */
	public void setDriverOffline(YasdiDriver d) {
		ensureInitialized();
		c_setDriverOffline(d.getId());
		activeDrivers.remove(d);
	}

	/**
	 * Starts the device detection.
	 * 
	 * @param nrDevices
	 *            the minimum nr of devices to detect
	 * @throws IOException
	 *             if fewer than <tt>nrDevices</tt> devices are detected or if
	 *             no devices can be detected at all.
	 */
	public void detectDevices(int nrDevices) throws IOException {
		ensureInitialized();
		state = State.DEVICE_DETECTION;
		try {
			c_detectDevices(nrDevices);
		} catch (IOException e) {
			state = State.INITIALIZED;
			throw e;
		}
		state = State.INITIALIZED;
	}

	/**
	 * Returns the number of detected devices.
	 */
	public int getNrDevices() {
		return devices.length;
	}

	/**
	 * Returns a copy of the array of detected devices.
	 */
	public YasdiDevice[] getDevices() {
		YasdiDevice[] d = new YasdiDevice[devices.length];
		System.arraycopy(devices, 0, d, 0, d.length);
		return d;
	}

	/**
	 * Adds a device event listener to the device event listener list.
	 */
	public void addDeviceListener(YasdiDeviceListener l) {
		if (l == null) {
			return;
		}
		deviceListeners.add(l);
	}

	/**
	 * Removes a device event listener from the device event listener list.
	 */
	public void removeDeviceListener(YasdiDeviceListener l) {
		if (l == null) {
			return;
		}
		deviceListeners.remove(l);
	}

	/**
	 * Sets the access level of YASDI (see YASDI manual for details).
	 * 
	 * @param user
	 *            the user name
	 * @param password
	 *            the password
	 * @return true if the access level change was successful; false otherwise.
	 */
	public boolean setAccessLevel(String user, String password) {
		if (user == null || password == null) {
			throw new NullPointerException();
		}
		return c_setAccessLevel(user, password);
	}

	/**
	 * Returns the current state of the master.
	 */
	public State getState() {
		return state;
	}

	void ensureInitialized() {
		if (state.equals(State.NOT_INITIALIZED)) {
			throw new IllegalStateException("Master is not initialized");
		} else if (state.equals(State.SHUTDOWN)) {
			throw new IllegalStateException("Master is already shut down");
		} else if (state.equals(State.DEVICE_DETECTION)) {
			throw new IllegalStateException(
					"Master is in device detection state");
		}
	}

	// used by native code
	@SuppressWarnings(value = "unused")
	private void deviceAdded(int handle, String name, long sn, String type) {
		YasdiDeviceEvent e = new YasdiDeviceEvent(new YasdiDevice(this, handle,
				name, sn, type));
		for (YasdiDeviceListener l : deviceListeners) {
			l.deviceAdded(e);
		}
	}

	// used by native code
	@SuppressWarnings(value = "unused")
	private void deviceRemoved(int handle, String name, long sn, String type) {
		YasdiDeviceEvent e = new YasdiDeviceEvent(new YasdiDevice(this, handle,
				name, sn, type));
		for (YasdiDeviceListener l : deviceListeners) {
			l.deviceRemoved(e);
		}
	}

	private native void c_initialize(String configFile) throws IOException;

	private native void c_shutdown();

	private native void c_reset();

	private native boolean c_setDriverOnline(int DriverID);

	private native void c_setDriverOffline(int DriverID);

	private native void c_detectDevices(int nrDevices) throws IOException;

	private native boolean c_setAccessLevel(String user, String password);
}
