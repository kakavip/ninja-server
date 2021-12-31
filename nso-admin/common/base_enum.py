from enum import IntEnum, Enum
from typing import Any, Dict, List, Tuple


class BaseEnum(Enum):
    def __eq__(self, value):
        if isinstance(self.value, value.__class__):
            return self.value == value

        return super().__eq__(value)

    @classmethod
    def to_choices(cls) -> tuple:
        result: List[Any] = []
        for mem in dict(cls.__members__).values():
            result.append((mem.value, mem.name))

        return tuple(result)


class BaseIntEnum(IntEnum):
    def __eq__(self, value):
        if isinstance(self.value, value.__class__):
            return self.value == value

        return super().__eq__(value)

    @classmethod
    def to_choices(cls) -> tuple:
        result: List[Any] = []
        for mem in dict(cls.__members__).values():
            result.append((mem.value, mem.name))

        return tuple(result)
