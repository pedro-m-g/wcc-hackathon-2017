# -*- coding: utf-8 -*-
from __future__ import unicode_literals

from django.shortcuts import render

# Create your views here.

def save_screenshot(request):
	img = request.POST['img']
	geo = request.POST['geo']