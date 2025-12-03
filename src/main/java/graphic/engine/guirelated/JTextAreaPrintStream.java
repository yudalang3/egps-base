package graphic.engine.guirelated;
import java.io.OutputStream;
import java.io.PrintStream;

import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

/**
 * PrintStream implementation that redirects output to a JTextArea for GUI logging.
 */
public class JTextAreaPrintStream extends PrintStream {
    private final JTextArea textArea;

    public JTextAreaPrintStream(JTextArea textArea) {
        super(new JTextAreaOutputStream(textArea));
        this.textArea = textArea;
    }

    private static class JTextAreaOutputStream extends OutputStream {
        private final JTextArea textArea;

        public JTextAreaOutputStream(JTextArea textArea) {
            this.textArea = textArea;
        }

        @Override
        public void write(int b) {
            // 将一个字节写入 JTextArea
            textArea.append(String.valueOf((char) b));
            // 自动滚动到底部
            textArea.setCaretPosition(textArea.getDocument().getLength());
        }

        @Override
        public void write(byte[] b, int off, int len) {
            // 将字节数组的一部分写入 JTextArea
            textArea.append(new String(b, off, len));
            // 自动滚动到底部
            textArea.setCaretPosition(textArea.getDocument().getLength());
        }
    }

    public static void main(String[] args) {
        // 创建 JFrame
        JFrame frame = new JFrame("JTextArea PrintStream Example");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(500, 400);

        // 创建 JTextArea 并将其放入 JScrollPane 中
        JTextArea textArea = new JTextArea();
        JScrollPane scrollPane = new JScrollPane(textArea);
        frame.add(scrollPane);

        // 创建 JTextAreaPrintStream 并设置为 System.out
        PrintStream printStream = new JTextAreaPrintStream(textArea);
        System.setOut(printStream);
        System.setErr(printStream);

		printStream.println();

        // 显示 JFrame
        frame.setVisible(true);

        // 打印一些测试信息
        System.out.println("Hello, World!");
        System.out.println("This is a test message.");
        System.err.println("This is an error message.");
    }
}