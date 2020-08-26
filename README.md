# client-server-calculator
Simple Calculator as a Client-Server Communication (Socket Programming)

Consider a server that provides a series of computational operations to two clients.
This server sends five command lines to the client every 60 seconds. In even times, the commands sent by the first client and in odd times the commands sent by the second client are calculated. Clients perform the task of preparing and sending reports to the server . After receiving and calculating the received commands, a report is prepared and puts it in a file and then Sends the address of the desired file to the server. Note that it is possible to send wrong commands from the server and these commands must be identified by the client and a separate file called the error file must be saved and its address sent to the server. The server notifies the client of the receipt of response and error file paths by sending a confirmation message. At the end we have a 5-minute report from this server.
