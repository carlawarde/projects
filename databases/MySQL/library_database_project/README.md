<h2>Info:</h2>
<p><b>Title: </b>Library Database Project</p>
<p><b>Description: </b>In this project, my group was tasked with creating a database with views, triggers, functions and procedures. We decided to create a library database
that keeps tracks of members and books in the library.</p>
<p><b>Last Updated: </b>2018-11-30</p>
<p><b>Author(s): </b>Carla Warde, Vincent Kiely, Cole Wallin</p>
<p><b>Utilised: </b>MySQL</p>

<h2>Instructions:</h2>
<ol>
<li>Running the database will require setting up a database and inserting the three files included in this folder. (Apologies for not having it hosted on a server.)</li>
</ol>

<h2>User Story</h2>
<ul>
<li>The user can insert items into the database.</li>
<li>The user can delete items from the database.</li>
<li>The user can use the views in the library.</li>
<li>The user can activate the triggers by doing certain actions, which then calls for the functions and prodcedures.</li>
</ul>

<h2>Database Functions:</h2>

<h3>Triggers</h3>
<ol>
<li>
	<b>dateInsert: </b>This trigger fires when a new loan is inserted into the database. 
	Since there is no way to set a default datetime to something like ‘now() + 2 weeks’ the due date for the loan needed to be created after the row is inserted into the database. 
	The trigger simply takes the new entry, adds a two week time interval to the checkout time, and updates the rows' due date column to be this value. 
</li>
<li>
	<b>itemReturned: </b>If a particular item in the library is unavailable and then a copy of it is returned, its available status needs to be set to true. 
	This trigger checks the ‘available’ column for the returned item and if it is currently false, it changes it to true.
</li>
<li>
	<b>updateAvailibility: </b>When an item is being checked out, we need to know if it was the last one in the library so we can update the availability on the item. 
	This trigger is fired when a new loan is inserted into the database. It then counts the total number of loans in the table that have the same ISBN and compares this value to the ‘itemCount’  column of the same item. 
	If these numbers are the same, then all items under that ISBN are checked out and the ‘available’ status is changed to false. 
</li>
</ol>

<h3>Procedures</h3>
<ol>
<li>
	<b>checkoutItem: </b>This procedure gets called when a library member is checking out an item. 
	It takes the ISBN of the item and the ID of the library member. The procedure first checks the number of items the member already has on loan to make sure they don’t already have 3 things checked out (3 is the loan limit for any one person in our library). 
	If they have less than 3, then a new loan is inserted into the loans table. 
</li>
<li>
	<b>returnItem: </b>If a particular item in the library is unavailable and then a copy of it is returned, its available status needs to be set to true. 
	This trigger checks the ‘available’ column for the returned item and if it is currently false, it changes it to true.
</li>
<li>
	<b>makeAvailable: </b>This procedure is called by the ‘itemReturned’ trigger. 
	The procedure simply changes the available value for any given item to true. 
	This procedure is useful because it makes the itemReturned trigger clearer and easier to read.
</li>
</ol>

<h3>Functions</h3>
<ol>
<li>
	<b>checkAvailibility: </b>This function takes the ISBN of any given item in the library. 
	It returns true if there are items of that ISBN available in the library for checkout and false otherwise. 
	This function is useful in other triggers and procedures on the database and would also be useful for members who are looking through a library catalogue and trying to see if the item they are seeking is available.
</li>
<li>
	<b>loanCount: </b>This function is called in the checkoutItem procedure to ensure a member has not exceeded the max number of items they are allowed to have checked out. 
	It takes a member’s ID number and returns the number of loans they currently have checked out.  
</li>
</ol>

<h3>Views</h3>
<ol>
<li>
	<b>members_Who_Can_Loan: </b>This view returns the number of loans a member of the library has, as well as their memberID, provided that they have less than three loans. 
	In our system, three is the maximum number of items a member of the library can take out, but this can easily be adjusted for any library system. 
	This is a very beneficial view for library staff as they can quickly check if a member is allowed take out anymore loans.
</li>
<li>
	<b>status_Of_Loans: </b>This view returns the ISBN, MemberID and Loan Status of a loan. 
	The LoanStatus compares the return date to the current date and will either say ‘IN DATE’ or ‘OVERDUE’ based on that comparison. 
	This view is essential to any library as you need a way of keeping track of the status of loans – and what members have overdue loans.
</li>
<li>
	<b>makeAvailable: </b>This procedure is called by the ‘itemReturned’ trigger. 
	The procedure simply changes the available value for any given item to true. 
	This procedure is useful because it makes the itemReturned trigger clearer and easier to read.
</li>
</ol>
