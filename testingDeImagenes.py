import cv2
import pandas as pd

content = []
data = []
imagen = cv2.imread('atest.png')
#print img.item(0,0,0)
for w in range(30):
    for j in range(30):
        content.append(imagen.item(w,j,0))
data.append(content)
                
df  = pd.DataFrame(data)
df  = df.sample(frac=1).reset_index(drop=True)
df.to_csv('prueba.csv',index=False)
print df.head(5)