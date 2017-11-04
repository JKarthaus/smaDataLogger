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
 * <code>YasdiChannelAccessRights</code> represents the access rights of a
 * channel. The access rights depend on the access level that can be set with
 * setAccessLevel(String, String) in <code>YasdiMaster</code>.
 * 
 * @author Michael Denk <code@michaeldenk.de>
 */
public class YasdiChannelAccessRights {
	private boolean canRead;
	private boolean canWrite;

	private YasdiChannelAccessRights(boolean canRead, boolean canWrite) {
		super();
		this.canRead = canRead;
		this.canWrite = canWrite;
	}

	/**
	 * Returns true if this channel can be read.
	 */
	public boolean canRead() {
		return canRead;
	}

	/**
	 * Returns true if this channel can be written to.
	 */
	public boolean canWrite() {
		return canWrite;
	}

	@Override
	public String toString() {
		return "[access rights:" + "read: " + canRead + ", write: " + canWrite
				+ "]";
	}
}