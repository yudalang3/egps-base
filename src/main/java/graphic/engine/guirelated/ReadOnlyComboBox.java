package graphic.engine.guirelated;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * 一个可以显示所有选项但无法更改选中项的只读 JComboBox
 */
public class ReadOnlyComboBox<T> extends JComboBox<T> {
    private int lockedIndex = 0;  // 默认选中项的索引

    public ReadOnlyComboBox(T[] items, int defaultIndex) {
        super(items);
        this.lockedIndex = defaultIndex;
        setSelectedIndex(defaultIndex);

        // 阻止用户更改选中项
        addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (getSelectedIndex() != lockedIndex) {
                    SwingUtilities.invokeLater(() -> setSelectedIndex(lockedIndex));
                }
            }
        });

        // 自定义渲染器：灰色下拉选项
        setRenderer(new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(
                JList<?> list, Object value, int index,
                boolean isSelected, boolean cellHasFocus
            ) {
                Component c = super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                c.setEnabled(false);  // 灰色不可点
                return c;
            }
        });

        setFocusable(false);
    }

    public void setLockedIndex(int index) {
        this.lockedIndex = index;
        setSelectedIndex(index);
    }

    public int getLockedIndex() {
        return lockedIndex;
    }
}
