---------- Tour Program ----------

	This program was designed to 
	use image processing 
	techniques to try and have nao
	give a tour of the computer 
	science and physics hallway.
	
	Do to stability issues with
	how nao walks and taking 
	pictures it could not be 
	complete.  However if the
	camera has stability this
	program should still work.
	
1)	Raspberry Pi

	The image processing techniques 
	are completed but it does not 
	interface with anything. Images
	are still hardcoded and should
	talk to nao over a socket.
	
	run: python main.py
	
2)  MaptheHall

	Run in Choreographe to have nao
	create a node structure and
	store simulated rooms in his
	memory.
	
	After opening the project in
	Choreographe open the MapTheHall
	block by double clicking it.
	
	Walking+Sonars and Room Input are
	both just toggle blocks.  Click 
	the wrench and tick the box to
	turn them on or off.
	
	The Input Room Simulator feeds 
	fake room input to the node
	structure in ListOfRooms.
	
	ListOfRooms is a node structure
	to store rooms with a name, side
	of hall, and connectors to the
	rooms on either side of it.