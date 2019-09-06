class Chips:
    def __init__(self, amount = 100):
        self.chips_pool = amount
        self.current_bet = 0

    def make_bet(self,bet_amount):
        if bet_amount <= self.chips_pool:
            self.current_bet = bet_amount
            return True
        else:
            return False

    def win_bet(self):
        self.chips_pool += self.current_bet*2

    def lose_bet(self):
        self.chips_pool -= self.current_bet
