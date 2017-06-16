#!/bin/bash
./gradlew buildDocker
docker push dockernovinet/smtp-mailer-brochureware:latest