# mlat-service

Cервис многопозиционная система наблюдения транспорта на поверхности

# :hammer: Build

Чтобы собрать проект из исходного кода, склонируйте этот репозиторий:

```
git@github.com:zagaynov-andrew/mlat-service.git
```

Затем перейдите в директорию `mlat-service` и скомпилируйте сервер, используя Maven:

```
cd mlat-service && mvn compile assembly:single
```

:pushpin: Для успешной сборки вам понадобятся установленные Maven и Java 8.

## :rocket: Launch

Для запуска сервера исполните Java-архив:

```
java -jar .\server\server.jar
```

Для запуска тестирующих станций:

```
java -jar .\test-datacenter\test-datacenter.jar .\test-datacenter\config1.txt
```
