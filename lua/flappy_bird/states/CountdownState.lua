--[[
	Countdown State
	Author: Carla Warde
	Last Modified: 2020-06-19
	Description: Countdown state for Flappy Bird remake as part of Harvard's CSG50 course
]]

CountdownState = Class{__includes = BaseState}

COUNTDOWN_TIME = 0.75

function CountdownState:init()
	self.count = 3
	self.timer = 0
end

function CountdownState:update(dt)
	self.timer = self.timer + dt
	
	if self.timer > COUNTDOWN_TIME then
		-- tidy up timer value
		self.timer = self.timer % COUNTDOWN_TIME
		
		self.count = self.count - 1
	end
	
	if self.count == 0 then
		gStateMachine:change('play')
	end
end

function CountdownState:render()
	love.graphics.setFont(hugeFont)
	love.graphics.printf(tostring(self.count),0, 100, VIRTUAL_WIDTH, 'center')
end