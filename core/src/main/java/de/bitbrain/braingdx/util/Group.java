package de.bitbrain.braingdx.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Provides basic grouping functionality.
 */
public class Group<K, T> {

   private Map<K, List<T>> map = new HashMap<K, List<T>>();
   private Map<T, K> sourceToKeyMap = new HashMap<T, K>();
   private List<T> all = new ArrayList<T>();
   private List<K> groupKeys = new ArrayList<K>();

   public void addToGroup(K groupKey, T object) {
      List<T> list = getGroup(groupKey);
      if (!list.contains(object)) {
         list.add(object);
         all.add(object);
         sourceToKeyMap.put(object, groupKey);
      }
   }

   public List<T> getAll() {
      return all;
   }

   public List<K> getGroupKeys() {
      return groupKeys;
   }

   public void remove(T object) {
      K key = sourceToKeyMap.get(object);
      if (key != null) {
         removeFromGroup(key, object);
      }
   }

   public void removeFromGroup(K groupKey, T object) {
      List<T> list = getGroup(groupKey);
      list.remove(object);
      all.remove(object);
      sourceToKeyMap.remove(object);
   }

   public List<T> getGroup(K groupKey) {
      List<T> group = map.get(groupKey);
      if (group == null) {
         group = new ArrayList<T>();
         map.put(groupKey, group);
         groupKeys.add(groupKey);
      }
      return group;
   }

   public void clearGroup(K groupKey) {
      List<T> list = map.get(groupKey);
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
