package ch02;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.zip.ZipFile;
import java.util.zip.ZipInputStream;

/**
 * ZIP或JAR文件形式的类路径
 */
public class ZipEntry implements Entry {
	
	/** 存放ZIP或JAR文件的绝对路径 */
	private String absPath;
	
	public ZipEntry(String path) {
		File absDirPath = new File(path);
		this.absPath = absDirPath.toString();
	}

	/**
	 * 在指定ZIP或JAR中寻找和加载class文件
	 * 打开指定ZIP或JAR，遍历每一个文件匹配名字和传入className相同的文件读取出来
	 * @param className 文件名称 示例：Test.class
	 * @return class文件的字节数组
	 */
	public byte[] readClass(String className) throws Exception {
		try (InputStream in=new BufferedInputStream(new FileInputStream(this.absPath));
		        ZipInputStream zin=new ZipInputStream(in);) {
			ZipFile zf=new ZipFile(this.absPath);
			java.util.zip.ZipEntry ze;
			while((ze=zin.getNextEntry())!=null){
				if(ze.isDirectory()){
				} else {
					if (ze.getName().equals(className) || ze.getName().contains("/" + className)) {
						try (InputStream is = zf.getInputStream(ze);
								ByteArrayOutputStream bos = new ByteArrayOutputStream(1000);) {
							byte[] b = new byte[1000];  
				            int n;
				            while ((n = is.read(b)) != -1) {  
				                bos.write(b, 0, n);  
				            }
				            return bos.toByteArray();
						}
					}
				}
			}
		}
		return null;
	}

	/**
	 * 返回变量的字符串表示
	 * @return 字符串格式的变量
	 */
	public String string() {
		return this.absPath;
	}
	
	public static void main(String[] args) {
		ZipEntry ze = new ZipEntry("D:\\zengjia/zengjia.zip");
		try {
			byte[] bs = ze.readClass("test.class");
			if (bs == null) {
				System.out.println("未找到class");
			} else {
				for (byte b : bs) {
					String hex = Integer.toHexString(b & 0xFF);
		            if (hex.length() == 1)
		            {
		                hex = '0' + hex;
		            }
		            System.out.print(hex.toUpperCase() + " ");
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}