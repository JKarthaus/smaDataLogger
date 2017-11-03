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

/**
 * <code>YasdiDevice</code> represents a device with multiple channels. Each
 * device has a unique serial number and a unique name.
 * 
 * @author Michael Denk <code@michaeldenk.de>
 */
public class YasdiDevice {
	public static final int SPOT_VALUE_CHANNEL_MASK = 0x090f;
	public static final int PARAMETER_CHANNEL_MASK = 0x040f;

	YasdiMaster master;

	private int handle;
	private String name;
	private long sn;
	private String type;

	YasdiDevice(YasdiMaster master, int handle, String name, long sn,
			String type) {
		this.master = master;
		this.handle = handle;
		this.name = name;
		this.sn = sn;
		this.type = type;
	}

	/**
	 * Returns the device name. The device name is a String of the form
	 * <tt>WR700-07 SN:9380933</tt>, i.e. <blockquote>
	 * 
	 * <pre>
	 * getType() + &quot; SN:&quot; + getSN();
	 *</pre>
	 * 
	 * </blockquote>
	 */
	public String getName() {
		return name;
	}

	/**
	 * Returns the serial number of this device.
	 */
	public long getSN() {
		return sn;
	}

	/**
	 * Returns the device type. The device type is a String of exactly 8
	 * characters.
	 */
	public String getType() {
		return type;
	}

	/**
	 * Returns all spot value channels of this device. This is the same as
	 * calling {@link #getChannels(int, int)
	 * getChannels(SPOT_VALUE_CHANNEL_MASK, 0)}.
	 */
	public YasdiChannel[] getSpotValueChannels() {
		return getChannels(SPOT_VALUE_CHANNEL_MASK, 0);
	}

	/**
	 * Returns all parameter channels of this device. This is the same as
	 * calling {@link #getChannels(int, int) getChannels(PARAMETER_CHANNEL_MASK,
	 * 0)}.
	 */
	public YasdiChannel[] getParameterChannels() {
		return getChannels(PARAMETER_CHANNEL_MASK, 0);
	}

	/**
	 * Returns all channels matching <tt>channelMask</tt> and
	 * <tt>channelIndex</tt>. If <tt>channelIndex</tt> is 0, all channels
	 * matching <tt>channelMask</tt> are returned; otherwise only the one
	 * channel matching <tt>channelMask</tt> with the specified index is
	 * returned (see YASDI manual for details).
	 */
	public YasdiChannel[] getChannels(int channelMask, int channelIndex) {
		master.ensureInitialized();
		return c_getChannels(handle, channelMask, channelIndex);
	}

	/**
	 * Return the first channel that has the name <tt>name</tt>.
	 */
	public YasdiChannel getChannel(String name) {
		master.ensureInitialized();
		return c_findChannel(handle, name);
	}

	@Override
	public String toString() {
		return name;
	}

	@Override
	public boolean equals(Object obj) {
		return handle == ((YasdiDevice) obj).handle;
	}

	@Override
	public int hashCode() {
		return handle;
	}

	int getHandle() {
		return handle;
	}

	private native YasdiChannel[] c_getChannels(int devHandle, int channelMask,
			int channelIndex);

	private native YasdiChannel c_findChannel(int handle, String name);
}