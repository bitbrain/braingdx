package de.bitbrain.braingdx.demo.discovery;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

import org.reflections.Reflections;
import org.reflections.scanners.ResourcesScanner;
import org.reflections.scanners.SubTypesScanner;
import org.reflections.util.ClasspathHelper;
import org.reflections.util.ConfigurationBuilder;
import org.reflections.util.FilterBuilder;

import de.bitbrain.braingdx.BrainGdxGame;

public class AppDiscovery {

   private static final String APP_PACKAGE = "de.bitbrain.braingdx.apps";

   public Collection<Class<? extends BrainGdxGame>> discover() {
      List<ClassLoader> classLoadersList = new LinkedList<ClassLoader>();
      classLoadersList.add(ClasspathHelper.contextClassLoader());
      classLoadersList.add(ClasspathHelper.staticClassLoader());

      Reflections reflections = new Reflections(new ConfigurationBuilder()
            .setScanners(new SubTypesScanner(false /* don't exclude Object.class */), new ResourcesScanner())
            .setUrls(ClasspathHelper.forClassLoader(classLoadersList.toArray(new ClassLoader[0])))
            .filterInputsBy(new FilterBuilder().include(FilterBuilder.prefix(APP_PACKAGE))));
      return reflections.getSubTypesOf(BrainGdxGame.class);
   }

}
