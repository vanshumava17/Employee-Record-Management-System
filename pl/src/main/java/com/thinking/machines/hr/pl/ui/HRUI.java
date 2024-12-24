package com.thinking.machines.hr.pl.ui;

import javax.swing.*;
import javax.swing.event.*;
import java.awt.*;
import java.awt.event.*;

public class HRUI extends JFrame {
    private JMenuBar menuBar;
    private JMenu menu;
    private JMenuItem employeesMenuItem;
    private JMenuItem designationsMenuItem;
    private JLabel titleJLabel;
    private JLabel welcomeMessageJLabel;
    private Container container;

    public HRUI() {
        initComponents();
        setAppearance();
        addListeners();
    }

    private void initComponents() {
        titleJLabel = new JLabel("HR Employee Record Management System");
        welcomeMessageJLabel = new JLabel("WEL-COME!!!");
        employeesMenuItem = new JMenuItem("Employees");
        designationsMenuItem = new JMenuItem("Designations");
        menu = new JMenu("Menu");
        menuBar = new JMenuBar();
    }

    private void setAppearance() {
        Font titleFont = new Font("Verdana", Font.BOLD, 18);
        Font welcomeMessageFont = new Font("Verdana", Font.BOLD, 24);
        titleJLabel.setFont(titleFont);
        welcomeMessageJLabel.setFont(welcomeMessageFont);

        menu.add(employeesMenuItem);
        menu.add(designationsMenuItem);
        menuBar.add(menu);
        int x = 0;
        int y = 0;
        titleJLabel.setBounds(x + 20, y + 20, 500, 20);
        welcomeMessageJLabel.setBounds(x + 20, y + 20 + 40, 500, 60);
        container = getContentPane();
        container.setLayout(null);
        container.add(titleJLabel);
        container.add(welcomeMessageJLabel);
        setJMenuBar(menuBar);
        setVisible(true);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        int w, h;
        w = 600;
        h = 400;
        setSize(w, h);
        Dimension d = Toolkit.getDefaultToolkit().getScreenSize();
        setLocation((d.width / 2) - (w / 2), (d.height / 2) - (h / 2));

    }

    private void addListeners() {
        designationsMenuItem.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ev) {
                HRUI.this.setEnabled(false);
                HRUI.this.setVisible(false);
                HRUI.this.welcomeMessageJLabel.setText("HOME");
                DesignationUI designationUI = new DesignationUI(HRUI.this);
                designationUI.setVisible(true);

            }
        });

        employeesMenuItem.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ev) {
                HRUI.this.setEnabled(false);
                HRUI.this.setVisible(false);
                HRUI.this.welcomeMessageJLabel.setText("HOME");
                EmployeeUI employeeUI = new EmployeeUI(HRUI.this);
                employeeUI.setVisible(true);
            }
        });
    }
}
