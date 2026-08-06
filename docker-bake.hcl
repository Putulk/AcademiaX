// Drives the initial cold-cache build via `docker buildx bake`. All targets
// below share the same Dockerfile and context, so BuildKit resolves the
// whole graph in one solve session and the shared "build" stage (the
// Maven reactor compile) runs exactly once, instead of racing across
// parallel `docker compose build` invocations on a cold cache. Steady-state
// rebuilds after the first one are fast either way since the layer cache
// is warm — `docker compose build` alone is fine from then on.

group "default" {
  targets = [
    "eureka-server",
    "api-gateway",
    "auth-service",
    "user-management",
    "student-management",
    "academic-management",
    "faculty-management",
    "attendance-management",
    "examination-management",
    "platform-core",
  ]
}

target "eureka-server" {
  dockerfile = "Dockerfile"
  target     = "eureka-server"
  tags       = ["academiax/eureka-server:local"]
}

target "api-gateway" {
  dockerfile = "Dockerfile"
  target     = "api-gateway"
  tags       = ["academiax/api-gateway:local"]
}

target "auth-service" {
  dockerfile = "Dockerfile"
  target     = "auth-service"
  tags       = ["academiax/auth-service:local"]
}

target "user-management" {
  dockerfile = "Dockerfile"
  target     = "user-management"
  tags       = ["academiax/user-management:local"]
}

target "student-management" {
  dockerfile = "Dockerfile"
  target     = "student-management"
  tags       = ["academiax/student-management:local"]
}

target "academic-management" {
  dockerfile = "Dockerfile"
  target     = "academic-management"
  tags       = ["academiax/academic-management:local"]
}

target "faculty-management" {
  dockerfile = "Dockerfile"
  target     = "faculty-management"
  tags       = ["academiax/faculty-management:local"]
}

target "attendance-management" {
  dockerfile = "Dockerfile"
  target     = "attendance-management"
  tags       = ["academiax/attendance-management:local"]
}

target "examination-management" {
  dockerfile = "Dockerfile"
  target     = "examination-management"
  tags       = ["academiax/examination-management:local"]
}

target "platform-core" {
  dockerfile = "Dockerfile"
  target     = "platform-core"
  tags       = ["academiax/platform-core:local"]
}
