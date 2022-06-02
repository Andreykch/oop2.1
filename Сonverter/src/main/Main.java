package main;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

import main.Java.JavaLanguage;
import main.Pascal.PascalLanguage;

public class Main
{
	public static String getTextFromFile(String fullPath)
	{
    	File file = new File(fullPath);
    	if (!file.exists())
    	{
    		System.out.println("File '" + fullPath + "' doesn't exist.");
    		return null;
    	}
    	if (file.length() == 0)
    	{
    		System.out.println("File '" + fullPath + "' is empty.");
    		return null;
    	}
    	String text = null;
    	try
    	{
			Scanner scanner = new Scanner(file);
			scanner.useDelimiter("\\Z");
			text = scanner.next();
			scanner.close();
		}
    	catch (FileNotFoundException e) { }
    	return text;
	}
	
    public static void main(String[] args)
    {
    	Scanner scanner = new Scanner(System.in);
    	System.out.print("Input the full path to the program file: ");
    	String fullPath = scanner.nextLine();
    	scanner.close();
    	String text = getTextFromFile(fullPath);
    	Translator tr = new Translator();
    	tr.register("java", new JavaLanguage());
    	tr.register("pascal", new PascalLanguage());
    	tr.translate("java", "pascal", text);
    }
}
