import kivy
from kivy.app import App
from kivy.uix.button import Label
from kivy.uix.widget import Widget
from kivy.uix.dropdown import DropDown
from kivy.uix.button import Button
from kivy.uix.boxlayout import BoxLayout
from kivy.uix.listview import ListItemButton
from kivy.properties import ObjectProperty
from kivy.base import runTouchApp

#先import部分會用的資料庫

class ListButton(ListItemButton):#按鈕部分
    pass
#暫時跳過

class AmbulancePassing(BoxLayout):#宣告物件
    first_location_text_input = ObjectProperty()
    last_location_text_input = ObjectProperty()
    sth_list = ObjectProperty()

    def submit_location(self):#輸入部分
        first_location_spot = self.first_location_text_input.text

    def delete_location(self):#之後的地點移除
        pass

    def replace_location(self):#之後的地點交換
        pass

class AmbulancePassingApp(App):
    def build(self):
        return AmbulancePassing()
    
apApp = AmbulancePassingApp()
apApp.run()
#執行
