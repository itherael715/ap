#-*- coding: utf-8 -*-
import kivy
from kivy.app import App
from kivy.uix.button import Label
from kivy.uix.widget import Widget
from kivy.uix.dropdown import DropDown
from kivy.uix.button import Button
from kivy.uix.boxlayout import BoxLayout
from kivy.uix.listview import ListItemButton
from kivy.uix.textinput import TextInput
from kivy.properties import ObjectProperty
from kivy.base import runTouchApp
from kivy.core.text import LabelBase
import kivy.resources
from kivy.core.window import Window
from kivy.uix.popup import Popup
 

class CustomPopup(Popup):
    pass
 
class SampBoxLayout(BoxLayout):
 
    # For checkbox
    checkbox_is_active = ObjectProperty(False)
    
    # For radio buttons
    blue = ObjectProperty(True)
    red = ObjectProperty(False)
    green = ObjectProperty(False)
 
    # For Switch
    def switch_on(self, instance, value):
        if value is True:
            print("Switch On")
        else:
            print("Switch Off")
 

 
class SampleApp(App):
    def build(self):
        # Set the background color for the window
        Window.clearcolor = (1, 1, 1, 1)
        textinput = TextInput()
        textinput.bind()
        return SampBoxLayout()
    
sample_app = SampleApp()
sample_app.run()
