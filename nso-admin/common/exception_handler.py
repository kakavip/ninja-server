from rest_framework.exceptions import APIException
from rest_framework.views import exception_handler
from rest_framework.response import Response
from rest_framework import status

from .base_exception import BaseApiException
from rest_framework.utils.serializer_helpers import ReturnDict


def custom_exception_handler(exc, context):
    # Call REST framework's default exception handler first,
    # to get the standard error response.
    response = exception_handler(exc, context)
    if response is None:
        if isinstance(exc, BaseApiException):
            data: dict = {}
            data.update({"detail": exc.message})
            data.update({"error_code": exc.error_code})

            response = Response(data, status=status.HTTP_400_BAD_REQUEST)

    if isinstance(exc, APIException):
        data: dict = {}
        data.update({"error_code": exc.get_codes()})
        data.update({"detail": exc.detail})

        response = Response(data, status=exc.status_code)

    # Now add the HTTP status code to the response.
    if response is not None:
        response.data["error_code"] = response.data.get("error_code", "")
        response.data["error"] = response.status_code
        # track other data
        data_info: dict = response.data.copy()
        data_info.pop("error_code", "")
        data_info.pop("error", "")

        refined_message: str = _refine_error_detail(data_info) or ""
        response.data["message"] = response.data.get("detail", refined_message)

    return response


def _refine_error_detail(detail):
    if detail is str:
        return detail
    elif detail is list:
        return ". ".join([_refine_error_detail(item) for item in detail])
    else:
        if not isinstance(detail, dict) and not isinstance(detail, ReturnDict):
            if hasattr(detail, "__dict__"):
                detail = detail.__dict__
            else:
                detail = dict(detail)
        message_parts = []
        for item_key in detail.keys():
            if item_key in ["serializer"]:
                continue
            message_parts.append(
                _refine_error_detail_dict_item(item_key, detail[item_key])
            )
        return ". ".join(message_parts)


def _refine_error_detail_list_item(value):
    if isinstance(value, list):
        value = value[0]
    return str(value)


def _refine_error_detail_dict_item(key, value):
    if isinstance(value, list):
        value = value[0]
    field_display_name = key.replace("_", " ").capitalize()
    field_error_message = str(value)
    if field_display_name.lower() in field_error_message.lower():
        return field_error_message
    else:
        return "{}: {}".format(field_display_name, field_error_message)
