#!make
args = `arg="$(filter-out $@,$(MAKECMDGOALS))" && echo $${arg:-${1}}`

up:
	docker-compose up -d --build $(call args," ")

down:
	docker-compose down

backup:
	docker-compose exec mysql /bin/bash -c "mysqldump -uroot -p12345678 --databases nja > /backups/ninja_data_`(date +'%Y-%m-%d_%H%M%S')`.sql"

maintain:
	make backup
	git pull origin master
	make up nja_server
	make backup