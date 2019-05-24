// This file is automatically generated. Do not edit!
package com.sandpolis.core.attribute.key;

import com.sandpolis.core.attribute.AttributeGroupKey;
import com.sandpolis.core.attribute.AttributeKey;

public final class AK_VIEWER {
  /**
   * Viewer-only attributes.
   */
  public static final AttributeGroupKey VIEWER = new AttributeGroupKey(com.sandpolis.core.profile.store.DomainStore.get(null), 3, 0);

  /**
   * The viewer's username.
   */
  public static final AttributeKey<String> USERNAME = AttributeKey.newBuilder(VIEWER, 1).setDotPath("viewer.username").build();

  /**
   * The viewer's login IP address.
   */
  public static final AttributeKey<String> LOGIN_IP = AttributeKey.newBuilder(VIEWER, 2).setDotPath("viewer.login_ip").build();

  /**
   * The viewer's login timestamp.
   */
  public static final AttributeKey<Long> LOGIN_TIME = AttributeKey.newBuilder(VIEWER, 3).setDotPath("viewer.login_time").build();
}
