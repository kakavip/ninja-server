from django.db import models
from django.db.models.fields import IntegerField
from jsonfield import JSONField
from django.db.models import Sum
import json

from NSOAdmin.enums.item_type_enum import ItemTypeEnum
from NSOAdmin.enums.ninja_class_enum import NinjaClassEnum
from NSOAdmin.enums.ninja_gender_enum import NinjaGenderEnum


class Player(models.Model):
    STATUSES = (("active", "ACTIVE"), ("wait", "WAIT"), ("block", "BLOCK"))

    username = models.CharField(max_length=20, blank=False, null=False, unique=True)
    password = models.CharField(max_length=20, blank=False, null=False)
    luong = models.IntegerField(blank=False, null=False, default=0)
    ninja = JSONField(blank=True, null=True, default=[])
    coin = models.IntegerField(blank=False, null=False, default=0)
    ticket_gold = models.IntegerField(
        blank=False, null=False, default=0, db_column="ticketGold"
    )
    status = models.CharField(
        blank=False, null=False, default="active", max_length=20, choices=STATUSES
    )
    phone = models.CharField(blank=True, null=True, max_length=20)
    joined_time = models.DateTimeField(
        blank=True, null=True, db_column="ngaythamgia", auto_now_add=True
    )
    customer_group = models.CharField(
        blank=True,
        null=True,
        default="Thành viên",
        db_column="nhomkhachhang",
        max_length=30,
    )
    clan_territory_id = models.IntegerField(
        blank=True, null=True, default=-1, db_column="clanTerritoryId"
    )

    lock = models.BooleanField(default=False)

    class Meta:
        db_table = "player"

    def __str__(self) -> str:
        return f"{self.pk} - {self.username}"


class Character(models.Model):
    CHARACTER_CLASSES = (
        (1, "KIEM"),
        (2, "PHI TIEU"),
        (3, "KUNAI"),
        (4, "CUNG"),
        (5, "DAO"),
        (6, "QUAT"),
    )

    id = models.IntegerField(primary_key=True)
    name = models.CharField(blank=False, null=False, max_length=30)
    _class = models.IntegerField(
        blank=False, null=False, default=0, db_column="class", choices=CHARACTER_CLASSES
    )
    skill = JSONField(blank=True, null=True, default=[])
    spoint = models.IntegerField(default=0, blank=False, null=False)
    ppoint = models.IntegerField(default=0, blank=False, null=False)
    potential0 = models.IntegerField(default=5, blank=False, null=False)
    potential1 = models.IntegerField(default=5, blank=False, null=False)
    potential2 = models.IntegerField(default=5, blank=False, null=False)
    potential3 = models.IntegerField(default=10, blank=False, null=False)
    k_skill = JSONField(blank=True, null=True, default="[-1,-1,-1]", db_column="KSkill")
    o_skill = JSONField(
        blank=True, null=True, default="[-1,-1,-1,-1,-1]", db_column="OSkill"
    )
    level = models.IntegerField(blank=True, null=True, default=0)
    exp = models.BigIntegerField(blank=True, null=True, default=0)
    yen = models.IntegerField(blank=True, null=True, default=0)
    xu = models.IntegerField(blank=True, null=True, default=0)

    class Meta:
        abstract = True

    def save(
        self,
        *args,
        **kwargs,
    ) -> None:
        try:
            obj: Character = self.__class__.objects.get(id=self.id)
            if obj._class != self._class:
                self.transfer_class(self._class)
        except Exception as ex:
            pass
        return super().save(*args, **kwargs)

    def __str__(self) -> str:
        return f"{self.id} - {self.name}"

    def transfer_class(self, _class: int):
        from NSOAdmin.models import Level

        self._class = _class
        self.skill = []
        self.o_skill = [-1, -1, -1, -1, -1]
        self.k_skill = [-1, -1, -1]

        self.potential0 = 5
        self.potential1 = 5
        self.potential2 = 5
        self.potential3 = 10

        point_data: dict = Level.objects.filter(level__lte=self.level).aggregate(
            Sum("spoint"), Sum("ppoint")
        )
        self.ppoint = point_data["ppoint__sum"]
        self.spoint = point_data["spoint__sum"]


class Ninja(Character):
    site = JSONField(blank=True, null=True, default=[])

    class Meta:
        db_table = "ninja"


class CloneNinja(Character):
    class Meta:
        db_table = "clone_ninja"


class Level(models.Model):
    level = models.IntegerField(primary_key=True)
    exps = models.BigIntegerField(blank=True, default=0, null=True)
    ppoint = models.IntegerField(blank=True, null=True, default=0)
    spoint = models.IntegerField(blank=True, null=True, default=0)

    class Meta:
        db_table = "level"
        ordering = ["level"]

    def __str__(self) -> str:
        return f"Level {self.level}"


class Npc(models.Model):
    id = models.IntegerField(primary_key=True)
    name = models.CharField(blank=True, null=True, max_length=100)
    head = models.IntegerField()
    body = models.IntegerField()
    leg = models.IntegerField()
    type = models.IntegerField()
    talk_id = models.IntegerField(db_column="talkid")
    talk = JSONField(blank=True, null=True, default=[])

    class Meta:
        db_table = "npc"

    def __str__(self) -> str:
        return self.name


class NpcDaily(models.Model):
    id = models.IntegerField(primary_key=True)
    npc_chat = JSONField(blank=True, null=True, default=[])
    features = JSONField(blank=True, null=True, default=[])
    features_id = JSONField(blank=True, null=True, default=[])

    class Meta:
        db_table = "npc_daily"

    def __str__(self) -> str:
        return str(self.id)


class GiftCode(models.Model):
    id = models.IntegerField(primary_key=True)
    gift_code = models.CharField(
        blank=False, null=False, db_column="giftcode", unique=True, max_length=50
    )
    yen = models.IntegerField(blank=True, null=True, default=0)
    xu = models.IntegerField(blank=True, null=True, default=0)
    luong = models.IntegerField(blank=True, null=True, default=0)
    username = JSONField(blank=True, null=True, default=[])
    times = models.IntegerField(blank=True, null=True, default=0)
    limited = models.IntegerField(blank=True, null=True, default=0)
    mess_tb = models.TextField(blank=True, null=True, db_column="messTB")
    item_id = models.IntegerField(blank=True, null=True, default=0, db_column="itemId")
    item_quantity = models.IntegerField(
        blank=True, null=True, default=0, db_column="itemQuantity"
    )

    item_id_1 = models.IntegerField(
        blank=True, null=True, default=0, db_column="itemId1"
    )
    item_quantity_1 = models.IntegerField(
        blank=True, null=True, default=0, db_column="itemQuantity1"
    )

    item_id_2 = models.IntegerField(
        blank=True, null=True, default=0, db_column="itemId2"
    )
    item_quantity_2 = models.IntegerField(
        blank=True, null=True, default=0, db_column="itemQuantity2"
    )

    item_id_3 = models.IntegerField(
        blank=True, null=True, default=0, db_column="itemId3"
    )
    item_quantity_3 = models.IntegerField(
        blank=True, null=True, default=0, db_column="itemQuantity3"
    )

    item_id_4 = models.IntegerField(
        blank=True, null=True, default=0, db_column="itemId4"
    )
    item_quantity_4 = models.IntegerField(
        blank=True, null=True, default=0, db_column="itemQuantity4"
    )

    item_id_5 = models.IntegerField(
        blank=True, null=True, default=0, db_column="itemId5"
    )
    item_quantity_5 = models.IntegerField(
        blank=True, null=True, default=0, db_column="itemQuantity5"
    )

    class Meta:
        db_table = "giftcode"

    def __str__(self) -> str:
        return self.gift_code


class Item(models.Model):
    id = models.IntegerField(primary_key=True)
    type = models.IntegerField(
        blank=False, null=False, choices=ItemTypeEnum.to_choices()
    )
    nclass = models.IntegerField(
        blank=False,
        null=False,
        default=0,
        db_column="class",
        choices=NinjaClassEnum.to_choices(),
    )
    gender = models.IntegerField(
        blank=True, null=True, default=2, choices=NinjaGenderEnum.to_choices()
    )
    name = models.CharField(blank=True, null=True, max_length=100)
    description = models.TextField(null=True, blank=True)
    level = models.IntegerField(null=False, blank=False, default=0)
    icon_id = models.IntegerField(null=True, blank=True, db_column="iconID")
    part = models.IntegerField(null=True, blank=False, default=-1)
    uptoup = models.IntegerField(blank=True, null=True, default=0)
    is_expires = models.BooleanField(
        blank=True, null=True, default=False, db_column="isExpires"
    )
    seconds_expires = models.IntegerField(
        blank=True, null=True, default=0, db_column="secondsExpires"
    )
    sale_coin_lock = models.IntegerField(
        blank=True, null=True, default=0, db_column="saleCoinLock"
    )
    item_option = JSONField(blank=True, null=True, default=[], db_column="ItemOption")
    option_1 = JSONField(blank=True, null=True, default=[], db_column="Option1")
    option_2 = JSONField(blank=True, null=True, default=[], db_column="Option2")
    option_3 = JSONField(blank=True, null=True, default=[], db_column="Option3")

    def __str__(self) -> str:
        return self.name

    class Meta:
        db_table = "item"


class ItemSell(models.Model):
    id = models.IntegerField(primary_key=True)
    type = models.IntegerField(
        blank=False, null=False, choices=ItemTypeEnum.to_choices()
    )
    list_item = JSONField(blank=True, null=True, default=[], db_column="ListItem")

    def __str__(self) -> str:
        return str(self.id)

    class Meta:
        db_table = "itemsell"


class ItemShinwa(models.Model):
    id = models.IntegerField(primary_key=True)
    item = JSONField(blank=True, null=True, default=[])

    class Meta:
        db_table = "itemshinwa"

    def __str__(self) -> str:
        return str(self.id)


class OptionItem(models.Model):
    id = models.IntegerField(primary_key=True)
    name = models.CharField(max_length=100, blank=True, null=True)
    type = models.IntegerField(null=True, blank=True, default=0)

    class Meta:
        db_table = "optionitem"

    def __str__(self) -> str:
        return self.name


class OptionSkill(models.Model):
    id = models.IntegerField(primary_key=True)
    name = models.CharField(max_length=100, blank=True, null=True)

    class Meta:
        db_table = "optionskill"

    def __str__(self) -> str:
        return self.name


class Shop(models.Model):
    id = models.IntegerField(primary_key=True)
    add = JSONField(blank=True, null=True, default={})
    vnd = models.IntegerField(default=0, null=False, blank=False)
    icon = models.IntegerField(default=0, null=False, blank=False)
    mota = models.TextField(blank=True, null=True)
    ruong = models.IntegerField(blank=True, null=True, default=0)

    class Meta:
        db_table = "shop"

    def __str__(self) -> str:
        return str(self.id)


class Skill(models.Model):
    id = models.IntegerField(primary_key=True)
    name = models.CharField(max_length=100, blank=False, null=False)
    nclass = models.IntegerField(
        choices=NinjaClassEnum.to_choices(), blank=True, null=True, db_column="class"
    )
    max_point = models.IntegerField(
        blank=False, null=False, default=0, db_column="maxPoint"
    )
    type = models.IntegerField(blank=True, null=True, default=0)
    icon_id = models.IntegerField(blank=True, null=True, default=0, db_column="iconId")
    desc = models.CharField(max_length=256, blank=True, null=True)
    skill_templates = JSONField(
        blank=True, null=True, default=[], db_column="skillTemplates"
    )

    class Meta:
        db_table = "skill"

    def __str__(self) -> str:
        return self.name


class Effect(models.Model):
    id = models.IntegerField(primary_key=True)
    type = models.IntegerField(default=0)
    name = models.CharField(max_length=256)
    icon_id = models.IntegerField(db_column="iconId")

    class Meta:
        db_table = "effect"

    def __str__(self) -> str:
        return self.name


class Task(models.Model):
    id = models.IntegerField(primary_key=True)
    tasks = JSONField(blank=True, null=True, default=[])
    maptasks = JSONField(blank=True, null=True, default=[])

    class Meta:
        db_table = "tasks"

    def __str__(self) -> str:
        return str(self.id)


class Tournament(models.Model):
    id = models.IntegerField(primary_key=True)
    tournaments = JSONField(blank=True, null=True, default=[])

    class Meta:
        db_table = "tournament"


class Map(models.Model):
    id = models.IntegerField(primary_key=True)
    title_id = models.IntegerField(db_column="tileID", default=0)
    bg_id = models.IntegerField(db_column="bgID", default=0)
    type_map = models.IntegerField(db_column="typeMap", default=0)
    name = models.CharField(max_length=100)
    vgo = JSONField(default=[], db_column="Vgo")
    mob = JSONField(default=[], db_column="Mob")
    npc = JSONField(default=[], db_column="NPC", blank=True, null=True)
    maxplayer = models.IntegerField(default=20)
    numzone = models.IntegerField(default=30)
    x0 = models.IntegerField(default=0)
    y0 = models.IntegerField(default=0)

    class Meta:
        db_table = "map"

    def __str__(self) -> str:
        return self.name


class Mob(models.Model):
    id = models.IntegerField(primary_key=True)
    name = models.CharField(max_length=100)
    type = models.IntegerField(default=0)
    hp = models.IntegerField(default=0, null=True, blank=True)
    range_move = models.IntegerField(default=0, db_column="rangeMove")
    speed = models.IntegerField(default=0)
    type_fly = models.IntegerField(default=0, db_column="typeFly")
    n_image = models.IntegerField(default=0, db_column="nImage")
    flag = models.IntegerField(default=0)
    frame_boss_move = JSONField(
        blank=True, null=True, default=[], db_column="frameBossMove"
    )
    frame_boss_attack = JSONField(
        blank=True, null=True, default=[], db_column="frameBossAttack"
    )
    frame_boss = JSONField(blank=True, null=True, default=[], db_column="frameBoss")
    info = models.IntegerField(default=0)
    img_1 = JSONField(blank=True, null=True, default=[], db_column="Img1")
    img_2 = JSONField(blank=True, null=True, default=[], db_column="Img2")
    img_3 = JSONField(blank=True, null=True, default=[], db_column="Img3")
    img_4 = JSONField(blank=True, null=True, default=[], db_column="Img4")

    class Meta:
        db_table = "mob"

    def __str__(self) -> str:
        return self.name


class Clan(models.Model):
    id = models.IntegerField(primary_key=True)
    name = models.CharField(max_length=100)
    exp = models.IntegerField(default=0)
    level = models.IntegerField(default=0)
    item_level = models.IntegerField(
        db_column="itemLevel", default=0, blank=True, null=True
    )
    coin = models.IntegerField(default=0)
    # reg_date = models.DateTimeField(null=True, blank=True, auto_now_add=True)
    log = models.TextField(null=True, blank=True)
    use_card = models.IntegerField(default=0)
    alert = models.CharField(max_length=200, blank=True, null=True, default="abc")
    open_dun = models.IntegerField(default=0, db_column="openDun")
    debt = models.IntegerField(default=0)
    members = JSONField(blank=True, null=True, default=[])
    items = JSONField(blank=True, null=True, default=[])
    # week = models.DateTimeField(null=True, blank=True)
    clan_battle_data = models.TextField(null=True, blank=True)
    clan_than_thu = JSONField(blank=True, null=True, default=[])

    class Meta:
        db_table = "clan"

    def __str__(self) -> str:
        return self.name


class ClanItem(models.Model):
    id = models.IntegerField(primary_key=True)
    id_shop = models.IntegerField()
    conghien = models.IntegerField()
    time = models.IntegerField()
    id_clan = models.IntegerField()

    class Meta:
        db_table = "clan_item"

    def __str__(self) -> str:
        return str(self.id)


class ClanShop(models.Model):
    id = models.IntegerField(primary_key=True)
    add = JSONField(blank=True, null=True, default=[])
    luong = models.IntegerField(default=0)
    conghien = models.IntegerField(default=0)
    mota = models.TextField(null=True, blank=True)
    icon = models.IntegerField(default=0)
    an = models.IntegerField(default=0)

    class Meta:
        db_table = "clan_shop"

    def __str__(self) -> str:
        return str(self.id)
