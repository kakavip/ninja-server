from django.contrib import admin
from .models import Player


@admin.register(Player)
class PlayerAdmin(admin.ModelAdmin):
    list_display = ["pk", "username", "password", "ninja", "luong", "joined_time"]
    list_display_links = ["pk", "username"]
    empty_display_value = "--empty--"
    fields = ["username", "password", "luong"]
