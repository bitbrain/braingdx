package de.bitbrain.braingdx.demo;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import com.badlogic.gdx.ApplicationListener;

import de.bitbrain.braingdx.core.BrainGdxApp;

public class AppBootstrapper {

    private ExecutorService executor;

    public AppBootstrapper() {
	executor = Executors.newCachedThreadPool();
    }

    public void run() {
	javax.swing.SwingUtilities.invokeLater(new Runnable() {
	    public void run() {
		JFrame frame = new JFrame(DemoConfig.APP_NAME);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		JPanel contentPane = new JPanel();
		contentPane.setLayout(new BoxLayout(contentPane, BoxLayout.PAGE_AXIS));
		JScrollPane pane = new JScrollPane(contentPane);
		for (int i = 0; i < 50; ++i) {
		    JButton button = new JButton(BrainGdxApp.class.getSimpleName());
		    button.addActionListener(createListener(BrainGdxApp.class));
		    contentPane.add(button);
		}
		frame.getContentPane().add(pane);
		frame.pack();
		frame.setVisible(true);
	    }
	});
	
    }

    private ActionListener createListener(final Class<? extends ApplicationListener> clazz) {
	return new ActionListener() {

	    @Override
	    public void actionPerformed(ActionEvent e) {
		AppRunner.run(clazz, executor);
	    }
	};
    }

    public static void main(String[] args) {
	AppBootstrapper boot = new AppBootstrapper();
	boot.run();
    }
}
