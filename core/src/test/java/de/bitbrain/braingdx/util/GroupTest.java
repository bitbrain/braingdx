package de.bitbrain.braingdx.util;

import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Test class for {@link Group}
 */
public class GroupTest {

   @Test
   public void addToGroup() {
      Group<String, String> group = new Group<String, String>();
      group.addToGroup("a", "b");
      assertThat(group.getGroup("a")).containsExactly("b");
   }

   @Test
   public void removeFromGroup() {
      Group<String, String> group = new Group<String, String>();
      group.addToGroup("a", "b");
      group.addToGroup("a", "c");
      group.removeFromGroup("a", "b");
      assertThat(group.getGroup("a")).containsExactly("c");
   }

   @Test
   public void clearGroup() {
      Group<String, String> group = new Group<String, String>();
      group.addToGroup("a", "b");
      group.addToGroup("a", "c");
      group.addToGroup("b", "b");
      group.clearGroup("a");
      assertThat(group.getGroup("a")).isEmpty();
      assertThat(group.getGroup("b")).containsExactly("b");
   }

   @Test
   public void clear() {
      Group<String, String> group = new Group<String, String>();
      group.addToGroup("a", "b");
      group.addToGroup("a", "c");
      group.addToGroup("b", "b");
      group.clear();
      assertThat(group.getGroup("a")).isEmpty();
      assertThat(group.getGroup("b")).isEmpty();
   }
}