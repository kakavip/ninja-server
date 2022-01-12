#!make
args = `arg="$(filter-out $@,$(MAKECMDGOALS))" && echo $${arg:-${1}}`

up:
	docker-compose up -d --build $(call args," ")

down:
	docker-compose down
