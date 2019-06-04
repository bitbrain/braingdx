package de.bitbrain.braingdx.util;

public interface ArgumentFactory<K, T> {

   T create(K supplier);
}
