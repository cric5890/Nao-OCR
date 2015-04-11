---------- Twitter and Selfie Program ----------

	This program requires 3 programs all on 
	different hardware.  One for Nao, one for the
	raspberry pi, and one on a external server.
	
1)	Nao Program
		
		Nao's program will start by pressing 
		his head which will start a count down.
		He will put his arm into position with
		the selfie stick and send a signal to 
		the raspberry pi.
		
2)	Raspberry Pi

		The pi will accept a signal from Nao and
		then take the picture.  The pi will make 
		an HTTP request to the server with a
		string to tweet and an image.
		
		run: python selfie.py
		
3)	Server

		The server only needs to sit an a web
		server, ie Apache.  Then the HTTP 
		request from the PI will hit tweet.php.
		"cacert.pem" is a certificate that is
		required by the program for a secure
		connection.

