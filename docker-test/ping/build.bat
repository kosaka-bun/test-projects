docker stop ping-test
docker rm ping-test
docker rmi ping-test:latest

docker build -t ping-test:latest .
docker run --name ping-test -d ping-test