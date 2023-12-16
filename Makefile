build: build-server build-client build-docker

clean: clean-server clean-client

build-docker:
	./gradlew bootBuildImage --imageName ghcr.io/melo0187/iban:$(shell git rev-parse --short HEAD)

docker-run:
	docker run --rm -p 8080:8080 ghcr.io/melo0187/iban:$(shell git rev-parse --short HEAD)

docker-push:
	docker push ghcr.io/melo0187/iban:$(shell git rev-parse --short HEAD)

build-server: clean-server
	./gradlew build

clean-server:
	./gradlew clean

build-client: clean-client
	cd frontend && npm ci && npm run build && cp -r build ../src/main/resources/static

clean-client:
	cd frontend && npm run clean && rm -rf ../src/main/resources/static