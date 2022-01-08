from typing import Optional
import re
from django.http.response import JsonResponse
from rest_framework.decorators import api_view
from rest_framework.response import Response
from django.shortcuts import render
from rest_framework import status
from django.http import FileResponse
from django.db import transaction
from ratelimit.decorators import ratelimit
from common import response, secure
from django.http import HttpResponse
from django.core.cache import caches

from NSOAdmin.models import Player
from common.base_exception import BaseApiException


cache = caches["default"]


@api_view(["GET"])
def server_list(request):
    """
    List all code snippets, or create a new snippet.
    """
    if request.method == "GET":
        server_format: str = "MoonSmile:34.87.101.206:14444:0:0,Bokken:112.213.84.18:14444:0:0,Shuriken:27.0.14.73:14444:0:0,Tessen:27.0.14.73:14444:1:0,Kunai:112.213.94.135:14444:0:0,Katana:112.213.94.161:14444:0:0"
        return HttpResponse(server_format, content_type="text/plain; charset=utf8")


@api_view(["GET"])
def srvip(request):
    if request.method == "GET":
        server_format: str = "34.87.101.206:14444"

        return HttpResponse(server_format, content_type="text/plain; charset=utf8")


def index(request):
    return render(request, "index.html")


def downloads(request):
    return render(request, "downloads.html")


# @ratelimit(key="ip", rate="3/365d", block=True)
@api_view(["POST"])
@transaction.atomic
def register(request):
    client_ip: str = secure.get_ip_from_request(request)

    if request.method == "POST":
        username = request.data.get("user")
        password = request.data.get("pass")

        if not (username and password):
            return response.fail("Tài khoản và mật khẩu không được trống.")

        if not (
            username == re.findall(r"([\w|\d]+)", username)[0]
            and password == re.findall(r"([\w|\d]+)", password)[0]
        ):
            return response.fail("Tài khoản và mật khẩu phải là số hoặc chữ.")

        player: Optional[Player] = Player.objects.filter(username=username).first()
        if not player:
            num_regis: int = cache.get_or_set(client_ip, 0, timeout=86400 * 365)

            status: str = "active"
            if num_regis >= 3:
                status = "wait"

            Player.objects.create(
                username=username, password=password, luong=1000, status=status
            )
            cache.incr(client_ip)
        else:
            raise BaseApiException(
                f"Bạn đã đăng kí tài khoàn thất bại. Username: {username} đã tồn tại."
            )

        return response.success(
            {"status": "Success", "message": "Bạn đã đăng kí tài khoàn thành công."}
        )


def apk_file(request):
    return FileResponse(open("packages/NSO_WORLD_v1.5.6.NODOMAIN.apk", "rb"))


def apk_hack_file(request):
    return FileResponse(open("packages/Ninja-148.MS_NODOMAIN.apk", "rb"))


def jar_file(request):
    return FileResponse(open("packages/nso-ms.nodomain.v1.jar", "rb"))


def jar_x3_file(request):
    return FileResponse(open("packages/nso-ms.domain.x3.jar", "rb"))


def jar_hsl_x3_file(request):
    return FileResponse(open("packages/nso-ms.hsl_x3.jar", "rb"))
