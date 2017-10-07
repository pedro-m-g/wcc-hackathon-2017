from PIL import Image
from os import listdir
import sys

list_Imagenes = listdir()#especificar carpeta

for i in list_Imagenes:
    imagen = Image.open(i)
    imagenReducida = imagen.resize((300,300))
    imagenReducida.save("my"+i)

