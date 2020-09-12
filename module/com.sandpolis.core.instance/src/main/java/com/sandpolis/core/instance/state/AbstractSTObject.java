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
package com.sandpolis.core.instance.state;

import static com.sandpolis.core.instance.state.STStore.STStore;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.eventbus.EventBus;

public abstract class AbstractSTObject {

	private static final Logger log = LoggerFactory.getLogger(AbstractSTObject.class);

	private EventBus bus;

	/**
	 * The number of listeners registered to the {@link #bus}.
	 */
	private int listeners;

	protected synchronized <T> void fireAttributeValueChangedEvent(STAttribute<T> attribute, T oldValue, T newValue) {
		if (getTag() == 0)
			return;

		if (log.isTraceEnabled()) {
			log.trace("Attribute ({}) changed value from \"{}\" to \"{}\"", attribute.oid(), oldValue, newValue);
		}

		if (bus != null) {
			STStore.pool().submit(() -> {
				bus.post(new STAttribute.ChangeEvent<T>(attribute, oldValue, newValue));
			});
		}

		if (parent() != null)
			parent().fireAttributeValueChangedEvent(attribute, oldValue, newValue);
	}

	protected synchronized void fireCollectionAddedEvent(STCollection collection, STDocument newDocument) {
		if (getTag() == 0)
			return;

		if (log.isTraceEnabled()) {
			log.trace("Document ({}) added to collection ({})", newDocument.oid(), collection.oid());
		}

		if (bus != null) {
			STStore.pool().submit(() -> {
				bus.post(new STCollection.DocumentAddedEvent(collection, newDocument));
			});
		}

		if (parent() != null)
			parent().fireCollectionAddedEvent(collection, newDocument);
	}

	protected synchronized void fireCollectionRemovedEvent(STCollection collection, STDocument oldDocument) {
		if (getTag() == 0)
			return;

		if (log.isTraceEnabled()) {
			log.trace("Document ({}) removed from collection ({})", oldDocument.oid(), collection.oid());
		}

		if (bus != null) {
			STStore.pool().submit(() -> {
				bus.post(new STCollection.DocumentRemovedEvent(collection, oldDocument));
			});
		}

		if (parent() != null)
			parent().fireCollectionRemovedEvent(collection, oldDocument);
	}

	public synchronized void addListener(Object listener) {
		if (bus == null) {
			bus = new EventBus();
		}
		bus.register(listener);
		listeners++;
	}

	public synchronized void removeListener(Object listener) {
		if (bus != null) {
			bus.unregister(listener);
			listeners--;
		}
		if (listeners == 0) {
			bus = null;
		}
	}

	public abstract int getTag();

	public abstract AbstractSTObject parent();
}