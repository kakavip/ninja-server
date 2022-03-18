from common.base_enum import BaseIntEnum


class CardStatusEnum(BaseIntEnum):
    FAILURE: int = -1
    INITIAL: int = 0
    IN_PROGRESS: int = 1
    SUCCESS: int = 2
    WRONG_VALUE: int = 3
    DONE: int = 4

    def __int__(self) -> int:
        return self.value
