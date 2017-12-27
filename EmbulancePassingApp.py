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
 
class Input_Box_Layout(BoxLayout):
    pass
 
class EmbulancePassingApp(App):
    def build(self):
        Window.clearcolor = (.85, .85, .85, .3)
        textinput = TextInput()
        textinput.bind()
        return Input_Box_Layout()
    
EP_app = EmbulancePassingApp()
EP_app.run()
