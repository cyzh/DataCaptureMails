package cn.j.utils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;

import org.apache.log4j.Logger;

/**
 * 将获取的所有内容保存在文件中
 * 
 * @author WenC
 * 
 */
public class BackUpToFile {

	private static final Logger LOG = Logger.getLogger(BackUpToFile.class);

	public static void saveToFile(String output) {

		if (output != null) {
			String path = BackUpToFile.class.getResource("/").getPath();
			String currentDate = DateTime.currentDate();
			String filePath = path + "/backupList/"
					+ currentDate.substring(0, currentDate.length() - 3);
			File file = new File(filePath);
			if (!file.exists()) {
				file.mkdirs();
			}
			OutputStreamWriter osw = null;
			try {
				File outFile = new File(filePath + "/" + currentDate + ".txt");
				if (!outFile.exists()) {
					outFile.createNewFile();
				}
				FileOutputStream fos = new FileOutputStream(outFile, true);
				osw = new OutputStreamWriter(fos, "utf-8");
				osw.write(output + "\n");
				osw.flush();
			} catch (FileNotFoundException e) {
				LOG.error(e, e);
				e.printStackTrace();
			} catch (IOException e) {
				LOG.error(e, e);
				e.printStackTrace();
			} finally {
				if (osw != null) {
					try {
						osw.close();
					} catch (IOException e) {
						LOG.error(e, e);
						e.printStackTrace();
					}
				}
			}

		}
	}
}
