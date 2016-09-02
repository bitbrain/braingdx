package de.bitbrain.braingdx.demo;

import de.bitbrain.braingdx.core.BrainGdxApp;

public class AppBootstrapper {
    
    private AppRunner runner;

    public AppBootstrapper() {
	this.runner = new AppRunner();
    }

    public void run() {
	runner.run(BrainGdxApp.class);
    }

    public static void main(String[] args) {
	AppBootstrapper boot = new AppBootstrapper();
	boot.run();
    }
}
