from typing import List

from NSOAdmin.models import Player


def lock_some_accounts_action(modeladmin, request, queryset: List[Player]):
    queryset.update(lock=True)
