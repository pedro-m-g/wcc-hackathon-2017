import cv2
import cv
import numpy as np



def cv2array(im):
  depth2dtype = {
        cv.IPL_DEPTH_8U: 'uint8',
        cv.IPL_DEPTH_8S: 'int8',
        cv.IPL_DEPTH_16U: 'uint16',
        cv.IPL_DEPTH_16S: 'int16',
        cv.IPL_DEPTH_32S: 'int32',
        cv.IPL_DEPTH_32F: 'float32',
        cv.IPL_DEPTH_64F: 'float64',
    }
  
  arrdtype=im.depth
  a = np.fromstring(
         im.tostring(),
         dtype=depth2dtype[arrdtype],
         #dtype='uint16',
         count=im.width*im.height*im.nChannels)
  a.shape = (im.height,im.width,im.nChannels)
  return a

#im = cv.LoadImage('letras/ds9/A/b/depth_1_0002.png',cv.CV_LOAD_IMAGE_COLOR)
#myImage = cv2array(im)

img = cv2.imread('letras/ds9/A/b/depth_1_0002.png',4)
maxVal = cv2.minMaxLoc(img)
newImg = cv2.convertScaleAbs(img,255/maxVal[1])
cv2.imshow('aa',newImg)
cv2.waitKey(0)
cv2.destroyAllWindows()