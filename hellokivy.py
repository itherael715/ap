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

#總之先import了一堆東西

class ListButton(ListItemButton):
    pass
#還沒有寫

class AmbulancePassing(BoxLayout):
    first_location_text_input = ObjectProperty()
    last_location_text_input = ObjectProperty()
    sth_list = ObjectProperty()
#定義了一堆東西

    def submit_location(self):
        first_location_spot = self.first_location_text_input.text
#應該是可以輸入的東西

    def delete_location(self):
        pass

    def replace_location(self):
        pass

class AmbulancePassingApp(App):
    def build(self):
        return AmbulancePassing()
    
apApp = AmbulancePassingApp()
apApp.run()
#總之有個黑色的頁面
