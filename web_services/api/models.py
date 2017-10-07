# -*- coding: utf-8 -*-
from __future__ import unicode_literals
import base64

from django.db import models
# Create your models here.

class Screenshot(models.Model):
  _img = models.TextField('img')

  def set_img(self, data):
    self._img = base64.encodestring(img)

  def get_img(self):
    return base64.decodestring(self._img)

  img = property(get_img, set_img)
  lat = models.DecimalField(decimal_places=6, max_digits=6)
  lng = models.DecimalField(decimal_places=6, max_digits=6)