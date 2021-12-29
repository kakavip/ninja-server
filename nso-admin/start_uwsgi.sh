#!/bin/sh

python manage.py makemigrations
python manage.py migrate

gunicorn NSOAdmin.wsgi:application -c gunicorn_config.py