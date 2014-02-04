/**
 * 
 */
package cn.edu.fudan.se.helpseeking.utils;

import java.awt.Color;
import java.awt.Component;

import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;

/**
 * @author Grand  2012-11-15下午4:19:18
 *
 */
public class JTableUtil {
	
	public static void makeTableFace(JTable table)
	{
		try {
			DefaultTableCellRenderer tcr=new DefaultTableCellRenderer()            //自定义表格单元作色模式   重写作色方法
			{
				/**
				 * 
				 */
				private static final long serialVersionUID = 1L;

				public Component getTableCellRendererComponent(JTable table,Object value, boolean isSelected, boolean hasFocus, int row , int column)
				{
					if (row%2==0) 
						setBackground(Color.white);
					else						
						if (row%2==1)
							setBackground(new Color(206,231,255));

					return super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);


				}

			};

			//根据表格单元的新作色定义，对每个单元设置新的作色
			for (int i = 0; i < table.getColumnCount(); i++) {
				table.getColumn(table.getColumnName(i)).setCellRenderer(tcr);
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 
	 */

}
