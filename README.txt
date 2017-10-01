Elevator Simulator Final Implementation README



Author: Michael McClellan



What it is:

	

This is a simulation of a building with a number of elevators that react to the real-time
 creation of people on random floors of the building with a random destination. The 
simulation creates people at a specified rate for a specified number of minutes, and the
 multi-threaded elevators react to take them to their destinations. Once all created
	people have arrived at their proper floors, the elevators return to the first floor and 
the simulation ends, printing to the console information pertaining to the average wait
 and ride times of each person.
	




Input directions:

	

Open up SimulationInputs.xml to edit the relevant fields between the XML braces. 

	
All inputs must be integers.
	


Design:

	The algs package contains a simulation outputter that prints the final results of the simulation, as well
as an elevator controller delegate that decides which elevator to send to a floor request. My design doesn not
use a separate pending request delegate, since pending requests are reprocessed each time an elevator goes idle.