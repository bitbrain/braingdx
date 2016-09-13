package de.bitbrain.braingdx.demo;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;

import de.bitbrain.braingdx.apps.BrainGdxApp;

public class SimpleApp {

    public static void main(String args[]) {
	LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
	config.useGL30 = true;
	new LwjglApplication(new BrainGdxApp(), config);
    }

}
