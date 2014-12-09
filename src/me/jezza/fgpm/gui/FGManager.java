package me.jezza.fgpm.gui;

import me.jezza.fgpm.ModManager;
import me.jezza.fgpm.core.ModState;
import me.jezza.fgpm.core.managers.BuildManager;
import me.jezza.fgpm.core.managers.CommandManager;
import me.jezza.fgpm.core.managers.ConfigManager;
import me.jezza.fgpm.gui.components.VersionField;
import me.jezza.fgpm.gui.frames.HoverFrame;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.LinkedList;

public class FGManager {

    private JFrame mainFrame;
    private HoverFrame hoverFrame;

    private JButton buttonEditMod;
    private JButton buttonBuild;
    private JButton buttonIncrement;
    private JButton buttonDecrement;

    private VersionField versionField;
    private JComboBox<ModState> comboBox;

    private boolean updated = false;

    public FGManager() {
        initialize();
        mainFrame.setVisible(true);
    }

    private void initialize() {
        mainFrame = new JFrame();
        mainFrame.addWindowFocusListener(new WindowFocusListener() {
            public void windowGainedFocus(WindowEvent e) {
                bringToFront();
            }

            public void windowLostFocus(WindowEvent e) {
            }
        });
        mainFrame.setTitle("ForgeGradle Manager - " + ModManager.getVersion());
        mainFrame.setResizable(false);
        mainFrame.setBounds(100, 100, 378, 153);
        mainFrame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        mainFrame.getContentPane().setLayout(null);
        mainFrame.setIconImage(new ImageIcon(getClass().getClassLoader().getResource("icon.png")).getImage());

        hoverFrame = new HoverFrame(mainFrame, 383, 16);
        hoverFrame.setLocationRelativeTo(mainFrame);
        initModDependentComponents();

        JButton installButton = new JButton("Install");
        installButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                hoverFrame.updateHover(true);
            }
        });
        installButton.setBounds(266, 26, 100, 23);
        installButton.setToolTipText("Opens a sub-menu.");
        add(installButton);

        JButton setupButton = new JButton("Setup");
        setupButton.setToolTipText(CommandManager.GradleCommand.setupDecompWorkspace.name());
        setupButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                CommandManager.GradleCommand.setupDecompWorkspace.executeCommand();
            }
        });
        setupButton.setBounds(266, 48, 100, 23);
        add(setupButton);

        JCheckBox autoIncrement = new JCheckBox("Auto Increment");
        autoIncrement.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                BuildManager.getInstance().toggleAutoIncrement();
            }
        });
        autoIncrement.setToolTipText("If selected this program auto increments the version number after building.");
        autoIncrement.setBounds(262, 4, 104, 23);
        add(autoIncrement);

        JButton buttonAddMod = new JButton("Add Mod");
        buttonAddMod.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                GuiAddMenu.createAddMenu(null);
            }
        });
        buttonAddMod.setBounds(266, 94, 100, 23);
        buttonAddMod.setToolTipText("Add a ModState");
        add(buttonAddMod);
    }

    private void initModDependentComponents() {
        buttonEditMod = new JButton("Edit Mod");
        buttonEditMod.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                GuiAddMenu.createAddMenu(getSelectedModState());
            }
        });
        buttonEditMod.setBounds(266, 71, 100, 23);
        add(buttonEditMod);

        comboBox = new JComboBox<ModState>();

        versionField = new VersionField();
        versionField.setBounds(70, 17, 186, 20);
        versionField.setColumns(10);
        add(versionField);

        // Button: Build
        buttonBuild = new JButton("Start build");
        buttonBuild.setBounds(70, 47, 186, 37);
        buttonBuild.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent arg0) {
                BuildManager.getInstance().buildWithModState(getSelectedModState());
            }
        });
        buttonBuild.setToolTipText("Change the version, and run the build script.");
        add(buttonBuild);

        // Button: Plus
        buttonIncrement = new JButton("+");
        buttonIncrement.setToolTipText("Increment the version number.");
        buttonIncrement.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent arg0) {
                incrementVersion();
            }
        });
        buttonIncrement.setBounds(10, 9, 50, 37);
        add(buttonIncrement);

        // Button: Minus
        buttonDecrement = new JButton("-");
        buttonDecrement.setToolTipText("Decrement the version number.");
        buttonDecrement.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent arg0) {
                decrementVersion();
            }
        });
        buttonDecrement.setBounds(10, 47, 50, 37);
        add(buttonDecrement);

        // Update the combo box now that everything has been initialised
        comboBox.addItemListener(new ItemListener() {
            public void itemStateChanged(ItemEvent e) {
                updateButtons();
            }
        });
        comboBox.setBounds(11, 95, 244, 20);
        add(comboBox);

        updateComboBox();
        updateButtons();
    }

    private void add(Component comp) {
        mainFrame.getContentPane().add(comp);
    }

    public void updateModVersion() {
        if (hasSelectedModState())
            getSelectedModState().updateVersion(versionField.getText());
    }

    private void bringToFront() {
        if (!updated) {
            GuiAddMenu.bringToFront();
            hoverFrame.toFront();
            mainFrame.toFront();
        }
        updated = !updated;
    }

    public void updateModComponents() {
        updateComboBox();
        updateButtons();
    }

    public void updateComboBox() {
        comboBox.removeAllItems();
        LinkedList<ModState> modList = ConfigManager.getModList();
        for (ModState mod : modList)
            comboBox.addItem(mod);
    }

    private void updateButtons() {
        boolean flag = hasSelectedModState();
        setFlag(flag);
        versionField.setText(flag ? getSelectedModState().getVersion() : "");
    }

    private void setFlag(boolean flag) {
        buttonIncrement.setEnabled(flag);
        buttonDecrement.setEnabled(flag);
        buttonBuild.setEnabled(flag);
        versionField.setEnabled(flag);
        buttonEditMod.setEnabled(flag);
    }

    public ModState getSelectedModState() {
        return (ModState) comboBox.getSelectedItem();
    }

    private boolean hasSelectedModState() {
        return comboBox.getSelectedItem() != null;
    }

    public void incrementVersion() {
        versionField.incrementVersion();
        updateModVersion();
    }

    public void decrementVersion() {
        versionField.decrementVersion();
        updateModVersion();
    }

}
