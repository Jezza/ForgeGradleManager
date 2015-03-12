package me.jezza.fgpm.gui;

import me.jezza.fgpm.App;
import me.jezza.fgpm.core.managers.BuildManager;
import me.jezza.fgpm.gui.components.ButtonBuild;
import me.jezza.fgpm.gui.components.ButtonEdit;
import me.jezza.fgpm.gui.components.ComboModList;
import me.jezza.fgpm.gui.components.FieldVersion;
import me.jezza.fgpm.gui.frames.HoverFrame;
import me.jezza.fgpm.gui.lib.IModList;
import me.jezza.fgpm.gui.lib.IModListener;
import me.jezza.fgpm.gui.lib.IModProvider;
import me.jezza.fgpm.mod.ModState;

import javax.swing.*;
import java.awt.GridBagConstraints;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowFocusListener;
import java.util.ArrayList;
import java.util.List;

public class FGManager extends MainWindowAbstract implements IModProvider {

    private HoverFrame hoverFrame;

    private ComboModList comboBox;

    private boolean updated = false;

    private List<IModListener> listeners;

    public FGManager() {
    }

    @Override
    protected void createUI() {
        mainFrame.setTitle("ForgeGradle Manager - v" + App.getVersion());
        mainFrame.setResizable(false);
        mainFrame.setIconImage(new ImageIcon(getClass().getClassLoader().getResource("icon.png")).getImage());
        listeners = new ArrayList<>();
        mainFrame.addWindowFocusListener(new WindowFocusListener() {
            public void windowGainedFocus(WindowEvent e) {
                bringToFront();
            }

            public void windowLostFocus(WindowEvent e) {
            }
        });
        hoverFrame = new HoverFrame(mainFrame, 360, 25);

        // Button: Plus
        JButton buttonIncrement = new JButton("+");
        buttonIncrement.setToolTipText("Increment the version number.");
        constraints.fill = GridBagConstraints.BOTH;
        constraints.insets.set(2, 2, 0, 1);
        constraints.gridheight = 2;
        constraints.weighty = 0.5F;
        constraints.ipadx = 12;
        constraints.gridx = 0;
        constraints.gridy = 0;
        add(buttonIncrement);

        // Button: Minus
        JButton buttonDecrement = new JButton("-");
        buttonDecrement.setToolTipText("Decrement the version number.");
        constraints.fill = GridBagConstraints.BOTH;
        constraints.insets.set(4, 2, 4, 1);
        constraints.gridheight = 2;
        constraints.weighty = 0.5F;
        constraints.ipadx = 12;
        constraints.ipady = 5;
        constraints.gridx = 0;
        constraints.gridy = 2;
        add(buttonDecrement);

        FieldVersion fieldVersion = new FieldVersion(this, buttonIncrement, buttonDecrement);
        listeners.add(fieldVersion);
        fieldVersion.setColumns(10);
        constraints.gridx = 1;
        constraints.gridy = 0;
        constraints.gridheight = 2;
        constraints.ipadx = 90;
        constraints.ipady = 0;
        constraints.insets.set(0, 6, 0, 6);
        constraints.fill = GridBagConstraints.HORIZONTAL;
        add(fieldVersion);

        // Button: Build
        ButtonBuild buttonBuild = new ButtonBuild("Start build");
        listeners.add(buttonBuild);
        buttonBuild.setToolTipText("Change the version, and run the build script.");
        buttonBuild.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent arg0) {
                BuildManager.getInstance().buildWithModState(getModState());
            }
        });
        constraints.insets.set(1, 6, 0, 6);
        constraints.gridheight = 2;
        constraints.gridx = 1;
        constraints.gridy = 2;
        constraints.ipadx = 100;
        constraints.ipady = 15;
        add(buttonBuild);

        JCheckBox autoIncrement = new JCheckBox("Auto Increment");
        autoIncrement.setToolTipText("If selected this program auto increments the version number after building.");
        autoIncrement.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                BuildManager.getInstance().toggleAutoIncrement();
            }
        });
        constraints.gridx = 2;
        constraints.gridy = 0;
        constraints.fill = GridBagConstraints.BOTH;
        add(autoIncrement);

        JButton installButton = new JButton("Commands");
        installButton.setToolTipText("Opens a sub-menu.");
        installButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                hoverFrame.updateHover(true);
            }
        });
        constraints.fill = GridBagConstraints.HORIZONTAL;
        constraints.insets.set(0, 0, 0, 1);
        constraints.gridx = 2;
        constraints.gridy = 1;
        constraints.gridheight = 2;
        add(installButton);

        ButtonEdit buttonEditMod = new ButtonEdit("Edit Mod", this);
        listeners.add(buttonEditMod);
        constraints.fill = GridBagConstraints.HORIZONTAL;
        constraints.insets.set(0, 0, 0, 1);
        constraints.gridx = 2;
        add(buttonEditMod);

        JButton buttonAddMod = new JButton("Add Mod");
        buttonAddMod.setToolTipText("Add a ModState");
        buttonAddMod.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                GuiAddMenu.createAddMenu(null);
            }
        });
        constraints.fill = GridBagConstraints.HORIZONTAL;
        constraints.insets.set(0, 0, 1, 1);
        constraints.gridx = 2;
        add(buttonAddMod);

        comboBox = new ComboModList(this);
        constraints.fill = GridBagConstraints.BOTH;
        constraints.insets.set(1, 3, 2, 4);
        constraints.gridwidth = 2;
        constraints.gridx = 0;
        constraints.gridy = 4;
        add(comboBox);
    }

    private void bringToFront() {
        if (!updated) {
            GuiAddMenu.bringToFront();
            hoverFrame.mainFrame.toFront();
            mainFrame.toFront();
        }
        updated = !updated;
    }

    @Override
    public boolean hasModState() {
        return comboBox.hasSelectedMod();
    }

    @Override
    public ModState getModState() {
        return comboBox.getSelectedMod();
    }

    public IModList getModList() {
        return comboBox;
    }

    @Override
    public void triggerUpdate() {
        comboBox.forceUpdate();
        for (IModListener listener : listeners)
            listener.update(hasModState());
    }
}
