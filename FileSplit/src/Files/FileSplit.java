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
	 * ʵ�ֶԴ��ļ����и���ϲ��� ��ָ�������У����һ���ļ��г�10�ݣ���ָ����С�У���ÿ����󲻳���10M���������ַ�ʽ�����ԡ�
	 */
	public static void main(String[] args) {
		JFileChooser jfc = new JFileChooser();// Swing�е�ѡ���ļ�
		// ѡ���ļ�
		String lastPath = Registery.getInstance().getValue("openPath");
		if(lastPath != null) {
			jfc.setCurrentDirectory(new File(lastPath));
		} 
		int result = jfc.showOpenDialog(null);// ��ʾ�������ѡ���ļ�
		File file = null;// Ҫ�и���ļ�
		File dest = null;// Ŀ�ĵ��ļ�
		try {
			if (result == JFileChooser.APPROVE_OPTION) {// ѡ���ļ�
				// �и��ļ�
				file = jfc.getSelectedFile();// �û�ѡ����ļ�
				String path = file.getParent();
				Registery.getInstance().writeValue("openPath", path);
				dest = new File(file.getParent(), "spliFile");
				cutingFile(file, dest);// �и��
				// 2�ϲ�(����ʱ��ֱ�ӶԸղ��и����Щ�ļ���Ƭ���кϲ�)
//				String fileName = file.getName();
//				mergeDemo(dest, fileName);// �ϲ��ļ�
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private static void mergeDemo(File dest, String fileName) throws IOException {
		// ��׳�Է���(��File����ȥ����)
		if (!dest.exists()) {
			throw new RuntimeException("�ļ�������");
		}
		// ��һ���ļ����齫������ļ���װ����
		File parth[] = dest.listFiles();// ����һ������·�������飬��Щ·������ʾ�˳���·������ʾ��Ŀ¼�е��ļ���
		if (parth.length == 0) {
			throw new RuntimeException("��Ƭ������");
		}
		// y�����������ϲ�
		ArrayList<FileInputStream> list = new ArrayList<FileInputStream>();
		// for (int i = 0; i < parth.length; i++) {
		// list.add(new FileInputStream(parth[i]));//���������������ϲ��������ļ���˳���ҵ�
		// }
		for (int i = 0; i < parth.length; i++) {
			list.add(new FileInputStream(new File(dest, fileName + (i + 1) + "part")));// �׽Ӽ������ļ��ӵ�˳��Ҫ��ԭ�ļ�һ��
		}
		// ö�ٶ���ӿ�
		Enumeration<FileInputStream> en = Collections.enumeration(list);
		SequenceInputStream sq = new SequenceInputStream(en);
		// д�뵽���ļ���
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
		// �и�
		try {
			FileInputStream fis = new FileInputStream(source);
			if (!dest.exists()) {// �ļ�����IO��Ҫ�ж��ļ��Ƿ���ڡ�
				dest.mkdir();
			}
			byte buf[] = new byte[1024 * 1024 * 10];// 10M
			int len = 0;
			int cout = 1;
			while ((len = fis.read(buf)) != -1) {
				// ��OUT�����и��ļ�
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
