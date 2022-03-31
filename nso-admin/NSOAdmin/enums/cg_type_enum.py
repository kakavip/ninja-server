from common.base_enum import BaseEnum


class CGTypeEnum(BaseEnum):
    TRIAL: str = "Dùng Thử"
    MEMBER: str = "Thành viên"

    def __str__(self) -> str:
        return self.value
