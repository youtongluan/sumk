package org.yx.sumk.batis;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.yx.util.Assert;

public class FileDBResource implements DBResource {

	private final File root;

	public FileDBResource(File root) {
		Assert.isTrue(root.isDirectory() && root.exists(),
				root.getAbsolutePath() + " either is not a directory or is not exist");
		this.root = root;
	}

	@Override
	public InputStream dbIni() throws Exception {
		return new FileInputStream(new File(root, "db.ini"));
	}

	@Override
	public Map<String, InputStream> sqlXmls() throws Exception {
		Map<String, InputStream> map = new HashMap<>();
		List<File> xmlFiles = new ArrayList<>();
		parseFileList(xmlFiles, root.getAbsolutePath());
		for (File f : xmlFiles) {
			map.put(f.getAbsolutePath(), new FileInputStream(f));
		}
		return map;
	}

	public void parseFileList(List<File> filelist, String strPath) {
		File dir = new File(strPath);
		File[] files = dir.listFiles();
		if (files != null) {
			for (File f : files) {
				if (f.isDirectory()) {
					parseFileList(filelist, f.getAbsolutePath());
				} else if (f.getName().endsWith(".xml")) {
					filelist.add(f);
				}
			}

		}
	}

}
