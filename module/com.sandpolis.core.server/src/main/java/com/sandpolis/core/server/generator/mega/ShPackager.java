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
package com.sandpolis.core.server.generator.mega;

import com.sandpolis.core.instance.Generator.GenConfig;
import com.sandpolis.core.server.generator.MegaGen;

/**
 * This generator produces a shell script.
 *
 * @author cilki
 * @since 5.0.0
 */
public class ShPackager extends MegaGen {
	public ShPackager(GenConfig config) {
		super(config, ".sh", "/lib/sandpolis-client-installer.sh");
	}

	@Override
	protected byte[] generate() throws Exception {
		return null;
	}
}