#-*- coding: utf-8 

from matplotlib import pyplot as plt
import numpy as np
import time
import requests
from xml.etree import ElementTree

#將城市的即時道路資訊擷取下來，以利統計

readStatic = requests.get('http://xml11.kctmc.nat.gov.tw:8080/XML/cctv_info.xml')#靜態資料
readStatic.encoding = 'unicode'

readDynamic = requests.get('http://xml11.kctmc.nat.gov.tw:8080/XML/cctv_value.xml') #動態資料
readDynamic.encoding = 'unicode'

# https://stackoverflow.com/questions/18308529/python-requests-package-handling-xml-response
Statree = ElementTree.fromstring(readStatic.content) #將XML檔案轉為文字
Staroot = ElementTree.fromstring(readStatic.content)

Dyntree = ElementTree.fromstring(readDynamic.content) #將XML檔案轉為文字
Dynroot = ElementTree.fromstring(readDynamic.content)


#https://stackoverflow.com/questions/4573237/how-to-extract-xml-attribute-using-python-elementtree
import numpy as np 
import pandas as pd
#將裡面的資訊都存起來，變成csv檔案
df = pd.DataFrame(np.ones((0,8)),columns=['cctvid','roadsection','locationpath','startlocationpoint','endlocationpoint','px','py','url'])
i =0
for members in Staroot.find('Infos'):
    insert = pd.DataFrame([[members.attrib['cctvid'],members.attrib['roadsection'],members.attrib['locationpath'],members.attrib['startlocationpoint'],
                            members.attrib['endlocationpoint'],members.attrib['px'],members.attrib['py'],Dynroot.find('Infos')[i].attrib['url']]]
                          ,columns=['cctvid','roadsection','locationpath','startlocationpoint','endlocationpoint','px','py','url'])
    i=i+1
    result = df.append(insert,ignore_index=True)
    df = result
    
df.to_csv('traffic.csv')

