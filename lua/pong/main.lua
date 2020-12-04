--[[ Pong Game main.lua file
	-- Programmed by Carla Warde
	-- Uses assets from Harvard's CS50G course
	-- Last Modified: 2020-05-24
]]

-- code from https://github.com/vrld/hump/blob/master/class.lua
Class = require 'class'

-- code from https://github.com/Ulydev/push 
push = require 'push'

require 'Ball'
require 'Paddle'

WINDOW_HEIGHT = 720
WINDOW_WIDTH =  1280

VIRTUAL_HEIGHT = 243
VIRTUAL_WIDTH = 432

paddleSpeed = 150
 
-- Initialises the game
function love.load()
	love.window.setTitle('Pong')
	love.graphics.setDefaultFilter('nearest', 'nearest')
	
	math.randomseed(os.time())
	
	push:setupScreen(VIRTUAL_WIDTH, VIRTUAL_HEIGHT, WINDOW_WIDTH, WINDOW_HEIGHT, {
        fullscreen = false,
        resizable = false,
        vsync = true
	})
	
	-- sounds used in game
	sounds = {
        ['paddle_hit'] = love.audio.newSource('sounds/paddle_hit.wav', 'static'),
        ['score'] = love.audio.newSource('sounds/score.wav', 'static'),
        ['wall_hit'] = love.audio.newSource('sounds/wall_hit.wav', 'static')
    }
	
	smallFont = love.graphics.newFont('font.ttf', 8)
	largeFont = love.graphics.newFont('font.ttf', 16)
	scoreFont = love.graphics.newFont('font.ttf', 32)

	playerOne = Paddle(10, 30, 5, 20)
	playerTwo = Paddle(VIRTUAL_WIDTH - 15, VIRTUAL_HEIGHT - 50, 5, 20)
	
	ball = Ball(VIRTUAL_WIDTH/2 - 2, VIRTUAL_HEIGHT/2 - 2, 4, 4)
	
	playerOneScore = 0
	playerTwoScore = 0
	
	servingPlayer = math.random(2)
	
	fps = false
	
	gameState = 'start'
end

-- Updates the game every frame; called automatically by Love
function love.update(dt)
	-- player one movement
	if love.keyboard.isDown('w') then
		playerOne.dy = -paddleSpeed
	elseif love.keyboard.isDown('s') then
		playerOne.dy = paddleSpeed
	else
		playerOne.dy = 0
	end
	
	-- player two movement
	if love.keyboard.isDown('up') then
		playerTwo.dy = -paddleSpeed
	elseif love.keyboard.isDown('down') then
		playerTwo.dy = paddleSpeed
	else
		playerTwo.dy = 0
	end
	
	-- randomises the ball's y velocity and sets the x velocity based on what player is serving
	if gameState == 'serve' then
		ball.dy = math.random(-50, 50)
        if currentPlayer == 1 then
            ball.dx = math.random(100, 150)
        else
            ball.dx = -math.random(100, 150)
        end
	end
	
	if gameState == 'play' then
		
		-- checks if balls has collided with player one paddle
		if ball:collision(playerOne) then
			sounds['paddle_hit']:play()
			ball.dx = -ball.dx * 1.03
			ball.x = ball.x + 5
			
			-- keep velocity going in the same direction, but randomize it
            if ball.dy < 0 then
                ball.dy = -math.random(10, 150)
            else
                ball.dy = math.random(10, 150)
            end
		end
		
		-- checks if balls has collided with player two paddle
		if ball:collision(playerTwo) then
			sounds['paddle_hit']:play()
			ball.dx = -ball.dx * 1.03
			ball.x = ball.x - 4
			
			if ball.dy < 0 then
                ball.dy = -math.random(10, 150)
            else
                ball.dy = math.random(10, 150)
            end
		end
		
		-- checks if the ball collides with the top or bottom of the screen. inverts the y velocity
		if ball.y < 0 then
			sounds['wall_hit']:play()
			ball.y = 0
			ball.dy = -ball.dy
		elseif ball.y > VIRTUAL_HEIGHT then
			sounds['wall_hit']:play()
			ball.y = VIRTUAL_HEIGHT - 4
			ball.dy = -ball.dy
		end
		
		
		-- updates the scoring player's score depending on whether ball hits the left or right side of the screen
		-- also resets the ball, changes the game state to serve and sets the serving player
		if ball.x < 0 then
			playerTwoScore = playerTwoScore + 1
			ball:reset()
			currentPlayer = 1
			sounds['score']:play()
			gameState = 'serve'
		elseif ball.x > VIRTUAL_WIDTH then
			playerOneScore = playerOneScore + 1
			ball:reset()
			currentPlayer = 2
			sounds['score']:play()
			gameState = 'serve'
		end
		
		-- if a player has reached a score of ten the game ends
		if playerTwoScore == 10 then
			gameState = 'win'
			currentPlayer = 2
		elseif playerOneScore == 10 then
			gameState = 'win'
			currentPlayer = 1
		end
		
		ball:update(dt)
	end
	
	playerOne:update(dt)
	playerTwo:update(dt)
end

-- Renders the game every frame; called after update
function love.draw()
	push:apply('start')
	
	love.graphics.setFont(smallFont)
	love.graphics.clear(40/255, 45/255, 52/255, 1)
	
	-- ui messages to print to screen during different states
	if gameState == 'start' then
		love.graphics.printf('Welcome to Pong!', 0, 20, VIRTUAL_WIDTH, 'center')
		love.graphics.printf('Press Enter to begin', 0, 40, VIRTUAL_WIDTH, 'center')
	elseif gameState == 'play' then
		love.graphics.setFont(largeFont)
		love.graphics.printf('Game On!', 0, 20, VIRTUAL_WIDTH, 'center')
	elseif gameState == 'serve' then
		love.graphics.setFont(largeFont)
		love.graphics.printf('Game On!', 0, 20, VIRTUAL_WIDTH, 'center')
		love.graphics.setFont(smallFont)
		love.graphics.printf('Player '..tostring(currentPlayer)..'\'s Serve\nPress Enter to start', 0, 40, VIRTUAL_WIDTH, 'center')
	elseif gameState == 'win' then
		love.graphics.printf('Player '..tostring(currentPlayer)..' wins!\nPress Enter to play again.', 0, 20, VIRTUAL_WIDTH, 'center')
	end
	
	-- printing scores to screen
	love.graphics.setFont(scoreFont)
	love.graphics.print(tostring(playerOneScore), VIRTUAL_WIDTH/2 - 70, VIRTUAL_HEIGHT / 4)
	love.graphics.print(tostring(playerTwoScore), VIRTUAL_WIDTH/2 + 50, VIRTUAL_HEIGHT / 4)
	
	-- rendering paddles and ball
	playerOne:render()
	playerTwo:render()
	ball:render()
	
	-- displays the FPS in the top left of the screen if the use pressed 'F'
	if fps then
		love.graphics.setFont(smallFont)
		love.graphics.setColor(0, 255, 0, 255)
		love.graphics.print(tostring(love.timer.getFPS()), 20, 20)
	end
	
	push:apply('end')
end

function love.keypressed(key)
    if key == 'escape' then
        love.event.quit()
	elseif key == 'enter' or key == 'return' then
		if gameState == 'start' then
			gameState = 'play'
		elseif gameState == 'serve' then
			gameState = 'play'
		elseif gameState == 'win' then
			resetGame()
		end
	elseif key == 'f' then
		if fps then
			fps = false
		else
			fps = true
		end
    end
	
end

-- resets the scores and ball. Returns the game to the start state
function resetGame() 
	playerOneScore = 0
	playerTwoScore = 0
	ball:reset()
	gameState = 'start'
end
