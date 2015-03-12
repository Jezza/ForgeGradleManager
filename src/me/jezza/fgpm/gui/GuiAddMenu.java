package me.jezza.fgpm.gui;

import me.jezza.fgpm.App;
import me.jezza.fgpm.core.Utils;
import me.jezza.fgpm.gui.components.ButtonLocation;
import me.jezza.fgpm.gui.components.LabelLocation;
import me.jezza.fgpm.mod.InvalidVersionException;
import me.jezza.fgpm.mod.ModState;
import me.jezza.fgpm.mod.Version;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.Color;
import java.awt.GridBagConstraints;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class GuiAddMenu extends MainWindowAbstract implements DocumentListener {
    private static GuiAddMenu INSTANCE = null;

    private Version currentVersion;

    private JLabel lblVersion, lblStrippedVersion;
    private JTextField fieldModID, fieldVersion, fieldGroup;
    private LabelLocation lblCommonLocation, lblResourceLocation;
    private JButton btnAdd, btnDelete;

    private static int temp = 0;

    public GuiAddMenu() {
    }

    @Override
    protected void createUI() {
        mainFrame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                disposeOfAddMenu();
            }
        });
        mainFrame.setResizable(false);
        mainFrame.setTitle("Add a Mod");
        mainFrame.setType(Window.Type.UTILITY);
        mainFrame.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);

        JLabel lblModID = new JLabel("Mod ID:");
        lblModID.setHorizontalAlignment(JLabel.CENTER);
        constraints.fill = GridBagConstraints.BOTH;
        add(lblModID);

        fieldModID = new JTextField();
        fieldModID.setColumns(10);
        constraints.fill = GridBagConstraints.BOTH;
        constraints.gridwidth = 2;
        add(fieldModID);

        lblVersion = new JLabel("Version:");
        lblVersion.setForeground(Color.RED);
        lblVersion.setToolTipText(getDefaultTooltip() + "</html>");
        lblVersion.setHorizontalAlignment(SwingConstants.LEFT);
        mainFrame.getContentPane().add(lblVersion);
        constraints.fill = GridBagConstraints.BOTH;
        constraints.gridx = 0;
        add(lblVersion);

        fieldVersion = new JTextField();
        fieldVersion.getDocument().addDocumentListener(this);
        fieldVersion.setColumns(10);
        constraints.fill = GridBagConstraints.BOTH;
        constraints.gridx = 1;
        constraints.gridwidth = 2;
        add(fieldVersion);

        JLabel lblGroup = new JLabel("Group:");
        lblGroup.setHorizontalAlignment(SwingConstants.LEADING);
        constraints.fill = GridBagConstraints.BOTH;
        constraints.gridx = 0;
        add(lblGroup);

        fieldGroup = new JTextField();
        fieldGroup.setColumns(10);
        constraints.fill = GridBagConstraints.BOTH;
        constraints.gridx = 1;
        constraints.gridwidth = 2;
        add(fieldGroup);

        lblCommonLocation = new LabelLocation("Unselected");
        ButtonLocation btnSourceLocation = new ButtonLocation("Source Code Location", lblCommonLocation);
        constraints.fill = GridBagConstraints.BOTH;
        constraints.gridx = 0;
        constraints.weightx = 0.6F;
        constraints.gridwidth = 2;
        add(btnSourceLocation);

        lblStrippedVersion = new LabelLocation("");
        lblStrippedVersion.setHorizontalAlignment(SwingConstants.CENTER);
        lblStrippedVersion.setVisible(false);
        constraints.fill = GridBagConstraints.BOTH;
        constraints.gridx = 2;
        constraints.gridy = 3;
        add(lblStrippedVersion);

        constraints.gridwidth = 3;
        constraints.gridx = 0;
        add(lblCommonLocation);

        lblResourceLocation = new LabelLocation("Unselected");
        ButtonLocation btnResourceLocation = new ButtonLocation("Resource Location", lblResourceLocation);
        constraints.fill = GridBagConstraints.BOTH;
        constraints.gridx = 0;
        constraints.weightx = 0.6F;
        constraints.gridwidth = 2;
        add(btnResourceLocation);

        JButton tempButton = new JButton("Fill");
        tempButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                fieldModID.setText("TestMod" + temp);
                fieldVersion.setText("1.2.1");
                fieldGroup.setText("me.jezza.test");
                String temp = "C:/directoryTest";
                lblResourceLocation.setText(temp);
                lblCommonLocation.setText(temp);
                GuiAddMenu.temp += 1;
            }
        });
        constraints.gridx = 2;
        constraints.gridy = 5;
        add(tempButton);

        constraints.gridwidth = 3;
        constraints.gridx = 0;
        add(lblResourceLocation);

        btnAdd = new JButton("Done/Add");
        btnAdd.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                addModState();
            }
        });
        constraints.fill = GridBagConstraints.BOTH;
        constraints.gridx = 0;
        constraints.ipadx = 40;
        constraints.ipady = 20;
        constraints.weightx = 0.5F;
        constraints.gridwidth = 2;
        add(btnAdd);

        btnDelete = new JButton("Delete");
        btnDelete.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                removeModState();
            }
        });
        btnDelete.setEnabled(false);
        constraints.fill = GridBagConstraints.BOTH;
        constraints.weightx = 0.5F;
        constraints.ipadx = 40;
        constraints.ipady = 20;
        constraints.gridx = 2;
        constraints.gridy = 7;
        add(btnDelete);

        run();
    }

    private String getDefaultTooltip() {
        StringBuilder toolTip = new StringBuilder("<html>Red: Not a valid version number.");
        toolTip.append("<br>");
        toolTip.append("Green: Valid version number.");
        toolTip.append("<br>");
        toolTip.append("Blue: Valid version number, but something was stripped.");
        toolTip.append("<br>");
        return toolTip.toString();
    }

    public GuiAddMenu setState(ModState modState) {
        btnAdd.setBounds(98, 176, 155, 39);
        btnDelete.setEnabled(true);

        fieldModID.setText(modState.modID);
        fieldVersion.setText(modState.version.toString());
        fieldGroup.setText(modState.groupName);
        lblCommonLocation.setText(modState.commonFolder);
        lblResourceLocation.setText(modState.resourceFolder);
        confirmVersion();
        return this;
    }

    private void removeModState() {
        App.getFGManager().getModList().removeModState(new ModState(fieldModID.getText(), currentVersion, lblCommonLocation.getText(), lblResourceLocation.getText(), fieldGroup.getText()));
        App.getFGManager().triggerUpdate();
        exit();
    }

    private void addModState() {
        String reason = "";
        String modID = fieldModID.getText();
        String group = fieldGroup.getText();
        String commonFolder = lblCommonLocation.getText();
        String resourceFolder = lblResourceLocation.getText();

        if (commonFolder.equals("Unselected"))
            reason = "Need to input a source folder";
        if (resourceFolder.equals("Unselected"))
            reason = "Need to input a resources folder";
        if (group.equals(""))
            reason = "Need to input a Group Name.";
        if (currentVersion == null)
            reason = "Need to input a starting version.";
        if (modID.equals(""))
            reason = "Need to input a Mod ID.";

        if (!reason.isEmpty()) {
            Utils.showErrorMessage("Failed to add mod: " + reason);
            return;
        }

        App.getFGManager().getModList().addModState(new ModState(modID, currentVersion, commonFolder, resourceFolder, group));
        App.getFGManager().triggerUpdate();
        exit();
    }

    private void exit() {
        mainFrame.setVisible(false);
        mainFrame.dispose();
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
    }

    public static void bringToFront() {
        if (INSTANCE != null)
            INSTANCE.mainFrame.toFront();
    }

    @Override
    public void insertUpdate(DocumentEvent e) {
        confirmVersion();
    }

    @Override
    public void removeUpdate(DocumentEvent e) {
        confirmVersion();
    }

    @Override
    public void changedUpdate(DocumentEvent e) {
        confirmVersion();
    }

    private void confirmVersion() {
        boolean valid = true;
        try {
            currentVersion = Version.parse(fieldVersion.getText());
        } catch (InvalidVersionException e) {
            currentVersion = null;
            valid = false;
        }
        boolean stripFlag = Version.wasLastParseStripped();
        lblVersion.setForeground(valid ? stripFlag ? Color.BLUE : Color.GREEN.darker() : Color.RED);
        fieldVersion.setForeground(valid ? stripFlag ? Color.BLUE : Color.GREEN.darker() : Color.RED);
        lblStrippedVersion.setForeground(valid ? stripFlag ? Color.BLUE : Color.GREEN.darker() : Color.RED);

        lblStrippedVersion.setVisible(valid);
        lblStrippedVersion.setText(valid ? currentVersion.toString() : "");
    }
}
