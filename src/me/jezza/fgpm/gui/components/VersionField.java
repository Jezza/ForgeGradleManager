package me.jezza.fgpm.gui.components;

import javax.swing.*;
import java.util.ArrayList;

public class VersionField extends JTextField {

    private int stringLength = 0;
    private ArrayList<Integer> intList;

    public VersionField() {
        intList = new ArrayList<Integer>();
    }

    public void incrementVersion() {
        setVersionNumber(getTextFieldAsNumber() + 1);
    }

    public void decrementVersion() {
        setVersionNumber(getTextFieldAsNumber() - 1);
    }

    public long getTextFieldAsNumber() {
        String temp = getText();
        stringLength = temp.length();
        intList.clear();
        int index = 0;
        for (int i = 0; i < stringLength; i++) {
            if (temp.charAt(i) == '.') {
                intList.add(i);
                index++;
            }
        }
        temp = temp.replaceAll("[^\\d]", "");
        stringLength -= index;
        long num = 0;
        try {
            num = Long.parseLong(temp);
        } catch (NumberFormatException e) {
            System.out.println("Failed Parsing Number: " + temp);
        }
        return num;
    }

    public void setVersionNumber(long num) {
        if (num < 1)
            num = 1;
        String temp = String.valueOf(num);
        while (temp.length() < stringLength)
            temp = "0" + temp;
        for (Integer i : intList)
            temp = temp.substring(0, i) + "." + temp.substring(i);
        setText(temp);
    }

}
