from common.base_enum import BaseEnum


class CardTypeEnum(BaseEnum):
    VIETTEL: str = "VIETTEL"
    MOBIFONE: str = "MOBIFONE"
    VINAPHONE: str = "VINAPHONE"
    MOMO: str = "MOMO"
    BANK: str = "BANK"

    def __str__(self) -> str:
        return self.value
