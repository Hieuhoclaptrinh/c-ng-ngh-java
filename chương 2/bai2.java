import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;

/**
 * Bài 2: Bốn layout manager hay dùng nhất.
 *
 * Layout manager quyết định các thành phần được xếp ở đâu và to nhỏ thế nào.
 * Hãy chạy chương trình rồi KÉO GIÃN cửa sổ để thấy rõ sự khác nhau:
 *
 *   FlowLayout   - xếp ngang như dòng chữ, hết dòng thì xuống dòng, giữ nguyên kích thước
 *   BorderLayout - 5 vùng NORTH/SOUTH/WEST/EAST/CENTER, vùng CENTER "ăn" hết chỗ trống
 *   GridLayout   - lưới ô vuông đều nhau, mọi ô luôn bằng nhau
 *   BoxLayout    - xếp một hàng dọc (hoặc ngang), chèn được khoảng trống linh hoạt
 */
class Bai2Layout {

    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
        }
        SwingUtilities.invokeLater(() -> taoGiaoDien());
    }

    static void taoGiaoDien() {
        JFrame frame = new JFrame("Bài 2 - Các layout manager");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // JTabbedPane: nhiều thẻ, mỗi thẻ chứa một panel demo
        JTabbedPane tabs = new JTabbedPane();
        tabs.addTab("FlowLayout", demoFlow());
        tabs.addTab("BorderLayout", demoBorder());
        tabs.addTab("GridLayout", demoGrid());
        tabs.addTab("BoxLayout", demoBox());

        frame.add(tabs);
        frame.setSize(560, 340);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    static JPanel demoFlow() {
        JPanel p = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 8));
        p.setBorder(BorderFactory.createTitledBorder("Xếp ngang, tự xuống dòng khi hết chỗ"));
        for (int i = 1; i <= 7; i++) {
            p.add(new JButton("Nút " + i));
        }
        return p;
    }

    static JPanel demoBorder() {
        JPanel p = new JPanel(new BorderLayout(6, 6));
        p.setBorder(BorderFactory.createTitledBorder("5 vùng - CENTER chiếm hết phần còn lại"));
        p.add(oMau("NORTH", new Color(0xBBDEFB)), BorderLayout.NORTH);
        p.add(oMau("SOUTH", new Color(0xC8E6C9)), BorderLayout.SOUTH);
        p.add(oMau("WEST", new Color(0xFFE0B2)), BorderLayout.WEST);
        p.add(oMau("EAST", new Color(0xFFE0B2)), BorderLayout.EAST);
        p.add(oMau("CENTER", new Color(0xF8BBD0)), BorderLayout.CENTER);
        return p;
    }

    static JPanel demoGrid() {
        // GridLayout(3, 4): 3 dòng, 4 cột - tất cả ô luôn bằng nhau
        JPanel p = new JPanel(new GridLayout(3, 4, 6, 6));
        p.setBorder(BorderFactory.createTitledBorder("Lưới 3 x 4, mọi ô bằng nhau"));
        for (int i = 1; i <= 12; i++) {
            p.add(new JButton(String.valueOf(i)));
        }
        return p;
    }

    static JPanel demoBox() {
        JPanel p = new JPanel();
        p.setLayout(new BoxLayout(p, BoxLayout.Y_AXIS)); // Y_AXIS = xếp dọc
        p.setBorder(BorderFactory.createTitledBorder("Xếp dọc, chèn khoảng trống bằng Box"));

        p.add(new JLabel("Dòng 1"));
        p.add(Box.createVerticalStrut(15)); // khoảng trống cố định 15px
        p.add(new JLabel("Dòng 2 (cách dòng 1 15px)"));
        p.add(Box.createVerticalGlue()); // "keo" giãn ra, đẩy phần dưới xuống đáy
        p.add(new JLabel("Dòng này bị glue đẩy xuống dưới cùng"));
        return p;
    }

    // Ô màu có chữ ở giữa, dùng cho demo BorderLayout
    static JLabel oMau(String chu, Color mau) {
        JLabel lb = new JLabel(chu, JLabel.CENTER);
        lb.setOpaque(true); // JLabel mặc định trong suốt, phải bật mới thấy màu nền
        lb.setBackground(mau);
        lb.setBorder(BorderFactory.createEmptyBorder(18, 18, 18, 18));
        return lb;
    }
}
