package co.grandcircus;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;

public class CountriesTextFile {

	public static ArrayList<Country> readCountryList() {
		ArrayList<Country> countries = new ArrayList<>();
		String fileName = "countries.txt";
		Path filePath = Paths.get("src", "co", "grandcircus", fileName);
		File file = filePath.toFile();
		BufferedReader br = null;
		try {
			br = new BufferedReader(new FileReader(file));
			String line = br.readLine();
			while (line != null) {
				String[] split = line.split("\\|");
				countries.add(new Country(split[0], Integer.parseInt(split[1])));
				line = br.readLine();
			}
			br.close();
			return countries;
		} catch (FileNotFoundException e) {
			System.out.println("ERROR: File Not Found");
		} catch (IOException e) {
			System.out.println("Wow, that error is nuts.");
		}
		return countries;
	}

	public static void writeCountry(Country c) {
		String fileName = "countries.txt";
		Path filePath = Paths.get("src", "co", "grandcircus", fileName);
		File file = filePath.toFile();
		PrintWriter output = null;
		try {
			output = new PrintWriter(new FileOutputStream(file, true));
			output.println(c);
			System.out.println(c.getName() + " successfully printed to file.");
			output.close();
		} catch (FileNotFoundException e) {
			System.out.println("ERROR: File Not Found");
		}
	}

	public static void eraseCountry(Country c) {
		String readFileName = "countries.txt";
		Path readFilePath = Paths.get("src", "co", "grandcircus", readFileName);
		File readFile = readFilePath.toFile();
		String tempFileName = "temp.txt";
		createCountriesFile(tempFileName);
		Path tempFilePath = Paths.get("src", "co", "grandcircus", tempFileName);
		File tempFile = tempFilePath.toFile(); 
		PrintWriter output = null;
		BufferedReader br = null;
		try {
			output = new PrintWriter(new FileOutputStream(tempFile));
			br = new BufferedReader(new FileReader(readFile));
			String line = br.readLine();
			while (line != null) {
				String[] lineCheck = line.split("\\|");
				if (!lineCheck[0].equals(c.getName())) {
					output.println(line);
				}
				line = br.readLine();
			}
			readFile.delete();
			tempFile.renameTo(readFile);
			System.out.println(c.getName() + " successfully removed from file.");
			output.close();
		} catch (FileNotFoundException e) {
			System.out.println("ERROR: File Not Found");
		} catch (IOException e) {
			System.out.println("Something went terribly wrong!");
		}
	}

	public static void createCountriesFile(String fileName) {
		Path filePath = Paths.get("src", "co", "grandcircus", fileName);
		if (Files.notExists(filePath)) {
			try {
				Files.createFile(filePath);
				//System.out.println("File created successfully.");
			} catch (IOException e) {
				System.out.println("Something went terribly wrong.");
			}
		} else {
			System.out.println("The file already exists.");
		}
	}

}
