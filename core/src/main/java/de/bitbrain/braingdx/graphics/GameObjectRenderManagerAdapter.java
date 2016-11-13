package de.bitbrain.braingdx.graphics;

import de.bitbrain.braingdx.GameObject;
import de.bitbrain.braingdx.GameWorld.GameWorldListener;

public class GameObjectRenderManagerAdapter extends GameWorldListener {

    private final GameObjectRenderManager manager;

    public GameObjectRenderManagerAdapter(GameObjectRenderManager manager) {
	this.manager = manager;
    }

    @Override
    public void onUpdate(GameObject object, float delta) {
	manager.render(object, delta);
    }
}
