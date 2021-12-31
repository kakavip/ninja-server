from django.contrib import admin
from .models import CloneNinja, GiftCode, Level, Ninja, NpcDaily, Player


@admin.register(Player)
class PlayerAdmin(admin.ModelAdmin):
    list_display = [
        "pk",
        "username",
        "password",
        "ninja",
        "luong",
        "joined_time",
        "status",
    ]
    list_display_links = ["pk", "username"]
    empty_display_value = "--empty--"
    fields = ["username", "password", "luong", "status"]
    search_fields = ["username", "ninja"]


@admin.register(Level)
class LevelAdmin(admin.ModelAdmin):
    list_display = ["level", "exps", "ppoint", "spoint"]
    list_display_links = ["level"]
    empty_display_value = "--empty--"
    fields = ["level", "exps", "ppoint", "spoint"]


class CharacterAdmin(admin.ModelAdmin):
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
    search_fields = ["name"]
    list_filter = ["_class"]


@admin.register(Ninja)
class NinjaAdmin(CharacterAdmin):
    pass


@admin.register(CloneNinja)
class CloneNinjaAdmin(CharacterAdmin):
    pass


@admin.register(NpcDaily)
class NpcDailyAdmin(admin.ModelAdmin):
    list_display = ["id", "npc_chat", "features", "features_id"]
    list_display_links = ["id"]
    empty_display_value = "--empty--"
    fields = ["id", "npc_chat", "features", "features_id"]


@admin.register(GiftCode)
class GiftCodeAdmin(admin.ModelAdmin):
    list_display = [
        "id",
        "gift_code",
        "yen",
        "xu",
        "luong",
        "username",
        "times",
        "limited",
        "mess_tb",
        "item_id",
        "item_quantity",
        "item_id_1",
        "item_quantity_1",
        "item_id_2",
        "item_quantity_2",
        "item_id_3",
        "item_quantity_3",
        "item_id_4",
        "item_quantity_4",
        "item_id_5",
        "item_quantity_5",
    ]
    list_display_links = ["id"]
    empty_display_value = "--empty--"
    fields = [
        "gift_code",
        "yen",
        "xu",
        "luong",
        "username",
        "times",
        "limited",
        "mess_tb",
        "item_id",
        "item_quantity",
        "item_id_1",
        "item_quantity_1",
        "item_id_2",
        "item_quantity_2",
        "item_id_3",
        "item_quantity_3",
        "item_id_4",
        "item_quantity_4",
        "item_id_5",
        "item_quantity_5",
    ]
    search_fields = ["gift_code"]
