package com.pan;

import java.io.*;

/**
 * 测试类加载器
 * @author Pan
 *
 */
public class MyClassLoader extends ClassLoader {
	/**
	 * 类文件根目录
	 */
	private String rootDir = null;
	
	/**
	 * 
	 * @param rootDir 类文件根目录
	 */
	public MyClassLoader(String rootDir) {
		this.rootDir = rootDir;
	}
	
	@Override
	protected Class<?> findClass(String name) throws ClassNotFoundException {
		String clzDir = rootDir + File.separatorChar + name.replace('.', File.separatorChar) + ".class";//通过包名和类名组装完整路径
		System.out.println("name=" + name);
		System.out.println("clzDir=" + clzDir);
		
		byte[] classData = getClassData(clzDir);

		if (classData == null) {
			throw new ClassNotFoundException();
		} else {
			return defineClass(name, classData, 0, classData.length);
		}
	}

	/**
	 * 获取类文件数据,可以在此进行一些类加密解密操作
	 * @param path
	 * @return
	 */
	private byte[] getClassData(String path) {
		try (InputStream ins = new FileInputStream(path); ByteArrayOutputStream baos = new ByteArrayOutputStream()) {

			int bufferSize = 4096;
			byte[] buffer = new byte[bufferSize];
			int bytesNumRead = 0;
			while ((bytesNumRead = ins.read(buffer)) != -1) {
				baos.write(buffer, 0, bytesNumRead);
			}
			return baos.toByteArray();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}
}
