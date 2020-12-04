--[[
	ScoreState
	Author: Carla Warde
	Last Modified: 2020-06-19
	Description: ScoreState for Flappy Bird remake as part of Harvard's CSG50 course
]]

ScoreState = Class{__includes = BaseState}

function ScoreState:enter(params)
    self.score = params.score
end

function ScoreState:update(dt)
	if love.keyboard.wasPressed('enter') or love.keyboard.wasPressed('return') then
        gStateMachine:change('countdown')
    end
end

function ScoreState:render()
	love.graphics.setFont(hugeFont)
	love.graphics.printf('Game Over',0, 30, VIRTUAL_WIDTH, 'center')
	
	love.graphics.setFont(mediumFont)
	love.graphics.printf('Score: '..tostring(self.score),0, 100, VIRTUAL_WIDTH, 'center')
	
	love.graphics.setFont(smallFont)
    love.graphics.printf('Press Enter to play again', 0, 140, VIRTUAL_WIDTH, 'center')
end