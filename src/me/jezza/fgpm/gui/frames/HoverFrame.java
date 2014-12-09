package me.jezza.fgpm.gui.frames;

import me.jezza.fgpm.core.managers.CommandManager;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;

public class HoverFrame extends JFrame {

    private JFrame parent;
    private int x, y;

    public HoverFrame(JFrame parent, int x, int y) {
        this.parent = parent;
        this.x = x;
        this.y = y;
        setUndecorated(true);
        setSize(100, 100);
        getContentPane().setBackground(Color.LIGHT_GRAY);
        getContentPane().setLayout(null);
        setType(Type.UTILITY);
        setLocationRelativeTo(parent);

        parent.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentMoved(ComponentEvent e) {
                updateHover(false);
            }
        });
        initComponents();
    }

    private void initComponents() {
        JButton eclipseButton = new JButton("Eclipse");
        eclipseButton.setBounds(5, 5, 90, 40);
        eclipseButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                CommandManager.GradleCommand.eclipse.executeCommand();
            }
        });
        eclipseButton.setToolTipText(CommandManager.GradleCommand.eclipse.name());
        getContentPane().add(eclipseButton);

        JButton ideaButton = new JButton("IntelliJ");
        ideaButton.setBounds(5, 55, 90, 40);
        ideaButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                CommandManager.GradleCommand.idea.executeCommand();
            }
        });
        ideaButton.setToolTipText(CommandManager.GradleCommand.idea.name());
        getContentPane().add(ideaButton);
    }

    public void updateHover(boolean toggle) {
        setLocation(parent.getX() + x, parent.getY() + y);
        if (toggle)
            setVisible(!isVisible());
    }

}
