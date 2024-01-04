FROM mysql:8.0

COPY create_tab.sql /docker-entrypoint-initdb.d/create_tables.sql

VOLUME /mysql/data /var/lib/mysql

ENV MYSQL_ROOT_PASSWORD 077617

EXPOSE 3306