# FRIDGE

## Setup

NOTE: Setup steps tested with the following versions:
* Node v8.11.1
* NPM 5.8.0
* Typescript 2.8.1

```bash
# Install the Typescript compiler and nodemon (for hot server reload), if you haven't already:
npm i -g typescript nodemon

# Optionally, install `typeorm` globally for CLI utils:
npm i -g typeorm

# Setup the actual repo, run from project root.
npm i
```

## DB Setup

```bash
# Install postgres
brew install postgresql

# Start Postgrest with Docker
docker-compose up

# Connect from the CLI
psql -U fridge -d fridge # password for testing is "fridge"
```

## Running

```bash
# Start the server in watch mode
npm run watch-server

# Hit the /ingredients endpoint
curl http://localhost:3000/ingredients | jq
```

