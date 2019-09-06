import random
from card import Card

class Deck():
    '''
    Description: The Deck class represents a Deck of 52 cards. The __init__ method automaticalls populates the deck with cards. You can shuffle the deck and deal cards.
    Attributes: deck = list of cards
    Methods: __init__(self), __str__(self), shuffle_deck(self), deal_card(self)
    '''
    suits = ["Hearts","Diamonds","Spades","Clubs"]
    ranks = ["Two","Three","Four","Five","Six","Seven","Eight","Nine","Ten","Jack","Queen","King","Ace"]

    def __init__(self):
        # Populate deck with cards
        self.deck = []
        for suit in Deck.suits:
            for rank in Deck.ranks:
                self.deck.append(Card(suit,rank))

    def __str__(self):
        deck_str = ""
        for card in self.deck:
            deck_str += card.__str__()+"\n"

        return "The deck has: "+deck_str

    def shuffle_deck(self):
        # Shuffles the deck
        random.shuffle(self.deck)

    def deal_card(self):
        '''
        Description: Pops a card off the top of self.deck and returns it
        Input: self
        Return: Card
        '''
        card = self.deck.pop()
        return card
