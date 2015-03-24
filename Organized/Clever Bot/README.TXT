README - Uses NAO to say things returned from Clever Bot

--INSTRUCTIONS--

This system requires 3 parts, a server, the NAO project, and a PuTTY connection.

First the server code must be running, this can be found in the "Amazon Server" directory.
To run it use "python bot.py", and port 50007-50008 must be open.  

Next you will need to use a RAW PuTTY connection to connect to your server on port 50007.
The server should then say it is listening for nao to connect.  The PuTTY window will be
what you type to clever bot.

Finally run the Nao project, which can be found in the "Nao" directory.  Once the program 
is loaded on NAO tap the front of his head and it will connect to the server.  You will need 
to go into <BLOCK> and change the server address so that it will communicate with your server.

 