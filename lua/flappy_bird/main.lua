--[[
	-- Programmed by Carla Warde
	-- Uses assets from Harvard's CS50G course
	-- Last Modified: 2020-06-19
]]

Class = require 'class'

-- code from https://github.com/Ulydev/push 
push = require 'push'

require 'Bird'
require 'Pipe'
require 'PipePair'

require 'StateMachine'
require 'states/BaseState'
require 'states/TitleScreenState'
require 'states/PlayState'
require 'states/ScoreState'
require 'states/CountdownState'

-- physical screen dimensions
WINDOW_WIDTH = 1280
WINDOW_HEIGHT = 720

-- virtual resolution dimensions
VIRTUAL_WIDTH = 512
VIRTUAL_HEIGHT = 288

-- intialise background and foreground images and their scrolling speeds
local background = love.graphics.newImage('assets/sprites/background.png')
local BACKGROUND_SCROLL_SPEED = 30
local BACKGROUND_LOOPING_POINT = 413
local backgroundScroll = 0

local foreground = love.graphics.newImage('assets/sprites/ground.png')
local FOREGROUND_SCROLL_SPEED = 60
local foregroundScroll = 0

function love.load()

	-- initialise nearest-neighbor filter
    love.graphics.setDefaultFilter('nearest', 'nearest')
	
	-- window title
	love.window.setTitle("Not Flappy Bird");
	
	-- initialise text fonts
    smallFont = love.graphics.newFont('assets/fonts/font.ttf', 8)
    mediumFont = love.graphics.newFont('assets/fonts/flappy.ttf', 14)
    flappyFont = love.graphics.newFont('assets/fonts/flappy.ttf', 28)
    hugeFont = love.graphics.newFont('assets/fonts/flappy.ttf', 56)
    love.graphics.setFont(flappyFont)
	
	-- initialise sound effects and music
	sounds = {
		['explosion'] 	= 	love.audio.newSource('assets/sounds/explosion.wav','static'),
		['hurt']		=	love.audio.newSource('assets/sounds/hurt.wav','static'),
		['jump']		=	love.audio.newSource('assets/sounds/jump.wav','static'),
		['music']		=	love.audio.newSource('assets/sounds/marios_way.mp3','static'),
		['score']		=	love.audio.newSource('assets/sounds/score.wav','static'),
	}
	-- lower volume of audio assets ('cause they're louuud)
	sounds['explosion']:setVolume(0.2)
	sounds['hurt']:setVolume(0.2)
	sounds['jump']:setVolume(0.2)
	sounds['music']:setVolume(0.1)
	sounds['score']:setVolume(0.2)
	
	-- set music looping & play
	sounds['music']:setLooping(true)
	sounds['music']:play()
	
    -- initialise virtual resolution
    push:setupScreen(VIRTUAL_WIDTH, VIRTUAL_HEIGHT, WINDOW_WIDTH, WINDOW_HEIGHT, {
        vsync = true,
        fullscreen = false,
        resizable = true
    })
	
	-- initialise input table
	love.keyboard.keysPressed = {}
	
	-- initialise state machine with all state-returning functions
	gStateMachine = StateMachine {
        ['title'] = function() return TitleScreenState() end,
        ['play'] = function() return PlayState() end,
		['score'] = function() return ScoreState() end,
		['countdown'] = function () return CountdownState() end,
	}
	
	gStateMachine:change('title')

end

function love.resize(w, h)
	push:resize(w, h)
end

function love.keypressed(key)
	-- add to our table of keys pressed this frame
	love.keyboard.keysPressed[key] = true

    if key == 'escape' then
        love.event.quit()
    end
end

--[[
    New function used to check our global input table for keys we activated during
    this frame, looked up by their string value.
]]
function love.keyboard.wasPressed(key)
    if love.keyboard.keysPressed[key] then
        return true
    else
        return false
    end
end

function love.update(dt)
	if not pause then 
		-- update the background and foreground scroll based on their speeds, dt and looping point
		backgroundScroll = (backgroundScroll + BACKGROUND_SCROLL_SPEED * dt) % BACKGROUND_LOOPING_POINT
		foregroundScroll = (foregroundScroll + FOREGROUND_SCROLL_SPEED * dt) % VIRTUAL_WIDTH
	end
	
	gStateMachine:update(dt)
	
	-- reset input table
    love.keyboard.keysPressed = {}
end

function love.draw()
	push:start()
	
	love.graphics.draw(background, -backgroundScroll, 0)
    gStateMachine:render()
    love.graphics.draw(foreground, -foregroundScroll, VIRTUAL_HEIGHT - 16)
	
	push:finish()
end