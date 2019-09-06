import re

tiles = ["#","X","O"]
board = [" "] * 9
availableSpaces = [str(num) for num in range(1,10)]

def displayBoard():
    print("Available Moves\t\tCurrent Board")
    print("|-----|-----|-----|\t|-----|-----|-----|")
    print("|  {}  |  {}  |  {}  |\t|  {}  |  {}  |  {}  |".format(availableSpaces[0],availableSpaces[1],availableSpaces[2],board[0],board[1],board[2]))
    print("|-----|-----|-----|\t|-----|-----|-----|")
    print("|  {}  |  {}  |  {}  |\t|  {}  |  {}  |  {}  |".format(availableSpaces[3],availableSpaces[4],availableSpaces[5],board[3],board[4],board[5]))
    print("|-----|-----|-----|\t|-----|-----|-----|")
    print("|  {}  |  {}  |  {}  |\t|  {}  |  {}  |  {}  |".format(availableSpaces[6],availableSpaces[7],availableSpaces[8],board[6],board[7],board[8]))
    print("|-----|-----|-----|\t|-----|-----|-----|")

def chooseXO():
    print("Player 1, would you like to be X or O?")
    valid = False

    while not valid:
        userInput = input("Enter X or O: ").upper()
        if userInput == "X":
            valid = True
        elif userInput == "O":
            tiles[1] = "O"
            tiles[2] = "X"
            valid = True
        else:
            print("Error: Invalid input detected. Please try again.\n")

def chooseTile(player):
    valid = False
    while not valid:
        userInput = input("Player {}, choose where to place your tile: ".format(player))
        if validateNum(userInput):
            valid = True
        else:
            print("Error: Invalid input detected. Please try again.\n")
    return int(userInput)


def validateNum(num):
    return int(num) in range(1,10)

def checkSpace(num):
    return board[num-1] == " "

def placeTile(num,player):
    if checkSpace(num):
        board[num-1] = tiles[player]
        availableSpaces[num-1] = tiles[player]
        return True
    else:
        print("\nThis space is already taken. Please enter a different number.\n")
        return False

def checkIfWon(player):
    if board[0] == tiles[player] and board[1] == tiles[player] and board[2] == tiles[player]: return True # First Row
    elif board[3] == tiles[player] and board[4] == tiles[player] and board[5] == tiles[player]: return True # Second Row
    elif board[6] == tiles[player] and board[7] == tiles[player] and board[8] == tiles[player]: return True # Third Row
    elif board[0] == tiles[player] and board[3] == tiles[player] and board[6] == tiles[player]: return True # First Column
    elif board[1] == tiles[player] and board[4] == tiles[player] and board[7] == tiles[player]: return True # Second Column
    elif board[2] == tiles[player] and board[5] == tiles[player] and board[8] == tiles[player]: return True # First Column
    elif board[0] == tiles[player] and board[4] == tiles[player] and board[8] == tiles[player]: return True # Left Diagonal
    elif board[2] == tiles[player] and board[4] == tiles[player] and board[6] == tiles[player]: return True # Right Diagonal
    else: return False

def playAgain():
    valid = False
    while not valid:
        userInput = input("Would you like to play again? (y/n) ").lower()
        if userInput == "n":
            return False
        elif userInput == "y":
            print("\n"*100)
            reset()
            return True
        else:
            print("Error: Invalid input detected. Please try again.")

def reset():
    global board
    global availableSpaces
    board = [" "] * 9
    availableSpaces = [str(num) for num in range(1,12)]

def tilesLeft():
    return " " in board

def run():
    print("Welcome to TicTacToe! To play the game you must enter the number that corresponds with the tile on the board.\n")
    running = True

    while running:
        gameOn = True
        turn = 1
        player = "1"
        chooseXO()
        displayBoard()
        while gameOn:
            if turn == 1:
                player = "1"
            else:
                player = "2"

            turnTaken = False
            while not turnTaken:
                tile = chooseTile(player)
                if placeTile(tile,turn):
                    if checkIfWon(turn):
                        print("Player {} has won the game!".format(player))
                        gameOn = False
                        break
                    elif not tilesLeft():
                        print("There's no more spaces left. It's a tie!")
                        gameOn = False
                        break
                    else:
                        turn *= -1
                        turnTaken = True
            displayBoard()
        running = playAgain()

run()
