package com.natia.secretmod.utils;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

public class FrameMaker {
    List<JLabel> jlabels = new ArrayList<>();
    List<JButton> jButtons = new ArrayList<>();
    List<JProgressBar> jProgressBars = new ArrayList<>();
    List<JTextField> jTextFields = new ArrayList<>();
    List<JPanel> jRects = new ArrayList<>();

    JFrame frame;
    JPanel panel;

    Dimension windowDimensions;
    String windowtitle;
    int closeOperation;
    boolean resizeable;
    /**
     * Creates the basic window
     */
    public FrameMaker(String windowtitle, Dimension windowDimensions, int closeOperation, boolean resizeable) {
        this.windowDimensions = windowDimensions;
        this.windowtitle = windowtitle;
        this.closeOperation = closeOperation;
        this.resizeable = resizeable;
        this.frame = new JFrame(windowtitle);
        this.panel = new JPanel();
    }

    public JPanel getJPanel() {
        return panel;
    }

    public JFrame getJFrame() {
        return frame;
    }
    /**
     * Create text and add to window
     */
    public JLabel addText(String text, int x, int y, int fontSize, boolean bold) {
        System.out.println("     [+] Creating text \"" + text + "\"");

        JLabel label = new JLabel(text);
        if (bold)
            label.setFont(new Font("Arial", Font.BOLD, fontSize));
        else
            label.setFont(new Font("Arial", Font.PLAIN, fontSize));

        Dimension size = label.getPreferredSize();
        label.setBounds(x, y, size.width, size.height);

        jlabels.add(label);
        return label;
    }

    /**
     * Creates button with an ActionListener
     */
    public JButton addButton(String text, int x, int y, int scale, ActionListener listener) {
        System.out.println("     [+] Creating button \"" + text + "\"");
        JButton button = new JButton(text);
        Dimension size = button.getPreferredSize();
        button.setBounds(x, y, scale, size.height);
        button.setFocusPainted(false);
        button.addActionListener(listener);
        jButtons.add(button);
        return button;
    }

    /**
     * Creates button with an ActionListener (Scalable) & Action can be Nullable
     */
    public JButton addButton(String text, int x, int y, int scaleX, int scaleY, ActionListener listener) {
        System.out.println("     [+] Creating button \"" + text + "\"");
        JButton button = new JButton(text);
        Dimension size = button.getPreferredSize();
        button.setBounds(x, y, scaleX, scaleY);
        button.setFocusPainted(false);
        if (listener != null)
            button.addActionListener(listener);

        jButtons.add(button);
        return button;
    }

    /**
     * Create a progress bar
     */
    public JProgressBar addProgressBar(int lengthOfTask, int x, int y, int scaleX, int scaleY) {
        JProgressBar progressBar = new JProgressBar(0, lengthOfTask);
        progressBar.setValue(0);
        progressBar.setBounds(x, y, scaleX, scaleY);
        progressBar.setStringPainted(true);
        jProgressBars.add(progressBar);
        return progressBar;
    }
    /**
     * Create an image
     */
    public JLabel addImage(ImageIcon image, int x, int y, int width, int height) {
        JLabel imageLabel = new JLabel(image);
        imageLabel.setBounds(x, y, width, height);
        jlabels.add(imageLabel);
        return imageLabel;
    }
    /**
     * Create a textbox
     */
    public JTextField addTextField(int x, int y, int scale) {
        JTextField field = new JTextField();
        field.setBounds(x, y, scale, field.getPreferredSize().height);
        jTextFields.add(field);
        return field;
    }
    /**
     * Clears the entire panel pack
     */
    public void clear() {
        jlabels.clear();
        jButtons.clear();
        jProgressBars.clear();
        jTextFields.clear();
        jRects.clear();
    }

    /**
     * Overrides the current jFrame with new elements
     */
    public JFrame override() {
        changeLook();

        panel = new JPanel();
        frame.getContentPane().removeAll();
        frame.repaint();

        panel.setLayout(null);
        jlabels.forEach(panel::add);
        jButtons.forEach(panel::add);
        jProgressBars.forEach(panel::add);
        jTextFields.forEach(panel::add);
        jRects.forEach(panel::add);

        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        frame.setDefaultCloseOperation(closeOperation);
        frame.add(panel);
        frame.setPreferredSize(windowDimensions);
        frame.pack();
        frame.setResizable(resizeable);
        frame.setVisible(true);
        return frame;
    }
    /**
     * Packs everything together then displays the window
     */
    public JFrame pack() {
        changeLook();

        frame.getContentPane();

        panel.setLayout(null);
        jlabels.forEach(panel::add);
        jButtons.forEach(panel::add);
        jProgressBars.forEach(panel::add);
        jTextFields.forEach(panel::add);
        jRects.forEach(panel::add);

        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        frame.setDefaultCloseOperation(closeOperation);
        frame.add(panel);
        frame.setPreferredSize(windowDimensions);
        frame.setVisible(true);
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setResizable(resizeable);

        return frame;
    }

    /**
     * Changes from default (ugly) look to the windows/linux/mac look
     */
    private void changeLook() {
        try {
            // Set System L&F
            UIManager.setLookAndFeel(
                    UIManager.getSystemLookAndFeelClassName());
        }
        catch (UnsupportedLookAndFeelException | ClassNotFoundException | InstantiationException | IllegalAccessException e) {
            e.printStackTrace();
        }
    }
}