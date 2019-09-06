from card import Card

# Equivalent Blackjack values for cards
values = {"Two":2, "Three":3, "Four":4, "Five":5, "Six":6, "Seven":7, "Eight":8, "Nine":9, "Ten":10, "Jack":10, "Queen":10, "King":10, "Ace":11}

class Hand():
    '''
    Description: The Hand class keeps track of the cards in a player's hand and the value of those cards.
    Attributes: cards = list of cards in the Hand, total_card_value = value of the cards, aces = number of aces
    Methods: __init__(self), add_card(self,card), adjust_for_ace(self), __str__(self)
    '''
    def __init__(self):
        self.cards = []
        self.total_card_value = 0
        self.aces = 0

    def add_card(self,card):
        '''
        Description: Adds a Card to the self.cards list, updates self.total_card_value and if there is an ace, it will increment self.ace
        Input: self, card
        Return: null
        '''
        self.cards.append(card)
        self.total_card_value += values[card.rank]
        if card.rank == "Ace":
            self.aces += 1

    def adjust_for_ace(self):
        '''
        Description: Based on self.total_card_value, this method will calculate whether to choose 1 or 11 for the value of the aces in a hand
        Input: self
        Return: null
        '''
        while self.total_card_value > 21 and self.aces > 0:
            self.total_card_value -= 10
            self.aces -= 1

    def __str__(self):
        hand_str = ""
        for card in self.cards:
            hand_str += "\n "+card.__str__()
        return "hand is:\n"+ hand_str
