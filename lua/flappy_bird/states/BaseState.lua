--[[
	Class: Base State
	
	Used as a base class for other state machine classes
]]
BaseState = Class{}

function BaseState:init() end
function BaseState:enter() end
function BaseState:exit() end
function BaseState:update(dt) end
function BaseState:render() end