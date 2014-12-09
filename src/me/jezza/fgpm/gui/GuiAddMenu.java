package me.jezza.fgpm.gui;

import me.jezza.fgpm.ModManager;
import me.jezza.fgpm.core.ModState;
import me.jezza.fgpm.core.Utils;
import me.jezza.fgpm.core.managers.ConfigManager;
import me.jezza.fgpm.gui.components.LocationLabel;
import me.jezza.fgpm.gui.components.LocationSelector;

import javax.swing.*;
import java.awt.Window.Type;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class GuiAddMenu {
    private static GuiAddMenu INSTANCE = null;

    public JFrame mainWindow;

    private JTextField modIDField, versionField, groupField;
    private LocationLabel lblCommonLocation, lblResourceLocation;
    private JButton btnAdd, deleteButton;
    private LocationSelector btnSourceLocation, btnResourceLocation;

    public GuiAddMenu() {
        mainWindow = new JFrame();
        mainWindow.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                disposeOfAddMenu();
            }
        });
        mainWindow.setResizable(false);
        mainWindow.setTitle("Add a Mod");
        mainWindow.setType(Type.UTILITY);
        mainWindow.setBounds(100, 100, 263, 249);
        mainWindow.getContentPane().setLayout(null);

        lblCommonLocation = new LocationLabel("Unselected");
        lblCommonLocation.setBounds(6, 115, 247, 14);
        mainWindow.getContentPane().add(lblCommonLocation);

        lblResourceLocation = new LocationLabel("Unselected");
        lblResourceLocation.setBounds(6, 158, 247, 14);
        mainWindow.getContentPane().add(lblResourceLocation);

        btnSourceLocation = new LocationSelector("Source Code Location", lblCommonLocation);
        btnSourceLocation.setBounds(4, 89, 153, 23);
        mainWindow.getContentPane().add(btnSourceLocation);

        btnResourceLocation = new LocationSelector("Resource Location", lblResourceLocation);
        btnResourceLocation.setBounds(4, 132, 153, 23);
        mainWindow.getContentPane().add(btnResourceLocation);

        JLabel lblModID = new JLabel("Mod ID:");
        lblModID.setBounds(5, 14, 46, 14);
        mainWindow.getContentPane().add(lblModID);

        JLabel lblVersion = new JLabel("Version:");
        lblVersion.setBounds(5, 39, 46, 14);
        mainWindow.getContentPane().add(lblVersion);

        JLabel lblGroup = new JLabel("Group:");
        lblGroup.setBounds(5, 64, 46, 14);
        mainWindow.getContentPane().add(lblGroup);

        modIDField = new JTextField();
        modIDField.setBounds(50, 11, 203, 20);
        modIDField.setColumns(10);
        mainWindow.getContentPane().add(modIDField);

        versionField = new JTextField();
        versionField.setBounds(50, 36, 203, 20);
        versionField.setColumns(10);
        mainWindow.getContentPane().add(versionField);

        groupField = new JTextField();
        groupField.setBounds(50, 64, 203, 20);
        groupField.setColumns(10);
        mainWindow.getContentPane().add(groupField);

        btnAdd = new JButton("Done/Add");
        btnAdd.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                addModState();
            }
        });
        btnAdd.setBounds(4, 176, 249, 39);
        mainWindow.getContentPane().add(btnAdd);

        deleteButton = new JButton("Delete");
        deleteButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                removeModState();
            }
        });
        deleteButton.setBounds(4, 176, 94, 39);
        deleteButton.setVisible(false);
        deleteButton.setEnabled(false);
        mainWindow.getContentPane().add(deleteButton);

        mainWindow.setVisible(true);
    }

    public GuiAddMenu setState(ModState modState) {
        btnAdd.setBounds(98, 176, 155, 39);
        deleteButton.setVisible(true);
        deleteButton.setEnabled(true);

        modIDField.setText(modState.getModID());
        versionField.setText(modState.getVersion());
        groupField.setText(modState.getGroupName());
        lblCommonLocation.setText(modState.getCommonFolder());
        lblResourceLocation.setText(modState.getResourceFolder());
        return this;
    }

    private void removeModState() {
        ConfigManager.removeModState(new ModState(modIDField.getText(), lblCommonLocation.getText(), lblResourceLocation.getText(), versionField.getText(), groupField.getText()));
        exit();
    }

    private void addModState() {
        String reason = "";
        String modID = modIDField.getText();
        String version = versionField.getText();
        String group = groupField.getText();
        String commonFolder = lblCommonLocation.getText();
        String resourceFolder = lblResourceLocation.getText();

        if (lblCommonLocation.getText().equals("Unselected"))
            reason = "Need to input a source folder";
        if (lblResourceLocation.getText().equals("Unselected"))
            reason = "Need to input a resources folder";
        if (group.equals(""))
            reason = "Need to input a Group Name.";
        if (version.equals(""))
            reason = "Need to input a starting version.";
        if (modID.equals(""))
            reason = "Need to input a Mod ID.";

        if (!reason.equals("")) {
            Utils.showErrorMessage("Failed to add mod: " + reason);
            return;
        }

        new ModState(modID, commonFolder, resourceFolder, version, group).addState();
        exit();
    }

    private void exit() {
        mainWindow.setVisible(false);
        mainWindow.dispose();
        disposeOfAddMenu();
    }

    public static GuiAddMenu createAddMenu(ModState modState) {
        if (INSTANCE != null)
            return INSTANCE;
        INSTANCE = new GuiAddMenu();
        if (modState != null)
            INSTANCE.setState(modState);
        return INSTANCE;
    }

    public static void disposeOfAddMenu() {
        INSTANCE = null;
        ModManager.getFGManager().updateModComponents();
    }

    public static void bringToFront() {
        if (INSTANCE != null)
            INSTANCE.mainWindow.toFront();
    }

}
