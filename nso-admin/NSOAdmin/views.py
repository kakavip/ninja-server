from typing import Optional
import re
from django.http.response import JsonResponse
from rest_framework.decorators import api_view
from rest_framework.response import Response
from django.shortcuts import render
from rest_framework import status
from django.http import FileResponse
from django.db import transaction


from django.http import HttpResponse

from NSOAdmin.models import Player


@api_view(["GET"])
def server_list(request):
    """
    List all code snippets, or create a new snippet.
    """
    if request.method == "GET":
        server_format: str = "MoonSmile:34.87.101.206:14444:0:0,Bokken:112.213.84.18:14444:0:0,Shuriken:27.0.14.73:14444:0:0,Tessen:27.0.14.73:14444:1:0,Kunai:112.213.94.135:14444:0:0,Katana:112.213.94.161:14444:0:0"
        return HttpResponse(server_format, content_type="text/plain; charset=utf8")


def index(request):
    return render(request, "index.html")


def downloads(request):
    return render(request, "downloads.html")


@api_view(["POST"])
def register(request):
    if request.method == "POST":
        username = request.data.get("user")
        password = request.data.get("pass")

        if not (username and password):
            return JsonResponse(
                {
                    "status": "Failure",
                    "message": f"Tài khoản và mật khẩu không được trống.",
                },
                status=status.HTTP_400_BAD_REQUEST,
                safe=False,
            )

        if not (
            username == re.findall(r"([\w|\d]+)", username)[0]
            and password == re.findall(r"([\w|\d]+)", password)[0]
        ):
            return JsonResponse(
                {
                    "status": "Failure",
                    "message": f"Tài khoản và mật khẩu phải là số hoặc chữ.",
                },
                status=status.HTTP_400_BAD_REQUEST,
                safe=False,
            )

        try:
            with transaction.atomic():
                player: Optional[Player] = Player.objects.filter(
                    username=username
                ).first()
                if not player:
                    Player.objects.create(
                        username=username, password=password, luong=1000
                    )
                else:
                    raise Exception()

            return JsonResponse(
                {
                    "status": "Success",
                    "message": "Bạn đã đăng kí tài khoàn thành công.",
                },
                status=status.HTTP_200_OK,
                safe=False,
            )
        except:
            return JsonResponse(
                {
                    "status": "Failure",
                    "message": f"Bạn đã đăng kí tài khoàn thất bại. Username: {username} đã tồn tại.",
                },
                status=status.HTTP_400_BAD_REQUEST,
                safe=False,
            )


def apk_file(request):
    return FileResponse(open("packages/NSO_WORLD_v1.5.6.MS-DOMAIN.apk", "rb"))


def jar_file(request):
    return FileResponse(open("packages/nso-ms.domain.v1.jar", "rb"))
