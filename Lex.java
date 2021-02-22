/*
 * Kristen Zuber
 * Dr. Salimi
 * Programming Languages
 * 15 April 2020
 */

package parserPackage;

import java.util.*;
import java.io.*;

public class Lex {

	 // Global declarations of variables
	static final int MAX_LEXEME_LEN = 100;
	static Token charClass;                            // Compare to enum to identify the character's class
	static int lexLen;                                 // Current lexeme's length
	static char lexeme[] = new char[MAX_LEXEME_LEN];   // Current lexeme's character array
	static char nextChar;
	static Token nextToken;
	static int charIndex;
	static char ch = '0';
	static FileWriter myOutput;
	
	 //Tokens and categories
	enum Token {
		INT_LIT,
		IDENT,
		ASSIGN_OP,
		ADD_OP,
		SUB_OP,
		MULT_OP,
		DIV_OP,
		LEFT_PAREN,
		RIGHT_PAREN,
		LETTER,
		DIGIT,
		UNKNOWN,
		END_OF_FILE
	}

 
	 /*********************** Main driver ***********************/
	public static void main(String[] args) throws IOException {

		 // Variables 
		String line;
		Scanner scan;
	    myOutput = new FileWriter("lexOutput.txt");

		 /* Formatting for the beginning of the output 
		  * (Each print statement goes to the console and the output file for convenience) 
		  */
		System.out.println("Kristen Z. Student, CSCI4200, Spring 2020, Lexical Analyzer"); 
		myOutput.write("Kristen Z. Student, CSCI4200, Spring 2020, Lexical Analyzer\n");
		System.out.println("********************************************************************************");
		myOutput.write("********************************************************************************\n");

		// Open the file, and scan each line for lexical analysis 
		try {
			scan = new Scanner(new File("sourceStatements.txt"));

			// For each line, grab each character 
			while (scan.hasNextLine()) {
				line = scan.nextLine();
				System.out.println("Input: " + line);
				myOutput.write("Input: " + line + "\n");
				charIndex = 0;
				
				if (getChar(line)) {
					// Perform lexical analysis within array bounds 
					while (charIndex < line.length()) {
						lex(line);
					}

					System.out.println("********************************************************************************");
					myOutput.write("********************************************************************************\n");
				}
			}

			// If there are no more lines, it must be the end of file 
			if (!scan.hasNext()) {
				System.out.printf("Next token is: %-12s Next lexeme is %s\n", "END_OF_FILE", "EOF");
				myOutput.write(String.format("Next token is: %-12s Next lexeme is %s\n", "END_OF_FILE", "EOF"));
				System.out.print("Lexical analysis of the program is complete!");
				myOutput.write("Lexical analysis of the program is complete!\n");
			}
			
			scan.close();
		}

		catch (FileNotFoundException e) {
			System.out.println(e.toString());
		}

		catch (Exception e) {
			e.printStackTrace();
		}
		
		myOutput.close();
	}

	
	/************************************************
	 *  Assign each lexeme with its respective token.
	 * This allows the lexical analyzer to determine
	 * what the Token names connect to.
	 ************************************************/
	private static Token lookup(char ch) {

		switch (ch) {
		
		case '(':
			addChar();
			nextToken = Token.LEFT_PAREN;
			break;

		case ')':
			addChar();
			nextToken = Token.RIGHT_PAREN;
			break;

		case '+':
			addChar();
			nextToken = Token.ADD_OP;
			break;

		case '-':
			addChar();
			nextToken = Token.SUB_OP;
			break;

		case '*':
			addChar();
			nextToken = Token.MULT_OP;
			break;

		case '/':
			addChar();
			nextToken = Token.DIV_OP;
			break;

		case '=':
			addChar();
			nextToken = Token.ASSIGN_OP;
			break;

	/*
	 * No default case - each lexeme should fall
	 * within one of the categories set above.
	 */
		}

		return nextToken;
	}

	
	/************* addChar - a function to add nextChar to lexeme *************/
	private static boolean addChar() {
		
		if (lexLen <= 98) {
			lexeme[lexLen++] = nextChar;
			lexeme[lexLen] = 0;
			return true;
		}
		
		else {
			System.out.println("Error - lexeme is too long \n");
			return false;
		}
	}

	
	/************* getChar - a function to get the next character in the line *************/
	protected static boolean getChar(String ln) {

		if (charIndex >= ln.length()) {
			return false;
		}

		nextChar = ln.charAt(charIndex++);

		if (Character.isDigit(nextChar)) {
			charClass = Token.DIGIT;
		} 
		
		else if (Character.isAlphabetic(nextChar)) {
			charClass = Token.LETTER;
		} 
		
		else {
			charClass = Token.UNKNOWN;
		}

		return true;
	}

	/************* getNonBlank - a method to skip whitespace *************/
	public static boolean getNonBlank(String ln) {
		while (Character.isSpaceChar(nextChar) || nextChar == '	') {
			if (!getChar(ln)){
				return false;
			}
		}
		return true;
	}
	

	 /* @throws IOException ***************************************************/
	/************* lex - a simple lexical analyzer for arithmetic expressions *************/
	public static Token lex(String ln) throws IOException {
		
		lexLen = 0;
		getNonBlank(ln);
		
		switch (charClass) {

		// Parse identifiers 
		case LETTER:
			nextToken = Token.IDENT;
			addChar();
			
			if (getChar(ln)) {
				while (charClass == Token.LETTER || charClass == Token.DIGIT) {
					addChar();
					
					if (!getChar(ln)) {
						break;
					}
				}
				
				if (charClass == Token.UNKNOWN && charIndex == ln.length()) {
					charIndex--;
				}
			}
			break;
			
		// Parse integer literals 
		case DIGIT:
			nextToken = Token.INT_LIT;
			addChar();
			
			if (getChar(ln)) {
				while (charClass == Token.DIGIT) {
					addChar();
					
					if(!getChar(ln)) {
						break;
					}
				}
				
				if (charClass == Token.UNKNOWN && charIndex == ln.length()) {
					charIndex--;
				}
			}
			break;
			
		 // Parentheses and operators 
		case UNKNOWN:
			lookup(nextChar);
			getChar(ln);
			break;
			
		default:
			nextToken = Token.UNKNOWN;
			break;
		} // End of switch 
		
		 // Print each token and its respective lexeme 
		System.out.printf("Next token is: %-12s\n", String.valueOf(nextToken));
		
		return nextToken;
	} // End of function lex 
}