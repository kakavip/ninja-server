from django.contrib import admin
from django.db import models
from .models import (
    CloneNinja,
    Effect,
    GiftCode,
    Item,
    ItemSell,
    ItemShinwa,
    Level,
    Ninja,
    Npc,
    NpcDaily,
    OptionItem,
    OptionSkill,
    Player,
    Shop,
    Skill,
)


@admin.register(Player)
class PlayerAdmin(admin.ModelAdmin):
    list_display = [
        "pk",
        "username",
        "status",
        "ninja",
        "luong",
        "password",
        "joined_time",
    ]
    list_display_links = ["pk", "username"]
    empty_display_value = "--empty--"
    fields = ["username", "password", "luong", "status"]
    search_fields = ["username", "ninja"]
    list_filter = ["status"]


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


@admin.register(Npc)
class NpcAdmin(admin.ModelAdmin):
    list_display = ["id", "name", "head", "body", "leg", "type", "talk_id", "talk"]
    list_display_links = ["id", "name"]
    empty_display_value = "--empty--"
    search_fields = ["name"]
    fields = ["name", "head", "body", "leg", "type", "talk_id", "talk"]


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


@admin.register(Item)
class ItemAdmin(admin.ModelAdmin):
    list_display = [
        "id",
        "name",
        "description",
        "type",
        "nclass",
        "gender",
        "level",
        "icon_id",
        "part",
        "uptoup",
        "is_expires",
        "seconds_expires",
        "sale_coin_lock",
        "item_option",
        "option_1",
        "option_2",
        "option_3",
    ]

    list_display_links = ["id", "name"]
    empty_display_value = "--empty--"
    list_filter = ["type", "nclass", "gender"]
    search_fields = ["name", "description"]
    fields = [
        "name",
        "description",
        "type",
        "nclass",
        "gender",
        "level",
        "icon_id",
        "part",
        "uptoup",
        "is_expires",
        "seconds_expires",
        "sale_coin_lock",
        "item_option",
        "option_1",
        "option_2",
        "option_3",
    ]


@admin.register(ItemSell)
class ItemSellAdmin(admin.ModelAdmin):
    list_display = ["id", "type", "list_item"]
    list_display_links = ["id"]
    empty_display_value = "--empty--"
    list_filter = ["type"]
    fields = ["type", "list_item"]


@admin.register(ItemShinwa)
class ItemShinwaAdmin(admin.ModelAdmin):
    list_display = ["id", "item"]
    list_display_links = ["id"]
    empty_display_value = "--empty--"
    fields = ["item"]


@admin.register(OptionItem)
class OptionItemAdmin(admin.ModelAdmin):
    list_display = ["id", "name", "type"]
    list_display_links = ["id", "name"]
    empty_display_value = "--empty--"
    search_fields = ["name"]
    list_filter = ["type"]
    fields = ["name", "type"]


@admin.register(OptionSkill)
class OptionSkillAdmin(admin.ModelAdmin):
    list_display = ["id", "name"]
    list_display_links = ["id", "name"]
    empty_display_value = "--empty--"
    search_fields = ["name"]
    fields = ["name"]


@admin.register(Shop)
class ShopAdmin(admin.ModelAdmin):
    list_display = ["id", "add", "vnd", "icon", "mota", "ruong"]
    list_display_links = ["id"]
    empty_display_value = "--empty--"
    search_fields = ["mota"]
    fields = ["add", "vnd", "icon", "mota", "ruong"]


@admin.register(Skill)
class SkillAdmin(admin.ModelAdmin):
    list_display = [
        "id",
        "name",
        "nclass",
        "max_point",
        "type",
        "icon_id",
        "desc",
        "skill_templates",
    ]
    list_display_links = ["id", "name"]
    empty_display_value = "--empty--"
    search_fields = ["desc", "name"]
    list_filter = ["nclass"]
    fields = [
        "name",
        "nclass",
        "max_point",
        "type",
        "icon_id",
        "desc",
        "skill_templates",
    ]


@admin.register(Effect)
class EffectAdmin(admin.ModelAdmin):
    list_display = ["id", "type", "name", "icon_id"]
    list_display_links = ["id", "name"]
    empty_display_value = "--empty--"
    search_fields = ["name"]
    fields = ["type", "name", "icon_id"]
