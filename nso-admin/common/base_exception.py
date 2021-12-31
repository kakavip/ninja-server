class BaseApiException(Exception):
    def __init__(self, message: str, error_code: str = "base_exception"):
        self.error_code = error_code
        self.message = message
        super(Exception, self).__init__(message)
