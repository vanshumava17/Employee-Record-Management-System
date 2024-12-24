import com.thinking.machines.hr.pl.model.*;

import java.awt.*;
import javax.swing.*;

public class DesignationModelTestCase extends JFrame
{
private JTable table;
private DesignationModel designationModel;
private Container container;
private JScrollPane jsp;

public DesignationModelTestCase()
{
designationModel = new DesignationModel();
table = new JTable(designationModel);
jsp = new JScrollPane(table,ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS,ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
container = getContentPane();
container.setLayout(new BorderLayout());
container.add(jsp);

setLocation(100,100);
setSize(500,300);
setVisible(true);
}

public static void main(String gg[])
{
new DesignationModelTestCase();
}
}
