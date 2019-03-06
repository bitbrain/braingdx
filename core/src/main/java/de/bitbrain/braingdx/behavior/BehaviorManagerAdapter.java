package de.bitbrain.braingdx.behavior;

import de.bitbrain.braingdx.util.Updateable;
import de.bitbrain.braingdx.world.GameObject;
import de.bitbrain.braingdx.world.GameWorld.GameWorldListener;

public class BehaviorManagerAdapter extends GameWorldListener implements Updateable {

   private BehaviorManager behaviorManager;

   public BehaviorManagerAdapter(BehaviorManager behaviorManager) {
      this.behaviorManager = behaviorManager;
   }

   @Override
   public void onUpdate(GameObject object, float delta) {
      behaviorManager.updateGlobally(object, delta);
      behaviorManager.updateLocally(object, delta);
   }

   @Override
   public void onUpdate(GameObject object, GameObject other, float delta) {
      behaviorManager.updateLocallyCompared(object, other, delta);
      behaviorManager.updateGloballyCompared(object, other, delta);
   }

   @Override
   public void onRemove(GameObject object) {
      behaviorManager.remove(object);
   }

   @Override
   public void onClear() {
      behaviorManager.clear();
   }

   @Override
   public void update(float delta) {
      behaviorManager.update(delta);
   }
}
