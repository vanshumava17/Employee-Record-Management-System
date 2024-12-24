package com.thinking.machines.hr.pl.ui;

import com.thinking.machines.hr.pl.model.*;
import com.thinking.machines.hr.bl.exceptions.*;
import com.thinking.machines.hr.bl.interfaces.pojo.*;
import com.thinking.machines.hr.bl.pojo.*;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.event.*;
import javax.swing.table.*;

import java.io.*;

public class EmployeeUI extends JFrame {
    private HRUI hrui = null; // to enable the home window taking it's reference heres
    private EmployeeModel employeeModel;
    private JLabel titleLabel;
    private JLabel searchErrorLabel;
    private JLabel searchLabel;
    private JTextField searchTextField;
    private JButton clearSearchTextFieldButton;
    private JTable employeeTable;
    private JScrollPane scrollPane;
    private EmployeePanel employeePanel;

    private enum MODE {
        ADD, VIEW, EDIT, DELETE, EXPORT_TO_PDF
    };

    private MODE mode;
    private Container container;
    private ImageIcon logoIcon;
    private ImageIcon addIcon;
    private ImageIcon saveIcon;
    private ImageIcon deleteIcon;
    private ImageIcon editIcon;
    private ImageIcon cancelIcon;
    private ImageIcon pdfIcon;
    private ImageIcon clearIcon;

    public EmployeeUI(HRUI hrui) {
        this.hrui = hrui;
        initComponents();
        setAppearance();
        addListeners();
        setViewMode();
        employeePanel.setViewMode();
    }

    private void initComponents() {
        logoIcon = new ImageIcon(this.getClass().getResource("/icons/logoIcon.png"));
        setIconImage(logoIcon.getImage());
        addIcon = new ImageIcon(this.getClass().getResource("/icons/addImage.png"));
        saveIcon = new ImageIcon(this.getClass().getResource("/icons/saveImage.png"));
        editIcon = new ImageIcon(this.getClass().getResource("/icons/editImage.png"));
        deleteIcon = new ImageIcon(this.getClass().getResource("/icons/deleteImage.png"));
        cancelIcon = new ImageIcon(this.getClass().getResource("/icons/cancelImage.png"));
        pdfIcon = new ImageIcon(this.getClass().getResource("/icons/exportToPdf.png"));
        clearIcon = new ImageIcon(this.getClass().getResource("/icons/clearImage.png"));
        employeeModel = new EmployeeModel();
        titleLabel = new JLabel("Employees");
        searchErrorLabel = new JLabel("");
        searchLabel = new JLabel("Search");
        searchTextField = new JTextField();
        clearSearchTextFieldButton = new JButton(clearIcon);
        employeeTable = new JTable(employeeModel);
        scrollPane = new JScrollPane(employeeTable, ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS,
                ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        container = getContentPane();
        employeePanel = new EmployeePanel();
    }

    private void setAppearance() {
        Font titleFont = new Font("Verdana", Font.BOLD, 18);
        Font searchErrorFont = new Font("Verdana", Font.BOLD, 12);
        Font captionFont = new Font("Verdana", Font.BOLD, 16);
        Font dataFont = new Font("Verdana", Font.PLAIN, 16);
        Font columnHeaderFont = new Font("Verdana", Font.BOLD, 16);
        titleLabel.setFont(titleFont);
        searchErrorLabel.setFont(searchErrorFont);
        searchErrorLabel.setForeground(Color.RED);
        searchLabel.setFont(captionFont);
        searchTextField.setFont(dataFont);
        employeeTable.setFont(dataFont);
        employeeTable.setRowHeight(35);
        employeeTable.getColumnModel().getColumn(0).setPreferredWidth(20);
        employeeTable.getColumnModel().getColumn(1).setPreferredWidth(100);
        employeeTable.getColumnModel().getColumn(2).setPreferredWidth(200);
        JTableHeader header = employeeTable.getTableHeader();
        header.setFont(columnHeaderFont);
        employeeTable.setRowSelectionAllowed(true);
        employeeTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION); // will allow user to select only one
                                                                                // row at a time
        header.setReorderingAllowed(false);
        header.setResizingAllowed(false);

        container.setLayout(null);
        int lm = 0;
        int tm = 0;
        titleLabel.setBounds(lm + 10, tm + 10, 200, 40);
        searchErrorLabel.setBounds(lm + 10 + 100 + 400 + 10 - 75, tm + 10 + 20 + 10, 100, 20);
        searchLabel.setBounds(lm + 10, tm + 10 + 40 + 10, 100, 30);
        searchTextField.setBounds(lm + 10 + 100 + 5, tm + 10 + 40 + 10, 400, 30);
        clearSearchTextFieldButton.setBounds(lm + 10 + 100 + 5 + 400 + 5, tm + 10 + 40 + 10, 30, 30);
        scrollPane.setBounds(lm + 10, tm + 10 + 40 + 10 + 30 + 10, 565, 300);
        employeePanel.setBounds(lm + 10, tm + 10 + 40 + 10 + 30 + 10 + 300 + 10, 565, 200);

        clearSearchTextFieldButton.setBorderPainted(false);

        container.add(titleLabel);
        container.add(searchErrorLabel);
        container.add(searchLabel);
        container.add(searchTextField);
        container.add(clearSearchTextFieldButton);
        container.add(scrollPane);
        container.add(employeePanel);

        setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        int w, h;
        w = 600;
        h = 700;
        setSize(w, h);
        Dimension d = Toolkit.getDefaultToolkit().getScreenSize();
        setLocation((d.width / 2) - (w / 2), (d.height / 2) - (h / 2));
    }

    private void addListeners() {
        
        // window listener to enabel/visible the home window from where EmployeeUI object 
        // is created 
        this.addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                hrui.setEnabled(true);  // Re-enable HRUI
                hrui.setVisible(true);
                dispose();  // Close the current window
            }
        });

        searchTextField.getDocument().addDocumentListener(new DocumentListener() {
            public void changedUpdate(DocumentEvent de) {
                searchEmployee();
            }

            public void removeUpdate(DocumentEvent de) {
                searchEmployee();
            }

            public void insertUpdate(DocumentEvent de) {
                searchEmployee();
            }
        });

        clearSearchTextFieldButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ev) {
                searchTextField.setText("");
                searchTextField.requestFocus();
            }
        });

        employeeTable.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
            public void valueChanged(ListSelectionEvent ev) {
                int selectedRowIndex = employeeTable.getSelectedRow();
                try {
                    EmployeeInterface employee = employeeModel.getEmployeeAt(selectedRowIndex);
                    employeePanel.setEmployee(employee);
                } catch (BLException blException) {
                    employeePanel.clearEmployee();
                }
            }
        });
    }

    private void searchEmployee() {
        searchErrorLabel.setText("");
        String title = searchTextField.getText().trim();
        if (title.length() == 0)
            return;
        clearSearchTextFieldButton.setEnabled(true); // this line is added by me to enhance performance
        int rowIndex = 0;
        try {
            rowIndex = employeeModel.indexOfName(title, true);
        } catch (BLException blException) {
            searchErrorLabel.setText("Not Found");
            return;
        }
        employeeTable.setRowSelectionInterval(rowIndex, rowIndex);
        Rectangle rectangle = employeeTable.getCellRect(rowIndex, 0, true); // why true we don't know but we need to
                                                                               // specify as true
        employeeTable.scrollRectToVisible(rectangle);
    }

    private void setViewMode() {
        this.mode = MODE.VIEW;
        if (employeeModel.getRowCount() == 0) {
            this.searchTextField.setEnabled(false);
            this.clearSearchTextFieldButton.setEnabled(false);
            this.employeeTable.setEnabled(false);
        } else {
            this.searchTextField.setEnabled(true);
            this.clearSearchTextFieldButton.setEnabled(false);
            this.employeeTable.setEnabled(true);
        }
    }

    private void setAddMode() {
        this.mode = MODE.ADD;
        this.searchTextField.setEnabled(false);
        this.clearSearchTextFieldButton.setEnabled(false);
        this.employeeTable.setEnabled(false);
    }

    private void setEditMode() {
        this.mode = MODE.EDIT;
        this.searchTextField.setEnabled(false);
        this.clearSearchTextFieldButton.setEnabled(false);
        this.employeeTable.setEnabled(false);
    }

    private void setDeleteMode() {
        this.mode = MODE.DELETE;
        this.searchTextField.setEnabled(false);
        this.clearSearchTextFieldButton.setEnabled(false);
        this.employeeTable.setEnabled(false);
    }

    private void setExportToPDFMode() {
        this.mode = MODE.EXPORT_TO_PDF;
        this.searchTextField.setEnabled(false);
        this.clearSearchTextFieldButton.setEnabled(false);
        this.employeeTable.setEnabled(false);
    }

    // inner classes
    private class EmployeePanel extends JPanel {
        private JLabel titleCaptionLabel;
        private JLabel titleLabel;
        private JTextField titleTextField;
        private JButton clearTitleTextFieldButton;
        private JButton addButton;
        private JButton editButton;
        private JButton cancelButton;
        private JButton deleteButton;
        private JButton exportToPDFButton;
        private JPanel buttonsPanel;
        private EmployeeInterface employee; // will be very imp

        EmployeePanel() {
            setBorder(BorderFactory.createLineBorder(new Color(165, 165, 165)));
            initComponents();
            setAppearance();
            addListeners();
        }

        private void initComponents() {
            employee = null;
            titleCaptionLabel = new JLabel("Employee");
            titleLabel = new JLabel("");
            titleTextField = new JTextField();
            clearTitleTextFieldButton = new JButton(clearIcon);
            addButton = new JButton(addIcon);
            editButton = new JButton(editIcon);
            cancelButton = new JButton(cancelIcon);
            deleteButton = new JButton(deleteIcon);
            exportToPDFButton = new JButton(pdfIcon);
            buttonsPanel = new JPanel();
            addButton.setBorderPainted(false);
            deleteButton.setBorderPainted(false);
            editButton.setBorderPainted(false);
            cancelButton.setBorderPainted(false);
            exportToPDFButton.setBorderPainted(false);
            clearTitleTextFieldButton.setBorderPainted(false);
        }

        private void setAppearance() {
            Font captionFont = new Font("Verdana", Font.BOLD, 16);
            Font dataFont = new Font("Verdana", Font.PLAIN, 16);
            titleCaptionLabel.setFont(captionFont);
            titleLabel.setFont(dataFont);
            titleTextField.setFont(dataFont);
            setLayout(null);
            int lm, tm;
            lm = 0;
            tm = 0;
            titleCaptionLabel.setBounds(lm + 10, tm + 20, 110, 30);
            titleLabel.setBounds(lm + 10 + 110 + 20, tm + 20, 350, 30);
            titleTextField.setBounds(lm + 10 + 110 + 20, tm + 20, 350, 30);
            clearTitleTextFieldButton.setBounds(lm + 10 + 110 + 20 + 350 + 5, tm + 20, 30, 30);

            buttonsPanel.setBounds(50, tm + 20 + 30 + 30, 465, 75);
            buttonsPanel.setBorder(BorderFactory.createLineBorder(new Color(165, 165, 165)));
            addButton.setBounds(70, 12, 50, 50);
            editButton.setBounds(70 + 50 + 20, 12, 50, 50);
            cancelButton.setBounds(70 + 50 + 20 + 50 + 20, 12, 50, 50);
            deleteButton.setBounds(70 + 50 + 20 + 50 + 20 + 50 + 20, 12, 50, 50);
            exportToPDFButton.setBounds(70 + 50 + 20 + 50 + 20 + 50 + 20 + 50 + 20, 12, 50, 50);
            buttonsPanel.setLayout(null);
            buttonsPanel.add(addButton);
            buttonsPanel.add(editButton);
            buttonsPanel.add(cancelButton);
            buttonsPanel.add(deleteButton);
            buttonsPanel.add(exportToPDFButton);
            add(titleTextField);
            add(titleCaptionLabel);
            add(titleLabel);
            add(clearTitleTextFieldButton);
            add(buttonsPanel);
        }

        private void addListeners() {
            this.addButton.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent ev) {
                    if (mode == MODE.VIEW) { // change to view mode Button(add) clicked
                        setAddMode();
                    } else // save employee and change to view mode Button(save) clicked
                    {
                        if (addEmployee()) {
                            setViewMode();
                        }
                    }
                }
            });

            this.editButton.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent ev) {
                    if (mode == MODE.VIEW) {
                        setEditMode();
                    } else {
                        if (updateEmployee()) {
                            setViewMode();
                        }
                    }
                }
            });

            this.cancelButton.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent ev) {
                    setViewMode();
                }
            });

            this.deleteButton.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent ev) {
                    setDeleteMode();
                }
            });

            this.exportToPDFButton.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent ev) {
                    JFileChooser jfc = new JFileChooser();
                    jfc.setCurrentDirectory(new File("."));
                    int selectedOption = jfc.showSaveDialog(EmployeeUI.this);
                    if (selectedOption == JFileChooser.APPROVE_OPTION) {
                        try {
                            File selectedFile = jfc.getSelectedFile();
                            String pdfFileString = selectedFile.getAbsolutePath();
                            if (pdfFileString.endsWith("."))
                                pdfFileString += "pdf";
                            else if (pdfFileString.endsWith(".pdf") == false)
                                pdfFileString += ".pdf";
                            File file = new File(pdfFileString);
                            File parent = new File(file.getParent());
                            if (parent.exists() == false || parent.isDirectory() == false) {
                                JOptionPane.showMessageDialog(EmployeeUI.this,
                                        "Incorrect path : " + parent.getAbsolutePath());
                                return;
                            }
                            employeeModel.exportToPDF(file);
                            JOptionPane.showMessageDialog(EmployeeUI.this,
                                    "Employees exported to " + file.getAbsolutePath());
                        } catch (BLException blException) {
                            if (blException.hasGenericException()) {
                                JOptionPane.showMessageDialog(EmployeeUI.this, blException.getGenericException());
                            }
                        } catch (Exception e) {
                            System.out.println(e);
                        }
                    }
                }
            });

            this.clearTitleTextFieldButton.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent ev) {
                    titleTextField.setText("");
                }
            });
        } // addListeners ends here

        private boolean addEmployee() {
            return false;
        }

        private boolean updateEmployee() {
            return false; 
            // to be implemented
        }

        private void removeEmployee() {
            // to be implemented
        }

        public void setEmployee(EmployeeInterface employee) {
            this.employee = employee;
            titleLabel.setText(employee.getName());
        }

        public void clearEmployee() {
            this.employee = null;
            titleLabel.setText("");
        }

        private void setViewMode() {
            EmployeeUI.this.setViewMode();
            this.addButton.setIcon(addIcon);
            this.editButton.setIcon(editIcon);
            this.titleTextField.setVisible(false);
            this.titleLabel.setVisible(true);
            this.addButton.setEnabled(true);
            this.cancelButton.setEnabled(false);
            this.clearTitleTextFieldButton.setVisible(false);
            if (employeeModel.getRowCount() == 0) {
                this.editButton.setEnabled(false);
                this.deleteButton.setEnabled(false);
                this.exportToPDFButton.setEnabled(false);
            } else {
                this.editButton.setEnabled(true);
                this.deleteButton.setEnabled(true);
                this.exportToPDFButton.setEnabled(true);
            }
        }

        private void setAddMode() {
            EmployeeUI.this.setAddMode();
            this.clearTitleTextFieldButton.setVisible(true);
            this.titleTextField.setText("");
            this.titleLabel.setVisible(false);
            this.titleTextField.setVisible(true);
            this.addButton.setIcon(saveIcon); // saveIcon
            this.editButton.setEnabled(false);
            this.cancelButton.setEnabled(true);
            this.deleteButton.setEnabled(false);
            this.exportToPDFButton.setEnabled(false);
        }

        private void setEditMode() {
            if (employeeTable.getSelectedRow() < 0
                    || employeeTable.getSelectedRow() >= employeeModel.getRowCount()) {
                JOptionPane.showMessageDialog(this, "Select employee to edit");
                return;
            }
            EmployeeUI.this.setEditMode();
            this.clearTitleTextFieldButton.setEnabled(true);
            this.titleTextField.setText(employee.getName());
            this.titleLabel.setVisible(false);
            this.titleTextField.setVisible(true);
            this.addButton.setEnabled(false);
            this.cancelButton.setEnabled(true);
            this.deleteButton.setEnabled(false);
            this.exportToPDFButton.setEnabled(false);
            this.editButton.setIcon(editIcon);// saveIcon
        }

        private void setDeleteMode() {
            if (employeeTable.getSelectedRow() < 0
                    || employeeTable.getSelectedRow() >= employeeModel.getRowCount()) {
                JOptionPane.showMessageDialog(this, "Select employee to Delete");
                return;
            }
            EmployeeUI.this.setDeleteMode();
            this.addButton.setEnabled(false);
            this.cancelButton.setEnabled(false);
            this.deleteButton.setEnabled(false);
            this.exportToPDFButton.setEnabled(false);
            this.editButton.setEnabled(false);
            removeEmployee();
            setViewMode();
        }

        private void setExportToPDFMode() {
            EmployeeUI.this.setExportToPDFMode();
            this.addButton.setEnabled(false);
            this.cancelButton.setEnabled(false);
            this.deleteButton.setEnabled(false);
            this.exportToPDFButton.setEnabled(false);
            this.editButton.setEnabled(false);
        }
    }
}