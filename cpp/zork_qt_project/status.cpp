#include "status.h"

status::status() {

}
void status::getHealthStatus(int health) {
    if(health <= 0)
        emit this->lose();
}

void status::getItemsStatus(Character c) {
    if(c.hasItem("Sam Maguire"))
        emit this->win();
}

void status::getTimeStatus(int time) {
    if(time <= 0)
        emit this->time();
}
