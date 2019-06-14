#ifndef ZORKUL_H_
#define ZORKUL_H_

#include "Command.h"
#include "Parser.h"
#include "Room.h"
#include "item.h"
#include "Character.h"
#include <iostream>
#include <string>
#include <vector>
#include <stdlib.h>
#include <time.h>
#include "QObject"

using namespace std;

vector<Room*>operator+=(vector<Room*>& rooms, Room* item);

class ZorkUL {

private:
    Parser parser;
    Room *currentRoom;
    Character *mainCharacter;

    void createRooms();
    bool processCommand(Command command);
    void printHelp();
    void goRoom(Command command);
    void createItems();
    void displayItems();
    void createCharacters();


public:
    ZorkUL();
    void play();
    string go(string direction);
    string goTeleport();
    Character getCharacter();
    void generateItem(Item);
    string takeItem();
    string printWelcome();
    friend class mainwindow;
};

#endif /*ZORKUL_H_*/
