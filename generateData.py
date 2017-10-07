import pandas as pd
import cv2
import string
import numpy as np

content = {}
subletters= list(string.ascii_lowercase)
subletters.remove('j')
subletters.remove('z')
alphabet = ['A','B','C','D','E']
imageSize = 30
data = []
def mapear(a):
    for i in range(len(subletters)):
        if(a==subletters[i]):
            return i
for alphabetLetter in subletters:
    for i in range(30):
        for letter in alphabet:
            content = []
            imagen = cv2.imread('fingerspelling5/dataset5/dataset/finalTraining/'+alphabetLetter+'_'+str(i)+letter+'.png')
            #print img.item(0,0,0)
            content.append(mapear(alphabetLetter))
            for w in range(imageSize):
                for j in range(imageSize):
                    content.append(imagen.item(w,j,0))
            data.append(content)
                
df  = pd.DataFrame(data)
df  = df.sample(frac=1).reset_index(drop=True)
df.to_csv('fulldata.csv',index=False)
print df.head(5)