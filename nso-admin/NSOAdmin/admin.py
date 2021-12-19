from django.contrib import admin
from .models import Level, Ninja, Player


@admin.register(Player)
class PlayerAdmin(admin.ModelAdmin):
    list_display = ["pk", "username", "password", "ninja", "luong", "joined_time"]
    list_display_links = ["pk", "username"]
    empty_display_value = "--empty--"
    fields = ["username", "password", "luong"]


@admin.register(Level)
class LevelAdmin(admin.ModelAdmin):
    list_display = ["level", "exps", "ppoint", "spoint"]
    list_display_links = ["level"]
    empty_display_value = "--empty--"
    fields = ["level", "exps", "ppoint", "spoint"]


@admin.register(Ninja)
class NinjaAdmin(admin.ModelAdmin):
    list_display = [
        "id",
        "name",
        "_class",
        "skill",
        "spoint",
        "k_skill",
        "o_skill",
        "ppoint",
        "potential0",
        "potential1",
        "potential2",
        "potential3",
        "level",
        "yen",
        "xu",
    ]
    list_display_links = ["id", "name"]
    empty_display_value = "--empty--"
    fields = [
        "name",
        "_class",
        "skill",
        "spoint",
        "k_skill",
        "o_skill",
        "level",
        "yen",
        "xu",
    ]
