#include "mainwindow.h"
#include "ui_mainwindow.h"

MainWindow::MainWindow(QWidget *parent) :
    QMainWindow(parent),
    ui(new Ui::MainWindow)
{
    ui->setupUi(this);
    this->setFixedSize(this->size());
    temp = new ZorkUL();
    setUp();

    timer = new QTimer(this);
    connect(timer, SIGNAL(timeout()), this, SLOT(update()));
    timer->start(1000);

    connect(&stats, SIGNAL(win()),this, SLOT(onWin())); // if you find Sam Maguire.
    connect(&stats, SIGNAL(lose()),this, SLOT(onLose())); //the signal sends when the variable healthBar is at zero. Teleporting damages the player.
    connect(&stats, SIGNAL(time()),this, SLOT(onTimeOut()));
}

MainWindow::~MainWindow()
{
    delete ui;
}

void MainWindow::on_pushButton_clicked()
{
    //Teleport
    ui->textEdit->append(QString::fromStdString(temp->goTeleport()+"\n"));
    healthBar -= 20;
    ui->HealthBar->setValue(healthBar);
    stats.getHealthStatus(healthBar);
}

void MainWindow::on_pushButton_6_clicked()
{
    //West
    ui->textEdit->append(QString::fromStdString(temp->go("west") +"\n"));
}

void MainWindow::on_pushButton_5_clicked()
{
    //North
    ui->textEdit->append(QString::fromStdString(temp->go("north") +"\n"));
}

void MainWindow::on_pushButton_4_clicked()
{
    //South
    ui->textEdit->append(QString::fromStdString(temp->go("south") +"\n"));
}

void MainWindow::on_pushButton_3_clicked()
{
    //East
    ui->textEdit->append(QString::fromStdString(temp->go("east") +"\n"));
}

void MainWindow::setUp() {
    healthBar = 100;
    timerBar = 100;
    ui->HealthBar->setValue(healthBar);
    ui->TimerBar->setValue(timerBar);
    ui->textEdit->append(QString::fromStdString(temp->printWelcome()));
    ui->textEdit->setReadOnly(true);
}

void MainWindow::onWin() {
    ui->textEdit->setText(QString::fromStdString("Mayo for Sammmmm!!!! You win!"));
    ui->textEdit->setStyleSheet("border-image: url(:/new/sam/sammy-cup.jpg)");
    timer->stop();
}

void MainWindow::onLose() {
    ui->textEdit->setText(QString::fromStdString("You Died! Only Dubs teleport -_-"));
    ui->textEdit->setStyleSheet("border-image: url(:/new/sam/dub.jpg)");
    timer->stop();
}

void MainWindow::onTimeOut() {
    ui->textEdit->setText(QString::fromStdString("Someone else found Sam! You Lose!"));
}

void MainWindow::update() {
    timerBar -= 5;
    ui->TimerBar->setValue(timerBar);
    stats.getTimeStatus(timerBar);
}

void MainWindow::on_pushButton_2_clicked()
{
    ui->textEdit->append(QString::fromStdString(temp->takeItem() + "\n"));
    stats.getItemsStatus(temp->getCharacter());
}
