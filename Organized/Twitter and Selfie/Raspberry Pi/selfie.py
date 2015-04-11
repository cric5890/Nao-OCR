#	Takes a selfie through the pi camera, and then sends it to a
#  	server to be tweeted.
#
#	run:	python selfie.py
import picamera
import time
import socket
import urllib
import signal
import sys

s = None
conn = None

#	Will create the HTTP request for the server and send it
#
#	Parameters:
#		message 	- string 	- the mesage you would like to tweet
#		filename 	- string	- the image name
#		host		- string	- the hostname/ip
#		port		- int		- the port number
#		recordFolder- string	- the path to the image
def tweet_photo(message, filename, host, port, recordFolder):
	with open(recordFolder+filename, "rb") as image_file:
		image_contents = image_file.read()

	boundary = "SUPERCOOLFILESEPERATORSTRING$$$$"

	body = "--" + boundary + "\r\n"
	body = body + "Content-Disposition: form-data; name=\"tweet\"\r\n\r\n"
	body = body + message + "\r\n"
	body = body + "--" + boundary + "\r\n"
	body = body + "Content-Disposition: form-data; name=\"file\"; filename=\"" + filename + "\"\r\n"
	body = body + "Content-Type: image/jpeg\r\n\r\n"
	body = body + image_contents + "\r\n"
	body = body + "--" + boundary + "--"

	header =  "POST /twitter/tweet.php HTTP/1.1\r\n"
	header = header + "Host: " + host + "\r\n"
	header = header + "User-Agent: Mozilla/5.0\r\n"
	header = header + "Accept-Language: en-US,en;q=0.8\r\n"
	header = header + "Connection: keep-alive\r\n"
	header = header + "Cache-Control: max-age=0\r\n"
	header = header + "Content-Length: " + str(len(body)) + "\r\n"
	header = header + "Content-Type: multipart/form-data; boundary=" + boundary + "\r\n"
	header = header + "\r\n"

	data = header + body

	try:
		client = socket.socket (socket.AF_INET, socket.SOCK_STREAM)
		result = client.connect ((host, port))
		client.send (data)

		data = client.recv (1024)
		#print data
		client.close()
		return data

	except Exception, e:
		return e

#	Designed to handle ctrl-c
def signal_handler(signal, frame):
	print('Quiting!')
	try:
		conn.close()
	except Exception, e:
		print e
	try:
		s.close()
	except Exception, e:
		print e
	sys.exit(0)
		
signal.signal(signal.SIGINT, signal_handler)
		
HOST = ''
PORT = 5013
image_name = "image.jpg"
conn = None
camera = picamera.PiCamera()
s = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
s.bind((HOST, PORT))
s.listen(1)
tweet_list = ["Me and my friends at Poster Day 2015","Look at all the fun we are having at Poster Day 2015", "Taking a selfie with Wilf!", "So much fun today at Laurier", "Selfie time", "Laurier is the best","I love being here at Laurier", "It's great to be a Laurier Golden Hawk", "WLU is so much fun", "Me and my good looking friends!"]
tweet_counter = 0;
while 1:
	try:
		print "Listening..."
		conn, addr = s.accept()
		#User has connected
		print 'Connected by', addr
		conn.close()

		camera.hflip = False
		camera.vflip = False
		camera.resolution = (1920, 1080)

		camera.capture(image_name)

		print "Took picture"
		
		recordFolder = ""
		print tweet_photo(tweet_list[tweet_counter%len(tweet_list)], image_name, "ec2-52-11-5-102.us-west-2.compute.amazonaws.com", 80, recordFolder)
		tweet_counter = tweet_counter + 1
		
	except Exception, e:
		if conn != None:
			conn.close()
		print e
