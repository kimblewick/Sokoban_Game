# Beaver's Day Out

## Overview
Beaver's Day Out is a Sokoban (box pushing puzzle) type game, with endless randomly generated levels. The player controls the character Justin Beaver to push logs into whirlpools to complete the level.

## Key Features
- **Infinite Gameplay** - Endless procedurally generated levels for unlimited puzzle-solving
- **Smart Level Generation** - Dynamic programming algorithm ensures all levels are solvable
- **Pure Java Implementation** - Built entirely with Java Swing without external game engines
- **Efficient Memory Usage** - Custom data structure using integer masks for optimized performance

## Technologies Used
Java, Java Swing, PixilArt

## Results
- Successfully generates infinite unique, solvable puzzle levels
- Implemented BFS algorithm preventing impossible level generation (0% unsolvable levels)
- Lightweight implementation using only built-in Java libraries
- Smooth gameplay with responsive controls and intuitive UI

## Demo
![video1506679091](https://github.com/user-attachments/assets/66e6f455-dafe-45a5-8784-0ef235d62dcf)


## How to Run
### Prerequisites
- Java 8 or higher installed on your system

### Running the Game
1. Clone this repository
2. Navigate to the project directory
3. Compile: `javac *.java`
4. Run: `java GameModel`

**Or** open in your preferred Java IDE (IntelliJ, Eclipse) and run `GameModel.java`
