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
package com.sandpolis.core.instance.state.oid;

import com.sandpolis.core.instance.state.STCollection;
import com.sandpolis.core.instance.state.VirtObject;

/**
 * An {@link Oid} that corresponds to a {@link STCollection}.
 *
 * @param <T> The type of the corresponding collection
 */
public class STCollectionOid<T extends VirtObject> extends OidBase implements AbsoluteOid<T>, RelativeOid<T> {

	public STCollectionOid(String oid) {
		super(oid);
	}

	public STCollectionOid(int[] oid) {
		super(oid);
	}

	@Override
	public STCollectionOid<?> resolve(int... tags) {
		return resolve(STCollectionOid::new, tags);
	}

	@Override
	public STCollectionOid<?> head(int length) {
		return head(STCollectionOid::new, length);
	}

	@Override
	public STCollectionOid<?> child(int tag) {
		return child(STCollectionOid::new, tag);
	}

	@Override
	public STCollectionOid<T> tail() {
		return tail(STCollectionOid::new);
	}

	@Override
	public STDocumentOid<?> parent() {
		return parent(STDocumentOid::new);
	}
}
