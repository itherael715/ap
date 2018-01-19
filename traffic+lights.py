
#-*- coding: utf-8 
get_ipython().magic('matplotlib inline')
import cv2
import pandas as pd
import numpy as np
import urllib



f = pd.read_csv("traffic.csv",encoding='big5')

for i in range(len(f)):
    print(f['url'][i])

#目前不曉得如何將網站上的影片檔案擷取下來給opencv使用
import urllib.request
ur = f['url'][1]
with urllib.request.urlopen('http://cctv1.kctmc.nat.gov.tw/axis-cgi/jpg/image.cgi?camera=1&amp;1516370167097') as url:
    s = url.read()

cap = cv2.VideoCapture(s)


import numpy as np
import urllib
import cv2
import urllib.request

# METHOD #1: OpenCV, NumPy, and urllib
def url_to_image(url):
    # download the image, convert it to a NumPy array, and then read
    # it into OpenCV format
    with urllib.request.urlopen(url) as response:
       html = response.read()
    image = np.asarray(bytearray(html), dtype="uint8")
    image = cv2.imdecode(image, cv2.IMREAD_COLOR)
 
    # return the image
    return image

cv2.imshow("Image",  url_to_image('http://cctv1.kctmc.nat.gov.tw/axis-cgi/jpg/image.cgi?camera=1&amp;1516370167097'))




cv2.destroyAllWindows()

