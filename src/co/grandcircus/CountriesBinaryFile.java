package co.grandcircus;

import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;

public class CountriesBinaryFile {

	// This method creates a countries.dat file if one does not exist already.
	public static void createCountriesFile(String fileName) {
		Path filePath = Paths.get("src", "co", "grandcircus", fileName);
		if (Files.notExists(filePath)) {
			try {
				Files.createFile(filePath);
			} catch (IOException e) {
				System.out.println("Something went terribly wrong.");
			}
		} else {
			System.out.println("The file already exists.");
		}
	}

	// This method takes in a Country object and writes it to the countries.dat file
	public static void writeCountry(Country c) {
		String fileName = "countries.dat";
		Path filePath = Paths.get("src", "co", "grandcircus", fileName);
		File file = filePath.toFile();
		DataOutputStream output = null;
		try {
			output = new DataOutputStream(new FileOutputStream(file, true));
			// Calls.writeBytes on a string including a space at the start to address a
			// reading issue wherein the first character was not being read.
			output.writeBytes(" " + c.getName());
			output.writeBytes(","); // Writes the bytes for a "," to separate name and population pairs
			output.writeBytes(Integer.toString(c.getPopulation())); // Convert the population integer to a string and
																	// write it to the .dat file as bytes.
			System.out.println(c.getName() + " successfully printed to file.");
			output.close();
		} catch (FileNotFoundException e) {
			System.out.println("ERROR: File Not Found");
		} catch (IOException e) {
			System.out.println("Horrific error, wow.");
		}
	}

	// This method returns an ArrayList<Country> of the countries stored in the
	// countries.dat file.
	public static ArrayList<Country> readCountryList() {
		ArrayList<Country> countries = new ArrayList<>();
		String fileName = "countries.dat";
		Path filePath = Paths.get("src", "co", "grandcircus", fileName);
		File file = filePath.toFile();
		FileInputStream fi = null;
		try {
			fi = new FileInputStream(file);
			while (fi.read() != -1) {
				// This constructor converts the Byte[] saved in the countries.dat file to a
				// String of UTF-8 characters
				String str = new String(fi.readAllBytes(), "UTF-8");
				String[] arr = str.split(",| "); // Split the string into a ray on spaces and commas.

				// For loop increments i by 2 each pass because data was encoded in the
				// countries.dat file in pairs of name strings and population integers. A
				// country object is created using each pair.
				for (int i = 0; i < arr.length; i += 2) {
					countries.add(new Country(arr[i], Integer.parseInt(arr[i + 1])));
				}
			}
			fi.close();
			return countries;
		} catch (FileNotFoundException e) {
			System.out.println("ERROR: File Not Found");
		} catch (IOException e) {
			System.out.println("Wow, that error is nuts.");
		}
		return countries;
	}

	// This method takes in a Country object and removes its data (saved in byte
	// form) from the countries.dat file.
	public static void eraseCountry(Country c) {
		String readFileName = "countries.dat";
		Path readFilePath = Paths.get("src", "co", "grandcircus", readFileName);
		File readFile = readFilePath.toFile();
		String tempFileName = "temp.txt";
		createCountriesFile(tempFileName); // Generate a temporary file which wiil eventually replace the current file
		Path tempFilePath = Paths.get("src", "co", "grandcircus", tempFileName);
		File tempFile = tempFilePath.toFile();
		DataOutputStream output = null;
		FileInputStream fi = null;
		try {
			fi = new FileInputStream(readFile);
			output = new DataOutputStream(new FileOutputStream(tempFile, true));
			while (fi.read() != -1) {
				// This constructor converts the Byte[] saved in the countries.dat file to a
				// String of UTF-8 characters
				String str = new String(fi.readAllBytes(), "UTF-8");
				String[] arr = str.split(",| "); // Split the string on commas and spaces
				// For loop increments i by 2 each pass because data was encoded in the
				// countries.dat file in pairs of name strings and population integers.
				for (int i = 0; i < arr.length; i += 2) {
					// If the first String in the pair does not match the entered Country name ...
					if (!arr[i].equals(c.getName())) {
						// ... then write it and the other String in the pair to the tempFile. This will
						// mean the entered country will be excluded from the new temp file.
						output.writeBytes(" " + arr[i]);
						output.writeBytes(",");
						output.writeBytes(arr[i + 1]);
					}
				}
			}
			readFile.delete(); //Delete the old countries.dat file
			tempFile.renameTo(readFile); //Rename the new temp file to countries.dat
			System.out.println(c.getName() + " successfully removed from file.");
			fi.close();
			output.close();
		} catch (FileNotFoundException e) {
			System.out.println("ERROR: File Not Found");
		} catch (IOException e) {
			System.out.println("Something went terribly wrong!");
		}
	}

}
