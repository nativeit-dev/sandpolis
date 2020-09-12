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

import java.util.function.Function;

import com.sandpolis.core.instance.State.ProtoAttributeValue;

/**
 * A wrapper for a {@link EphemeralAttribute}'s value. All implementations are
 * automatically generated by the codegen plugin.
 *
 * @param <T> The type of the value
 */
public class EphemeralAttributeValue<T> implements STAttributeValue<T> {

	private static final Function<String, ProtoAttributeValue.Builder> STRING_SERIALIZER = value -> ProtoAttributeValue
			.newBuilder().addString(value);

	private static final Function<ProtoAttributeValue, String> STRING_DESERIALIZER = proto -> proto.getString(0);

	private T value;

	private long timestamp;

	private Function<T, ProtoAttributeValue.Builder> serializer;

	private Function<ProtoAttributeValue, T> deserializer;

	public EphemeralAttributeValue(T value) {
		this(value, System.currentTimeMillis());
	}

	public EphemeralAttributeValue(T value, long timestamp) {
		this.value = value;
		this.timestamp = timestamp;

		// Set serializer/deserializer
		if (value instanceof String) {
			serializer = (Function) STRING_SERIALIZER;
			deserializer = (Function) STRING_DESERIALIZER;
		}
//		else if (value instanceof Boolean) {
//			return (EphemeralAttributeValue<T>) new BooleanAttributeValue();
//		} else if (value instanceof Integer) {
//			return (EphemeralAttributeValue<T>) new IntegerAttributeValue();
//		} else if (value instanceof InstanceFlavor) {
//			return (EphemeralAttributeValue<T>) new InstanceFlavorAttributeValue();
//		} else if (value instanceof InstanceType) {
//			return (EphemeralAttributeValue<T>) new InstanceTypeAttributeValue();
//		} else if (value instanceof Long) {
//			return (EphemeralAttributeValue<T>) new LongAttributeValue();
//		} else if (value instanceof X509Certificate) {
//			return (EphemeralAttributeValue<T>) new X509CertificateAttributeValue();
//		} else if (value instanceof List) {
//			var list = ((List<?>) test);
//			if (list.size() == 0)
//				throw new IllegalArgumentException("List cannot be empty");
//
//			// Take an element from the list for type discovery
//			var item = list.get(0);
//			if (item instanceof String) {
//				return (EphemeralAttributeValue<T>) new StringListAttributeValue();
//			} else if (item instanceof Boolean) {
//				return (EphemeralAttributeValue<T>) new BooleanListAttributeValue();
//			} else if (item instanceof Integer) {
//				return (EphemeralAttributeValue<T>) new IntegerListAttributeValue();
//			} else if (item instanceof InstanceFlavor) {
//				return (EphemeralAttributeValue<T>) new InstanceFlavorListAttributeValue();
//			} else if (item instanceof InstanceType) {
//				return (EphemeralAttributeValue<T>) new InstanceTypeListAttributeValue();
//			} else if (item instanceof Long) {
//				return (EphemeralAttributeValue<T>) new LongListAttributeValue();
//			}
//		}
//
//		throw new IllegalArgumentException("Unknown type");
	}

	/**
	 * Directly get the value.
	 *
	 * @return The value
	 */
	@Override
	public T get() {
		return value;
	}

	/**
	 * Directly set the value.
	 *
	 * @param value The new value
	 */
	@Override
	public void set(T value) {
		this.value = value;
	}

	@Override
	public long getTimestamp() {
		return timestamp;
	}

	@Override
	public void setTimestamp(long timestamp) {
		this.timestamp = timestamp;
	}

	/**
	 * Get the value as a {@link ProtoAttributeValue}.
	 *
	 * @return The value
	 */
	@Override
	public ProtoAttributeValue toProto() {
		return serializer.apply(value).setTimestamp(timestamp).build();
	}

	/**
	 * Set the value from a {@link ProtoAttributeValue}.
	 *
	 * @param av The new value
	 */
	@Override
	public void fromProto(ProtoAttributeValue av) {
		this.timestamp = av.getTimestamp();
		this.value = deserializer.apply(av);
	}
}
