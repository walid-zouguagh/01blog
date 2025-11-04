docker run --name 01blogdb \
      -e POSTGRES_USER=admin \
      -e POSTGRES_PASSWORD=123 \
      -e POSTGRES_DB=01blogdb \
      -p 5432:5432 \
      -d postgres:15