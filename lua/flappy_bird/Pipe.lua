--[[
	Pipe Class
	Author: Carla Warde
]]

Pipe = Class{}

PIPE_SCROLL = -60
PIPE_SPEED = 60
PIPE_IMAGE = love.graphics.newImage('assets/sprites/pipe.png')

-- height of pipe image, globally accessible
PIPE_HEIGHT = 288
PIPE_WIDTH = 70

function Pipe:init(orientation, y)
	self.width = PIPE_WIDTH
    self.height = PIPE_HEIGHT
	
	self.x = VIRTUAL_WIDTH
	-- set the Y to a random value halfway below the screen
    self.y = y
	
	self.orientation = orientation
end

function Pipe:update(dt)
	
end

function Pipe:render()
	love.graphics.draw(PIPE_IMAGE, self.x, 
        (self.orientation == 'top' and self.y + PIPE_HEIGHT or self.y), 
        0, 1, self.orientation == 'top' and -1 or 1)
end