#!/bin/bash
# buildTestSuite.sh - Unix/Linux version

# Load environment variables from .env file
if [ -f .env ]; then
    export $(cat .env | grep -v '^#' | xargs)
fi

# Check if email parameter was provided
if [ $# -ne 1 ]; then
    echo "Usage: $0 email@address.com"
    exit 1
fi

EMAIL=$1
BUILD_STATUS=false
TEST_STATUS=false
LOG_FILE="build_test.log"
TEST_RESULTS="test_results.txt"

# Create necessary directories
mkdir -p build
mkdir -p lib
mkdir -p test-results

# Download JUnit and other dependencies if they don't exist
if [ ! -f "lib/junit-platform-console-standalone.jar" ]; then
    echo "Downloading JUnit dependencies..."
    curl -L https://repo1.maven.org/maven2/org/junit/platform/junit-platform-console-standalone/1.8.2/junit-platform-console-standalone-1.8.2.jar -o lib/junit-platform-console-standalone.jar
fi

if [ ! -f "lib/mockito-core.jar" ]; then
    echo "Downloading Mockito dependencies..."
    curl -L https://repo1.maven.org/maven2/org/mockito/mockito-core/3.12.4/mockito-core-3.12.4.jar -o lib/mockito-core.jar
    curl -L https://repo1.maven.org/maven2/net/bytebuddy/byte-buddy/1.11.13/byte-buddy-1.11.13.jar -o lib/byte-buddy.jar
    curl -L https://repo1.maven.org/maven2/net/bytebuddy/byte-buddy-agent/1.11.13/byte-buddy-agent-1.11.13.jar -o lib/byte-buddy-agent.jar
    curl -L https://repo1.maven.org/maven2/org/objenesis/objenesis/3.2/objenesis-3.2.jar -o lib/objenesis.jar
fi

CLASSPATH="build/main:build/test:lib/*"


# Compile the main classes
echo "Building main classes..."
if javac -d build/main Src/*.java; then
    # Compile the test classes with all dependencies in classpath
    echo "Building test classes..."
    if javac -cp "$CLASSPATH" -d build/test Tests/*.java; then
        BUILD_STATUS=true
        echo "Build successful"
    fi
fi

# Run tests if build was successful
if [ "$BUILD_STATUS" = true ]; then
    echo "Running tests..."
    java -cp "$CLASSPATH" org.junit.platform.console.ConsoleLauncher \
        --scan-class-path \
        --class-path build/main:build/test \
        --details=tree \
        --reports-dir=test-results > "$TEST_RESULTS" 2>&1

    if [ $? -eq 0 ]; then
        TEST_STATUS=true
        echo "Tests passed"
    else
        echo "Some tests failed"
    fi
fi

# Create Python email script
cat > send_email.py << EOF
import smtplib
from email.mime.text import MIMEText
from email.mime.multipart import MIMEMultipart
import sys
import os

def read_test_results():
    try:
        with open('test_results.txt', 'r') as file:
            return file.read()
    except Exception as e:
        return f"Error reading test results: {str(e)}"

def send_email(recipient, build_status, test_status):
    sender_email = "rahulchowdary.namala@gmail.com"
    password = os.environ.get('EMAIL_PASSWORD')

    message = MIMEMultipart()
    message["From"] = sender_email
    message["To"] = recipient
    message["Subject"] = "Build and Test Status Report"

    # Get test results
    test_results = read_test_results()

    # Create email body with formatting
    body = f"""
Build and Test Status Report
===========================

Build Status: {build_status}
Test Status: {test_status}

Detailed Test Results:
=====================
{test_results}

---
This is an automated message from the build system.
"""
    message.attach(MIMEText(body, "plain"))

    try:
        server = smtplib.SMTP_SSL("smtp.gmail.com", 465)
        server.login(sender_email, password)
        server.sendmail(sender_email, recipient, message.as_string())
        server.quit()
        print("Email sent successfully!")
        return True
    except Exception as e:
        print(f"Failed to send email: {str(e)}")
        return False

if __name__ == "__main__":
    if len(sys.argv) != 4:
        print("Usage: python send_email.py recipient build_status test_status")
        sys.exit(1)

    success = send_email(sys.argv[1], sys.argv[2], sys.argv[3])
    sys.exit(0 if success else 1)
EOF

# Send email with status using Python script
if command -v python3 > /dev/null; then
    echo "Sending email notification..."
    python3 send_email.py "$EMAIL" "$BUILD_STATUS" "$TEST_STATUS"
else
    echo "Python3 not found. Cannot send email notification."
fi

# Print status to console
echo "Build Status: $BUILD_STATUS"
echo "Test Status: $TEST_STATUS"

# Cleanup temporary files
rm -f send_email.py

exit 0
