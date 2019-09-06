from card_deck import Deck
from card_hand import Hand
from player_chips import Chips

# ENDINGS: They all call win_bet() or lose_bet() depending on whether the player wins or loses.

def player_busts(chips):
    print("Player busts!")
    chips.lose_bet()

def player_wins(chips):
    print("Player wins!")
    chips.win_bet()

def dealer_busts(chips):
    print("Dealer busts!")
    chips.win_bet()

def dealer_wins(chips):
    print("Dealer wins!")
    chips.lose_bet()

# GAME FUNCTIONS

def hit(hand, deck):
    '''
    Description: Calls a card from the deck, and adds it to the player hand. It also adjusts for aces in the hand.
    Input: hand, deck
    Return: null
    '''
    hand.add_card(deck.deal_card())
    hand.adjust_for_ace()

def hit_or_stand(hand, deck):
    '''
    Description: Asks the player if they want to hit or stand.
    Input: hand, deck
    Return: boolean = true if player hits, False if player stands
    '''
    while True:
        userInput = input("Player, do you want to Hit or Stand? Please enter \"hit\" or \"stand\": ")
        if userInput.lower() == "hit":
            hit(hand, deck)
            return True
        elif userInput.lower() == "stand":
            print("Player stands. Dealer is now playing.\n")
            return False
        else:
            print("Error: Invalid input. Please try again.\n")
            continue
        break

def show_cards(player, dealer):
    '''
    Description: Prints the hand of the player and dealer and their respective hand values.
    Input: player, dealer
    Return: null
    '''
    print("Player"+player.__str__())
    print("\nPlayer cards value: " + str(player.total_card_value)+"\n")
    print("Dealer"+dealer.__str__())
    print("\nDealer cards value: " + str(dealer.total_card_value)+"\n")

def show_some_cards(player, dealer):
    '''
    Description: Prints the hand and hand value of the player. Prints the dealers hand but hides the dealer's first card and hand value.
    Input: player, dealer
    Return: null
    '''
    print("Player"+player.__str__())
    print("\nPlayer cards value: " + str(player.total_card_value)+"\n")
    print("Dealer hand is:\n\n <<card hidden>>")
    print(" "+dealer.cards[1].__str__())
    print("\nDealer cards value: ???\n")

def deal_cards(player, dealer, deck):
    '''
    Description: Adds two cards to the player's and dealer's hand
    Input: player, dealer, deck
    Return: null
    '''
    player.add_card(deck.deal_card())
    dealer.add_card(deck.deal_card())
    player.add_card(deck.deal_card())
    dealer.add_card(deck.deal_card())

def set_up_bet(chips):
    '''
    Description: Asks how much the player wants to bet, and checks if the player has the required amount of chips to make the bet
    Input: chips
    Return: null
    '''
    print("Current amount of chips: {}".format(chips.chips_pool))
    while True:
        try:
            bet_amount = int(input("How much would you like to bet? "))
        except:
            print("Please enter numerical input only!")
        else:
            if not chips.make_bet(bet_amount):
                print("Oops! You do not have that many chips. Try again.")
            else:
                break

# GAME LOOP

def run():
    print("Welcome to Blackjack! Get as close to 21 as you can without going bust! Dealer hits until they reach 17 or over. Aces count as 1 or 11 depending on your current hand value.\n")
    player_chips = Chips() # Initialise player's chip pool. It is outside of the main loop so it carries between hands
    while True:
        playing = True # used for the player's turn
        card_deck = Deck()
        card_deck.shuffle_deck() # setting up the deck

        betting = input("Player, would you like to bet on this round? (y/n) ") # asks if the player wants to bet
        if betting == "y":
            set_up_bet(player_chips)

        # intialising the player's and dealer's hands
        player = Hand()
        dealer = Hand()

        deal_cards(player, dealer, card_deck)

        # player's turn; the loop will continue until the player stands or goes bust
        while playing:
            show_some_cards(player,dealer)

            # runs if the player is bust
            if player.total_card_value > 21:
                player_busts(player_chips)
                break

            playing = hit_or_stand(player,card_deck) # asks if the player wants to hit or stand

        if player.total_card_value <= 21:

            # the dealer hits until their hand value is greater than 17
            while dealer.total_card_value < 17:
                hit(dealer,card_deck)

            show_cards(player, dealer) # shows players and dealers cards

            # checks the outcome of the game and calls the respective method (which sorts the bet outcome if there was one)
            if dealer.total_card_value > 21:
                dealer_busts(player_chips)
            elif dealer.total_card_value > player.total_card_value:
                dealer_wins(player_chips)
            elif dealer.total_card_value < player.total_card_value:
                player_wins(player_chips)
            else:
                print("Dealer and Player tie! It's a push.")

        # checks if the player wants to play another hand
        play_again = input("\nWould you like to play another hand? y/n: ")
        if play_again.lower() == "y":
            playing = True
            print("\n"*100)
        else:
            print("Thanks for playing! You have left the table with {} chips.".format(player_chips.chips_pool))
            break
run()
