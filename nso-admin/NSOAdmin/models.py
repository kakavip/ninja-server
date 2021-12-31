from django.db import models
from django.db.models.fields import IntegerField
from jsonfield import JSONField
from django.db.models import Sum
import json

from NSOAdmin.enums.item_type_enum import ItemTypeEnum
from NSOAdmin.enums.ninja_class_enum import NinjaClassEnum
from NSOAdmin.enums.ninja_gender_enum import NinjaGenderEnum


class Player(models.Model):
    STATUSES = (("active", "ACTIVE"), ("wait", "WAIT"))

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
