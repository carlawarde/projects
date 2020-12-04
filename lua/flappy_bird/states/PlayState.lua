--[[
	Playstate
	Author: Carla Warde
	Last Modified: 2020-06-19
	Description: PlayState for Flappy Bird remake as part of Harvard's CSG50 course
]]

PlayState = Class{__includes = BaseState}

PIPE_SPEED = 60
PIPE_WIDTH = 70
PIPE_HEIGHT = 288

BIRD_WIDTH = 38
BIRD_HEIGHT = 24

pause = false

function PlayState:init()
	self.bird = Bird()
	self.pipePairs = {}
	self.spawnTimer = 0
	
	-- initialize our last recorded Y value for a gap placement to base other gaps off of
	self.lastY = -PIPE_HEIGHT + math.random(80) + 30
	
	self.score = 0
end

function PlayState:update(dt)
	if not pause then
		
		-- add dt passed to spawnTimer
		self.spawnTimer = self.spawnTimer + dt
		
		-- If spawn timer is greater than two, add new pipePair to the game
		if self.spawnTimer > 2 then
			local y = math.max(-PIPE_HEIGHT + 10, 
                math.min(self.lastY + math.random(-20, 20), VIRTUAL_HEIGHT - 90 - PIPE_HEIGHT))
            self.lastY = y
            
            table.insert(self.pipePairs, PipePair(y))
            self.spawnTimer = 0
		end
		
		
		
		-- update each pipePair
		for i, pair in pairs(self.pipePairs) do
			pair:update(dt)
			
			if not pair.scored then
				if pair.x + PIPE_WIDTH < self.bird.x then
					sounds['score']:play()
					self.score = self.score + 1
					pair.scored = true
				end
			end
		end
		
		-- check each pipePair to see if remove = true, and if so, remove the pair from the table
		for i, pair in pairs(self.pipePairs) do
			if pair.remove then
				table.remove(self.pipePairs, i)
			end
		end
		
		self.bird:update(dt)
		
		-- check to see if bird collided with pipe
		for i, pair in pairs(self.pipePairs) do
			for j, pipe in pairs(pair.pipes) do
				if self.bird:collides(pipe) then
					sounds['hurt']:play()
					sounds['explosion']:play()
					gStateMachine:change('score', {
						score = self.score
					})
				end
			end
		end
		
		-- if bird has gone above the top of the screen
		if self.bird.y < 0 then
			sounds['hurt']:play()
			sounds['explosion']:play()
			gStateMachine:change('score', {
				score = self.score
			})
		end
		
		-- if bird has gone below the bottom of the screen
		if (self.bird.y + self.bird.height) > VIRTUAL_HEIGHT -15 then
			sounds['hurt']:play()
			sounds['explosion']:play()
			gStateMachine:change('score', {
				score = self.score
			})
		end
	end
	
	if love.keyboard.wasPressed('p') then
		if pause then
			pause = false
			sounds['music']:play()
		else
			pause = true
			sounds['music']:pause()
		end
	end
end

function PlayState:render()
	for i, pipe in pairs(self.pipePairs) do
		pipe:render()
	end
	
	self.bird:render()
	
	love.graphics.setFont(hugeFont)
	love.graphics.printf(self.score,0, 20, VIRTUAL_WIDTH, 'center')
end