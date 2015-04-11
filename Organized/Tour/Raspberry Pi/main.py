'''
Created on Dec 2, 2014

@author: Wesley Crick
'''

import cv2
import time
from numpy import *

def calculateHistogram(image, rows, cols):
    hist = array([])
    for i in range( 0, cols):
        hist = append(hist,  cv2.calcHist( [ image[0:rows, i] ], [0], None, [1], [200, 255], True, False ) )
    return hist

def findLines(hist, threshold):
    lines = array([])
    start = 0;
    len = 0;
    for i in range(0, hist.size):
        if hist[i] > 0.0:
            len = len + 1
        elif hist[i] == 0.0 and len > threshold:
            container = array([])
            container = append(container, start)
            container = append(container, len)
            start = i
            len = 1
            lines = append(lines, container)
        else:
            start = i
            len = 1
    return lines

def removeModeAndLess(old_array):
    values = array([])
    counts = array([])
    values = append(values, old_array[0])
    counts = append(counts, 1)
    '''count elements'''
    for i in range(1, old_array.size):
        temp_value = old_array[i]
        contains = False
        for j in range(0, values.size):
            if ( values[j] == temp_value and temp_value != 0.0 ):
                contains = True
                counts[j] = counts[j] + 1
                break
        if ( contains == False and temp_value != 0.0 ):
            values = append(values, temp_value)
            counts = append(counts, 1)
    '''find mode'''
    mode_index = 0;
    mode_greatest = counts[0]
    for i in range(1, values.size):
        if ( counts[i] >= mode_greatest and values[i] != 0.0 ):
            mode_greatest = counts[i]
            mode_index = i
    '''remove mode and less'''
    mode = values[mode_index]
    new_array = old_array
    for i in range(0, old_array.size):
        if ( old_array[i] <= mode ):
            new_array[i] = 0.0
    return new_array


''' Image Loading - 0.38s '''
opencv_image = cv2.imread("image5.jpg")

''' Image Filtering - 0.30s '''
kernel1 = array( [-1, -5, -1, 
                   0, 0, 0, 
                   1, 5, 1] )
kernel2 = array( [ -1, 0, 1, 
                   -5, 0, 5,
                   -1, 0, 1] )

gray_image = cv2.cvtColor(opencv_image, cv2.COLOR_BGR2GRAY)
filtered_image = cv2.filter2D(gray_image, -1, kernel1)#returns a 2d array
rows,cols = gray_image.shape
#print(gray_image.shape)

#print("==== Horizontal Histogram ====")
horz_hist = calculateHistogram(filtered_image, rows, cols)
horz_hist = removeModeAndLess(horz_hist)

gray_rot_90_image = cv2.transpose(gray_image)
gray_rot_90_image = cv2.flip(gray_rot_90_image, 1)
filtered_rot_90_image = cv2.filter2D(gray_rot_90_image, -1, kernel1)
rows,cols = gray_rot_90_image.shape

vert_hist = calculateHistogram(filtered_rot_90_image, rows, cols)
#vert_hist = removeModeAndLess(vert_hist)


horz_lines = findLines( horz_hist, 50 ) 
vert_lines = findLines( vert_hist, 50 ) 
print(horz_lines)
print(vert_lines)

'''
=====================================================================================================================
Found plaque, now to zoom on numbers
'''
plaque = gray_image[vert_lines[0]:vert_lines[0] +vert_lines[1] ,horz_lines[0]:horz_lines[0]+horz_lines[1] ] 
cv2.imwrite("plaque.jpg", plaque)
#rows,cols = plaque.shape
'''horz_hist = calculateHistogram(plaque, rows, cols)
#for i in range(0, horz_hist.size):
#    print(horz_hist[i])
'''
plaque_90 = cv2.transpose(plaque)
plaque_90 = cv2.flip(plaque_90, 1)
plaque_90 = cv2.flip(plaque_90, -1)
rows,cols = plaque_90.shape
kernel1 = array( [-1, -3, -1,
                   0, 0, 0,
                   1, 3, 1] )
plaque_90 = cv2.filter2D(plaque_90, -1, kernel1)
vert_hist =  calculateHistogram(plaque_90, rows, cols)
vert_hist = removeModeAndLess(vert_hist)
#for i in range(0, vert_hist.size):
#    print(vert_hist[i])

vert_lines = findLines( vert_hist, 10)
#print(vert_lines)

rows,cols = plaque.shape
#print(rows,cols)
numbers = plaque[vert_lines[0]:vert_lines[0]+vert_lines[1],0:cols] 
'''
======================================================================================================
===============
Found zoomed numbers, now find each individual
'''
kernel2 = array( [[-1,0,1], [-5,0,5], [-1, 0, 1] ] ) 
filtered_numbers = cv2.filter2D(numbers, -1, kernel2)
rows,cols = numbers.shape
horz_hist = calculateHistogram(filtered_numbers, rows, cols)
horz_hist = removeModeAndLess(horz_hist)
horz_lines = findLines(horz_hist, 5)


for i in range(0, len(horz_lines), 2):
    number = numbers[0:rows, horz_lines[i]:horz_lines[i] + horz_lines[i+1] ]
#    cv2.imwrite("numbers" + str(i/2) + ".jpg", number)

# -----Image Saving----- # 
#cv2.imwrite("filtered.jpg", filtered_image)
#cv2.imwrite("filtered_rot_90.jpg", filtered_rot_90_image)
#cv2.imwrite("plaque.jpg", plaque)
#cv2.imwrite("plaque_90.jpg", plaque_90)
#cv2.imwrite("numbers.jpg", numbers)
