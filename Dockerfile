FROM eclipse-temurin:21.0.3_9-jre-jammy@sha256:e2318d304bc50c798edea630a23f7b90b1adb14a02658de91d2c99027a071c26

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
