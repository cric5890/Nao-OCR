from math import pow, sqrt

NUMBERS = 10
FEATURE_LENGTH = 8
cluster_centers=[[0 for j in range(FEATURE_LENGTH)] for i in range(10)]

def initClusterCenter(number, feature):
	if len(feature) == FEATURE_LENGTH:
		if number > 0 and number < NUMBERS:
			cluster_centers[number] = feature
	return

def includeFeature(number, feature):
	if len(feature) == FEATURE_LENGTH:
		if number > 0 and number < NUMBERS:
			center = cluster_centers[number]
			cluster_centers[number][0] = (center[0] + feature[0]) / 2
			cluster_centers[number][1] = (center[1] + feature[1]) / 2
			cluster_centers[number][2] = (center[2] + feature[2]) / 2
			cluster_centers[number][3] = (center[3] + feature[3]) / 2
			cluster_centers[number][4] = (center[4] + feature[4]) / 2
			cluster_centers[number][5] = (center[5] + feature[5]) / 2
			cluster_centers[number][6] = (center[6] + feature[6]) / 2
			cluster_centers[number][7] = (center[7] + feature[7]) / 2
	return

def getClosestCenter(feature):
	closest = -1
	if len(feature) == FEATURE_LENGTH:
		center = cluster_centers[0];
		dist = sqrt(pow(feature[0]-center[0])+pow(feature[1]-center[1])+pow(feature[2]-center[2])+pow(feature[3]-center[3])+pow(feature[4]-center[4])+pow(feature[5]-center[5])+pow(feature[6]-center[6])+pow(feature[7]-center[7]))
		minDist = dist
		closest = 0
		for i in range(1, len(cluster_centers)):
			center = cluster_centers[i]
			dist = sqrt(pow(feature[0]-center[0])+pow(feature[1]-center[1])+pow(feature[2]-center[2])+pow(feature[3]-center[3])+pow(feature[4]-center[4])+pow(feature[5]-center[5])+pow(feature[6]-center[6])+pow(feature[7]-center[7]))
			if dist < minDist:
				minDist = dist
				closest = i

	return closest

def loadCenters():
	f = open('./centers.csv')
	
	l = f.readline()
	index = 0
	while l != "":
		s = l.split(',')
		if len(s) == FEATURE_LENGTH:
			feature = [int(s[0]),int(s[1]),int(s[2]),int(s[3]),int(s[4]),int(s[5]),int(s[6]),int(s[7])]
			initClusterCenter(index,feature)
			l = f.readline()
			index = index+1
		else:
			#error
			return False 

	return True

def saveCenters():
	f = open('./centers.csv','w')

	for i in range(len(cluster_centers)):
		center = cluster_centers[i]
		csv = "{0},{1},{2},{3},{4},{5},{6},{7}".format(center[0],center[1],center[2],center[3],center[4],center[5],center[6],center[7])                                                                            
		f.write(csv+"/n")
	return
	
