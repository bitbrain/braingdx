package de.bitbrain.braingdx.graphics;

public interface BatchResolver<T> {
   void beforeRender();
   Class<T> getBatchClass();
   T getBatch();
   void begin();
   void end();

}
