package gui.simple.tools;

import cli.tools.ClipboardPathNormalized;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * GUI tool for normalizing file paths from clipboard.
 */
public class FilePathNormalizedGUI {
    public static void main(String[] args) {
        // 创建窗口
        JFrame frame = new JFrame("ClipboardPathNormalizedPath");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(400, 200);

        // 设置面板
        JPanel panel = new JPanel();
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        panel.setLayout(new BorderLayout());
        frame.add(panel);

        // 添加按钮
        JButton button = new JButton("Clipboard PathNormalized Path");
        panel.add(button, BorderLayout.NORTH);

        // 添加文本区域用于显示信息
        JTextArea textArea = new JTextArea(3, 25); // 行数，列数
        textArea.setEditable(false); // 禁止编辑
        panel.add(new JScrollPane(textArea), BorderLayout.CENTER); // 使用滚动面板包裹文本区域
        textArea.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        // 按钮事件监听器
        button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // 更新文本区域内容
                textArea.append("\nalready normalize the file path and\n copy to the clipboard");
                new ClipboardPathNormalized().run();
            }
        });

        // 显示窗口
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }
}