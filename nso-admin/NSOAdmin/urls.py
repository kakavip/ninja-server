"""NSOAdmin URL Configuration

The `urlpatterns` list routes URLs to views. For more information please see:
    https://docs.djangoproject.com/en/2.2/topics/http/urls/
Examples:
Function views
    1. Add an import:  from my_app import views
    2. Add a URL to urlpatterns:  path('', views.home, name='home')
Class-based views
    1. Add an import:  from other_app.views import Home
    2. Add a URL to urlpatterns:  path('', Home.as_view(), name='home')
Including another URLconf
    1. Import the include() function: from django.urls import include, path
    2. Add a URL to urlpatterns:  path('blog/', include('blog.urls'))
"""
from django.contrib import admin
from django.urls import path
from django.contrib.staticfiles.urls import staticfiles_urlpatterns

from NSOAdmin.views import (
    apk_file,
    downloads,
    index,
    jar_file,
    register,
    server_list,
    srvip,
)


urlpatterns = [
    path("admin/", admin.site.urls),
    path("nja.txt", server_list),
    path("srvip/nj.txt", srvip),
    path("", index, name="home"),
    path("register", register, name="register"),
    path("downloads", downloads, name="downloads"),
    path("nso_moonsmile.apk", apk_file, name="apk_file"),
    path("nso_moonsmile.jar", jar_file, name="jar_file"),
]

urlpatterns += staticfiles_urlpatterns()
