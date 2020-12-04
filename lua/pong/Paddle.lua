--[[ Paddle Class for Pong Game
	-- Programmed by Carla Warde
	-- Uses assets from Harvard's CS50G course
	-- Last Modified: 2020-05-24
]]

Paddle = Class{}

function Paddle:init(x, y, width, height)
	self.x = x
	self.y = y
	self.width = width
	self.height = height
	
	-- initialising y velocity variable
	self.dy = 0
end

function Paddle:update(dt)
	if self.dy < 0 then
		-- the max statement stops the paddle from going beyond the top of the screen
		self.y = math.max(0, self.y + self.dy * dt)
	else
		-- the min statement stops the paddle from going beyond the bottom of the screen
		self.y = math.min(VIRTUAL_HEIGHT - self.height, self.y + self.dy * dt)
	end
end

function Paddle:render() 
	love.graphics.rectangle('fill', self.x, self.y, self.width, self.height)
end