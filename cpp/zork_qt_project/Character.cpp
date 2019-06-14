#include "Character.h"

Character::Character(string description, int health) {
	this->description = description;
    this->maxHealth = health;
    this->currentHealth = health;
}
void Character::addItem(Item &item) {
    itemsInCharacter.push_back(item);
}
void Character::addItem(Item *item) {
    itemsInCharacter.push_back(*item);
    delete item;
}
string Character::longDescription()
{
  string ret = this->description;
  ret += "\n Item list:\n";
  for (vector<Item>::iterator i = itemsInCharacter.begin(); i != itemsInCharacter.end(); i++)
    ret += "\t"+ (*i).getLongDescription() + "\n";
  return ret;
}

void Character::setCurrentHealth(int h) {
    if(h <= getMaxHealth())
        this->currentHealth = h;
    else
        this->currentHealth = getMaxHealth();
}

int Character::getMaxHealth() {
    return this->maxHealth;
}

int Character::getCurrentHealth() {
    return this->currentHealth;
}

bool Character::hasItem(string item) {
    for (vector<Item>::iterator i = itemsInCharacter.begin(); i != itemsInCharacter.end(); i++) {
        if((*i).getShortDescription().compare(item) == 0)
            return true;
    }
    return false;
}
