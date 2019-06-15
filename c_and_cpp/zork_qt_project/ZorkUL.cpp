#include <iostream>

using namespace std;
#include "ZorkUL.h"

vector<Room*> rooms;
vector<Item *> items;

/*int main(int argc, char argv[]) {
	ZorkUL temp;
    QApplication a(argc, argv);
    temp.show();
    srand(time(NULL));
	temp.play();
	return 0;
}*/

ZorkUL::ZorkUL() {
    createRooms();
    createCharacters();
    generateItem(Item("Sam Maguire"));
}

vector<Room*>operator+=(vector<Room*>& rooms,Room* item) {
    rooms.push_back(item);
    return rooms;
}
void ZorkUL::createRooms()  {
    Room *a, *b, *c, *d, *e, *f, *g, *h, *i, *j;

    a = new Room("Mayo");
    b = new Room("Sligo");
    c = new Room("Galway");
    d = new Room("Limerick");
    e = new Room("Dublin");
    f = new Room("Donegal");
    g = new Room("Cavan");
    h = new Room("Kildare");
    i = new Room("Kerry");
    j = new Room("Cork");

    rooms+=a;
    rooms+=b;
    rooms+=c;
    rooms+=d;
    rooms+=e;
    rooms+=f;
    rooms+=g;
    rooms+=h;
    rooms+=i;
    rooms+=j;

    //             (N, E, S, W)
	a->setExits(f, b, d, c);
	b->setExits(NULL, NULL, NULL, a);
	c->setExits(NULL, a, NULL, NULL);
    d->setExits(a, e, j, i);
	e->setExits(NULL, NULL, NULL, d);
	f->setExits(NULL, g, a, h);
	g->setExits(NULL, NULL, NULL, f);
	h->setExits(NULL, f, NULL, NULL);
    i->setExits(NULL, d, NULL, NULL);
    j->setExits(d,NULL,NULL,NULL);

    currentRoom = a;
}

void ZorkUL::createCharacters() {
    Character *a;
    a = new Character("Rogue",200);

    mainCharacter = a;
}

string ZorkUL::printWelcome() {
  return "Find Sam for Mayo!\n\n"+currentRoom->longDescription()+"\n";
}

/**
 * Given a command, process (that is: execute) the command.
 * If this command ends the ZorkUL game, true is returned, otherwise false is
 * returned.
 */
bool ZorkUL::processCommand(Command command) {
	if (command.isUnknown()) {
		cout << "invalid input"<< endl;
		return false;
	}

	string commandWord = command.getCommandWord();
	if (commandWord.compare("info") == 0)
		printHelp();

	else if (commandWord.compare("map") == 0)
		{
                cout << "[h] --- [f] --- [g]" << endl;
                cout << "         |         " << endl;
                cout << "         |         " << endl;
                cout << "[c] --- [a] --- [b]" << endl;
                cout << "         |         " << endl;
                cout << "         |         " << endl;
                cout << "[i] --- [d] --- [e]" << endl;
                cout << "         |         " << endl;
                cout << "         |         " << endl;
                cout << "        [j]        " << endl;
		}

	else if (commandWord.compare("go") == 0)
		goRoom(command);

    else if (commandWord.compare("take") == 0)
    {
       	if (!command.hasSecondWord()) {
		cout << "incomplete input"<< endl;
        }
        else
         if (command.hasSecondWord()) {
            cout << "you're trying to take " + command.getSecondWord() << endl;
            int location = currentRoom->isItemInRoom(command.getSecondWord());
            if (location  < 0 )
                cout << "item is not in room" << endl;
            else {
                cout << "item is in room" << endl;
                cout << "index number " << + location << endl;
                cout << endl;
                cout << currentRoom->longDescription() << endl;
            }
        }
    }

    else if (commandWord.compare("put") == 0)
    {

    }
    /*
    {
    if (!command.hasSecondWord()) {
		cout << "incomplete input"<< endl;
        }
        else
            if (command.hasSecondWord()) {
            cout << "you're adding " + command.getSecondWord() << endl;
            itemsInRoom.push_Back;
        }
    }
*/
    else if (commandWord.compare("quit") == 0) {
		if (command.hasSecondWord())
			cout << "overdefined input"<< endl;
		else
			return true; /**signal to quit*/
	}

    else if (commandWord.compare("teleport") == 0)
    {
        goTeleport();
    }
	return false;
}
/** COMMANDS **/
void ZorkUL::printHelp() {
	cout << "valid inputs are; " << endl;
	parser.showCommands();

}

void ZorkUL::goRoom(Command command) {
	if (!command.hasSecondWord()) {
		cout << "incomplete input"<< endl;
		return;
	}

	string direction = command.getSecondWord();

	// Try to leave current room.
	Room* nextRoom = currentRoom->nextRoom(direction);

	if (nextRoom == NULL)
		cout << "underdefined input"<< endl;
	else {
		currentRoom = nextRoom;
		cout << currentRoom->longDescription() << endl;
	}
}

string ZorkUL::go(string direction) {
	//Make the direction lowercase
	//transform(direction.begin(), direction.end(), direction.begin(),:: tolower);
	//Move to the next room
	Room* nextRoom = currentRoom->nextRoom(direction);
	if (nextRoom == NULL)
		return("direction null");
	else
	{
		currentRoom = nextRoom;
		return currentRoom->longDescription();
	}
}

string ZorkUL::goTeleport() {
    bool sameRoom = true;
    int x = 0;

    while(sameRoom) {
        x = (rand() % rooms.size());
        if(!(currentRoom == rooms.at(x)))
            sameRoom = false;
    }
    currentRoom = rooms.at(x);
    return currentRoom->longDescription();
}

Character ZorkUL::getCharacter() {
    return *mainCharacter;
}

void ZorkUL::generateItem(Item h) {
    int randNum = 0;
    bool startRoom = true;
    while(startRoom) {
        randNum = (rand() % rooms.size());
        if(!(currentRoom == rooms.at(randNum)))
            startRoom = false;
    }
    rooms.at(randNum)->addItem(&h);
}

string ZorkUL::takeItem() {
    int numberOfItems = currentRoom->numberOfItems();
    if(numberOfItems > 0) {
        Item a = currentRoom->getItem(numberOfItems-1);
        mainCharacter->addItem(a);
        currentRoom->removeItemFromRoom(numberOfItems-1);
        return a.getShortDescription() + " has been taken.";
    }
    else
        return "There's nothing for you to take here...";
}

template<typename T>
void printToConsole(T item) {
    cout << item << "\n";
}
