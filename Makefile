#!make
args = `arg="$(filter-out $@,$(MAKECMDGOALS))" && echo $${arg:-${1}}`


build:
	docker-compose build $(call args," ")

up:
	docker-compose up -d $(call args," ")

down:
	docker-compose down

push:
	docker-compose push

pull:
	docker-compose pull

backup:
	docker-compose exec mysql /bin/bash -c "mysqldump -uroot -p12345678 --databases nja > /backups/ninja_data_`(date +'%Y-%m-%d_%H%M%S')`.sql"

maintain:
	make backup
	git pull origin master
	make up nja_server
	make backup