package me.jezza.fgpm.gui.components;

import com.google.common.base.Strings;
import me.jezza.fgpm.core.Utils;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ButtonLocation extends JButton implements ActionListener {

    private LabelLocation textLabel;

    public ButtonLocation(String title, LabelLocation textLabel) {
        super(title);
        addActionListener(this);
        this.textLabel = textLabel;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        String temp = Utils.chooseLocation();
        if (Strings.isNullOrEmpty(temp))
            temp = "Unselected";
        textLabel.setText(temp);
        setToolTipText(temp);
    }
}
