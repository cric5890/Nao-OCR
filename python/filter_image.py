import Image

#image = 0
def histVert(image_data, filter, width, height):
    count = 0
    array = []
    for x in range(0,width):
        count = 0
        for y in range(0, height):
            c = image_data[y*height + x]
            if ( c > 250 ):
                count = count + 1
        percent = (float(count)/float(width)) * 100
        print percent
        array.append(percent)
    return array

def histHorz(image_data, filter, width, height):
    return

def findPlaque(image_data, width, height):
    top_filter = [ -1, -2, -1, 0, 0, 0, 1, 2, 1 ]
    left_filter = [ -1, 0, 1, -2, 0, 2, -1, 0, 1 ]
    vert_hist = histVert(image_data, left_filter, width, height)
    horz_hist = histHorz(image_data, left_filter, width, height)

def main():
    image_name = raw_input("Enter image name: ")
    image = Image.open(image_name)
    bw_image = image.convert('1')#converts to black and white
    bw_image_data = list(bw_image.getdata())
    # returns single values between 0 and 255
    # normal RGB image will return a 3-tuple of (r,g,b)
    findPlaque(bw_image_data, image.size[0], image.size[1])

main()
