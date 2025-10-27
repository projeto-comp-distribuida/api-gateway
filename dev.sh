#!/bin/bash

# DistriSchool API Gateway Development Script
# This script helps with common development tasks for the API Gateway

set -e

# Colors for output
RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
BLUE='\033[0;34m'
NC='\033[0m' # No Color

# Function to print colored output
print_status() {
    echo -e "${BLUE}[INFO]${NC} $1"
}

print_success() {
    echo -e "${GREEN}[SUCCESS]${NC} $1"
}

print_warning() {
    echo -e "${YELLOW}[WARNING]${NC} $1"
}

print_error() {
    echo -e "${RED}[ERROR]${NC} $1"
}

# Function to check if Java is installed
check_java() {
    if ! command -v java &> /dev/null; then
        print_error "Java is not installed. Please install Java 17 or higher."
        exit 1
    fi
    
    java_version=$(java -version 2>&1 | head -n 1 | cut -d'"' -f2 | cut -d'.' -f1)
    if [ "$java_version" -lt 17 ]; then
        print_error "Java version $java_version is not supported. Please install Java 17 or higher."
        exit 1
    fi
    
    print_success "Java version check passed"
}

# Function to check if Maven is installed
check_maven() {
    if ! command -v mvn &> /dev/null; then
        print_error "Maven is not installed. Please install Maven 3.6 or higher."
        exit 1
    fi
    
    print_success "Maven is installed"
}

# Function to build the application
build_app() {
    print_status "Building the API Gateway application..."
    mvn clean install -DskipTests
    print_success "Application built successfully"
}

# Function to run tests
run_tests() {
    print_status "Running tests..."
    mvn test
    print_success "Tests completed"
}

# Function to run the application
run_app() {
    print_status "Starting the API Gateway application..."
    mvn spring-boot:run
}

# Function to build Docker image
build_docker() {
    print_status "Building Docker image..."
    docker build -t distrischool-api-gateway .
    print_success "Docker image built successfully"
}

# Function to run Docker container
run_docker() {
    print_status "Starting Docker container..."
    docker run -p 8080:8080 distrischool-api-gateway
}

# Function to show help
show_help() {
    echo "DistriSchool API Gateway Development Script"
    echo ""
    echo "Usage: $0 [COMMAND]"
    echo ""
    echo "Commands:"
    echo "  build       Build the application"
    echo "  test        Run tests"
    echo "  run         Run the application locally"
    echo "  docker      Build and run Docker container"
    echo "  docker-build Build Docker image only"
    echo "  docker-run  Run Docker container only"
    echo "  check       Check prerequisites (Java, Maven)"
    echo "  help        Show this help message"
    echo ""
    echo "Examples:"
    echo "  $0 build    # Build the application"
    echo "  $0 run      # Run the application"
    echo "  $0 docker   # Build and run with Docker"
}

# Main script logic
case "${1:-help}" in
    "build")
        check_java
        check_maven
        build_app
        ;;
    "test")
        check_java
        check_maven
        run_tests
        ;;
    "run")
        check_java
        check_maven
        run_app
        ;;
    "docker")
        check_java
        check_maven
        build_docker
        run_docker
        ;;
    "docker-build")
        build_docker
        ;;
    "docker-run")
        run_docker
        ;;
    "check")
        check_java
        check_maven
        ;;
    "help"|*)
        show_help
        ;;
esac







