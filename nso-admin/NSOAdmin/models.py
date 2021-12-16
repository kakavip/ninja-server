from django.db import models
from jsonfield import JSONField


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
