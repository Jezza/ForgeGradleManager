package me.jezza.fgpm.gui.components;

import me.jezza.fgpm.gui.lib.IModListener;
import me.jezza.fgpm.gui.lib.IModProvider;
import me.jezza.fgpm.gui.lib.IVersion;
import me.jezza.fgpm.mod.InvalidVersionException;
import me.jezza.fgpm.mod.Version;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class FieldVersion extends JTextField implements DocumentListener, IModListener {

    private IModProvider provider;
    private JButton increment, decrement;

    public FieldVersion(final IModProvider provider, JButton increment, JButton decrement) {
        this.provider = provider;
        getDocument().addDocumentListener(this);
        this.increment = increment;
        increment.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                IVersion v = provider.getModState().version;
                if (v.canAlter())
                    v.increment();
            }
        });
        this.decrement = decrement;
        decrement.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                IVersion v = provider.getModState().version;
                if (v.canAlter())
                    v.decrement();
            }
        });
    }

    public void checkAlterations(boolean flag) {
        increment.setEnabled(flag);
        decrement.setEnabled(flag);
    }

    public boolean updateVersion() {
        if (provider.hasModState()) {
            Version v;
            try {
                v = Version.parse(getText());
            } catch (InvalidVersionException e) {
                checkAlterations(false);
                return false;
            }
            provider.getModState().version = v;
            checkAlterations(true);
            return true;
        }
        return false;
    }

    @Override
    public void insertUpdate(DocumentEvent e) {
        updateVersion();
    }

    @Override
    public void removeUpdate(DocumentEvent e) {
        updateVersion();
    }

    @Override
    public void changedUpdate(DocumentEvent e) {
        updateVersion();
    }

    @Override
    public void update(boolean flag) {
        setEnabled(flag);
        setText(flag ? provider.getModState().version.toString() : "");
        flag = flag && provider.getModState().version.canAlter();
        increment.setEnabled(flag);
        decrement.setEnabled(flag);
    }

//    public long getTextFieldAsNumber() {
//        String temp = getText();
//        stringLength = temp.length();
//        intList.clear();
//        int index = 0;
//        for (int i = 0; i < stringLength; i++) {
//            if (temp.charAt(i) == '.') {
//                intList.add(i);
//                index++;
//            }
//        }
//        temp = temp.replaceAll("[^\\d]", "");
//        stringLength -= index;
//        long num = 0;
//        try {
//            num = Long.parseLong(temp);
//        } catch (NumberFormatException e) {
//            System.out.println("Failed Parsing Number: " + temp);
//        }
//        return num;
//    }
//
//    public void setVersionNumber(long num) {
//        if (num < 1)
//            num = 1;
//        String temp = String.valueOf(num);
//        while (temp.length() < stringLength)
//            temp = "0" + temp;
//        for (Integer i : intList)
//            temp = temp.substring(0, i) + "." + temp.substring(i);
//        setText(temp);
//    }
}
