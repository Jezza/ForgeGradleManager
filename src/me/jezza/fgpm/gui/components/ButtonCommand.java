package me.jezza.fgpm.gui.components;

import me.jezza.fgpm.core.managers.CommandManager;
import me.jezza.fgpm.core.managers.CommandManager.GradleCommand;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayDeque;
import java.util.Deque;

public class ButtonCommand extends JButton implements ActionListener {

    private static final Deque<GradleCommand> queue = new ArrayDeque<>();

    private GradleCommand command;

    public ButtonCommand(String text, GradleCommand command) {
        super(text);
        addActionListener(this);
        setToolTipText(command.name());
        this.command = command;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if ((ActionEvent.CTRL_MASK & e.getModifiers()) != 0)
            queue.clear();

        if (queue.isEmpty() || queue.getLast() != command)
            queue.offerLast(command);
        if ((ActionEvent.SHIFT_MASK & e.getModifiers()) == 0)
            executeQueue();
    }

    public static void executeQueue() {
        if (queue.isEmpty())
            return;

        CommandManager.parseGradleCommand(getQueueString());
        queue.clear();
    }

    private static String getQueueString() {
        if (queue.isEmpty())
            return "";

        StringBuilder stringBuilder = new StringBuilder();
        GradleCommand lastCommand = queue.getFirst();
        stringBuilder.append(lastCommand.name());
        if (queue.size() > 1)
            for (GradleCommand command : queue)
                if (lastCommand != command) {
                    stringBuilder.append(" ").append(command.name());
                    lastCommand = command;
                }
        return stringBuilder.toString();
    }
}
