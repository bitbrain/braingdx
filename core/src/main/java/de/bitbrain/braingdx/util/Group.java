package de.bitbrain.braingdx.util;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.utils.Array;

import java.util.HashMap;
import java.util.Map;

/**
 * Provides basic grouping functionality.
 */
public class Group<K, T> {

   private Map<K, Array<T>> map = new HashMap<K, Array<T>>();
   private Map<T, K> sourceToKeyMap = new HashMap<T, K>();
   private Array<T> all = new Array<T>();
   private Array<K> groupKeys = new Array<K>();

   public void addToGroup(K groupKey, T object) {
      Array<T> list = getGroup(groupKey);
      if (!list.contains(object, false)) {
         list.add(object);
         all.add(object);
         sourceToKeyMap.put(object, groupKey);
      } else {
         Gdx.app.error("GROUP", "Unable to add " + object + " to group " + groupKey + ": already exists!");
      }
   }

   public Array<T> getAll() {
      return all;
   }

   public Array<K> getGroupKeys() {
      return groupKeys;
   }

   public void remove(T object) {
      K key = sourceToKeyMap.get(object);
      if (key != null) {
         removeFromGroup(key, object);
      }
   }

   public void removeFromGroup(K groupKey, T object) {
      Array<T> list = getGroup(groupKey);
      list.removeValue(object, false);
      all.removeValue(object, false);
      sourceToKeyMap.remove(object);
   }

   public Array<T> getGroup(K groupKey) {
      Array<T> group = map.get(groupKey);
      if (group == null) {
         group = new Array<T>(200);
         map.put(groupKey, group);
         groupKeys.add(groupKey);
      }
      return group;
   }

   public void clearGroup(K groupKey) {
      Array<T> list = map.get(groupKey);
      if (list != null) {
         list.clear();
      }
   }

   public void clear() {
      map.clear();
      groupKeys.clear();
      all.clear();
      sourceToKeyMap.clear();
   }
}
