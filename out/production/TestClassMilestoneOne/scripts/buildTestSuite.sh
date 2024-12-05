#!/bin/bash
#===============================================================================
# Team D'Buggers (Team 7)
# Scott Moser, Nicholas Benyo, Melissa Jones
# Professor McKee
# CPSC 5200-01
# 6 June 2019
#                              Milestone #2
#
# File: buildTestSuite.sh
#
# Description:
# This shell script builds the Src.Battleship project and associated J-Unit unit
# tests.
#
#   DEPENDENCIES, LIMITATIONS, & DESIGN NOTES:
#       Dependencies : 
#           1. JDK 1.8.0_211 must be installed at $JDK_PATH.
#       Design Notes :
#           1. All source files are built, then all J-Unit test files are built.
#           2. Artifacts are placed in the $OUT_DIR directory.
#       Limitations :
#           1. Due to memory limitations on SU's CS1 server, the $CS1_HACK
#              variable is used to limit the memory used by the JVM.
#
#   Example Usage:
#   "./buildTestSuite.sh"
#===============================================================================

#set -o errexit
#set -o pipefail
set -o nounset
#set -o xtrace

#===============================================================================
# Constants
#===============================================================================
OUT_DIR="out"
PROJECT_ROOT=".."
JDK_PATH="$PROJECT_ROOT/../jdk1.8.0_211/bin"
JUNIT_JAR="junit/junit-platform-console-standalone-1.5.0-M1.jar"

# Hack required due to memory limitations on CS1
CS1_HACK="-J-Xmx512m"  # Limit heap to 512 MB

# NOTE: This list needs to be updated if any additional source files are added
#       to the project!
sourceList=(
    Battleship
    Grid
    Location
    Randomizer
    Ship
)

# NOTE: This list needs to be updated if any additional test files are added
#       to the project!
testList=(
    LocationTest
    ShipTest
    GridTest
)

#===============================================================================
# Script
#===============================================================================
echo "Building test suite..."

echo "Creating directory for build artifacts..."
mkdir -p $PROJECT_ROOT/$OUT_DIR

echo "Removing stale artifacts..."
rm -f -v $PROJECT_ROOT/$OUT_DIR/*.class

echo "Printing javac version..."
$JDK_PATH/javac $CS1_HACK -version

echo "Building source code..."
for i in ${sourceList[@]}; do
    echo "Building: $i.java --> $i.class"
    $JDK_PATH/javac $CS1_HACK -d $PROJECT_ROOT/$OUT_DIR -cp $PROJECT_ROOT $PROJECT_ROOT/$i.java
    if [ $? != 0 ]; then
        echo "ERROR: Unable to build $i.java! Aborting build..."
        exit 1
    fi
done

echo "Building unit tests..."
for i in ${testList[@]}; do
    echo "Building: $i.java --> $i.class"
    $JDK_PATH/javac $CS1_HACK -d $PROJECT_ROOT/$OUT_DIR -cp $PROJECT_ROOT/$OUT_DIR:$PROJECT_ROOT/$JUNIT_JAR $PROJECT_ROOT/$i.java
    if [ $? != 0 ]; then
        echo "ERROR: Unable to build $i.java! Aborting build..."
        exit 2
    fi
done

echo "Build success!"
exit 0
