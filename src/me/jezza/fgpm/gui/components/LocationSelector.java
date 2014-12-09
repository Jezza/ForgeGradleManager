package me.jezza.fgpm.gui.components;

import me.jezza.fgpm.core.Utils;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class LocationSelector extends JButton implements ActionListener {

    private JLabel textLabel;

    public LocationSelector(String title, JLabel textLabel) {
        super(title);
        addActionListener(this);
        this.textLabel = textLabel;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        String temp = Utils.chooseLocation();
        if (temp == null || temp.equals(""))
            temp = "Unselected";
        textLabel.setText(temp);
        textLabel.setToolTipText(temp);
    }

}
