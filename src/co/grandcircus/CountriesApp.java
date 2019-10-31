package co.grandcircus;

import java.util.ArrayList;
import java.util.Scanner;

public class CountriesApp {
	// Create scanner to use in the class's methods
	public static Scanner scnr = new Scanner(System.in);

	public static void main(String[] args) {
		// Declare variables
		String cont = "yes";
		ArrayList<Country> countryList = null;
		// CountriesTextFile.createCountriesFile("countries.txt");
		CountriesBinaryFile.createCountriesFile("countries.dat");

		// Welcome user
		System.out.println("Welcome to the Countries App!");

		// Loop program while user chooses to continue
		while (cont.equalsIgnoreCase("yes")) {
			// Populate countryList with values saved in the countries.txt/countries.dat
			// file
			// countryList = CountriesTextFile.readCountryList();
			countryList = CountriesBinaryFile.readCountryList();

			// Display menu choices, user Validator.getInt to get user action input, then
			// switch on their input to execute requested actions by calling appropriate
			// method.
			System.out.println("1. Display the country list\n2. Add a country\n3. Delete country\n4. Exit");
			int menuChoice = Validator.getInt(scnr, "Select the action you would like to take (enter the number):\n", 1,
					4);
			switch (menuChoice) {
			case 1:
				displayCountries(countryList);
				System.out.println("Do you want to take another action? (yes/no)");
				cont = Validator.getCont(scnr);
				break;
			case 2:
				addCountry();
				System.out.println("Do you want to take another action? (yes/no)");
				cont = Validator.getCont(scnr);
				break;
			case 3:
				deleteCountry(countryList);
				break;
			case 4:
				cont = confirmQuit();
			}
		}

		// Say bye and close scnr.
		System.out.println("Bye!");
		scnr.close();
	}

	// If the user selects the 'quit' option from the menu, this method confirms
	// they do want to quit. Returns a string which is plugged into the "cont"
	// variable in the main method.
	public static String confirmQuit() {
		System.out.println("Are you sure you want to quit? (yes/no)");
		String quit = Validator.getCont(scnr); // Use Validator.getCont to validate input
		if (quit.equalsIgnoreCase("yes")) {
			return "no";
		} else {
			return "yes";
		}
	}

	// This method displays a formatted list of the countries in the countryList
	public static void displayCountries(ArrayList<Country> countryList) {
		// Declare variables used for formatting
		int counter = 1;
		int nameLength = 0;
		int popLength = 0;

		// For loop determines the length of the longest country name and population
		for (Country c : countryList) {
			if (c.getName().length() > nameLength) {
				nameLength = c.getName().length();
			}
			if (Integer.toString(c.getPopulation()).length() > popLength) {
				popLength = Integer.toString(c.getPopulation()).length();
			}
		}
		System.out.println("Country List");
		makeDivider(nameLength, popLength); // Method creates a divider of appropriate length using the nameLength and
											// popLength gathered above.

		// For each loop prints each country on the list to a formatted line
		for (Country c : countryList) {
			System.out.printf("%d. %-" + nameLength + "s (pop.: %-" + popLength + "d)\n", counter, c.getName(),
					c.getPopulation());
			counter++;
		}
		makeDivider(nameLength, popLength);
	}

	// Method creates a divider of a length determined with the nameLength and
	// popLength gathered in the displayCountries method.
	public static void makeDivider(int nameLength, int popLength) {
		for (int i = 0; i < nameLength + popLength + 13; i++) {
			System.out.print("*");
			if (i == nameLength + popLength + 12) {
				System.out.print("\n");
			}
		}
	}

	// This method takes user input to create a Country object and then calls
	// another method to write the country to the countries.txt/.dat file
	public static void addCountry() {
		System.out.println("Enter country name:");
		String name = scnr.nextLine();
		int pop = Validator.getInt(scnr, "Entry country population:\n", 1, Integer.MAX_VALUE);
		Country c = new Country(name, pop);
		// CountriesTextFile.writeCountry(c);
		CountriesBinaryFile.writeCountry(c);
	}

	// This method takes user input on the country to delete from the list, confirms
	// their selection, and then calls another method to remove the country from the
	// countries.txt/.dat file
	public static void deleteCountry(ArrayList<Country> countryList) {
		displayCountries(countryList);
		int deleteChoice = Validator.getInt(scnr, "Enter country to delete (number):\n", 1, countryList.size());
		System.out.println("Confirm this is the country you want to delete (yes/no)");
		System.out.printf("%s (pop.: %d)\n", countryList.get(deleteChoice - 1).getName(),
				countryList.get(deleteChoice - 1).getPopulation());
		String deleteConfirm = Validator.getCont(scnr); // Validator.getCont returns a "yes" or "no"
		if (deleteConfirm.equals("yes")) {
			// CountriesTextFile.eraseCountry(countryList.get(deleteChoice - 1));
			CountriesBinaryFile.eraseCountry(countryList.get(deleteChoice - 1));
		} else {
			System.out.println("OK, try again:");
			deleteCountry(countryList); // If user does not confirm their selection, recurs the method with a new
										// country entry.
		}
	}

}
