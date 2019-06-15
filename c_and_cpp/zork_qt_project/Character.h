#ifndef CHARACTER_H_
#define CHARACTER_H_

#include <string>
using namespace std;
#include <vector>
using std::vector;
#include "item.h"


class Character {
private:
	string description;
    vector < Item > itemsInCharacter;
    int maxHealth;
    int currentHealth;
public:
    void addItem(Item*);
    void addItem(Item&);
    void addItem(string);
    bool hasItem(string);

    int getMaxHealth();
    void setCurrentHealth(int);
    int getCurrentHealth();

public:
    Character(string description, int maxHealth);
	string shortDescription();
	string longDescription();

};

#endif //CHARACTER_H_
