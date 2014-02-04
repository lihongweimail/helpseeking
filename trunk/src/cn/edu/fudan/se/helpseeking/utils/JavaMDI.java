/**
 * 
 */
package cn.edu.fudan.se.helpseeking.utils;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Insets;
import java.awt.Point;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JLayeredPane;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.SwingConstants;
import javax.swing.border.Border;

/**
 * @author Grand  2012-11-15下午2:49:48
 *
 */
public class JavaMDI extends JPanel {

    private static final long serialVersionUID = 1L;


    private static final int BACKLAYER = 1;

    
    Font FONT = new Font("宋体", Font.PLAIN, 12);

    private final BackImagePane layerPane;
    //子窗体背景色
    private int[] colors = { 0xdddddd, 0xaaaaff, 0xffaaaa, 0xaaffaa, 0xffffaa, 0xffaaff, 0xaaffff , 0xddddff};
  
    private Color getColor(int i, float f) {
        int b = (int)((i & 0xff) * f);
        i = i >> 8;
        int g = (int)((i & 0xff) * f);
        i = i >> 8;
        int r = (int)((i & 0xff) * f);
        return new Color(r,g,b);
    }

    public JavaMDI() {
        super(new BorderLayout());
        
        Image image;
//        try {
        	String path=System.getProperty("user.dir");
            image = new ImageIcon(path+"/javamdi.jpg").getImage();
//        } catch (IOException e) {
//            image=null;
//        }
        layerPane = new BackImagePane();
        layerPane.setImage(image);

        //随机生成个子面板，作为内部窗体，实际使用时替换JPanel内部容器即可。
        for (int i=0; i<colors.length; i++) {
            JPanel p = createChildPanel(i);
            p.setLocation(i*80 + 20, i*50 + 15);
            layerPane.add(p, BACKLAYER);
        }
        add(layerPane, BorderLayout.CENTER);
        
    }


    /**
     * 创建子面板，作为在内部移动的窗体
     * @param i
     * @return
     */
    private JPanel createChildPanel(int i) {
        //使用html标记设定颜色
        String html = "<html><font color=#333333> 子窗体ID "+ i +"</font></html>";
        JLabel label = new JLabel(html);
        label.setFont(FONT);
        label.setOpaque(true);
        label.setHorizontalAlignment(SwingConstants.CENTER);
        //设定背景色
        label.setBackground(getColor(colors[i], 0.85f));
        //设定边距
        Border border1 = BorderFactory.createEmptyBorder(4, 4, 4, 4);;
        label.setBorder(border1);

        JTextArea text = new JTextArea();
        text.setBackground( new Color(colors[i]));
        text.setMargin(new Insets(4,4,4,4));
        text.setLineWrap(true);

        JPanel p = new JPanel();

        Color col = getColor(colors[i], 0.5f);
        Border border = BorderFactory.createLineBorder(col, 1);
        p.setBorder(border);

        //移动监听
        DragMouseListener  li = new DragMouseListener(p);
        p.addMouseListener(li);
        p.addMouseMotionListener(li);

        p.setLayout( new BorderLayout());
        p.add(label, BorderLayout.NORTH);
        p.add(text, BorderLayout.CENTER);
        //子窗体大小
        p.setSize( new Dimension(200, 150));
        return p;
    }


    /**
     * 子窗体拖拽监听
     * @author chenpeng
     *
     */
    class DragMouseListener implements MouseListener, MouseMotionListener {
        Point origin;
        JPanel panel;

        DragMouseListener(JPanel p) {
            panel = p;
        }
        public void mousePressed(MouseEvent e) {
            origin = new Point( e.getX(), e.getY());
            //移动
            layerPane.moveToFront(panel);
        }
        public void mouseDragged(MouseEvent e) {
            if (origin == null) return;
            int dx = e.getX() - origin.x;
            int dy = e.getY() - origin.y;
            Point p = panel.getLocation();
            panel.setLocation( p.x + dx, p.y + dy);
        }

        public void mouseClicked(MouseEvent e) {}
        public void mouseEntered(MouseEvent e) {}
        public void mouseExited(MouseEvent e) {}
        public void mouseReleased(MouseEvent e) {}
        public void mouseMoved(MouseEvent e) {}
    }

    //用分层面板JLayeredPane制作MDI背景
    class BackImagePane extends JLayeredPane {
        /**
         * 
         */
        private static final long serialVersionUID = 1L;

        public BackImagePane() {
            super();
        }

        void setImage(Image img) {
            bgImage = img;
        }
        private Image bgImage;

        public void paint(Graphics g) {
            if (bgImage != null) {
                int imageh = bgImage.getHeight(null);
                int imagew = bgImage.getWidth(null);
                Dimension d = getSize();
                for(int h=0; h<d.getHeight(); h += imageh) {
                    for(int w=0; w<d.getWidth(); w += imagew) {
                        g.drawImage(bgImage, w, h, this);
                    }
                }
            }
            super.paint(g);
        }
    }

//    public static void main(String[] args) {
//        EventQueue.invokeLater(new Runnable() {
//            public void run() {
//                createGUI();
//            }
//        });
//    }
//    public static void createGUI() {
//        final JFrame frame = new JFrame("JAVA实现可设置背景的MDI窗口");
//        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
//        frame.setSize(new Dimension(800, 600));
//        frame.getContentPane().add(new JavaMDI());
//        frame.setAlwaysOnTop(true);
//        frame.setLocationRelativeTo(null);
//        frame.setVisible(true);
//    }
}
