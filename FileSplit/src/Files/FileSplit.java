package Files;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.SequenceInputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Enumeration;

import javax.swing.JFileChooser;
/**
 * @author huangxianbo
 * @date 2019/12/19
 * @version V1.0
 */
public class FileSplit {

	/**
	 * 实现对大文件的切割与合并。 按指定个数切（如把一个文件切成10份）或按指定大小切（如每份最大不超过10M），这两种方式都可以。
	 */
	public static void main(String[] args) {
		JFileChooser jfc = new JFileChooser();// Swing中的选择文件
		// 选择文件
		String lastPath = Registery.getInstance().getValue("openPath");
		if(lastPath != null) {
			jfc.setCurrentDirectory(new File(lastPath));
		} 
		int result = jfc.showOpenDialog(null);// 显示框架用于选择文件
		File file = null;// 要切割的文件
		File dest = null;// 目的地文件
		try {
			if (result == JFileChooser.APPROVE_OPTION) {// 选中文件
				// 切割文件
				file = jfc.getSelectedFile();// 用户选择的文件
				String path = file.getParent();
				Registery.getInstance().writeValue("openPath", path);
				dest = new File(file.getParent(), "spliFile");
				cutingFile(file, dest);// 切割方法
				// 2合并(运行时，直接对刚才切割的那些文件碎片进行合并)
//				String fileName = file.getName();
//				mergeDemo(dest, fileName);// 合并文件
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private static void mergeDemo(File dest, String fileName) throws IOException {
		// 健壮性防护(用File对象去开道)
		if (!dest.exists()) {
			throw new RuntimeException("文件不存在");
		}
		// 用一个文件数组将里面的文件都装起来
		File parth[] = dest.listFiles();// 返回一个抽象路径名数组，这些路径名表示此抽象路径名表示的目录中的文件。
		if (parth.length == 0) {
			throw new RuntimeException("碎片不存在");
		}
		// y用序列流来合并
		ArrayList<FileInputStream> list = new ArrayList<FileInputStream>();
		// for (int i = 0; i < parth.length; i++) {
		// list.add(new FileInputStream(parth[i]));//不能这样，这样合并出来的文件是顺序乱的
		// }
		for (int i = 0; i < parth.length; i++) {
			list.add(new FileInputStream(new File(dest, fileName + (i + 1) + "part")));// 套接技术，文件加的顺序要和原文件一样
		}
		// 枚举对象接口
		Enumeration<FileInputStream> en = Collections.enumeration(list);
		SequenceInputStream sq = new SequenceInputStream(en);
		// 写入到新文件中
		FileOutputStream fou = new FileOutputStream(new File(dest, fileName));
		byte buf[] = new byte[1024];
		sq.read(buf);
		int len = 0;
		while ((len = sq.read(buf)) > 0) {
			fou.write(buf, 0, len);
		}
		fou.close();
		sq.close();
	}

	private static void cutingFile(File source, File dest) {
		// 切割
		try {
			FileInputStream fis = new FileInputStream(source);
			if (!dest.exists()) {// 文件操作IO流要判断文件是否存在。
				dest.mkdir();
			}
			byte buf[] = new byte[1024 * 1024 * 10];// 10M
			int len = 0;
			int cout = 1;
			while ((len = fis.read(buf)) != -1) {
				// 用OUT流来切割文件
				String targetName = source.getName();
				if (targetName.indexOf(".") != -1) {
					targetName = targetName.substring(0, targetName.indexOf(".")) + (cout++)
							+ targetName.substring(targetName.indexOf("."), targetName.length());
				} else {
					targetName = source.getName() + (cout++);
				}
				FileOutputStream fout = new FileOutputStream(new File(dest, targetName));
				fout.write(buf, 0, len);
				fout.close();
			}
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
