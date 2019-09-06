import re
import string

def run():
    '''
        This function is called when the programme starts.
        It handles user input and calls other functions accordingly.
    '''
    print("Welcome to the Pangram/Palindrome Checker!")
    running = True

    while running:
        print("1. Check Pangram\n2. Check Palindrome\n3. Quit\n")
        userInput = input("Enter number selection: ").lower()

        if(userInput == "3"):
            running = False
        elif(userInput == "1"):
            pangramCheck = input("\nEnter a word or sentence: ")
            if isPangram(pangramCheck):
                print("\n\"{}\" is a pangram.\n".format(pangramCheck))
            else:
                print("\n\"{}\" is not a pangram.\n".format(pangramCheck))
        elif(userInput == "2"):
            palindromeCheck = input("\nEnter a word or sentence: ")
            if isPalindrome(palindromeCheck):
                print("\n\"{}\" is a palindrome.\n".format(palindromeCheck))
            else:
                print("\n\"{}\" is not a palindrome.\n".format(palindromeCheck))
        else:
            print("\nError: Invalid input detected. Please try again.\n")

def isPalindrome(s):
    '''
        Checks whether a string is a palindrome.
        INPUT: string s
        OUTPUT: boolean
    '''
    s = s.lower()
    s = re.sub("\W|_","",s)

    for i, char in enumerate(s):
        if s[i-1] != s[-i]:
            return False
    return True

def isPangram(s):
    '''
        Checks whether a string is a pangram.
        INPUT: string s
        OUTPUT: boolean
    '''
    s = s.lower()
    s = re.sub("\W|_","",s)
    unique = set(s)

    if len(unique) == 26:
        return True
    else:
        return False

run()
