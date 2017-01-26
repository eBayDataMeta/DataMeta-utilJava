package org.ebay.datameta.util.guavaX;

import org.ebay.datameta.util.jdk.Api;
import com.google.common.base.Objects;

import javax.annotation.Nullable;
import java.util.Map;

/**
 * For whatever reason, {@link com.google.common.collect.AbstractMapEntry} class is made package-local.
 * @author Google Guava team
 */
@Api public abstract class AbstractMapEntry<K, V> implements Map.Entry<K, V> {

  @Override
  public abstract K getKey();

  @Override
  public abstract V getValue();

  @Override
  public V setValue(V value) {
    throw new UnsupportedOperationException();
  }

  @Override public boolean equals(@Nullable Object object) {
    if (object instanceof Map.Entry) {
      Map.Entry<?, ?> that = (Map.Entry<?, ?>) object;
      return Objects.equal(this.getKey(), that.getKey())
          && Objects.equal(this.getValue(), that.getValue());
    }
    return false;
  }

  @Override public int hashCode() {
    K k = getKey();
    V v = getValue();
    return ((k == null) ? 0 : k.hashCode()) ^ ((v == null) ? 0 : v.hashCode());
  }

  /**
   * Returns a string representation of the form {@code {key}={value}}.
   */
  @Override public String toString() {
    return getKey() + "=" + getValue();
  }
}
