import glob
import cv2
import string
import pandas as pd

subletters= list(string.ascii_lowercase)
subletters.remove('j')
subletters.remove('z')
alphabet = ['A','B','C','D','E']


imageSize = 30
imagesPerLetter = 30


for letter in alphabet:
    for alphabetLetter in subletters:
        images = [cv2.imread(file) for file in glob.glob("fingerspelling5/dataset5/"+letter+"/"+alphabetLetter+"/*.png")]
        images = images[:imagesPerLetter]
        resizedImages = [cv2.resize(image, (imageSize, imageSize)) for image in images]
        grayScaledImages = [cv2.cvtColor(image, cv2.COLOR_BGR2GRAY) for image in resizedImages]
        for i in range(len(grayScaledImages)):
            cv2.imwrite("fingerspelling5/dataset5/dataset/finalTraining/"+alphabetLetter+"_"+str(i)+letter+'.png',grayScaledImages[i])
            #Explorar la imagen completa
print('exito!')