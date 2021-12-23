from django.db import models
from django.db.models.fields import IntegerField
from jsonfield import JSONField
from django.db.models import Sum
import json


class Player(models.Model):
    username = models.CharField(max_length=20, blank=False, null=False)
    password = models.CharField(max_length=20, blank=False, null=False)
    luong = models.IntegerField(blank=False, null=False, default=0)
    ninja = JSONField(blank=True, null=True, default=[])
    coin = models.IntegerField(blank=False, null=False, default=0)
    ticket_gold = models.IntegerField(
        blank=False, null=False, default=0, db_column="ticketGold"
    )
    status = models.CharField(blank=False, null=False, default="active", max_length=20)
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
