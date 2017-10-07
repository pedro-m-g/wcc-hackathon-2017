# -*- coding: utf-8 -*-
# Generated by Django 1.11.6 on 2017-10-07 01:58
from __future__ import unicode_literals

from django.db import migrations, models


class Migration(migrations.Migration):

    initial = True

    dependencies = [
    ]

    operations = [
        migrations.CreateModel(
            name='Screenshot',
            fields=[
                ('id', models.AutoField(auto_created=True, primary_key=True, serialize=False, verbose_name='ID')),
                ('_img', models.TextField(verbose_name='img')),
                ('lat', models.DecimalField(decimal_places=6, max_digits=6)),
                ('lng', models.DecimalField(decimal_places=6, max_digits=6)),
            ],
        ),
    ]
