package com.edmProxy.util;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.LineNumberReader;
import java.io.RandomAccessFile;
import java.io.StreamTokenizer;
import java.nio.ByteBuffer;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.channels.FileChannel.MapMode;
import java.util.ArrayList;
import java.util.Scanner;

import com.edmProxy.entity.ProxyEntity;

public class ReadFile {
	// ��ȡtxt�ļ�
	public static ArrayList<String> ReadTxt(String path) throws IOException {
		ArrayList<String> list = new ArrayList();// ��str��������ݰ�,����ŵ������У���������û������ַ����
		String tempStr = ""; // ��Ŷ�ȡ�������ʼ���ַ����
		// ����������10000000���ַ�������
		char data[] = new char[10000000];
		FileInputStream fileInputStream = new FileInputStream(path);
		InputStreamReader inputStreamReader = new InputStreamReader(
				fileInputStream);
		// ��������bufferedReader
		BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
		int i = 0;
		while ((tempStr = bufferedReader.readLine()) != null) {
			if (tempStr != null) {
				if (!"".equals(tempStr)) {
					if (list.size() < 40000) {
						i++;
						list.add(tempStr.trim());
					} else {
						System.out.println("����������40000");
						return null;
					}
				}
			}
		}
		// �ر��������Ͷ�ȡ��
		fileInputStream.close();
		bufferedReader.close();
		return list;

	}
	

	public static void sumLines(String filename) throws IOException {
//		LineNumberReader lnr = new LineNumberReader(new FileReader(filename));
//		lnr.setLineNumber(1);
//		StreamTokenizer stok = new StreamTokenizer(lnr);
//		stok.parseNumbers();
//		stok.eolIsSignificant(true);
//		stok.nextToken();
//		while (stok.ttype != StreamTokenizer.TT_EOF) {
//			int lineno = lnr.getLineNumber();
//			double sum = 0;
//			while (stok.ttype != StreamTokenizer.TT_EOL) {
//				if (stok.ttype == StreamTokenizer.TT_NUMBER)
//					sum += stok.nval;
//				stok.nextToken();
//			}
//			System.out.println("Sum of line " + lineno + " is " + sum);
//			stok.nextToken();
//		}
//		 File file = new File(filename);//Text�ļ�
//         BufferedReader br = new BufferedReader(new FileReader(file));//����һ��BufferedReader������ȡ�ļ�
//         String s = null;
//         while((s = br.readLine())!=null){//ʹ��readLine������һ�ζ�һ��
//             System.out.println(s);
//         }
//         br.close();
	}

	public static void main(String[] args) throws IOException {
		ReadFile.sumLines("F:\\Test1-20000.txt");
	}
}
