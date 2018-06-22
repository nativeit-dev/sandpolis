/******************************************************************************
 *                                                                            *
 *                    Copyright 2017 Subterranean Security                    *
 *                                                                            *
 *  Licensed under the Apache License, Version 2.0 (the "License");           *
 *  you may not use this file except in compliance with the License.          *
 *  You may obtain a copy of the License at                                   *
 *                                                                            *
 *      http://www.apache.org/licenses/LICENSE-2.0                            *
 *                                                                            *
 *  Unless required by applicable law or agreed to in writing, software       *
 *  distributed under the License is distributed on an "AS IS" BASIS,         *
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  *
 *  See the License for the specific language governing permissions and       *
 *  limitations under the License.                                            *
 *                                                                            *
 *****************************************************************************/
package com.sandpolis.core.net;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.lang.reflect.Method;

import com.sandpolis.core.net.handler.ExecuteHandler;
import com.sandpolis.core.proto.net.MSG.Message;
import com.sandpolis.core.proto.net.MSG.Message.MsgOneofCase;

import io.netty.channel.embedded.EmbeddedChannel;

public class ExeletTest {

	/**
	 * The {@code Channel} that the {@code Exelet} is connected to.
	 */
	protected EmbeddedChannel channel;

	/**
	 * Test the following aspects of an {@code Exelet} class:
	 * 
	 * <ul>
	 * <li>All public methods are unique in name</li>
	 * <li>All public methods have a name that matches a {@code Message} type</li>
	 * <li>All public methods have a single {@code Message} parameter</li>
	 * </ul>
	 * 
	 * @param _class
	 *            The {@code Exelet} to be tested.
	 */
	protected void testDeclaration(Class<? extends Exelet> _class) {
		for (Method m : _class.getMethods()) {
			if (m.getDeclaringClass() == _class) {
				// Check parameters
				Class<?>[] params = m.getParameterTypes();
				assertEquals(1, params.length);
				assertEquals(Message.class, params[0]);

				// Check name
				try {
					MsgOneofCase.valueOf(m.getName().toUpperCase());
				} catch (IllegalArgumentException e) {
					fail("Missing Message for method: " + m.getName());
				}
			}
		}
	}

	/**
	 * Initialize a new testing channel.
	 */
	protected void initChannel() {
		channel = new EmbeddedChannel();
		channel.attr(Sock.EXECUTE_HANDLER_KEY).set(new ExecuteHandler(new Class[] {}));
		channel.attr(Sock.CVID_KEY).set(0);
	}

}