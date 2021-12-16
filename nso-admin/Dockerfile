FROM python:3.7.9-alpine

ENV PYTHONUNBUFFERED 1

# RUN apk update && apk upgrade
RUN apk add --no-cache bash \
    pkgconfig \
    git \
    gcc \
    openldap \
    libcurl \
    python3-dev \
    gpgme-dev \
    libc-dev \
    mariadb-dev \
    && rm -rf /var/cache/apk/*

RUN mkdir /code
WORKDIR /code

ADD ./requirements.txt ./requirements.txt
RUN pip install -r requirements.txt

ADD . ./

RUN python manage.py collectstatic --noinput

EXPOSE 8000

CMD [ "./start_uwsgi.sh"]