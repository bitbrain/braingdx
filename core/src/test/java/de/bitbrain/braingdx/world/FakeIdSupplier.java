package de.bitbrain.braingdx.world;

import de.bitbrain.braingdx.world.GameWorld.GameWorldListener;

public class FakeIdSupplier extends GameWorldListener {

    private String id;

    public FakeIdSupplier() {
    }

    @Override
    public void onUpdate(GameObject object, float delta) {
	generate();
	object.setId(id);
    }

    public String getCurrentId() {
	return id;
    }

    private void generate() {
	this.id = String.valueOf(Math.random());
    }
}
