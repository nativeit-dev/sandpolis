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
package com.sandpolis.core.server.state;

import java.security.cert.X509Certificate;
import java.util.List;

import com.sandpolis.core.instance.Metatypes.InstanceFlavor;
import com.sandpolis.core.instance.Metatypes.InstanceType;
import com.sandpolis.core.instance.State.ProtoAttributeValue;
import com.sandpolis.core.instance.state.BooleanAttributeValue;
import com.sandpolis.core.instance.state.BooleanListAttributeValue;
import com.sandpolis.core.instance.state.InstanceFlavorAttributeValue;
import com.sandpolis.core.instance.state.InstanceFlavorListAttributeValue;
import com.sandpolis.core.instance.state.InstanceTypeAttributeValue;
import com.sandpolis.core.instance.state.InstanceTypeListAttributeValue;
import com.sandpolis.core.instance.state.IntegerAttributeValue;
import com.sandpolis.core.instance.state.IntegerListAttributeValue;
import com.sandpolis.core.instance.state.LongAttributeValue;
import com.sandpolis.core.instance.state.LongListAttributeValue;
import com.sandpolis.core.instance.state.StringAttributeValue;
import com.sandpolis.core.instance.state.StringListAttributeValue;
import com.sandpolis.core.instance.state.X509CertificateAttributeValue;

/**
 * A wrapper for a {@link HibernateAttribute}'s value. All implementations are
 * automatically generated by the codegen plugin.
 *
 * @param <T> The type of the value
 */
abstract class HibernateAttributeValue<T> {

	/**
	 * Directly get the value.
	 *
	 * @return The value
	 */
	public abstract T get();

	/**
	 * Directly set the value.
	 *
	 * @param value The new value
	 */
	public abstract void set(T value);

	/**
	 * Build a new {@link HibernateAttributeValue} of the same type, but not
	 * necessarily with the same content.
	 */
	public abstract HibernateAttributeValue<T> clone();

	/**
	 * Get the value as a {@link ProtoAttributeValue}.
	 *
	 * @return The value
	 */
	public abstract ProtoAttributeValue.Builder getProto();

	/**
	 * Set the value from a {@link ProtoAttributeValue}.
	 *
	 * @param av The new value
	 */
	public abstract void setProto(ProtoAttributeValue av);

	/**
	 * Build a new attribute value according to the type of a test value. This is
	 * only necessary to build the very first {@link HibernateAttributeValue} for an
	 * attribute. It's more efficient to call {@link #clone} on an existing
	 * attribute value.
	 *
	 * @param test The test value which is only relevant for its type
	 * @return A new {@link HibernateAttributeValue} of the correct type
	 */
	@SuppressWarnings("unchecked")
	static <T> HibernateAttributeValue<T> newAttributeValue(T test) {
//		if (test instanceof String) {
//			return (HibernateAttributeValue<T>) new StringAttributeValue();
//		} else if (test instanceof Boolean) {
//			return (HibernateAttributeValue<T>) new BooleanAttributeValue();
//		} else if (test instanceof Integer) {
//			return (HibernateAttributeValue<T>) new IntegerAttributeValue();
//		} else if (test instanceof InstanceFlavor) {
//			return (HibernateAttributeValue<T>) new InstanceFlavorAttributeValue();
//		} else if (test instanceof InstanceType) {
//			return (HibernateAttributeValue<T>) new InstanceTypeAttributeValue();
//		} else if (test instanceof Long) {
//			return (HibernateAttributeValue<T>) new LongAttributeValue();
//		} else if (test instanceof X509Certificate) {
//			return (HibernateAttributeValue<T>) new X509CertificateAttributeValue();
//		} else if (test instanceof List) {
//			var list = ((List<?>) test);
//			if (list.size() == 0)
//				throw new IllegalArgumentException("List cannot be empty");
//
//			// Take an element from the list for type discovery
//			var item = list.get(0);
//			if (item instanceof String) {
//				return (HibernateAttributeValue<T>) new StringListAttributeValue();
//			} else if (item instanceof Boolean) {
//				return (HibernateAttributeValue<T>) new BooleanListAttributeValue();
//			} else if (item instanceof Integer) {
//				return (HibernateAttributeValue<T>) new IntegerListAttributeValue();
//			} else if (item instanceof InstanceFlavor) {
//				return (HibernateAttributeValue<T>) new InstanceFlavorListAttributeValue();
//			} else if (item instanceof InstanceType) {
//				return (HibernateAttributeValue<T>) new InstanceTypeListAttributeValue();
//			} else if (item instanceof Long) {
//				return (HibernateAttributeValue<T>) new LongListAttributeValue();
//			}
//		}

		throw new IllegalArgumentException("Unknown type");
	}
}
