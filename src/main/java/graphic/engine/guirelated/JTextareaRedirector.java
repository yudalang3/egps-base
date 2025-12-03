package graphic.engine.guirelated;

import java.awt.Font;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;

import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.SwingUtilities;

/**
 * 解决的问题：
 * 这里将 标准输出与标准错误输出重定向到一个 textArea
 * 后面所有程序中产生的输出都将捕获到这个text中
 * 
 * 要注意调用finished()方法，释放被占用的输出
 * @author yudal
 *
 */
public class JTextareaRedirector {

	private PrintStream oldOut;
	private PrintStream oldErr;

	public void initialize(JTextArea destination) {
		JTextAreaOutputStream out = new JTextAreaOutputStream(destination);

		oldOut = System.out;
		oldErr = System.err;

		System.setOut(new PrintStream(out));// 设置输出重定向
		System.setErr(new PrintStream(out));// 将错误输出也重定向,用于e.pritnStackTrace
	}

	public void finished() {
		System.setOut(oldOut);
		System.setErr(oldErr);

		oldOut = null;
		oldErr = null;
	}

	public void test() {
		JFrame jf = new JFrame();
		jf.setBounds(100, 100, 700, 400);
		JTextArea jta = new JTextArea();
		jta.setFont(new Font(null, Font.BOLD, 20));
		jta.setEditable(false);// 设置不可编辑
		JTextAreaOutputStream out = new JTextAreaOutputStream(jta);

		PrintStream out2 = System.out;
		PrintStream err = System.err;
		System.setOut(new PrintStream(out));// 设置输出重定向
		System.setErr(new PrintStream(out));// 将错误输出也重定向,用于e.pritnStackTrace
		JScrollPane jsp = new JScrollPane(jta);// 设置滚动条
		jf.add(jsp);
		jf.setVisible(true);

		System.setOut(out2);// 设置输出重定向
		System.setErr(err);// 将错误输出也重定向,用于e.pritnStackTrace
	}

	public static void main(String[] args) {
		JTextareaRedirector rd = new JTextareaRedirector();
		rd.test();
		try {
			Thread.sleep(1000*3);
		} catch (InterruptedException e) {
			e.printStackTrace();
		} // 休眠3秒
		
		System.out.println("重定向");
		System.out.println(12345);
		System.out.println(true);
		try {
			int[] s = { 1, 2, 3, 4, 5 };
			int i = s[3];
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}

class JTextAreaOutputStream extends OutputStream {
	private final JTextArea destination;

	public JTextAreaOutputStream(JTextArea destination) {
		if (destination == null)
			throw new IllegalArgumentException("Destination is null");

		this.destination = destination;
	}

	@Override
	public void write(byte[] buffer, int offset, int length) throws IOException {
		final String text = new String(buffer, offset, length);
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				destination.append(text);
			}
		});
	}

	@Override
	public void write(int b) throws IOException {
		write(new byte[] { (byte) b }, 0, 1);
	}
}