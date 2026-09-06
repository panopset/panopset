home ~ [stack](docs/stack.md) ~ [workstations](docs/workstations/README.md) ~ [setup](docs/setup/README.md)

# Panopset Source

## Installation

Install Panopset desktop software, by running the [installer](https://panopset.com/downloads.html) for your platform.

## Developers

You may alternatively build the installer, by using the installer created in the target directory, 
after running the build scripts described below.

You may also run Panopset applications directly from an IDE, by calling their launcher classes directly. 
Dash, for example, is:

    com.panopset.desk.utilities.Dash
	
Include this VM argument to avoid warnings about JavaFX using native OS calls:

    --enable-native-access=javafx.graphics

Good place to start is with setting up your [workstation](docs/workstations/README.md).

Once everything is set up, then you can update and build:

## projects folder

| Name    | Description                         |
|---------|-------------------------------------|
| beam    | API.                                |
| shoring | Desktop applications.               |
| slab    | Raw web artifacts for panopset.com. |


## Scripts

| Linux/Mac   | Windows    | Purpose                                     |
|-------------|------------|---------------------------------------------|
| ./setup.sh  | setup.cmd  | One time workstation setup.                 |
| ./allgit.sh | allgit.cmd | Update the git repo and run the all script. |
| ./all.sh    | all.cmd    | Run the clean, build and jpk scripts.       |
| ./clean.sh  | clean.cmd  | Clean target directories.                   |
| ./update.sh | update.cmd | Git update.                                 |
| ./build.sh  | build.cmd  | Build desktop and beam.                     |
| ./jpk.sh    | jpk.cmd    | Create an installer for this platform       |

## Requirements:

* [Java 25](https://adoptium.net/temurin/releases).
* [Maven](https://maven.apache.org/).

## Links

* [Intellij](https://www.jetbrains.com/idea/) IDE.
* [bruno](https://www.usebruno.com/downloads) API Client.
* [brew](https://brew.sh) Homebrew package manager for Linux and Macintosh.
* [start.spring.io](https://start.spring.io) Springboot.

## Panopset Blackjack

If you are looking for the Panopset Blackjack application, 
it has been moved to [fas21.com](https://fas21.com).
