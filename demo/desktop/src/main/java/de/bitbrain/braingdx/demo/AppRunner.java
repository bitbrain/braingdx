package de.bitbrain.braingdx.demo;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;

import java.io.File;
import java.io.IOException;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;

public final class AppRunner {

   private static final String JAVA_HOME = "java.home";
   private static final String CLASS_PATH = "java.class.path";

   private static LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();

   private static JavaProcess process = new JavaProcess();

   public static void run(final Class<? extends ApplicationListener> cl, ExecutorService executor) {
      executor.submit(new Callable<Integer>() {

         @Override
         public Integer call() throws Exception {
            return process.exec(AppRunner.class, cl);
         }

      });
   }

   @SuppressWarnings("unchecked")
   public static void main(String args[])
         throws InstantiationException, IllegalAccessException, ClassNotFoundException {
      if (args.length < 1) {
         throw new RuntimeException("No application provided!");
      }
      Class<ApplicationListener> app = (Class<ApplicationListener>) Class.forName(args[0]);
      config.width = 1500;
      config.height = 1000;
      new LwjglApplication(app.newInstance(), config);
   }

   public static class JavaProcess {

      public int exec(Class<?> klass, Class<?> internal) throws IOException, InterruptedException {
         String javaHome = System.getProperty(JAVA_HOME);
         String javaBin = javaHome + File.separator + "bin" + File.separator + "java";
         String classpath = System.getProperty(CLASS_PATH);
         String className = klass.getCanonicalName();
         String internalName = internal.getCanonicalName();
         ProcessBuilder builder = new ProcessBuilder(javaBin, "-cp", classpath, className, internalName);
         Process process = builder.start();
         process.waitFor();
         return process.exitValue();
      }

   }

}
