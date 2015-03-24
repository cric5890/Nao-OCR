#The main server to comunicate with clever bot
from chatterbotapi import ChatterBotFactory, ChatterBotType
import socket
import signal
import sys

conn = None
nao_conn = None
s = None
nao_s = None

#Used to handle ctrl-c or any other exiting interrupts
def handleExit(signum, frame):
	print "Exiting"
	if s != None:
		print "S closed"
		s.shutdown(socket.SHUT_RDWR)
		s.close()
	if nao_s != None:
		print "Nao S closed"
		nao_s.shutdown(socket.SHUT_RDWR)
		nao_s.close()
	if conn != None:
		print "Conn closed"
		conn.shutdown(socket.SHUT_RDWR)
		conn.close()
	if nao_conn != None:
		print "Nao Conn closed"
		nao_conn.shutdown(socket.SHUT_RDWR)
		nao_conn.close()
	sys.exit()

signal.signal(signal.SIGINT, handleExit)	
	
factory = ChatterBotFactory()

bot1 = factory.create(ChatterBotType.PANDORABOTS,'b0dafd24ee35a477')
bot1session = bot1.create_session()

print "Listening for SSH connection..."

#SSh Connect
HOST = ''                 
PORT = 50007              
s = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
s.setsockopt(socket.SOL_SOCKET, socket.SO_REUSEADDR, 1)
s.bind((HOST, PORT))
s.listen(1)
conn, addr = s.accept()

print "Listening for Nao connection..."

#Nao Connect
PORT = 50008              
nao_s = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
nao_s.setsockopt(socket.SOL_SOCKET, socket.SO_REUSEADDR, 1)
nao_s.bind((HOST, PORT))
nao_s.listen(1)
nao_conn, nao_addr = nao_s.accept()

print 'All Clients Connected'
try:
	#constantly receive data and then send back clever bots response
	while 1:
		data = conn.recv(1024)
		print "Received: " + str(data)
		s = bot1session.think(str(data))
		print "Bot says: " + s
		s = s + '\n'
		s = s.replace("Chomsky", "Wilf")		#just in case he ever says his name
		nao_conn.sendall(s)
except Exception, e:
	pass
conn.close()
nao_conn.close()
s.close()
nao_s.close()