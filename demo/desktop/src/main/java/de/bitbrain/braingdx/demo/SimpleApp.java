package de.bitbrain.braingdx.demo;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;

import de.bitbrain.braingdx.apps.LightingManagerTest;

public class SimpleApp {

    public static void main(String args[]) {
	LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
	config.width = 1200;
	config.height = 1000;
	new LwjglApplication(new LightingManagerTest(), config);
    }

}
