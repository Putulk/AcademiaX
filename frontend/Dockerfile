# Vite bakes VITE_* env vars in at build time, and the browser runs on the
# host (outside the Docker network), so these default to the same
# http://localhost:<port> values as frontend/.env.example — that's correct
# for this deployment shape (docker compose on one machine, open the
# browser to localhost) since every business service's port is published
# 1:1 to the host. Override with --build-arg only if you're publishing to
# different host ports.

FROM node:22-alpine AS build
WORKDIR /app

ARG VITE_AUTH_API_BASE_URL=http://localhost:8081
ARG VITE_USER_API_BASE_URL=http://localhost:8082
ARG VITE_STUDENT_API_BASE_URL=http://localhost:8083
ARG VITE_ACADEMIC_API_BASE_URL=http://localhost:8084
ARG VITE_FACULTY_API_BASE_URL=http://localhost:8085
ARG VITE_ATTENDANCE_API_BASE_URL=http://localhost:8087
ARG VITE_EXAMINATION_API_BASE_URL=http://localhost:8088
ARG VITE_PLATFORM_API_BASE_URL=http://localhost:8089

ENV VITE_AUTH_API_BASE_URL=$VITE_AUTH_API_BASE_URL \
    VITE_USER_API_BASE_URL=$VITE_USER_API_BASE_URL \
    VITE_STUDENT_API_BASE_URL=$VITE_STUDENT_API_BASE_URL \
    VITE_ACADEMIC_API_BASE_URL=$VITE_ACADEMIC_API_BASE_URL \
    VITE_FACULTY_API_BASE_URL=$VITE_FACULTY_API_BASE_URL \
    VITE_ATTENDANCE_API_BASE_URL=$VITE_ATTENDANCE_API_BASE_URL \
    VITE_EXAMINATION_API_BASE_URL=$VITE_EXAMINATION_API_BASE_URL \
    VITE_PLATFORM_API_BASE_URL=$VITE_PLATFORM_API_BASE_URL

COPY package*.json ./
RUN npm ci
COPY . .
RUN npm run build

FROM nginx:alpine
COPY --from=build /app/dist /usr/share/nginx/html
COPY nginx.conf /etc/nginx/conf.d/default.conf
EXPOSE 80
