package de.bitbrain.braingdx.demo;

import com.badlogic.gdx.ApplicationListener;
import de.bitbrain.braingdx.demo.discovery.AppDiscovery;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class AppBootstrapper {

   private ExecutorService executor;

   private AppDiscovery discovery;

   public AppBootstrapper() {
      executor = Executors.newCachedThreadPool();
      discovery = new AppDiscovery();
   }

   public static void main(String[] args) {
      AppBootstrapper boot = new AppBootstrapper();
      boot.run();
   }

   public void run() {
      javax.swing.SwingUtilities.invokeLater(new Runnable() {
         public void run() {
            JFrame frame = new JFrame(DemoConfig.APP_NAME);
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            JPanel contentPane = new JPanel();
            contentPane.setLayout(new BoxLayout(contentPane, BoxLayout.PAGE_AXIS));
            JScrollPane pane = new JScrollPane(contentPane);
            discoverApps(contentPane);
            frame.getContentPane().add(pane);
            frame.pack();
            frame.setVisible(true);
         }
      });

   }

   private void discoverApps(JPanel pane) {
      for (Class<? extends ApplicationListener> cl : discovery.discover()) {
         JButton button = new JButton(cl.getSimpleName());
         button.addActionListener(createListener(cl));
         pane.add(button);
      }
   }

   private ActionListener createListener(final Class<? extends ApplicationListener> clazz) {
      return new ActionListener() {

         @Override
         public void actionPerformed(ActionEvent e) {
            AppRunner.run(clazz, executor);
         }
      };
   }
}
