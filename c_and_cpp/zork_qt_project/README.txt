~~ Mayo For Sam ~~

Objective:

Traverse through the map to try to find the Sam Maguire Cup for Mayo before another county gets it!

Features:

-- West Button: Moves player West (if possible)
-- North Button: Moves player north (if possible)
-- East Button: Moves player east (if possible)
-- South Button: Moves player south (if possible)

-- Teleport Button: Teleports player to random room. Teleporting comes with a health penalty.
-- Take Button: Picks up an item in a room (if there is one)

-- Health bar: Displays player health
-- Timer bar: Displays player timer (the game lasts 20 seconds)

-- Items: Sam Maguire Cup is randomly generated on the map

Win Conditions:

-- Find the Sam Maguire Cup and pick it up

Lose Conditions:

-- Run out of Health
-- Timer reaches 0


Refactor Examples:

-- Templates: ZorkUL ln 240

-- Operator Overloading: ZorkUL ln 24

-- Friend classes: mainwindow is a friend class of ZorkUL

-- Inheritance: status is a subclass of QObject