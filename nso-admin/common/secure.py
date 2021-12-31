from ipware import get_client_ip



def get_ip_from_request(request):
    matched_client_ips, is_routable = get_client_ip(
        request,
        request_header_order=[
            "HTTP_X_FORWARDED_FOR",
            "X_FORWARDED_FOR",
            "REMOTE_ADDR",
        ],
    )

    return matched_client_ips and matched_client_ips.split(",")[0].strip() or ""
