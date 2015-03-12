package me.jezza.fgpm.gui.frames;

import me.jezza.fgpm.core.managers.CommandManager.GradleCommand;
import me.jezza.fgpm.gui.MainWindowAbstract;
import me.jezza.fgpm.gui.components.ButtonCommand;

import javax.swing.*;
import java.awt.Color;
import java.awt.Window;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;

public class HoverFrame extends MainWindowAbstract {

    private JFrame parent;
    private int x, y;

    public HoverFrame(JFrame parent, int x, int y) {
        super(false);
        this.parent = parent;
        this.x = x;
        this.y = y;
        createUI();
        run();
    }

    public void updateHover(boolean toggle) {
        mainFrame.setLocation(parent.getX() + x, parent.getY() + y);
        if (toggle)
            toggleVisibility();
    }

    @Override
    protected void createUI() {
        mainFrame.setUndecorated(true);
        mainFrame.setSize(100, 100);
        mainFrame.getContentPane().setBackground(Color.LIGHT_GRAY);
        mainFrame.setType(Window.Type.UTILITY);
        mainFrame.setLocationRelativeTo(parent);

        parent.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentMoved(ComponentEvent e) {
                updateHover(false);
            }
        });

        JButton eclipseButton = new ButtonCommand("Eclipse", GradleCommand.eclipse);
        constraints.insets.set(3, 3, 2, 3);
        constraints.ipadx = 10;
        constraints.ipady = 8;
        add(eclipseButton);

        JButton ideaButton = new ButtonCommand("IntelliJ", GradleCommand.idea);
        constraints.insets.set(3, 2, 2, 3);
        constraints.ipadx = 10;
        constraints.ipady = 8;
        constraints.gridx = 0;
        add(ideaButton);

        JButton setupButton = new ButtonCommand("Setup", GradleCommand.setupDecompWorkspace);
        constraints.insets.set(3, 2, 3, 3);
        constraints.ipadx = 10;
        constraints.ipady = 8;
        constraints.gridx = 0;
        add(setupButton);
    }

    @Override
    public void run() {
        mainFrame.pack();
//        mainFrame.setVisible(true);
    }
}
