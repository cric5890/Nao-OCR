'''
Created on Dec 3, 2014

@author: Wesley Crick
'''

import Image

class Histogram:
    '''
    classdocs
    '''


    def __init__(self):
        '''
        Constructor
        '''
    
    def verticalHistogram(self, image_file, image_data):
        '''
            Finds the vertical histogram of the given image
            
            Parameters:
                image    -    a gray scaled image
            
            Returns:    An array of percent
        '''
        array = []
        count = 0;
        width = image_file.size[0]
        height = image_file.size[1]
        for y in range(0,height):
            count = 0
            for x in range(0,width):
                pixel = image_data[x,y];
                
                if pixel > 250:
                    count = count + 1
            percent = float(float(count)/float(width))*100
            array.append( percent )
        return array  
            
            
            
            
            
            
            
            
            
            