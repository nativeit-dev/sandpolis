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
open module com.sandpolis.server.vanilla {

	requires com.sandpolis.core.cs;
	requires com.sandpolis.core.foundation;
	requires com.sandpolis.core.instance;
	requires com.sandpolis.core.net;
	requires com.sandpolis.core.server;
	requires com.sandpolis.core.sv;
	requires io.netty.buffer;
	requires io.netty.codec;
	requires io.netty.common;
	requires io.netty.handler;
	requires io.netty.transport;
	requires org.hibernate.orm.core;
	requires java.persistence;
	requires java.naming;
	requires org.slf4j;
}
