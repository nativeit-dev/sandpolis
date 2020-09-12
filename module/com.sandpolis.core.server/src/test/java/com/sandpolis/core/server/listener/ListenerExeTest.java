//============================================================================//
//                                                                            //
//                Copyright © 2015 - 2020 Subterranean Security               //
//                                                                            //
//  This source file is subject to the terms of the Mozilla Public License    //
//  version 2. You may not use this file except in compliance with the MPL    //
//  as published by the Mozilla Foundation at:                                //
//                                                                            //
//    https://mozilla.org/MPL/2.0                                             //
//                                                                            //
//=========================================================S A N D P O L I S==//
package com.sandpolis.core.server.listener;

import static com.sandpolis.core.instance.store.thread.ThreadStore.ThreadStore;
import static com.sandpolis.core.server.listener.ListenerStore.ListenerStore;
import static com.sandpolis.core.server.user.UserStore.UserStore;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.concurrent.Executors;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import com.sandpolis.core.net.MsgListener.RQ_AddListener;
import com.sandpolis.core.net.exelet.ExeletTest;
import com.sandpolis.core.server.listener.ListenerExe;
import com.sandpolis.core.instance.Listener.ListenerConfig;
import com.sandpolis.core.instance.User.UserConfig;
import com.sandpolis.core.instance.Result.Outcome;

class ListenerExeTest extends ExeletTest {

	@BeforeEach
	void setup() {
		UserStore.init(config -> {
			config.ephemeral();

			config.defaults.add(UserConfig.newBuilder().setUsername("junit").setPassword("12345678").build());
		});
		ListenerStore.init(config -> {
			config.ephemeral();

			config.defaults
					.add(ListenerConfig.newBuilder().setOwner("junit").setPort(5000).setAddress("0.0.0.0").build());
		});
		ThreadStore.init(config -> {
			config.ephemeral();

			config.defaults.put("store.event_bus", Executors.newSingleThreadExecutor());
		});

		initTestContext();
	}

	@Test
	void testDeclaration() {
		testNameUniqueness(ListenerExe.class);
	}

	@Test
	@DisplayName("Add a listener with a valid configuration")
	void rq_add_listener_1() {
		var rq = RQ_AddListener.newBuilder()
				.setConfig(ListenerConfig.newBuilder().setId(2).setOwner("junit").setPort(5000).setAddress("0.0.0.0"))
				.build();
		var rs = ListenerExe.rq_add_listener(rq);

		assertTrue(((Outcome) rs).getResult());
	}

}