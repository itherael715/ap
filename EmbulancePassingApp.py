#-*- coding: utf-8 -*-

import kivy
import datetime
from kivy.app import App
from kivy.uix.button import Label
from kivy.uix.widget import Widget
from kivy.uix.button import Button
from kivy.uix.boxlayout import BoxLayout
from kivy.uix.listview import ListItemButton
from kivy.uix.textinput import TextInput
from kivy.core.text import LabelBase
import kivy.resources
from kivy.core.window import Window
font_name = kivy.resources.resource_find('DroidSansFallback.ttf')

class Input_Box_Layout(BoxLayout):
    pass

class EmbulancePassingApp(App):
    def build(self):
        Window.clearcolor = (.85, .85, .85, .3)
            
    def buttonClicked(self,button):
        pass
    
        
if __name__ == "__main__":
    EmbulancePassingApp().run()
