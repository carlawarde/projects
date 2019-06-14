#ifndef MAINWINDOW_H
#define MAINWINDOW_H

#include <QMainWindow>
#include "Command.h"
#include "Parser.h"
#include "Room.h"
#include "item.h"
#include <iostream>
#include <string>
#include <vector>
#include <stdlib.h>
#include <time.h>
#include<QPushButton>
#include "status.h"
#include "QString"
#include <QTimer>
#include <QTime>
//#include <QSound>
using namespace std;

namespace Ui {
    class MainWindow;
}

class MainWindow : public QMainWindow
{
    Q_OBJECT

public:
    explicit MainWindow(QWidget *parent = 0);
    ~MainWindow();
    void setUp();

public slots:
    void onWin();
    void onLose();
    void onTimeOut();
    void update();

private slots:
    void on_pushButton_clicked();

    void on_pushButton_6_clicked();

    void on_pushButton_5_clicked();

    void on_pushButton_4_clicked();

    void on_pushButton_3_clicked();

    void on_pushButton_2_clicked();

private:
    ZorkUL *temp;
    Ui::MainWindow *ui;
    int healthBar;
    int timerBar;
    status stats;
    QTimer *timer;
    //QSound song;
};

#endif // MAINWINDOW_H
