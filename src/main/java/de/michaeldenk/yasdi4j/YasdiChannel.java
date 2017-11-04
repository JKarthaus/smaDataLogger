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

/**
 * <code>YasdiChannel</code> represents a single channel of a device.
 * 
 * @author Michael Denk <code@michaeldenk.de>
 */
public class YasdiChannel {
	/**
	 * The different channel types.
	 */
	public enum Type {
		ANALOG, DIGITAL, COUNTER, STATUS
	};

	private int handle;
	private String name;
	private String unit;
	private double value;
	private String valueText;
	boolean hasText;
	private YasdiDevice device;
	private int mask;
	private Type type;
	private YasdiChannelAccessRights accessRights;

	private YasdiChannel(int handle, YasdiDevice device, String name,
			String unit, boolean hasText, int mask,
			YasdiChannelAccessRights accessRights) {
		super();
		this.handle = handle;
		this.name = name;
		this.unit = unit;
		this.hasText = hasText;
		this.device = device;
		this.mask = mask;
		this.accessRights = accessRights;
		if ((mask & 0x0001) > 0) {
			type = Type.ANALOG;
		} else if ((mask & 0x0002) > 0) {
			type = Type.DIGITAL;
		} else if ((mask & 0x0004) > 0) {
			type = Type.COUNTER;
		} else if ((mask & 0x0008) > 0) {
			type = Type.STATUS;
		}
	}

	/**
	 * Updates the value of this channel, if the current value is older than
	 * <tt>maxValueAge</tt> seconds.
	 * 
	 * @param maxValueAge
	 *            The maximum value age in seconds.
	 * @throws IOException
	 *             if an I/O error occurs.
	 */
	public void updateValue(int maxValueAge) throws IOException {
		device.master.ensureInitialized();
		c_updateValue(handle, device.getHandle(), maxValueAge);
	}

	/**
	 * Returns the channel mask (see YASDI manual for details).
	 */
	public int getMask() {
		return mask;
	}

	/**
	 * Returns the channel type.
	 */
	public Type getType() {
		return type;
	}

	/**
	 * Returns true if this channel is a text value channel.
	 */
	public boolean hasText() {
		return hasText;
	}

	/**
	 * Gets the current channel value.
	 */
	public double getValue() {
		return value;
	}

	/**
	 * Gets the current channel value (if this channel has a text value).
	 */
	public String getValueText() {
		return valueText;
	}

	/**
	 * Returns the channel name.
	 */
	public String getName() {
		return name;
	}

	/**
	 * Returns the channel unit.
	 */
	public String getUnit() {
		return unit;
	}

	/**
	 * Returns the device that has this channel.
	 */
	public YasdiDevice getDevice() {
		return device;
	}

	/**
	 * Returns the channel access rights for this channel. This method may
	 * return null.
	 */
	public YasdiChannelAccessRights getAccessRights() {
		return accessRights;
	}

	@Override
	public String toString() {
		return name + " (" + unit + ")";
	}

	@Override
	public boolean equals(Object obj) {
		YasdiChannel c = (YasdiChannel) obj;
		if (!device.equals(c.device)) {
			return false;
		}
		return handle == c.handle;
	}

	@Override
	public int hashCode() {
		return 100 * device.getHandle() + handle;
	}

	private native void c_updateValue(int chanHandle, int devHandle,
			int maxValueAge) throws IOException;
}