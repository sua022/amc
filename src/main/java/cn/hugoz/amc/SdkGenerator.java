package cn.hugoz.amc;

import com.github.javaparser.ParseException;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;

import cn.hugoz.amc.asm.ClassGenerator;
import cn.hugoz.amc.parser.FileEntity;
import cn.hugoz.amc.parser.JavaFileParser;

/**
 * Created by hugozhong on 2016/6/20.
 */
public class SdkGenerator {

	private static void parse(ArrayList<FileEntity> fileEntites, String targetAnnotation, File dir)
			throws FileNotFoundException, ParseException {
		if (dir.isDirectory()) {
			File[] files = dir.listFiles();
			if (files != null) {
				for (File file : files) {
					parse(fileEntites, targetAnnotation, file);
				}
			}
		} else {
			FileEntity fileEntity = new JavaFileParser(targetAnnotation, dir).parse();
			System.out.println("parse " + dir + " finish.");
			if (fileEntity != null) {
				fileEntites.add(fileEntity);
			}
		}
	}

	public static void run(File dir, String targetAnnotation, String sdkDestDir) {
		ArrayList<FileEntity> fileEntites = new ArrayList<>();
		try {
			parse(fileEntites, targetAnnotation, dir);
			for (FileEntity fileEntity : fileEntites) {
				new ClassGenerator(sdkDestDir, fileEntity).generate();
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (ParseException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
