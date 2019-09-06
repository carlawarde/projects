class Card():
    '''
    Description: The Card class is the virtual equivalent of a card from a deck of cards. It has a suit and rank.
    Attributes: suit, rank
    Methods: __init__(self,suit,rank), __str__(self)
    '''
    def __init__(self,suit,rank):
        self.suit = suit
        self.rank = rank

    def __str__(self):
        return "{} of {}".format(self.rank, self.suit)
