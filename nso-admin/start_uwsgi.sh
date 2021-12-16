#!/bin/sh

gunicorn NSOAdmin.wsgi:application -c gunicorn_config.py