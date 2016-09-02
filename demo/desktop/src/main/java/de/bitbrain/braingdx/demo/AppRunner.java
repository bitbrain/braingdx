package de.bitbrain.braingdx.demo;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;

public class AppRunner {

    private LwjglApplicationConfiguration config;

    public AppRunner() {
	this.config = new LwjglApplicationConfiguration();
    }

    public void setConfiguration(LwjglApplicationConfiguration config) {
	this.config = config;
    }

    public void run(Class<? extends ApplicationListener> app) {
	try {
	    new LwjglApplication(app.newInstance(), config);
	} catch (InstantiationException e) {
	    System.out.println("Unable to create app!");
	} catch (IllegalAccessException e) {
	    System.out.println("Unable to create app!");
	}
    }

}
