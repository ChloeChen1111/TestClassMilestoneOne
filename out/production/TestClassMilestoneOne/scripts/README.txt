The shell scripts in this directory were tested against SU's CS1 server.

While they can be run standalone for development purposes, they can be called
by a build automation tool, like Jenkins, and serve as source-controller build
scripts.

An AWS EC2 t2.micro server running AWS Linux and Jenkins used these scripts 
during this class project to achieve a source-controlled CI/CD pipeline.

********************************************************************************
JAVA INSTALLATION INSTRUCTIONS
********************************************************************************
You will need to install the JDK per the instructions below for the scripts to
run since they have relative path dependencies for the JRE and Java compiler.

Environment Setup:
1. Clone GitHub repo
git clone https://github.com/mosers1/Java-Src.Battleship.git

2. Download JDK 8u211 for Linux x64 from here:
https://www.oracle.com/technetwork/java/javase/downloads/jdk8-downloads-2133151.html
* NOTE: You'll need to accept the license agreement and may need to make an Oracle account.

3. Transfer file to CS1 (or other server) and place them at same location as
the top-level Java-Src.Battleship directory as shown below:

[user@cs1 buildSystem]$ ls -al
total 190428
drwxrwx---. 3 user user        73 May 30 23:59 .
drwxrwx---. 4 user user        97 May 30 23:58 ..
drwxrwx---. 7 user user      4096 May 30 23:59 Java-Src.Battleship
-rw-r-----. 1 user user 194990602 May 30 23:59 jdk-8u211-linux-x64.tar.gz

4. Extract the tarball
tar -xvzf jdk-8u211-linux-x64.tar.gz

[user@cs1 buildSystem]$ ls -al
total 190432
drwxrwx---. 4 user user        96 May 31 00:01 .
drwxrwx---. 4 user user        97 May 30 23:58 ..
drwxrwx---. 7 user user      4096 May 30 23:59 Java-Src.Battleship
drwxr-x---. 7 user user      4096 Apr  1 20:51 jdk1.8.0_211
-rw-r-----. 1 user user 194990602 May 30 23:59 jdk-8u211-linux-x64.tar.gz

5. To build the test suite and run it:
   a. cd Java-Src.Battleship/scripts
   b. Change permissions to make scripts executable
      chmod 777 *
   c. Run the build script
      ./buildTestSuite.sh
   d. Run the test suite
      ./runTestSuite.sh <numIter> [e-mailAddr]
   e. Perform stress test
      ./stressTestApp.sh  ***(TBD)***

6. Create symlinks to java and javac [optional]
One can use the "ln -s" command to create symbolic links for "java" and "javac"
found in $PATH to point to your new installation.

7. Need to run "org.mockito:mockito-core:5.14.2" (use to import mockito since i used mocking)


Section 2: TEST CLASSES EXPLAINATION

1. Tests.PlayerTest - Unit Testing for Src.Player Class

This class contains a suite of unit tests to verify the functionality of the Src.Player class in a Src.Battleship-style game. These tests use JUnit 5 and Mockito for mocking dependencies and verifying behavior.

Setup and Teardown

	•	@BeforeAll: Initializes the mock objects and sets up the Src.Player instance with mocked Src.Ship and Src.Grid objects. This method is run once before all tests.
	•	@AfterAll: Ensures cleanup after all tests have run by nullifying objects and simulating a reset.

Tests Overview

	1.	testAddShips():
	•	Verifies that the player’s ships are correctly added to the grid.
	•	Simulates adding ships using the addShips() method and checks if they are added to the player’s grid.
	2.	testNumOfShipsLeft_AllShipsSet():
	•	Verifies that when all ships have their location and direction set, there are no ships left to place.
	•	Asserts that the number of ships left is 0.
	3.	testNoShipsSet():
	•	Simulates a scenario where no ships have their location or direction set.
	•	Asserts that all 5 ships are left to be placed.
	4.	testNumOfShipsLeft_SomeShipsSet():
	•	Tests a mixed scenario where some ships have their location and direction set, and others do not.
	•	Verifies the numOfShipsLeft() method, asserting the correct number of ships that are yet to be placed.
	5.	testNumOfShipsLeft_ThrowsErrorInSetup():
	•	Intentionally sets the Src.Player object to null and verifies that an exception is thrown when attempting to call numOfShipsLeft().
	•	Useful for checking how the setup handles errors like uninitialized objects.

Helper Methods

	•	setAllShipsLocationAndDirection():
	•	This helper method stubs the behavior of isLocationSet() and isDirectionSet() for all ships, ensuring that they return true. This simulates a scenario where all ships are properly placed.
	•	testNumOfShipsLeft_NoShipsSet():
	•	Another helper method that simulates the case where none of the ships have their location or direction set, ensuring that they return false.

Future Tests

	•	Simulate setting the location and direction of the ships.
	•	Ensure that ships are added to the player’s grid correctly after location and direction are set.

These are currently commented out but can be implemented once chooseShipLocation() is ready for further validation.

Technologies Used

	•	JUnit 5: For unit testing.
	•	Mockito: For mocking external dependencies such as Src.Ship and Src.Grid objects.
