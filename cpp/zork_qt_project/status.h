#ifndef STATUS_H
#define STATUS_H

#include "ZorkUL.h"
#include <QObject>

using namespace std;

// status inherits from QObject
class status : public QObject {
    Q_OBJECT

public:
    status();
    void getHealthStatus(int);
    void getItemsStatus(Character);
    void getTimeStatus(int);

signals:
    win();
    lose();
    time();
};
#endif // STATUS_H
