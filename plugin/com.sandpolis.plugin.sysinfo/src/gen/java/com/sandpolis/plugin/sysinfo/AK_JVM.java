// This file is automatically generated. Do not edit!
package com.sandpolis.plugin.sysinfo;

import com.sandpolis.core.attribute.AttributeGroupKey;
import com.sandpolis.core.attribute.AttributeKey;

public final class AK_JVM {
  /**
   * TODO.
   */
  public static final AttributeGroupKey JVM = new AttributeGroupKey(com.sandpolis.core.profile.store.DomainStore.get("com.sandpolis.plugin.sysinfo"), 3, 0);

  /**
   * JVM architecture.
   */
  public static final AttributeKey<String> ARCH = AttributeKey.newBuilder(JVM, 1).setStatic(true).build();

  /**
   * JVM base directory.
   */
  public static final AttributeKey<String> PATH = AttributeKey.newBuilder(JVM, 2).setStatic(true).build();

  /**
   * The JVM start time.
   */
  public static final AttributeKey<Long> START_TIMESTAMP = AttributeKey.newBuilder(JVM, 3).build();

  /**
   * The JVM vendor name.
   */
  public static final AttributeKey<String> VENDOR = AttributeKey.newBuilder(JVM, 4).setStatic(true).build();

  /**
   * The JVM version.
   */
  public static final AttributeKey<String> VERSION = AttributeKey.newBuilder(JVM, 5).build();
}