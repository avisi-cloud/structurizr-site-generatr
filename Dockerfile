FROM eclipse-temurin:21.0.4_7-jre-jammy@sha256:870aae69d4521fdaf26e952f8026f75b37cb721e6302d4d4d7100f6b09823057

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
