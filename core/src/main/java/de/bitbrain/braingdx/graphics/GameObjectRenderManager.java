/* Copyright 2017 Miguel Gonzalez Sanchez
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package de.bitbrain.braingdx.graphics;

import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.GdxRuntimeException;
import de.bitbrain.braingdx.util.ZIndexComparator;
import de.bitbrain.braingdx.world.GameObject;
import de.bitbrain.braingdx.world.GameWorld;

import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Handles rendering of game objects
 *
 * @author Miguel Gonzalez Sanchez
 * @version 1.0.0
 * @since 1.0.0
 */
public class GameObjectRenderManager implements Disposable {

   private final GameWorld gameWorld;
   private Comparator<GameObject> comparator = new ZIndexComparator();
   private final Map<Object, GameObjectRenderer<?>> rendererMap = new HashMap<Object, GameObjectRenderer<?>>();
   private final Map<Class<?>, BatchResolver<?>> batchResolverMap = new HashMap<Class<?>, BatchResolver<?>>();
   private final BatchResolver<?>[] allbatchResolvers;

   public GameObjectRenderManager(GameWorld gameWorld, BatchResolver<?> ... resolvers) {
      this.gameWorld = gameWorld;
      this.allbatchResolvers = resolvers;
      if (resolvers.length < 1) {
         throw new GdxRuntimeException("Unable to create " + GameObjectRenderManager.class.getName() + " no batch resolvers provided.");
      }
      for (BatchResolver resolver : resolvers) {
         batchResolverMap.put(resolver.getBatchClass(), resolver);
      }
   }

   /**
    * Combines multiple renderers for a particular game object
    *
    * @param gameObjectRenderers renderers
    * @return a new combined {@link GameObjectRenderer}
    */
   public static <T> GameObjectRenderer combine(GameObjectRenderer<T>... gameObjectRenderers) {
      return new CombinedGameObjectRenderer<T>(gameObjectRenderers);
   }

   public void setRenderOrderComparator(Comparator<GameObject> comparator) {
      this.comparator = comparator;
   }

   public void beforeRender() {
      for (BatchResolver<?> resolver : allbatchResolvers) {
         resolver.beforeRender();
      }
   }

   public void render(float delta) {
      List<GameObject> objects = gameWorld.getObjects(comparator);
      for (int i = 0; i < objects.size(); ++i) {
         GameObject object = objects.get(i);
         final GameObjectRenderer renderer = rendererMap.get(object.getType());
         if (renderer != null) {
            BatchResolver<?> batchResolver = batchResolverMap.get(renderer.getBatchClass());
            if (batchResolver == null) {
               throw new GdxRuntimeException("Unable to render type=" + object.getType()
                     + "! Renderer=" + renderer + " provided but no batch resolver registered.");
            }
            renderer.render(object, batchResolver.getBatch(), delta);
         }
      }
   }

   public void register(Object gameObjectType, GameObjectRenderer<?> renderer) {
      if (!rendererMap.containsKey(gameObjectType)) {
         rendererMap.put(gameObjectType, renderer);
      }
   }

   public void unregister(Object gameObjectType) {
      rendererMap.remove(gameObjectType);
   }

   @Override
   public void dispose() {
      for (GameObjectRenderer renderer : rendererMap.values()) {
         if (renderer instanceof Disposable) {
            ((Disposable) renderer).dispose();
         }
      }
      rendererMap.clear();
   }

   public interface GameObjectRenderer<BatchType> {
      Class<BatchType> getBatchClass();
      void render(GameObject object, BatchType batchType, float delta);
   }

   static class CombinedGameObjectRenderer<BatchType> implements GameObjectRenderer<BatchType> {

      private final GameObjectRenderer<BatchType>[] renderers;

      public CombinedGameObjectRenderer(GameObjectRenderer... gameObjectRenderers) {
         this.renderers = gameObjectRenderers;
      }

      @Override
      public Class<BatchType> getBatchClass() {
         return renderers[0].getBatchClass();
      }

      @Override
      public void render(GameObject object, BatchType batchType, float delta) {
         for (int i = 0; i < renderers.length; ++i) {
            renderers[i].render(object, batchType, delta);
         }
      }

   }

}
