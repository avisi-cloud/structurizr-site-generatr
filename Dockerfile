FROM eclipse-temurin:21.0.3_9-jre-jammy@sha256:0f8bc645fb0c9ab40c913602c9f5f12c32d9ae6bef3e34fa0469c98e7341333c

USER root
RUN apt update && apt install graphviz --yes && rm -rf /var/lib/apt/lists/*
RUN mkdir -p /var/model \
    && chown 65532:65532 /var/model
RUN useradd -d /home/generatr -u 65532 --create-home generatr

ENTRYPOINT ["/opt/structurizr-site-generatr/bin/structurizr-site-generatr"]

WORKDIR /opt/structurizr-site-generatr
COPY build/install/structurizr-site-generatr ./
RUN chmod +x /opt/structurizr-site-generatr/bin/structurizr-site-generatr

USER generatr
VOLUME ["/var/model"]
WORKDIR /var/model
