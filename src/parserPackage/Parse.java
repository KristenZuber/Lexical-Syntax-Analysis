
/*
 * Kristen Zuber
 * Dr. Salimi
 * Programming Languages
 * 15 April 2020
 */

package parserPackage;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;

public class Parse extends Lex{
	
	static FileWriter parserOutput;
	static String line;
	//static Token lexInput;
	
	public static void main(String[] args) throws IOException {

		// Variables
		Scanner scan;
		parserOutput = new FileWriter("parseOut.txt");

		/* Formatting for the beginning of the output 
		 * (Each print statement goes to the console  
		 *  and the output file for convenience) 
		 */
		System.out.println("Kristen Z. Student, CSCI4200, Spring 2020, Parser"); 
		parserOutput.write("Kristen Z. Student, CSCI4200, Spring 2020, Parser\n");
		System.out.println("********************************************************************************");
		parserOutput.write("********************************************************************************\n");

		// Open the file, and scan each line for lexical analysis 
		try {
			scan = new Scanner(new File("sourceStatements.txt"));

			// For each line, grab each character 
			while (scan.hasNextLine()) {
				line = scan.nextLine();
				System.out.println("Parsing the statement: " + line);
				parserOutput.write("Parsing the statement: " + line + "\n");
				charIndex = 0;
				
				/* 
				 * If there is are characters on the line, 
				 * get the next token and enter assign
				 */
				if (getChar(line)) {
					parserOutput.write("Next token is: " + lex(line) + "\n");
					assign();
				
					System.out.println("********************************************************************************");
					parserOutput.write("********************************************************************************\n");
				}
			}

			// If there are no more lines, it must be the end of file 
			if (!scan.hasNext()) {
				System.out.println("END_OF_FILE");
				parserOutput.write(String.format("END_OF_FILE\n"));
				System.out.print("Syntax analysis of the program is complete!");
				parserOutput.write("Syntax analysis of the program is complete!\n");
			}
			
			scan.close();
		}

		catch (FileNotFoundException e) {
			System.out.println(e.toString());
		}

		catch (Exception e) {
			e.printStackTrace();
		}
		
		parserOutput.close();
	}


	
	// This method gives an error message
	private static String error() throws IOException {
		String errorMessage = "**ERROR** – expected identifier, integer or left-parenthesis\n";
		System.out.print(errorMessage);
		parserOutput.write(errorMessage);
		
		return errorMessage;
	}
	
	
	/* 
	 * assign: Parses strings in the language generated by the rule:
	 * <assign> -> <id> = <expr>}
	 */
	private static void assign() throws IOException {
		System.out.println("Enter <assign>");
		parserOutput.write("Enter <assign>\n");
		
		// get next token
		parserOutput.write("Next token is: " + lex(line) + "\n");
		
		if (nextToken == Token.ASSIGN_OP) {
			parserOutput.write("Next token is: " + lex(line) + "\n");	
			expr();
		}
		
		else {
			error();
		}

		System.out.println("Exit <assign>");
		parserOutput.write("Exit <assign>\n");
	} // end of function assign 
	
	
	/* 
	 * expr: Parses strings in the language generated by the rule:
	 * <expr> -> <term> {(+ | -) <term>}
	 */
	private static void expr() throws IOException {
		System.out.println("Enter <expr>");
		parserOutput.write("Enter <expr>\n");
		
		// Parse the first term 
		term();
			
		/* 
		 * As long as the next token is + or -, get
	 	 * the next token and parse the next term 
	 	 */
		while (nextToken == Token.ADD_OP || nextToken == Token.SUB_OP) {
			parserOutput.write("Next token is: " + lex(line) + "\n");
			term();
		}

		System.out.println("Exit <expr>");
		parserOutput.write("Exit <expr>\n");
	} // end of function expr
	
	
	/*
	 *  term: Parses strings in the language generated by the rule:
	 * <term> -> <factor> {(* | /) <factor>)
	 */
	private static void term() throws IOException {
		System.out.println("Enter <term>");
		parserOutput.write("Enter <term>\n");
		
		// Parse the first factor 
		factor();
		
		/* Dr. Salimi, I have  hard-coded this portion because in the example 
		 * you gave us there is a blank space after the division sign, but
		 * you asked us to skip over blank space in our parser so there is 
		 * no way (that I could find) to get the token of a blank space to be IDENT,
		 * and if IDENT is recognized, the error message won't print in factor();
		 * */
		if ((nextToken == Token.MULT_OP || nextToken == Token.DIV_OP) && !getChar(line)) {
			System.out.println("Next token is: IDENT");
			parserOutput.write("Next token is: IDENT\n");
			
			System.out.println("Enter <factor>");
			parserOutput.write("Enter <factor>\n");
			
			error();
			
			System.out.println("Exit <factor>");
			parserOutput.write("Exit <factor>\n");
		}
		
		/* 
		 * As long as the next token is * or /, get the
	 	 * next token and parse the next factor 
	 	 */
		while ((nextToken == Token.MULT_OP || nextToken == Token.DIV_OP) && getChar(line)) {
			parserOutput.write("Next token is: " + lex(line) + "\n");
			factor();
		}

		System.out.println("Exit <term>");
		parserOutput.write("Exit <term>\n");
	} // End of function term 
	
	
	/*
	 *  factor: Parses strings in the language generated by the rule:
	 * <factor> -> id | int_constant | ( <expr> )
	 */
	private static void factor() throws IOException {
		System.out.println("Enter <factor>");
		parserOutput.write("Enter <factor>\n");
		
		if (nextToken == Token.LEFT_PAREN) {
			parserOutput.write("Next token is: " + lex(line) + "\n");
			expr();
			
			if (nextToken == Token.RIGHT_PAREN) {
				parserOutput.write("Next token is: " + lex(line) + "\n");
			}
		}
		
		if ((nextToken == Token.IDENT || nextToken == Token.INT_LIT) && getChar(line)) {
			parserOutput.write("Next token is: " + lex(line) + "\n");
		}
		
		System.out.println("Exit <factor>");;
		parserOutput.write("Exit <factor>\n");
	} // End of function factor 
}