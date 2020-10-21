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
package com.sandpolis.core.net.state.st.entangled;

import java.util.Collection;
import java.util.Objects;
import java.util.function.Consumer;
import java.util.function.Function;

import com.sandpolis.core.instance.State.ProtoCollection;
import com.sandpolis.core.instance.state.oid.Oid;
import com.sandpolis.core.instance.state.oid.RelativeOid;
import com.sandpolis.core.instance.state.st.AbstractSTObject;
import com.sandpolis.core.instance.state.st.STCollection;
import com.sandpolis.core.instance.state.st.STDocument;
import com.sandpolis.core.instance.state.st.STObject;
import com.sandpolis.core.instance.state.st.STRelation;
import com.sandpolis.core.instance.state.vst.VirtObject;
import com.sandpolis.core.instance.store.StoreMetadata;
import com.sandpolis.core.net.state.STCmd.STSyncStruct;

public class EntangledCollection extends EntangledObject<ProtoCollection> implements STCollection {

	private STCollection container;

	public EntangledCollection(STCollection container, STSyncStruct config) {
		this.container = Objects.requireNonNull(container);

		if (container instanceof EntangledObject)
			throw new IllegalArgumentException("Entanged objects cannot be nested");

		// Start streams
		switch (config.direction) {
		case BIDIRECTIONAL:
			startSource(config);
			startSink(config, ProtoCollection.class);
			break;
		case DOWNSTREAM:
			if (config.initiator) {
				startSink(config, ProtoCollection.class);
			} else {
				startSource(config);
			}
			break;
		case UPSTREAM:
			if (config.initiator) {
				startSource(config);
			} else {
				startSink(config, ProtoCollection.class);
			}
			break;
		default:
			throw new IllegalArgumentException();
		}
	}

	// Begin boilerplate

	@Override
	public void addListener(Object listener) {
		container.addListener(listener);
	}

	@Override
	public <E extends VirtObject> STRelation<E> collectionList(Function<STDocument, E> constructor) {
		return container.collectionList(constructor);
	}

	@Override
	public STDocument document(long tag) {
		return container.document(tag);
	}

	@Override
	public Collection<STDocument> documents() {
		return container.documents();
	}

	@Override
	public STDocument getDocument(long tag) {
		return container.getDocument(tag);
	}

	@Override
	public StoreMetadata getMetadata() {
		return container.getMetadata();
	}

	@Override
	public long getTag() {
		return ((AbstractSTObject) container).getTag();
	}

	@Override
	public void merge(ProtoCollection snapshot) {
		container.merge(snapshot);
	}

	@Override
	public STDocument newDocument() {
		return container.newDocument();
	}

	@Override
	public Oid oid() {
		return container.oid();
	}

	@Override
	public AbstractSTObject parent() {
		return ((AbstractSTObject) container).parent();
	}

	@Override
	public void remove(STDocument document) {
		container.remove(document);
	}

	@Override
	public void removeListener(Object listener) {
		container.removeListener(listener);
	}

	@Override
	public void setDocument(long tag, STDocument document) {
		container.setDocument(tag, document);
	}

	@Override
	public void setTag(long tag) {
		container.setTag(tag);
	}

	@Override
	public int size() {
		return container.size();
	}

	@Override
	public ProtoCollection snapshot(RelativeOid... oids) {
		return container.snapshot(oids);
	}

	@Override
	protected STObject<ProtoCollection> container() {
		return container;
	}

	@Override
	public void forEachDocument(Consumer<STDocument> consumer) {
		container.forEachDocument(consumer);
	}

}
