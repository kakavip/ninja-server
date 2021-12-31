from django.http import JsonResponse
from django.core.paginator import Paginator
from django.http.response import HttpResponseForbidden
from rest_framework import status
from django.http import HttpResponse
from rest_framework.response import Response
from ratelimit.exceptions import Ratelimited
from rest_framework import status


def fail(message, data={}):
    # localize message here
    # custom data here
    return JsonResponse(
        {"error": 400, "message": message, "data": data},
        safe=False,
        status=status.HTTP_400_BAD_REQUEST,
    )


def error(message, data={}):
    # localize message here
    # custom data here
    return JsonResponse(
        {"error": 500, "message": message, "data": data},
        safe=False,
        status=status.HTTP_500_INTERNAL_SERVER_ERROR,
    )


def unauthorize(message, data={}):
    # localize message here
    # custom data here
    return JsonResponse(
        {"error": 401, "message": message, "data": data},
        safe=False,
        status=status.HTTP_401_UNAUTHORIZED,
    )


def forbidden(message, data={}):
    # localize message here
    # custom data here
    return JsonResponse(
        {"error": 403, "message": message, "data": data},
        safe=False,
        status=status.HTTP_403_FORBIDDEN,
    )


def confirm(message, data={}):
    return JsonResponse(
        {"error": 0, "message": message, "data": data},
        safe=False,
        status=status.HTTP_200_OK,
    )


def success(data):
    return JsonResponse(data, safe=False, status=status.HTTP_200_OK)


def text_response(text_data: str, status: int):
    return HttpResponse(
        content=text_data, status=status, content_type="text/plain; charset=UTF-8"
    )


def xml_response(text_data: str, status: int):
    return HttpResponse(
        content=text_data, status=status, content_type="application/xml"
    )


# NOTE handler for ratelimited
def handler403(request, exeception=None):
    if isinstance(exeception, Ratelimited):
        return JsonResponse(
            {
                "message": "Sorry you are blocked",
                "error_code": "ratelimit_exception",
                "detail": "Sorry you are blocked",
                "error": 429,
            },
            safe=False,
            status=status.HTTP_429_TOO_MANY_REQUESTS,
        )

    return HttpResponseForbidden("Forbidden")
