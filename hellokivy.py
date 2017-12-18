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

class ListButton(ListItemButton):
    pass

class AmbulancePassing(BoxLayout):
    first_location_text_input = ObjectProperty()
    last_location_text_input = ObjectProperty()
    sth_list = ObjectProperty()

    def submit_location(self):
        first_location_spot = self.first_location_text_input.text

    def delete_location(self):
        pass

    def replace_location(self):
        pass

class AmbulancePassingApp(App):
    def build(self):
        return AmbulancePassing()

apApp = AmbulancePassingApp()
apApp.run()
