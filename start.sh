#!/bin/bash

# Clean up unused Docker resources
podman system prune -f

# Create the Docker network if it doesn't exist
if ! podman network ls | grep -q custom_network; then
 podman network create custom_network
fi

# Start Docker Compose
podman-compose up --build --no-cache && podman-compose logs -f
