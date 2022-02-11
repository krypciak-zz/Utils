package me.krypek.utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

public class Utils {

	@SafeVarargs
	public static <T> LinkedList<T> linkedListOf(T... args) {
		LinkedList<T> list = new LinkedList<>();
		for (T arg : args)
			list.add(arg);
		return list;
	}

	@SafeVarargs
	public static <T> ArrayList<T> listOf(T... args) {
		ArrayList<T> list = new ArrayList<>();
		for (T arg : args)
			list.add(arg);
		return list;
	}

	public static String exceptionToString(Exception e) {
		StringWriter sw = new StringWriter();
		PrintWriter pw = new PrintWriter(sw);
		e.printStackTrace(pw);
		return sw.toString();
	}

	public static String readFromFile(String path, String joining) {
		BufferedReader br;
		try {
			br = new BufferedReader(new FileReader(path));
			String content = br.lines().collect(Collectors.joining("\n"));
			br.close();
			return content;
		} catch (Exception e) {
			return null;
		}
	}

	public static String readFromFile(File file, String joining) {
		BufferedReader br;
		try {
			br = new BufferedReader(new FileReader(file));
			String content = br.lines().collect(Collectors.joining("\n"));
			br.close();
			return content;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	public static String[] readFromFileToArray(String path) {
		BufferedReader br;
		try {
			br = new BufferedReader(new FileReader(path));
			String[] content = br.lines().toArray(String[]::new);
			br.close();
			return content;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	public static String[] readFromFileToArray(File file) {
		BufferedReader br;
		try {
			br = new BufferedReader(new FileReader(file));
			String[] content = br.lines().toArray(String[]::new);
			br.close();
			return content;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	public static void writeIntoFile(String path, String content) {
		try {
			PrintWriter pw = new PrintWriter(path);
			pw.println(content);
			pw.close();
		} catch (Exception e) {
			e.printStackTrace();
			return;
		}
	}

	public static String getFileNameWithoutExtension(String name) {
		if(!name.contains("."))
			return name;
		return name.substring(0, name.lastIndexOf('.'));
	}

	public static String getFileExtension(String name) {
		if(!name.contains("."))
			return "";
		return name.substring(name.lastIndexOf('.') + 1);
	}

	public static Integer parseInt(String str) {
		try {
			double d = Double.parseDouble(str);
			if(d % 1 == 0)
				return (int) d;
		} catch (Exception e) {}
		return null;
	}

	public static Double parseDouble(String str) {
		try {
			return Double.parseDouble(str);
		} catch (Exception e) {
			return null;
		}
	}

	public static String[][] doubleSplit(String str, String regex1, String regex2) { return splitArray(str.split(regex1), regex2); }

	public static String[][] splitArray(String[] arr, String regex) {
		String[][] arr1 = new String[arr.length][];
		for (int i = 0; i < arr.length; i++)
			arr1[i] = arr[i].split(regex);
		return arr1;
	}

	public static String[][] splitListToArray(List<String> arr, String regex) {
		String[][] arr1 = new String[arr.size()][];
		for (int i = 0; i < arr.size(); i++)
			arr1[i] = arr.get(i).split(regex);
		return arr1;
	}

	public static boolean serialize(java.io.Serializable obj, String path) {
		new File(path).getParentFile().mkdirs();
		try {
			FileOutputStream file = new FileOutputStream(path);
			ObjectOutputStream out = new ObjectOutputStream(file);
			out.writeObject(obj);
			out.close();
			file.close();
			return true;
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}
	}

	public static <T> T deserialize(String path) {
		try {
			FileInputStream file = new FileInputStream(path);
			ObjectInputStream in = new ObjectInputStream(file);

			@SuppressWarnings("unchecked")
			T obj = (T) in.readObject();

			in.close();
			file.close();
			return obj;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	public static String[] getArrayElementsFromString(String str, char opening, char closing, String sepe) {
		String sub = str.substring(str.indexOf(opening) + 1, str.indexOf(closing));
		if(sub.isBlank())
			return new String[] {};
		String[] splited = sub.split(sepe);
		for (int i = 0; i < splited.length; i++)
			splited[i] = splited[i].strip();
		return splited;
	}

	public static String[] getArrayElementsFromString(String str, char befE, char aftE, RuntimeException e) {
		if(str.isBlank())
			return new String[] {};

		ArrayList<String> list = new ArrayList<>();
		char[] charA = str.toCharArray();
		boolean reading = false;
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < charA.length; i++) {
			char c = charA[i];
			if(c == befE) {
				if(reading)
					throw e;
				reading = true;
			} else if(c == aftE) {
				if(!reading)
					throw e;
				reading = false;
				list.add(sb.toString());
				sb = new StringBuilder();
			} else if(reading)
				sb.append(c);
		}
		return list.toArray(String[]::new);
	}

	public static String arrayToString(Object[] array, char opening, char closing, String sepe) {
		StringBuilder sb = new StringBuilder();
		sb.append(opening);
		int _arrayLen = array.length - 1;
		for (int i = 0; i < array.length; i++) {
			sb.append(array[i].toString());
			if(i != _arrayLen) {
				sb.append(sepe);
				sb.append(' ');
			}
		}
		sb.append(closing);
		return sb.toString();
	}

	public static String arrayToString(Object[] array, char befE, char aftE) {
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < array.length; i++) { sb.append(befE); sb.append(array[i].toString()); sb.append(aftE); }
		return sb.toString();
	}

}
