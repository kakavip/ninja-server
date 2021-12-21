from rest_framework.decorators import api_view
from rest_framework.response import Response

from django.http import HttpResponse


@api_view(["GET"])
def server_list(request):
    """
    List all code snippets, or create a new snippet.
    """
    if request.method == "GET":
        server_format: str = "MoonSmile:34.75.229.221:14444:0:0"
        return HttpResponse(server_format, content_type="text/plain; charset=utf8")
