# Java-Src.Battleship

The game, Src.Battleship, written in Java.

## Requirements / Dependencies

* Eclipse IDE for Java Developers (https://www.eclipse.org/downloads/packages/)
* JUnit 5 (built into Eclipse IDE)
* JDK (https://www.oracle.com/technetwork/java/javase/downloads/index.html)

## Importing Project

1. Make sure you've installed all requirements
2. Clone this repository:
  `git clone https://github.com/mosers1/Java-Battleship.git`
3. Open the Eclipse IDE
4. Import the project
    * File --> Open Projects from File System... --> Select directory --> Click OK

## Running Project & Unit Tests

**Running application**\
With the Eclipse IDE opened and the Src.Battleship project imported, right-click on Src.Battleship.java,
select Run As, then select Java Application. The program will launch in the IDE's Console window.

**Running unit tests (Tests.LocationTest.java example)**\
With the Eclipse IDE opened and the Src.Battleship project imported, right-click on Tests.LocationTest.java,
select Run As, then select JUnit Test. Test results will be displayed in the IDE's JUnit window.

For this example, Tests.LocationTest.java tests the paired accessor and mutator functions in Src.Location.java. Half of the mutators take no parameters or only a Boolean value resulting in simple test cases. The other half take integer parameters. Equivalence class partitioning was performed for these functions and their corresponding accessor functions.

**Code coverage analysis**\
With the Eclipse IDE opened and the Src.Battleship project imported, right-click on the top-level
Src.Battleship project directory, select Coverage As, then select JUnit Test. Coverage results
will be displayed in the IDE's Coverage window.

## Usage

Here's a short explanation on how to use **Java-Src.Battleship**:

* Use `java Src.Battleship` on the command-line to run the code
* When the application starts, you, the player, sets up your board by typing in the on-screen coordinates.
* Then, the computer makes its own board and you play against it by guessing its ships' locations.
	* NOTE: The computer's board is printed on screen, just so the game can go faster.
* The program ends when one side beats the other.

## CI Server Setup

See the following files for more information:
1. [Jenkins Installation Resources](https://github.com/mosers1/Java-Battleship/blob/master/docs/InstallingJenkins.txt)
2. [Java JDK Installation Instructions (necessary to run our scripts)](https://github.com/mosers1/Java-Battleship/blob/master/scripts/README.txt)

## Contributing

1. Fork it
2. Create your feature branch: `git checkout -b feature/my-new-feature`
3. Commit your changes: `git commit -m 'Add some feature'`
4. Push to the branch: `git push origin feature/my-new-feature`
5. Submit a pull request

## Original Author

This project was forked from ymarcus93's repository (https://github.com/ymarcus93/Java-Battleship)
to aid in a class project.

## Version

1.0.0

## License

The MIT License (MIT)

Copyright (c) 2015 Yuval Marcus

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all
copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
SOFTWARE.
